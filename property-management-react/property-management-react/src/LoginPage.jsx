import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from './api.js'
import GoogleButton from './GoogleButton.jsx'

export default function LoginPage({ onLoggedIn }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [googleError, setGoogleError] = useState('');

    async function handleSubmit(e) {
        e.preventDefault();
        setMessage('');
        try {
            const data = await api.login({ username, password });
            onLoggedIn({ id: data.id, token: data.token, username: data.username, role: data.role });
        } catch (err) {
            setMessage(err.message);
        }
    }

    async function handleGoogleCredential(idToken) {
        setGoogleError('');
        try {
            const data = await api.googleLogin({ idToken, role: 'BUYER' });
            onLoggedIn({ id: data.id, token: data.token, username: data.username, role: data.role });
        } catch (err) {
            setGoogleError(err.message);
        }
    }

    return (
        <section className="auth-screen">
            <div className="auth-card">
                <div className="wordmark">MyProperty</div>

                <form className="auth-form" onSubmit={handleSubmit} style={{ marginTop: 28 }}>
                    <label>
                        Username
                        <input value={username} onChange={e => setUsername(e.target.value)} required autoComplete="username" />
                    </label>
                    <label>
                        Password
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} required autoComplete="current-password" />
                    </label>
                    <button type="submit" className="btn-primary">Log In</button>
                    <p className="form-message">{message}</p>
                </form>

                <div className="auth-divider"><span>or</span></div>

                <GoogleButton role="BUYER" onCredential={handleGoogleCredential} />
                {googleError && <p className="form-message">{googleError}</p>}

                <p className="auth-switch">
                    Don't have an account? <Link to="/register">Create one here</Link>
                </p>
            </div>
        </section>
    );
}