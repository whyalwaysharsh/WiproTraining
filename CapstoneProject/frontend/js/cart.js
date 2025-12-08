document.addEventListener('DOMContentLoaded', () => {
    loadCart();
    loadUserDeliveryInfo();
});

// Load cart items
function loadCart() {
    const cart = getCart();
    displayCartItems(cart);
    updateCartSummary(cart);
}

// Display cart items
function displayCartItems(cart) {
    const container = document.getElementById('cartItems');
    
    if (cart.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>Your cart is empty</h3>
                <p>Add some delicious pizzas to get started!</p>
                <a href="menu.html" class="btn btn-primary">Browse Menu</a>
            </div>
        `;
        return;
    }

    container.innerHTML = cart.map(item => `
        <div class="cart-item">
            <img src="${item.imageUrl}" alt="${item.name}" onerror="this.src='https://via.placeholder.com/80?text=Pizza'">
            <div class="cart-item-details">
                <h4>${item.name}</h4>
                <p>₹${item.price.toFixed(2)} each</p>
            </div>
            <div class="cart-item-controls">
                <div class="quantity-controls">
                    <button onclick="updateQuantity(${item.id}, ${item.quantity - 1})">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
                </div>
                <p><strong>₹${(item.price * item.quantity).toFixed(2)}</strong></p>
                <button onclick="removeFromCart(${item.id})" class="btn btn-small btn-danger">Remove</button>
            </div>
        </div>
    `).join('');
}

// Update quantity
function updateQuantity(itemId, newQuantity) {
    if (newQuantity < 1) {
        removeFromCart(itemId);
        return;
    }

    const cart = getCart();
    const item = cart.find(i => i.id === itemId);
    if (item) {
        item.quantity = newQuantity;
        saveCart(cart);
        loadCart();
    }
}

// Remove from cart
function removeFromCart(itemId) {
    const cart = getCart();
    const updatedCart = cart.filter(item => item.id !== itemId);
    saveCart(updatedCart);
    loadCart();
}

// Update cart summary
function updateCartSummary(cart) {
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const tax = subtotal * 0.05; // 5% tax
    const deliveryCharge = 50;
    const total = subtotal + tax + deliveryCharge;

    document.getElementById('subtotal').textContent = subtotal.toFixed(2);
    document.getElementById('tax').textContent = tax.toFixed(2);
    document.getElementById('deliveryCharge').textContent = deliveryCharge.toFixed(2);
    document.getElementById('total').textContent = total.toFixed(2);
}

// Load user delivery info
function loadUserDeliveryInfo() {
    const user = getCurrentUser();
    if (user) {
        document.getElementById('deliveryAddress').value = user.address || '';
        document.getElementById('phoneNumber').value = user.phoneNumber || '';
    }
}

// Place order
async function placeOrder() {
    const cart = getCart();
    if (cart.length === 0) {
        alert('Your cart is empty!');
        return;
    }

    const user = getCurrentUser();
    if (!user) {
        alert('Please login to place an order');
        window.location.href = 'login.html';
        return;
    }

    const deliveryAddress = document.getElementById('deliveryAddress').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const specialInstructions = document.getElementById('specialInstructions').value;

    if (!deliveryAddress || !phoneNumber) {
        alert('Please fill in delivery address and phone number');
        return;
    }

    const orderRequest = {
        userId: user.id,
        items: cart.map(item => ({
            menuItemId: item.id,
            quantity: item.quantity
        })),
        deliveryAddress: deliveryAddress,
        phoneNumber: phoneNumber,
        specialInstructions: specialInstructions
    };

    try {
        // Create order
        const orderResponse = await fetch(`${API_BASE_URL}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${getAuthToken()}`
            },
            body: JSON.stringify(orderRequest)
        });

        if (!orderResponse.ok) {
            throw new Error('Failed to create order');
        }

        const order = await orderResponse.json();

        // Create bill
        const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        const tax = subtotal * 0.05;
        const deliveryCharge = 50;

        const billRequest = {
            orderId: order.id,
            userId: user.id,
            amount: subtotal,
            tax: tax,
            deliveryCharge: deliveryCharge
        };

        const billResponse = await fetch(`${API_BASE_URL}/billing`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${getAuthToken()}`
            },
            body: JSON.stringify(billRequest)
        });

        if (!billResponse.ok) {
            throw new Error('Failed to create bill');
        }

        const bill = await billResponse.json();

        // Clear cart
        localStorage.removeItem('cart');
        updateCartCount();

        // Redirect to billing page
        window.location.href = `billing.html?billId=${bill.id}`;

    } catch (error) {
        console.error('Error:', error);
        alert('Error placing order: ' + error.message);
    }
}