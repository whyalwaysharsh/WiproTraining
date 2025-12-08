let currentBill = null;

document.addEventListener('DOMContentLoaded', () => {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = 'login.html';
        return;
    }

    // Get billId from URL
    const urlParams = new URLSearchParams(window.location.search);
    const billId = urlParams.get('billId');

    if (billId) {
        loadBill(billId);
    } else {
        document.getElementById('billDetails').innerHTML = 
            '<p class="error-message">No bill ID provided</p>';
    }

    // Setup payment form
    setupPaymentForm();
});

// Load bill details
async function loadBill(billId) {
    try {
        const response = await fetch(`${API_BASE_URL}/billing/${billId}`, {
            headers: {
                'Authorization': `Bearer ${getAuthToken()}`
            }
        });

        if (response.ok) {
            currentBill = await response.json();
            displayBillDetails(currentBill);
        } else {
            document.getElementById('billDetails').innerHTML = 
                '<p class="error-message">Error loading bill</p>';
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('billDetails').innerHTML = 
            '<p class="error-message">Error loading bill</p>';
    }
}

// Display bill details
function displayBillDetails(bill) {
    const container = document.getElementById('billDetails');

    const statusClass = bill.paymentStatus === 'PAID' ? 'status-DELIVERED' : 
                       bill.paymentStatus === 'FAILED' ? 'status-CANCELLED' : 
                       'status-PENDING';

    container.innerHTML = `
        <h3>Bill Details</h3>
        <div class="summary-row">
            <span>Bill ID:</span>
            <span><strong>#${bill.id}</strong></span>
        </div>
        <div class="summary-row">
            <span>Order ID:</span>
            <span><strong>#${bill.orderId}</strong></span>
        </div>
        <div class="summary-row">
            <span>Date:</span>
            <span>${new Date(bill.createdAt).toLocaleString()}</span>
        </div>
        <hr>
        <div class="summary-row">
            <span>Amount:</span>
            <span>₹${bill.amount.toFixed(2)}</span>
        </div>
        <div class="summary-row">
            <span>Tax (5%):</span>
            <span>₹${bill.tax.toFixed(2)}</span>
        </div>
        <div class="summary-row">
            <span>Delivery Charge:</span>
            <span>₹${bill.deliveryCharge.toFixed(2)}</span>
        </div>
        <hr>
        <div class="summary-row total">
            <span><strong>Total Amount:</strong></span>
            <span><strong>₹${bill.totalAmount.toFixed(2)}</strong></span>
        </div>
        <hr>
        <div class="summary-row">
            <span>Payment Status:</span>
            <span class="order-status ${statusClass}">${bill.paymentStatus}</span>
        </div>
        ${bill.paymentMethod ? `
            <div class="summary-row">
                <span>Payment Method:</span>
                <span>${bill.paymentMethod.replace('_', ' ')}</span>
            </div>
        ` : ''}
        ${bill.transactionId ? `
            <div class="summary-row">
                <span>Transaction ID:</span>
                <span>${bill.transactionId}</span>
            </div>
        ` : ''}
        ${bill.paidAt ? `
            <div class="summary-row">
                <span>Paid At:</span>
                <span>${new Date(bill.paidAt).toLocaleString()}</span>
            </div>
        ` : ''}
    `;

    // Hide payment form if already paid
    if (bill.paymentStatus === 'PAID') {
        document.querySelector('.payment-section').style.display = 'none';
    }
}

// Setup payment form
function setupPaymentForm() {
    const paymentForm = document.getElementById('paymentForm');
    
    // Show/hide payment details based on selected method
    const paymentMethods = document.querySelectorAll('input[name="paymentMethod"]');
    paymentMethods.forEach(method => {
        method.addEventListener('change', (e) => {
            // Hide all payment details
            document.getElementById('cardDetails').style.display = 'none';
            document.getElementById('upiDetails').style.display = 'none';

            // Show relevant payment details
            if (e.target.value === 'CARD') {
                document.getElementById('cardDetails').style.display = 'block';
            } else if (e.target.value === 'UPI') {
                document.getElementById('upiDetails').style.display = 'block';
            }
        });
    });

    // Handle form submission
    paymentForm.addEventListener('submit', handlePayment);
}

// Handle payment
async function handlePayment(e) {
    e.preventDefault();

    if (!currentBill) {
        alert('No bill loaded');
        return;
    }

    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
    
    // Generate a mock transaction ID
    const transactionId = 'TXN' + Date.now() + Math.floor(Math.random() * 1000);

    const paymentRequest = {
        paymentMethod: paymentMethod,
        transactionId: transactionId
    };

    try {
        const response = await fetch(`${API_BASE_URL}/billing/${currentBill.id}/pay`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${getAuthToken()}`
            },
            body: JSON.stringify(paymentRequest)
        });

        if (response.ok) {
            alert('Payment successful!');
            window.location.href = 'orders.html';
        } else {
            const error = await response.json();
            alert('Payment failed: ' + (error.message || 'Unknown error'));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error processing payment');
    }
}