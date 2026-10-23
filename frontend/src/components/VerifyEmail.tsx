import { useEffect, useState, useRef } from 'react';

export default function VerifyEmail() {
  const [timeLeft, setTimeLeft] = useState(60);
  const [isRunning, setIsRunning] = useState(false);
  const inputRef = useRef<HTMLInputElement[]>([]);
  const [showSubmitButton, setShowSubmitButton] = useState(false);

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
        <>
          <p className="my-5 text-center text-slate-400 text-sm">
            Didn't receive the email? Check your spam folder.
          </p>

          {!isRunning ? (
            <button
              type="button"
              className="text-sm text-blue-500 hover:underline"
              onClick={startTimer}
            >
              Request New Verification Email
            </button>
          ) : (
            <div className="text-center text-sm text-gray-500 space-y-1">
              <p>Request a new verification email in {timeLeft}s</p>
              <p className="text-green-600">Verification email sent!</p>
            </div>
          )}
        </>
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
