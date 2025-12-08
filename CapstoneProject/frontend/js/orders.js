let allOrders = [];
let currentStatusFilter = 'all';

document.addEventListener('DOMContentLoaded', () => {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = 'login.html';
        return;
    }

    if (user.role === 'ADMIN') {
        document.getElementById('ordersTitle').textContent = 'All Orders';
        document.getElementById('adminFilters').style.display = 'flex';
        loadAllOrders();
    } else {
        loadUserOrders();
    }
});

// Load user orders
async function loadUserOrders() {
    const user = getCurrentUser();
    
    try {
        const response = await fetch(`${API_BASE_URL}/orders/user/${user.id}`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.ok) {
            allOrders = await response.json();
            displayOrders(allOrders);
        } else {
            document.getElementById('ordersList').innerHTML = 
                '<p class="error-message">Error loading orders</p>';
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('ordersList').innerHTML = 
            '<p class="error-message">Error loading orders</p>';
    }
}

// Load all orders (Admin)
async function loadAllOrders() {
    try {
        const response = await fetch(`${API_BASE_URL}/orders`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.ok) {
            allOrders = await response.json();
            displayOrders(allOrders);
        } else {
            document.getElementById('ordersList').innerHTML = 
                '<p class="error-message">Error loading orders</p>';
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('ordersList').innerHTML = 
            '<p class="error-message">Error loading orders</p>';
    }
}

// Display orders
function displayOrders(orders) {
    const container = document.getElementById('ordersList');
    const user = getCurrentUser();
    const isAdminUser = user && user.role === 'ADMIN';

    if (orders.length === 0) {
        container.innerHTML = '<p class="empty-state">No orders found</p>';
        return;
    }

    container.innerHTML = orders.map(order => `
        <div class="order-card fade-in">
            <div class="order-header">
                <div>
                    <h3>Order #${order.id}</h3>
                    <p>${new Date(order.createdAt).toLocaleString()}</p>
                    ${isAdminUser ? `<p><strong>User ID:</strong> ${order.userId}</p>` : ''}
                </div>
                <span class="order-status status-${order.status}">${order.status.replace('_', ' ')}</span>
            </div>
            
            <div class="order-items">
                <h4>Items:</h4>
                ${order.orderItems.map(item => `
                    <div class="order-item">
                        <span>${item.menuItemName} x ${item.quantity}</span>
                        <span>₹${item.subtotal.toFixed(2)}</span>
                    </div>
                `).join('')}
            </div>

            <div>
                <p><strong>Delivery Address:</strong> ${order.deliveryAddress}</p>
                <p><strong>Phone:</strong> ${order.phoneNumber}</p>
                ${order.specialInstructions ? `<p><strong>Special Instructions:</strong> ${order.specialInstructions}</p>` : ''}
            </div>

            <div class="order-footer">
                <div>
                    <strong>Total: ₹${order.totalAmount.toFixed(2)}</strong>
                </div>
                <div class="order-actions">
                    ${!isAdminUser && (order.status === 'PENDING' || order.status === 'CONFIRMED') ? `
                        <button onclick="cancelOrder(${order.id})" class="btn btn-small btn-danger">Cancel Order</button>
                    ` : ''}
                    <button onclick="viewBill(${order.id})" class="btn btn-small btn-secondary">View Bill</button>
                </div>
            </div>

            ${isAdminUser ? `
                <div class="admin-controls">
                    <select id="status-${order.id}" class="status-select">
                        <option value="PENDING" ${order.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                        <option value="CONFIRMED" ${order.status === 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                        <option value="PREPARING" ${order.status === 'PREPARING' ? 'selected' : ''}>Preparing</option>
                        <option value="OUT_FOR_DELIVERY" ${order.status === 'OUT_FOR_DELIVERY' ? 'selected' : ''}>Out for Delivery</option>
                        <option value="DELIVERED" ${order.status === 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                        <option value="CANCELLED" ${order.status === 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                    </select>
                    <button onclick="updateOrderStatus(${order.id})" class="btn btn-small btn-primary">Update Status</button>
                </div>
            ` : ''}
        </div>
    `).join('');
}

// Filter orders by status (Admin)
function filterOrders(status) {
    currentStatusFilter = status;
    
    // Update active filter button
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    // Filter orders
    if (status === 'all') {
        displayOrders(allOrders);
    } else {
        const filtered = allOrders.filter(order => order.status === status);
        displayOrders(filtered);
    }
}

// Update order status (Admin)
async function updateOrderStatus(orderId) {
    const newStatus = document.getElementById(`status-${orderId}`).value;

    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}/status?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.ok) {
            alert('Order status updated successfully!');
            loadAllOrders();
        } else {
            alert('Error updating order status');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error updating order status');
    }
}

// Cancel order
async function cancelOrder(orderId) {
    if (!confirm('Are you sure you want to cancel this order?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.ok) {
            alert('Order cancelled successfully!');
            loadUserOrders();
        } else {
            alert('Error cancelling order');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error cancelling order');
    }
}

// View bill
async function viewBill(orderId) {
    try {
        const response = await fetch(`${API_BASE_URL}/billing/order/${orderId}`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.ok) {
            const bill = await response.json();
            window.location.href = `billing.html?billId=${bill.id}`;
        } else {
            alert('Bill not found for this order');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error loading bill');
    }
}