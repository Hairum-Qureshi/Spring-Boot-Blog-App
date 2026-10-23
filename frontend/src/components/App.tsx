import { BrowserRouter, Route, Routes } from 'react-router-dom';
import '../css/index.css';
// import { GoogleOAuthProvider } from "@react-oauth/google";
import NotFound from '../pages/NotFound';
import Auth from '../pages/Auth';
import Home from '../pages/Home';
import VerifyEmail from '../pages/VerifyEmail';

export default function App() {
  return (
    <BrowserRouter>
      {/*<GoogleOAuthProvider*/}
      {/*    clientId={import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID}*/}
      {/*>*/}
      <Routes>
        <Route path="/sign-up" element={<Auth />} />
        <Route path="/sign-in" element={<Auth />} />
        <Route path="/verify-email" element={<VerifyEmail />} />
        <Route path="/" element={<Home />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      {/*</GoogleOAuthProvider>*/}
    </BrowserRouter>
  );
}
