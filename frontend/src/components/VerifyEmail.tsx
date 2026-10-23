export default function VerifyEmail() {
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
      <p className="text-center text-sm text-blue-500 hover:underline hover:cursor-pointer">
        Request New Email
      </p>
    </div>
  );
}
