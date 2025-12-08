// --- 1. SECURITY GUARD (Runs Immediately) ---
(function checkAccess() {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    const path = window.location.pathname;

    // List of keywords that mark a page as "Protected"
    const protectedPages = ['dashboard', 'orders', 'cart'];
    
    // Check if the current page is protected
    const isProtected = protectedPages.some(page => path.includes(page));

    if (isProtected) {
        // CASE A: User is NOT Logged In
        if (!token) {
            console.log("Access Denied: No token found");
            
            // FIX: If trying to access an Admin page, send to Admin Login
            if (path.includes('admin')) {
                window.location.href = 'admin-login.html';
            } else {
                // Otherwise, send to User Login
                window.location.href = 'login.html';
            }
            return;
        }

        // CASE B: User IS Logged In, but checking Role
        if (path.includes('admin') && userStr) {
            const user = JSON.parse(userStr);
            if (user.role !== 'ADMIN') {
                alert("Access Denied: Admins Only");
                window.location.href = 'user-dashboard.html';
            }
        }
    }
})();

// --- 2. APP LOGIC ---

// API Base URL
const API_BASE_URL = 'http://localhost:8083';

// Get current user from localStorage
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

// Get auth token
function getAuthToken() {
    return localStorage.getItem('token');
}

// Check if user is authenticated
function isAuthenticated() {
    return !!getAuthToken();
}

// Check if user is admin
function isAdmin() {
    const user = getCurrentUser();
    return user && user.role === 'ADMIN';
}

// Logout function (Hardened)
function logout() {
    if(confirm('Are you sure you want to logout?')) {
        // Clear EVERYTHING to ensure no data is left behind
        localStorage.clear(); 
        // Redirect to login (default to user login)
        window.location.href = 'login.html';
    }
}

// Setup logout button and Init
document.addEventListener('DOMContentLoaded', () => {
    // Attach listener to any logout button
    const logoutBtns = document.querySelectorAll('#logoutBtn');
    logoutBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    });

    // Load dashboards based on URL
    if (window.location.pathname.includes('user-dashboard.html')) {
        loadUserDashboard();
    } else if (window.location.pathname.includes('admin-dashboard.html')) {
        loadAdminDashboard();
    }

    // Setup navigation based on user role
    setupNavigation();
});

// Setup navigation
function setupNavigation() {
    const mainNav = document.getElementById('mainNav');
    if (!mainNav) return;

    const user = getCurrentUser();
    if (!user) {
        // Public View
        mainNav.innerHTML = `
            <a href="index.html">Home</a>
            <a href="menu.html">Menu</a>
            <a href="login.html">Login</a>
            <a href="register.html">Register</a>
        `;
        return;
    }

    if (user.role === 'ADMIN') {
        mainNav.innerHTML = `
            <a href="admin-dashboard.html">Dashboard</a>
            <a href="menu.html">Manage Menu</a>
            <a href="orders.html">All Orders</a>
            <button id="logoutBtn" class="btn btn-small" onclick="logout()">Logout</button>
        `;
    } else {
        const cartCount = getCartCount();
        mainNav.innerHTML = `
            <a href="user-dashboard.html">Dashboard</a>
            <a href="menu.html">Menu</a>
            <a href="cart.html">Cart <span id="cartCount" class="badge">${cartCount}</span></a>
            <a href="orders.html">My Orders</a>
            <button id="logoutBtn" class="btn btn-small" onclick="logout()">Logout</button>
        `;
    }
}

// Update cart count
function updateCartCount() {
    const cartCount = getCartCount();
    const cartCountElements = document.querySelectorAll('#cartCount');
    cartCountElements.forEach(el => {
        el.textContent = cartCount;
    });
}

// Get cart count
function getCartCount() {
    const cart = getCart();
    return cart.reduce((total, item) => total + item.quantity, 0);
}

// Get cart from localStorage
function getCart() {
    const cartStr = localStorage.getItem('cart');
    return cartStr ? JSON.parse(cartStr) : [];
}

// Save cart to localStorage
function saveCart(cart) {
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();
}

// Add to cart
function addToCart(menuItem) {
    const cart = getCart();
    const existingItem = cart.find(item => item.id === menuItem.id);

    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            id: menuItem.id,
            name: menuItem.name,
            price: menuItem.price,
            imageUrl: menuItem.imageUrl,
            quantity: 1
        });
    }

    saveCart(cart);
    alert(`${menuItem.name} added to cart!`);
    updateCartCount();
}

// Load User Dashboard
async function loadUserDashboard() {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = 'login.html';
        return;
    }

    const userNameEl = document.getElementById('userName');
    if (userNameEl) {
        userNameEl.textContent = user.fullName || user.username;
    }

    try {
        const ordersResponse = await fetch(`${API_BASE_URL}/orders/user/${user.id}`, {
            headers: { 'Authorization': `Bearer ${getAuthToken()}` }
        });

        if (ordersResponse.ok) {
            const orders = await ordersResponse.json();
            const totalOrdersEl = document.getElementById('totalOrders');
            if(totalOrdersEl) totalOrdersEl.textContent = orders.length;
            
            const pendingOrders = orders.filter(o => 
                o.status === 'PENDING' || o.status === 'CONFIRMED' || o.status === 'PREPARING'
            );
            const pendingOrdersEl = document.getElementById('pendingOrders');
            if(pendingOrdersEl) pendingOrdersEl.textContent = pendingOrders.length;

            displayRecentOrders(orders.slice(0, 5));
        }

        // Notifications (Optional)
        const unreadEl = document.getElementById('unreadNotifications');
        if(unreadEl) {
            const notifResponse = await fetch(`${API_BASE_URL}/notifications/user/${user.id}/unread-count`, {
                headers: { 'Authorization': `Bearer ${getAuthToken()}` }
            });
            if (notifResponse.ok) {
                const data = await notifResponse.json();
                unreadEl.textContent = data.unreadCount;
            }
        }
        
        const notifListEl = document.getElementById('notificationsList');
        if(notifListEl) {
            const notificationsResponse = await fetch(`${API_BASE_URL}/notifications/user/${user.id}`, {
                headers: { 'Authorization': `Bearer ${getAuthToken()}` }
            });
            if (notificationsResponse.ok) {
                const notifications = await notificationsResponse.json();
                displayNotifications(notifications.slice(0, 5));
            }
        }

    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

// Display Recent Orders
function displayRecentOrders(orders) {
    const container = document.getElementById('recentOrdersList');
    if (!container) return;

    if (orders.length === 0) {
        container.innerHTML = '<p class="empty-state">No orders yet</p>';
        return;
    }

    container.innerHTML = orders.map(order => `
        <div class="order-card">
            <div class="order-header">
                <div>
                    <strong>Order #${order.id}</strong>
                    <p>${new Date(order.createdAt || order.orderDate).toLocaleDateString()}</p>
                </div>
                <span class="order-status status-${order.status}">${order.status}</span>
            </div>
            <div class="order-footer">
                <strong>Total: ₹${order.totalAmount.toFixed(2)}</strong>
                <a href="orders.html" class="btn btn-small btn-secondary">View Details</a>
            </div>
        </div>
    `).join('');
}

// Display Notifications
function displayNotifications(notifications) {
    const container = document.getElementById('notificationsList');
    if (!container) return;

    if (notifications.length === 0) {
        container.innerHTML = '<p class="empty-state">No notifications</p>';
        return;
    }

    container.innerHTML = notifications.map(notif => `
        <div class="notification-item ${notif.isRead ? '' : 'unread'}">
            <div class="notification-content">
                <p>${notif.message}</p>
                <span class="notification-time">${new Date(notif.createdAt).toLocaleString()}</span>
            </div>
        </div>
    `).join('');
}

// Load Admin Dashboard
async function loadAdminDashboard() {
    const user = getCurrentUser();
    if (!user || user.role !== 'ADMIN') {
        window.location.href = 'login.html';
        return;
    }

    try {
        const ordersResponse = await fetch(`${API_BASE_URL}/orders`, {
            headers: { 'Authorization': `Bearer ${getAuthToken()}` }
        });

        if (ordersResponse.ok) {
            const orders = await ordersResponse.json();
            const totalOrdersEl = document.getElementById('totalOrders');
            if(totalOrdersEl) totalOrdersEl.textContent = orders.length;
            
            const pendingOrders = orders.filter(o => o.status === 'PENDING');
            const pendingOrdersEl = document.getElementById('pendingOrders');
            if(pendingOrdersEl) pendingOrdersEl.textContent = pendingOrders.length;

            displayRecentOrders(orders.slice(0, 5));
        }

        const menuResponse = await fetch(`${API_BASE_URL}/menu/admin/menu`, {
            headers: { 'Authorization': `Bearer ${getAuthToken()}` }
        });

        if (menuResponse.ok) {
            const menuItems = await menuResponse.json();
            const totalMenuEl = document.getElementById('totalMenuItems');
            if(totalMenuEl) totalMenuEl.textContent = menuItems.length;
        }

        // Revenue Load Fallback (Logic is mostly in admin-dashboard.html now)
        try {
            const today = new Date();
            const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
            const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
            
            const revenueResponse = await fetch(
                `${API_BASE_URL}/admin/revenue/total?startDate=${firstDay.toISOString().split('T')[0]}&endDate=${lastDay.toISOString().split('T')[0]}`,
                { headers: { 'Authorization': `Bearer ${getAuthToken()}` } }
            );

            if (revenueResponse.ok) {
                const revenueData = await revenueResponse.json();
                const totalRevEl = document.getElementById('totalRevenue');
                if(totalRevEl) totalRevEl.textContent = revenueData.totalRevenue.toFixed(2);
            }
        } catch(e) { /* Ignore - handled by frontend script in HTML */ }

    } catch (error) {
        console.error('Error loading admin dashboard:', error);
    }
}

// Show/Hide Modal
function showAddMenuItemModal() {
    const modal = document.getElementById('addMenuModal');
    if(modal) modal.style.display = 'block';
}

function closeAddMenuItemModal() {
    const modal = document.getElementById('addMenuModal');
    if(modal) modal.style.display = 'none';
}

// Add Menu Item Form
document.addEventListener('DOMContentLoaded', () => {
    const addMenuItemForm = document.getElementById('addMenuItemForm');
    if (addMenuItemForm) {
        addMenuItemForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const menuItem = {
                name: document.getElementById('menuItemName').value,
                description: document.getElementById('menuItemDescription').value,
                price: parseFloat(document.getElementById('menuItemPrice').value),
                category: document.getElementById('menuItemCategory').value,
                imageUrl: document.getElementById('menuItemImage').value,
                available: document.getElementById('menuItemAvailable').checked
            };

            try {
                const response = await fetch(`${API_BASE_URL}/menu/admin/menu/add`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${getAuthToken()}`
                    },
                    body: JSON.stringify(menuItem)
                });

                if (response.ok) {
                    alert('Menu item added successfully!');
                    closeAddMenuItemModal();
                    addMenuItemForm.reset();
                    window.location.reload();
                } else {
                    const error = await response.json();
                    alert('Error: ' + (error.message || 'Failed to add menu item'));
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Error adding menu item');
            }
        });
    }
});

// Load Revenue Report (Kept for compatibility)
async function loadRevenue() {
    // ... logic mostly removed from UI but function kept to prevent errors
    console.log("Revenue report loading...");
}

// Format date helper
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}