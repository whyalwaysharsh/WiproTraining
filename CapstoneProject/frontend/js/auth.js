document.addEventListener('DOMContentLoaded', () => {
    // We define the URL *inside* here so it never conflicts with other files
    const GATEWAY_URL = 'http://localhost:8083';
    
    console.log("Auth System Loaded. Target:", GATEWAY_URL);

    const loginForm = document.getElementById('loginForm');
    if (loginForm) loginForm.addEventListener('submit', (e) => handleLogin(e, GATEWAY_URL));

    const registerForm = document.getElementById('registerForm');
    if (registerForm) registerForm.addEventListener('submit', (e) => handleRegister(e, GATEWAY_URL));

    const adminLoginForm = document.getElementById('adminLoginForm');
    if (adminLoginForm) adminLoginForm.addEventListener('submit', (e) => handleAdminLogin(e, GATEWAY_URL));
});

// --- FUNCTIONS ---

async function handleLogin(e, baseUrl) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');

    try {
        const response = await fetch(`${baseUrl}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        handleResponse(response, errorMessage, 'user-dashboard.html', 'user');
    } catch (error) {
        console.error(error);
        if(errorMessage) errorMessage.textContent = 'Network Error';
    }
}

async function handleRegister(e, baseUrl) {
    e.preventDefault();
    // Gather form data...
    const inputs = e.target.querySelectorAll('input, textarea');
    const data = {};
    inputs.forEach(input => data[input.id] = input.value);
    
    const errorMessage = document.getElementById('errorMessage');

    try {
        const response = await fetch(`${baseUrl}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        handleResponse(response, errorMessage, 'user-dashboard.html', 'user');
    } catch (error) {
        console.error(error);
        if(errorMessage) errorMessage.textContent = 'Network Error';
    }
}

async function handleAdminLogin(e, baseUrl) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');

    try {
        const response = await fetch(`${baseUrl}/admin/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        handleResponse(response, errorMessage, 'admin-dashboard.html', 'admin');
    } catch (error) {
        console.error(error);
        if(errorMessage) errorMessage.textContent = 'Network Error';
    }
}

// Helper to clean up logic
async function handleResponse(response, errorElement, redirectUrl, userKey) {
    // Parse JSON safely
    const text = await response.text();
    let data = {};
    try { data = text ? JSON.parse(text) : {}; } catch(e) {}

    if (response.ok) {
        localStorage.setItem('token', data.token);
        // Store user/admin object based on key (data.user or data.admin)
        const userData = data[userKey] || data.user || data.admin; 
        localStorage.setItem('user', JSON.stringify(userData));
        window.location.href = redirectUrl;
    } else {
        if(errorElement) errorElement.textContent = data.message || 'Action failed';
    }
}