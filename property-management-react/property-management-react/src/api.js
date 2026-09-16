const API_BASE = 'http://localhost:8080/api/v1';

function getToken() {
    return localStorage.getItem('token');
}

async function request(path, options = {}) {
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
    const token = getToken();
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
    const isJson = res.headers.get('content-type')?.includes('application/json');
    const data = isJson ? await res.json() : null;

    if (!res.ok) {
        const error = new Error(data?.error || `Request failed (${res.status})`);
        error.status = res.status;
        throw error;
    }
    return data;
}

export const api = {
    register: (body) => request('/auth/register', { method: 'POST', body: JSON.stringify(body) }),
    login: (body) => request('/auth/login', { method: 'POST', body: JSON.stringify(body) }),
    googleLogin: (body) => request('/auth/google', { method: 'POST', body: JSON.stringify(body) }),
    getProperties: () => request('/properties'),
    addProperty: (body) => request('/properties/save', { method: 'POST', body: JSON.stringify(body) }),
    updateProperty: (id, body) => request(`/properties/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
    deleteProperty: (id) => request(`/properties/${id}`, { method: 'DELETE' }),
    getOwnerProfile: (ownerId) => request(`/owners/${ownerId}`),
};
