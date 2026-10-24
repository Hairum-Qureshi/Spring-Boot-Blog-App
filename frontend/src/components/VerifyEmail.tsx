import { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaArrowLeftLong } from 'react-icons/fa6';

export default function VerifyEmail() {
  const [timeLeft, setTimeLeft] = useState(60);
  const [isRunning, setIsRunning] = useState(false);
  const inputRef = useRef<HTMLInputElement[]>([]);
  const [showSubmitButton, setShowSubmitButton] = useState(false);
  const navigate = useNavigate();

  // TODO - replace placeholder email

  const startTimer = () => {
    setTimeLeft(60);
    setIsRunning(true);
  };

  useEffect(() => {
    if (!isRunning) return;

    if (timeLeft === 0) {
      setIsRunning(false);
      return;
    }

    const timeoutId = setTimeout(() => {
      setTimeLeft((prev) => prev - 1);
    }, 1000);

    return () => clearTimeout(timeoutId);
  }, [timeLeft, isRunning]);
  return (
    <div className="flex flex-col items-center justify-center h-full">
      <h1 className="text-2xl font-semibold">Verify Your Email</h1>

      <p className="my-2 text-center text-gray-600">
        We sent a code to user@placeholder.com
      </p>

      <div className="space-x-2">
        {new Array(6).fill(0).map((_, i) => (
          <input
            key={i}
            type="number"
            className="mt-2 p-2 border border-gray-300 rounded w-12 h-12 text-center"
            ref={(el) => {
              inputRef.current[i] = el!;
            }}
            min={0}
            max={9}
            onKeyDown={async (e) => {
              const inputs = inputRef.current;

              if (e.key === 'ArrowLeft' && i > 0) {
                e.preventDefault();
                inputs[i - 1]?.focus();
              }

              if (e.key === 'ArrowRight' && i < inputs.length - 1) {
                e.preventDefault();
                inputs[i + 1]?.focus();
              }

              if (e.key === 'v' && (e.ctrlKey || e.metaKey)) {
                const pastedText = await navigator.clipboard.readText();
                pastedText.split('').forEach((char, index) => {
                  if (index < inputRef.current.length) {
                    inputRef.current[index]!.value = char;
                  }
                });
              }

              if (e.key === 'Backspace' && i > 0 && !e.currentTarget.value) {
                e.preventDefault();
                inputs[i - 1]?.focus();
              }

              if (/^[0-9]$/.test(e.key) && i < inputs.length - 1) {
                setTimeout(() => {
                  inputs[i + 1]?.focus();
                }, 0);
              }

              if (i === inputs.length - 1 && e.currentTarget.value !== '') {
                setShowSubmitButton(true);
              } else {
                setShowSubmitButton(false);
              }
            }}
          />
        ))}
      </div>

      {!showSubmitButton ? (
        <div className="flex w-full flex-col items-center gap-5">
          {/* Email instructions */}
          <p className="text-center text-sm leading-relaxed text-slate-500">
            Didn't receive the email? Check your spam folder.
          </p>

          {/* Resend verification email */}
          <div className="flex flex-col items-center gap-2">
            {!isRunning ? (
              <button
                type="button"
                onClick={startTimer}
                className="text-sm font-medium text-blue-600 transition-colors hover:text-blue-700 hover:underline focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2 rounded hover:cursor-pointer"
              >
                Request New Verification Email
              </button>
            ) : (
              <div className="flex flex-col items-center gap-2 text-center">
                <p className="text-sm text-slate-500">
                  Request a new verification email in{' '}
                  <span className="font-semibold text-slate-700">
                    {timeLeft}s
                  </span>
                </p>

                <div
                  role="status"
                  className="flex items-center gap-2 text-sm font-medium text-green-600"
                >
                  Verification email sent!
                </div>
              </div>
            )}
          </div>

          {/* Back to login */}
          <button
            type="button"
            onClick={() => navigate('/login')}
            className="mt-2 inline-flex items-center justify-center gap-2 rounded-md px-3 py-2 text-sm font-medium text-slate-600 transition-colors hover:bg-slate-100 hover:text-blue-600 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 hover:cursor-pointer"
          >
            <FaArrowLeftLong className="h-4 w-4" />
            <span>Back to Login</span>
          </button>
        </div>
      ) : (
        <button
          type="button"
          className="my-5 w-1/2 rounded-md bg-black text-sm text-white p-3 hover:cursor-pointer"
          onClick={() => {
            // Handle OTP verification here
          }}
        >
          Verify Email
        </button>
      )}
    </div>
  );
}
