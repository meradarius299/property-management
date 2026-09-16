import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from './LoginPage.jsx'
import RegisterPage from './RegisterPage.jsx'
import Dashboard from './Dashboard.jsx'
import OwnerProfilePage from './OwnerProfilePage.jsx'

function readSession() {
    const token = localStorage.getItem('token');
    const id = localStorage.getItem('id');
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role');
    return token ? { id, token, username, role } : null;
}

export default function App() {
    const [session, setSession] = useState(readSession);

    useEffect(() => {
        if (session) {
            localStorage.setItem('token', session.token);
            localStorage.setItem('id', session.id);
            localStorage.setItem('username', session.username);
            localStorage.setItem('role', session.role);
        }
    }, [session]);

    function handleLogout() {
        localStorage.removeItem('token');
        localStorage.removeItem('id');
        localStorage.removeItem('username');
        localStorage.removeItem('role');
        setSession(null);
    }

    return (
        <BrowserRouter>
            <Routes>
                <Route
                    path="/login"
                    element={session ? <Navigate to="/" replace /> : <LoginPage onLoggedIn={setSession} />}
                />
                <Route
                    path="/register"
                    element={session ? <Navigate to="/" replace /> : <RegisterPage />}
                />
                <Route
                    path="/"
                    element={
                        session
                            ? <Dashboard session={session} onLogout={handleLogout} onSessionExpired={handleLogout} />
                            : <Navigate to="/login" replace />
                    }
                />
                <Route
                    path="/owner/:ownerId"
                    element={
                        session
                            ? <OwnerProfilePage session={session} onLogout={handleLogout} />
                            : <Navigate to="/login" replace />
                    }
                />
            </Routes>
        </BrowserRouter>
    );
}
