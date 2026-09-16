import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { api } from './api.js'

const priceFormatter = new Intl.NumberFormat('en-US', { maximumFractionDigits: 0 });

export default function OwnerProfilePage({ session, onLogout }) {
    const { ownerId } = useParams();
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState('');
    const [showAddForm, setShowAddForm] = useState(false);
    const [editingId, setEditingId] = useState(null);

    const isOwnProfile = String(session.id) === String(ownerId);
    const canManage = isOwnProfile && session.role === 'OWNER';

    useEffect(() => {
        loadProfile();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [ownerId]);

    function loadProfile() {
        api.getOwnerProfile(ownerId)
            .then(setProfile)
            .catch(err => setError(err.message));
    }

    async function handleAdd(newProperty) {
        try {
            await api.addProperty(newProperty);
            setShowAddForm(false);
            loadProfile();
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleUpdate(id, updatedProperty) {
        try {
            await api.updateProperty(id, updatedProperty);
            setEditingId(null);
            loadProfile();
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleDelete(id) {
        try {
            await api.deleteProperty(id);
            loadProfile();
        } catch (err) {
            setError(err.message);
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
                <Link to="/" className="back-link">← Back to list</Link>

                {error && <p className="content-note" style={{ color: '#B4483C' }}>{error}</p>}

                {profile && (
                    <>
                        <div className="content-head">
                            <h1>{isOwnProfile ? 'My Profile' : profile.username}</h1>
                            {canManage && (
                                <button className="btn-primary" onClick={() => setShowAddForm(s => !s)}>
                                    Add Property
                                </button>
                            )}
                        </div>
                        <p className="content-note">{profile.email}</p>

                        {canManage && showAddForm && <AddPropertyForm onSubmit={handleAdd} />}

                        <h2 className="section-subtitle">Listed Properties</h2>

                        {profile.properties.length === 0 ? (
                            <p className="empty-state">
                                {isOwnProfile ? 'You have not listed any properties yet.' : 'This owner has no listed properties at the moment.'}
                            </p>
                        ) : (
                            <table className="property-table">
                                <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Address</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    {canManage && <th></th>}
                                </tr>
                                </thead>
                                <tbody>
                                {profile.properties.map(p =>
                                    canManage && editingId === p.id ? (
                                        <EditableRow key={p.id} property={p} onCancel={() => setEditingId(null)} onSave={handleUpdate} />
                                    ) : (
                                        <tr key={p.id}>
                                            <td>{p.title}</td>
                                            <td>{p.address}</td>
                                            <td>{p.description || '—'}</td>
                                            <td className="price">€{priceFormatter.format(p.price)}</td>
                                            {canManage && (
                                                <td className="actions">
                                                    <button className="edit-link" onClick={() => setEditingId(p.id)}>Edit</button>
                                                    <button className="delete-link" onClick={() => handleDelete(p.id)}>Delete</button>
                                                </td>
                                            )}
                                        </tr>
                                    )
                                )}
                                </tbody>
                            </table>
                        )}
                    </>
                )}
            </main>
        </section>
    );
}

function AddPropertyForm({ onSubmit }) {
    const [title, setTitle] = useState('');
    const [address, setAddress] = useState('');
    const [price, setPrice] = useState('');
    const [description, setDescription] = useState('');

    function handleSubmit(e) {
        e.preventDefault();
        onSubmit({ title, address, price: parseFloat(price), description });
    }

    return (
        <form className="property-form" onSubmit={handleSubmit}>
            <input placeholder="Title" value={title} onChange={e => setTitle(e.target.value)} required />
            <input placeholder="Address" value={address} onChange={e => setAddress(e.target.value)} required />
            <input placeholder="Price (EUR)" type="number" step="0.01" value={price} onChange={e => setPrice(e.target.value)} required />
            <input placeholder="Description" value={description} onChange={e => setDescription(e.target.value)} />
            <button type="submit" className="btn-primary">Save</button>
        </form>
    );
}

function EditableRow({ property, onCancel, onSave }) {
    const [title, setTitle] = useState(property.title);
    const [address, setAddress] = useState(property.address);
    const [price, setPrice] = useState(property.price);
    const [description, setDescription] = useState(property.description || '');

    function handleSave() {
        onSave(property.id, { title, address, price: parseFloat(price), description });
    }

    return (
        <tr className="editing-row">
            <td><input value={title} onChange={e => setTitle(e.target.value)} /></td>
            <td><input value={address} onChange={e => setAddress(e.target.value)} /></td>
            <td><input value={description} onChange={e => setDescription(e.target.value)} /></td>
            <td><input type="number" step="0.01" value={price} onChange={e => setPrice(e.target.value)} /></td>
            <td className="actions">
                <button className="save-link" onClick={handleSave}>Save</button>
                <button className="cancel-link" onClick={onCancel}>Cancel</button>
            </td>
        </tr>
    );
}