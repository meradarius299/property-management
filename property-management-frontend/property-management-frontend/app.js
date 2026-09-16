const API_BASE = 'http://localhost:8080/api/v1';

const authScreen = document.getElementById('auth-screen');
const dashboardScreen = document.getElementById('dashboard-screen');

const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const loginMessage = document.getElementById('login-message');
const registerMessage = document.getElementById('register-message');

const authTabs = document.querySelectorAll('.auth-tab');

const roleBadge = document.getElementById('role-badge');
const usernameDisplay = document.getElementById('username-display');
const logoutBtn = document.getElementById('logout-btn');

const contentTitle = document.getElementById('content-title');
const contentNote = document.getElementById('content-note');
const addPropertyBtn = document.getElementById('add-property-btn');
const addPropertyForm = document.getElementById('add-property-form');

const tableHead = document.getElementById('table-head');
const tableBody = document.getElementById('table-body');
const emptyState = document.getElementById('empty-state');

function saveSession(token, username, role) {
    localStorage.setItem('token', token);
    localStorage.setItem('username', username);
    localStorage.setItem('role', role);
}

function getSession() {
    return {
        token: localStorage.getItem('token'),
        username: localStorage.getItem('username'),
        role: localStorage.getItem('role')
    };
}

function clearSession() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
}

authTabs.forEach(tab => {
    tab.addEventListener('click', () => {
        authTabs.forEach(t => t.classList.remove('is-active'));
        tab.classList.add('is-active');
        const isLogin = tab.dataset.tab === 'login';
        loginForm.classList.toggle('is-hidden', !isLogin);
        registerForm.classList.toggle('is-hidden', isLogin);
    });
});

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    registerMessage.textContent = '';
    registerMessage.classList.remove('is-success');

    const body = {
        username: document.getElementById('register-username').value.trim(),
        email: document.getElementById('register-email').value.trim(),
        password: document.getElementById('register-password').value,
        role: document.getElementById('register-role').value
    };

    try {
        const res = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const data = await res.json();
        if (!res.ok) {
            registerMessage.textContent = data.error || 'Could not create account.';
            return;
        }
        registerMessage.textContent = 'Account created. You can now log in.';
        registerMessage.classList.add('is-success');
        registerForm.reset();
    } catch (err) {
        registerMessage.textContent = 'Connection error: ' + err.message;
    }
});

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    loginMessage.textContent = '';

    const body = {
        username: document.getElementById('login-username').value.trim(),
        password: document.getElementById('login-password').value
    };

    try {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const data = await res.json();
        if (!res.ok) {
            loginMessage.textContent = data.error || 'Authentication failed.';
            return;
        }
        saveSession(data.token, data.username, data.role);
        enterDashboard();
    } catch (err) {
        loginMessage.textContent = 'Connection error: ' + err.message;
    }
});

logoutBtn.addEventListener('click', () => {
    clearSession();
    dashboardScreen.classList.add('is-hidden');
    authScreen.classList.remove('is-hidden');
    loginForm.reset();
});

function enterDashboard() {
    const { username, role } = getSession();
    authScreen.classList.add('is-hidden');
    dashboardScreen.classList.remove('is-hidden');

    usernameDisplay.textContent = username;
    roleBadge.textContent = role === 'OWNER' ? 'OWNER' : 'BUYER';

    const isOwner = role === 'OWNER';
    addPropertyBtn.classList.toggle('is-hidden', !isOwner);
    contentTitle.textContent = isOwner ? 'My Properties' : 'Available Properties';
    contentNote.textContent = isOwner
        ? 'View all details of your properties, including contact information.'
        : 'View properties available for sale. Owner contact details are visible to owners only.';

    renderTableHead(isOwner);
    loadProperties();
}

function renderTableHead(isOwner) {
    tableHead.innerHTML = isOwner
        ? `<tr><th>Title</th><th>Address</th><th>Description</th><th>Price</th><th>Owner</th><th>Email</th><th></th></tr>`
        : `<tr><th>Title</th><th>Address</th><th>Description</th><th>Price</th></tr>`;
}

async function loadProperties() {
    const { token } = getSession();
    try {
        const res = await fetch(`${API_BASE}/properties`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (res.status === 401) {
            clearSession();
            dashboardScreen.classList.add('is-hidden');
            authScreen.classList.remove('is-hidden');
            return;
        }
        const properties = await res.json();
        renderTable(properties);
    } catch (err) {
        contentNote.textContent = 'Error loading list: ' + err.message;
    }
}

function renderTable(properties) {
    const { role } = getSession();
    const isOwner = role === 'OWNER';
    tableBody.innerHTML = '';

    emptyState.classList.toggle('is-hidden', properties.length > 0);

    for (const p of properties) {
        const row = document.createElement('tr');
        const priceFormatted = new Intl.NumberFormat('en-US', { maximumFractionDigits: 0 }).format(p.price) + ' €';

        if (isOwner) {
            row.innerHTML = `
                <td>${escapeHtml(p.title)}</td>
                <td>${escapeHtml(p.address)}</td>
                <td>${escapeHtml(p.description || '—')}</td>
                <td class="price">${priceFormatted}</td>
                <td>${escapeHtml(p.ownerName || '—')}</td>
                <td>${escapeHtml(p.ownerEmail || '—')}</td>
                <td class="actions"><button class="delete-link" data-id="${p.id}">Delete</button></td>
            `;
        } else {
            row.innerHTML = `
                <td>${escapeHtml(p.title)}</td>
                <td>${escapeHtml(p.address)}</td>
                <td>${escapeHtml(p.description || '—')}</td>
                <td class="price">${priceFormatted}</td>
            `;
        }
        tableBody.appendChild(row);
    }

    document.querySelectorAll('.delete-link').forEach(btn => {
        btn.addEventListener('click', () => deleteProperty(btn.dataset.id));
    });
}

function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

addPropertyBtn.addEventListener('click', () => {
    addPropertyForm.classList.toggle('is-hidden');
});

addPropertyForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const { token } = getSession();

    const body = {
        title: document.getElementById('prop-title').value.trim(),
        address: document.getElementById('prop-address').value.trim(),
        price: parseFloat(document.getElementById('prop-price').value),
        description: document.getElementById('prop-description').value.trim()
    };

    try {
        const res = await fetch(`${API_BASE}/properties/save`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(body)
        });
        if (!res.ok) {
            const data = await res.json();
            contentNote.textContent = data.error || 'Could not save property.';
            return;
        }
        addPropertyForm.reset();
        addPropertyForm.classList.add('is-hidden');
        await loadProperties();
    } catch (err) {
        contentNote.textContent = 'Error saving: ' + err.message;
    }
});

async function deleteProperty(id) {
    const { token } = getSession();
    try {
        const res = await fetch(`${API_BASE}/properties/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (res.ok) {
            await loadProperties();
        }
    } catch (err) {
        contentNote.textContent = 'Error deleting: ' + err.message;
    }
}

(function init() {
    const { token } = getSession();
    if (token) {
        enterDashboard();
    }
})();