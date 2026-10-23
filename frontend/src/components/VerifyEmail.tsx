import { useEffect, useState } from 'react';

export default function VerifyEmail() {
  const [timeLeft, setTimeLeft] = useState(60);
  const [isRunning, setIsRunning] = useState(false);

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
        Please check your email for a verification link.
      </p>

      <div className="space-x-2">
        {new Array(6).fill(0).map((_, i) => (
          <input
            key={i}
            type="number"
            className="mt-2 p-2 border border-gray-300 rounded w-12 h-12 text-center"
          />
        ))}
      </div>

      <p className="my-5 text-center text-slate-400 text-sm">
        Didn't receive the email? Check your spam folder.
      </p>

      {!isRunning ? (
        <button
          type="button"
          className="text-sm hover:cursor-pointer text-blue-500 hover:underline"
          onClick={startTimer}
        >
          Request New Verification Email
        </button>
      ) : (
        <span className="text-center text-sm text-gray-500 space-y-1">
          <p>Request a new verification email in {timeLeft}s</p>
          <p className="text-green-600">Verification email sent!</p>
        </span>
      )}
    </div>
  );
}
