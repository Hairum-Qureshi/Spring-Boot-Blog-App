import { FaUser } from 'react-icons/fa';
import { MdEmail } from 'react-icons/md';
import { FaLock } from 'react-icons/fa6';
import { Link, useLocation } from 'react-router-dom';
import { FaGoogle } from 'react-icons/fa';

export default function AuthForm() {
  const location = useLocation();

  const isSignUp = location.pathname === '/sign-up' || !location.pathname;

  return (
    <div className="flex min-h-full items-center justify-center">
      <div className="w-full max-w-lg rounded-xl bg-gray-50 px-6 py-12 sm:p-10">
        <div className="mb-5 space-y-2">
          <h1 className="text-3xl font-semibold text-gray-800">
            {isSignUp ? 'Sign Up' : 'Sign In'}
          </h1>
          <p className="text-gray-500">
            {isSignUp
              ? 'Join Java Blogs today and start sharing your thoughts!'
              : 'Welcome back! Please sign in to continue.'}
          </p>
        </div>

        <button
          type="button"
          className="mt-4 flex w-full cursor-pointer items-center justify-center rounded-lg border border-gray-300 bg-white p-3 font-medium text-gray-800 shadow-sm hover:border-gray-400 hover:bg-gray-50"
        >
          <FaGoogle className="mr-3 text-lg text-blue-700" />
          Continue with Google
        </button>

        <div className="my-5 flex items-center gap-3">
          <div className="h-px flex-1 bg-gray-300"></div>
          <span className="text-sm text-gray-400">OR</span>
          <div className="h-px flex-1 bg-gray-300"></div>
        </div>

        <form className="w-full space-y-5">
          {isSignUp && (
            <div className="space-y-2">
              <label
                htmlFor="name"
                className="text-sm font-medium text-gray-700"
              >
                Name
              </label>
              <div className="flex items-center rounded-lg border border-gray-300 bg-white px-3 shadow-sm focus-within:border-blue-400">
                <FaUser className="shrink-0 text-gray-400" />
                <input
                  id="name"
                  type="text"
                  placeholder="Your name"
                  autoComplete="name"
                  className="min-w-0 flex-1 bg-white px-3 py-3 text-sm text-gray-800 outline-none placeholder:text-gray-400"
                />
              </div>
            </div>
          )}

          <div className="space-y-2">
            <label
              htmlFor="email"
              className="text-sm font-medium text-gray-700"
            >
              Email
            </label>
            <div className="flex items-center rounded-lg border border-gray-300 bg-white px-3 shadow-sm focus-within:border-blue-400">
              <MdEmail className="shrink-0 text-lg text-gray-400" />
              <input
                id="email"
                type="email"
                placeholder="Your email"
                autoComplete="email"
                className="min-w-0 flex-1 bg-white px-3 py-3 text-sm text-gray-800 outline-none placeholder:text-gray-400"
              />
            </div>
          </div>

          <div className="space-y-2">
            <label
              htmlFor="password"
              className="text-sm font-medium text-gray-700"
            >
              Password
            </label>
            <div className="flex items-center rounded-lg border border-gray-300 bg-white px-3 shadow-sm focus-within:border-blue-400">
              <FaLock className="shrink-0 text-gray-400" />
              <input
                id="password"
                type="password"
                placeholder={
                  isSignUp ? 'Create a password' : 'Enter your password'
                }
                autoComplete={isSignUp ? 'new-password' : 'current-password'}
                className="min-w-0 flex-1 bg-white px-3 py-3 text-sm text-gray-800 outline-none placeholder:text-gray-400"
              />
            </div>
          </div>

          {isSignUp && (
            <div className="space-y-2">
              <label
                htmlFor="confirm-password"
                className="text-sm font-medium text-gray-700"
              >
                Confirm Password
              </label>
              <div className="flex items-center rounded-lg border border-gray-300 bg-white px-3 shadow-sm focus-within:border-blue-400">
                <FaLock className="shrink-0 text-gray-400" />
                <input
                  id="confirm-password"
                  type="password"
                  placeholder="Confirm your password"
                  autoComplete="new-password"
                  className="min-w-0 flex-1 bg-white px-3 py-3 text-sm text-gray-800 outline-none placeholder:text-gray-400"
                />
              </div>
            </div>
          )}

          <button
            type="submit"
            className="w-full cursor-pointer rounded-lg bg-blue-500 py-2.5 font-medium text-white transition-colors hover:bg-blue-600 active:bg-blue-700"
          >
            {isSignUp ? 'Sign Up' : 'Log In'}
          </button>

          <p className="text-center text-sm text-gray-500">
            {isSignUp ? 'Already have an account?' : "Don't have an account?"}{' '}
            <Link
              to={isSignUp ? '/sign-in' : '/sign-up'}
              className="font-medium text-blue-600 hover:underline"
            >
              {isSignUp ? 'Sign In' : 'Sign Up'}
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
