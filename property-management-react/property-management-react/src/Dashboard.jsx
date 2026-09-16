import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { api } from './api.js'

const priceFormatter = new Intl.NumberFormat('en-US', { maximumFractionDigits: 0 });

export default function Dashboard({ session, onLogout, onSessionExpired }) {
    const [properties, setProperties] = useState([]);
    const [note, setNote] = useState('');

    useEffect(() => {
        loadProperties();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    async function loadProperties() {
        try {
            const data = await api.getProperties();
            setProperties(data);
        } catch (err) {
            if (err.status === 401) {
                onSessionExpired();
                return;
            }
            setNote('Failed to load property list: ' + err.message);
        }
    }

    return (
        <section className="dashboard-screen">
            <header className="topbar">
                <div className="wordmark wordmark--small">MyProperty</div>
                <div className="topbar-user">
                    <span className="role-badge">{session.role === 'OWNER' ? 'OWNER' : 'BUYER'}</span>
                    <Link className="username-link" to={`/owner/${session.id}`}>{session.username}</Link>
                    <button className="btn-ghost" onClick={onLogout}>Log out</button>
                </div>
            </header>

            <main className="content">
                <div className="content-head">
                    <h1>All Properties</h1>
                </div>
                <p className="content-note">
                    Browse available properties. Click on an owner's name to view their full profile.
                </p>
                {note && <p className="content-note" style={{ color: '#B4483C' }}>{note}</p>}

                {properties.length === 0 ? (
                    <p className="empty-state">No properties to display at the moment.</p>
                ) : (
                    <table className="property-table">
                        <thead>
                        <tr>
                            <th>Title</th>
                            <th>Address</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Owner</th>
                        </tr>
                        </thead>
                        <tbody>
                        {properties.map(p => (
                            <tr key={p.id}>
                                <td>{p.title}</td>
                                <td>{p.address}</td>
                                <td>{p.description || '—'}</td>
                                <td className="price">€{priceFormatter.format(p.price)}</td>
                                <td>
                                    {p.ownerId
                                        ? <Link className="owner-link" to={`/owner/${p.ownerId}`}>{p.ownerName || 'View Profile'}</Link>
                                        : '—'}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </main>
        </section>
    );
}