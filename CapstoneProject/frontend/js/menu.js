let allMenuItems = [];
let currentFilter = 'all';

// Load menu items
document.addEventListener('DOMContentLoaded', () => {
    loadMenuItems();
});

// Helper to get Token
function getMyToken() {
    if (typeof getAuthToken === 'function') return getAuthToken();
    return localStorage.getItem('authToken') || localStorage.getItem('token');
}

async function loadMenuItems() {
    const user = getCurrentUser();
    const isAdminUser = user && user.role === 'ADMIN';
    
    try {
        const endpoint = isAdminUser 
            ? `${API_BASE_URL}/menu/admin/menu` 
            : `${API_BASE_URL}/menu/menu`;
        
        const response = await fetch(endpoint, {
            headers: isAdminUser ? {
                'Authorization': `Bearer ${getMyToken()}`
            } : {}
        });

        if (response.ok) {
            allMenuItems = await response.json();
            displayMenuItems(allMenuItems);
        } else {
            document.getElementById('menuItems').innerHTML = 
                '<p class="error-message">Error loading menu items</p>';
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('menuItems').innerHTML = 
            '<p class="error-message">Error loading menu items</p>';
    }
}

// Display menu items
function displayMenuItems(items) {
    const container = document.getElementById('menuItems');
    
    if (!container) return; 

    if (items.length === 0) {
        container.innerHTML = '<p class="empty-state">No menu items available</p>';
        return;
    }

    const user = getCurrentUser();
    const isAdminUser = user && user.role === 'ADMIN';

    container.innerHTML = items.map(item => `
        <div class="menu-item ${!item.available ? 'unavailable' : ''} fade-in" 
             style="display: flex; flex-direction: column; height: 100%; min-height: 380px;">
            
            <img src="${item.imageUrl}" alt="${item.name}" 
                 style="width:100%; height:200px; object-fit:cover; border-radius:4px;" 
                 onerror="this.src='https://via.placeholder.com/280x200?text=Pizza'">
            
            <div class="menu-item-content" style="display: flex; flex-direction: column; flex-grow: 1; padding: 15px;">
                
                <h3 style="margin-top:0; margin-bottom: 10px;">${item.name}</h3>
                
                <p style="flex-grow: 1; margin-bottom: 20px; line-height: 1.5; color: #aaa; font-size: 0.95em;">
                    ${item.description}
                </p>
                
                <div class="menu-item-footer" style="margin-top: auto; display:flex; justify-content:space-between; align-items:center;">
                    <span class="price" style="font-weight:bold; font-size:1.2em;">₹${item.price.toFixed(2)}</span>
                    
                    ${isAdminUser ? `
                        <div style="display:flex; gap: 5px;">
                            <button onclick="toggleAvailability(${item.id}, ${!item.available})" 
                                    class="btn btn-small ${item.available ? 'btn-warning' : 'btn-success'}"
                                    style="pointer-events: auto; cursor: pointer; z-index: 10;">
                                ${item.available ? 'Disable' : 'Enable'}
                            </button>
                            
                            <button onclick="deleteMenuItem(${item.id})" 
                                    class="btn btn-small btn-danger"
                                    style="pointer-events: auto; cursor: pointer; z-index: 10;">
                                Delete
                            </button>
                        </div>
                    ` : item.available ? `
                        <button onclick='addToCartFromMenu(${JSON.stringify(item)})' class="btn btn-small btn-primary">Add to Cart</button>
                    ` : '<span style="color:red; font-weight:bold;">Sold Out</span>'}
                </div>
            </div>
        </div>
    `).join('');
}

// Filter menu
function filterMenu(category) {
    currentFilter = category;
    
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    if(typeof event !== 'undefined' && event.target) {
        event.target.classList.add('active');
    }

    if (category === 'all') {
        displayMenuItems(allMenuItems);
    } else {
        const filtered = allMenuItems.filter(item => item.category === category);
        displayMenuItems(filtered);
    }
}

function addToCartFromMenu(item) {
    if(typeof addToCart === 'function') {
        addToCart(item);
        if(typeof updateCartCount === 'function') updateCartCount();
    }
}

// Toggle Availability
async function toggleAvailability(id, targetStatus) {
    try {
        const response = await fetch(`${API_BASE_URL}/menu/admin/menu/${id}/availability?available=${targetStatus}`, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${getMyToken()}` }
        });

        if (response.ok) {
            loadMenuItems();
        } else {
            console.error('Server error:', response.status);
            alert('Error updating availability');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error updating availability');
    }
}

// Delete Item
async function deleteMenuItem(id) {
    if (!confirm('Are you sure you want to delete this menu item?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/menu/admin/menu/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${getMyToken()}` }
        });

        if (response.ok) {
            alert('Menu item deleted successfully!');
            loadMenuItems();
        } else {
            alert('Error deleting menu item');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error deleting menu item');
    }
}