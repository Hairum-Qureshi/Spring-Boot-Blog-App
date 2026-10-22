import AuthForm from '../components/AuthForm';

export default function Auth() {

  return (
    <div className="flex w-full h-screen overflow-0">
      <div className="w-1/2 bg-gradient-to-t from-black to-gray-900">
        <div className="flex flex-col items-center justify-center h-full space-y-3">
          <h1
            className="text-5xl font-semibold tracking-tight
            text-slate-200
            drop-shadow-[0_0_25px_rgba(148,163,184,0.3)]"
          >
            Java Blogs
          </h1>
          <div
            className="h-px w-1/2 rounded-full
    bg-gradient-to-r from-transparent
    via-slate-300/80 to-transparent
    shadow-[0_0_15px_2px_rgba(148,163,184,0.25)]"
          />{' '}
          <p className="text-base w-1/2 text-center text-slate-500 drop-shadow-[0_0_15px_rgba(148,163,184,0.3)]">
            Share your thoughts and connect with the Java community.
          </p>
        </div>
      </div>
      <div className="w-1/2 bg-slate-100/50">
        <AuthForm />
      </div>
    </div>
  );
}
