import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from './api.js'
import GoogleButton from './GoogleButton.jsx'

export default function RegisterPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('OWNER');
    const [message, setMessage] = useState('');
    const [success, setSuccess] = useState(false);
    const [googleError, setGoogleError] = useState('');

    async function handleSubmit(e) {
        e.preventDefault();
        setMessage('');
        try {
            await api.register({ username, email, password, role });
            setSuccess(true);
            setMessage('Account created. You can now log in.');
            setTimeout(() => navigate('/login'), 900);
        } catch (err) {
            setSuccess(false);
            setMessage(err.message);
        }
    }

    async function handleGoogleCredential(idToken, googleRole) {
        setGoogleError('');
        try {
            await api.googleLogin({ idToken, role: googleRole });
            navigate('/login');
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
                        <input value={username} onChange={e => setUsername(e.target.value)} required />
                    </label>
                    <label>
                        Email
                        <input type="email" value={email} onChange={e => setEmail(e.target.value)} required />
                    </label>
                    <label>
                        Password
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                    </label>
                    <label>
                        Role
                        <select value={role} onChange={e => setRole(e.target.value)}>
                            <option value="OWNER">Owner</option>
                            <option value="BUYER">Buyer</option>
                        </select>
                    </label>
                    <button type="submit" className="btn-primary">Create Account</button>
                    <p className={`form-message ${success ? 'is-success' : ''}`}>{message}</p>
                </form>

                <div className="auth-divider"><span>or</span></div>

                <label className="google-role-label">
                    New account created via Google will have role:
                    <select value={role} onChange={e => setRole(e.target.value)}>
                        <option value="OWNER">Owner</option>
                        <option value="BUYER">Buyer</option>
                    </select>
                </label>
                <GoogleButton role={role} onCredential={handleGoogleCredential} />
                {googleError && <p className="form-message">{googleError}</p>}

                <p className="auth-switch">
                    Already have an account? <Link to="/login">Log In</Link>
                </p>
            </div>
        </section>
    );
}