import { useState, useEffect } from 'react';
import Header from './components/Header';
import './App.css';

function App() {
  const [user, setUser] = useState(null);
  const [authMode, setAuthMode] = useState('login');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [authError, setAuthError] = useState('');

  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [placedOrder, setPlacedOrder] = useState(null);
  const [orderHistory, setOrderHistory] = useState([]);
  const [currentView, setCurrentView] = useState('products');
  const [expandedOrderId, setExpandedOrderId] = useState(null);
  const [revealedPhotoId, setRevealedPhotoId] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/products')
      .then((response) => response.json())
      .then((data) => setProducts(data));
  }, []);

  useEffect(() => {
    if (user) {
      fetch(`http://localhost:8080/orders/user/${user.id}`)
        .then((response) => response.json())
        .then((data) => setOrderHistory(data));
    }
  }, [user, placedOrder]);

  function handleAuth(e) {
    e.preventDefault();
    setAuthError('');

    const endpoint = authMode === 'login' ? '/auth/login' : '/auth/register';
    const body =
      authMode === 'login' ? { email, password } : { email, password, name };

    fetch(`http://localhost:8080${endpoint}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    }).then((response) => {
      if (!response.ok) {
        setAuthError('Invalid email or password');
        return;
      }
      response.json().then((data) => setUser(data));
    });
  }

  function logout() {
    setUser(null);
    setCart([]);
    setPlacedOrder(null);
    setOrderHistory([]);
    setCurrentView('products');
  }

  function addToCart(product) {
    const existingItem = cart.find((item) => item.product.id === product.id);
    if (existingItem) {
      setCart(
        cart.map((item) =>
          item.product.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
    } else {
      setCart([...cart, { product, quantity: 1 }]);
    }
  }

  function decreaseQuantity(productId) {
    setCart(
      cart
        .map((item) =>
          item.product.id === productId
            ? { ...item, quantity: item.quantity - 1 }
            : item
        )
        .filter((item) => item.quantity > 0)
    );
  }

  function removeFromCart(productId) {
    setCart(cart.filter((item) => item.product.id !== productId));
  }

  function placeOrder() {
    const orderRequest = {
      userId: user.id,
      items: cart.map((item) => ({
        productId: item.product.id,
        quantity: item.quantity,
      })),
    };

    fetch('http://localhost:8080/orders', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(orderRequest),
    })
      .then((response) => response.json())
      .then((data) => {
        setPlacedOrder(data);
        setCart([]);
        setCurrentView('confirmation');
      });
  }

  const cartTotal = cart.reduce(
    (sum, item) => sum + item.product.price * item.quantity,
    0
  );
  const cartCount = cart.reduce((sum, item) => sum + item.quantity, 0);

  if (!user) {
    return (
      <div className="auth-page">
        <div className="ticket">
          <div className="wordmark">Kartly</div>
          <div className="tagline">Sign in to continue</div>
          <form onSubmit={handleAuth}>
            <input
              className="form-input"
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <input
              className="form-input"
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {authMode === 'register' && (
              <input
                className="form-input"
                type="text"
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            )}
            <button className="btn btn-accent btn-full" type="submit">
              {authMode === 'login' ? 'Log in' : 'Register'}
            </button>
          </form>
          {authError && <p className="error-text">{authError}</p>}
          <button
            className="btn-ghost"
            onClick={() =>
              setAuthMode(authMode === 'login' ? 'register' : 'login')
            }
          >
            {authMode === 'login'
              ? 'Need an account? Register'
              : 'Have an account? Log in'}
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <Header
        user={user}
        cartCount={cartCount}
        currentView={currentView}
        onNavigate={setCurrentView}
        onLogout={logout}
      />

      {currentView === 'products' && (
        <div className="product-grid">
          {products.map((product) => (
            <div className="product-card" key={product.id}>
              <img src={product.imageUrl} alt={product.name} />
              <div className="product-name">{product.name}</div>
              <div className="product-price">${product.price}</div>
              <button className="btn btn-accent" onClick={() => addToCart(product)}>
                Add to cart
              </button>
            </div>
          ))}
        </div>
      )}

      {currentView === 'cart' && (
        <div>
          <div className="section-title">Your cart</div>
          {cart.length === 0 && <p className="empty-note">Your cart is empty.</p>}
          {cart.map((item) => (
            <div className="line-item" key={item.product.id}>
              <span
                className="name"
                onClick={() =>
                  setRevealedPhotoId(
                    revealedPhotoId === item.product.id ? null : item.product.id
                  )
                }
              >
                {item.product.name}
              </span>
              <span className="leader"></span>
              <button className="qty-btn" onClick={() => decreaseQuantity(item.product.id)}>
                −
              </button>
              <span className="qty">{item.quantity}</span>
              <button className="qty-btn" onClick={() => addToCart(item.product)}>
                +
              </button>
              <span className="price">
                ${(item.product.price * item.quantity).toFixed(2)}
              </span>
              <button className="remove-btn" onClick={() => removeFromCart(item.product.id)}>
                remove
              </button>
              {revealedPhotoId === item.product.id && (
                <img
                  className="reveal-photo"
                  src={item.product.imageUrl}
                  alt={item.product.name}
                />
              )}
            </div>
          ))}
          <div className="total-row">
            <span className="label">Total</span>
            <span className="value">${cartTotal.toFixed(2)}</span>
          </div>
          {cart.length > 0 && (
            <button className="btn btn-accent btn-full" onClick={placeOrder}>
              Place order
            </button>
          )}
        </div>
      )}

      {currentView === 'confirmation' && placedOrder && (
        <div className="confirmation-view">
          <div className="confirm-banner">
            <p className="heading">Order confirmed</p>
            <p>
              Order #{placedOrder.id} — ${placedOrder.totalAmount}
            </p>
          </div>
          <button className="btn btn-accent" onClick={() => setCurrentView('products')}>
            Keep shopping
          </button>
        </div>
      )}

      {currentView === 'history' && (
        <div>
          <div className="section-title">Past orders</div>
          {orderHistory.length === 0 && (
            <p className="empty-note">No orders yet.</p>
          )}
          {orderHistory.map((order) => (
            <div className="order-row" key={order.id}>
              <div
                className="top"
                onClick={() =>
                  setExpandedOrderId(expandedOrderId === order.id ? null : order.id)
                }
              >
                <span className="order-id">#{order.id}</span>
                <span>{new Date(order.createdAt).toLocaleDateString()}</span>
                <span className="price">${order.totalAmount}</span>
              </div>
              {expandedOrderId === order.id && (
                <div className="items">
                  {order.items.map((item) => (
                    <div className="line-item" key={item.id}>
                      <span
                        className="name"
                        onClick={() =>
                          setRevealedPhotoId(
                            revealedPhotoId === item.id ? null : item.id
                          )
                        }
                      >
                        {item.product.name}
                      </span>
                      <span className="leader"></span>
                      <span className="qty">× {item.quantity}</span>
                      <span className="price">
                        ${(item.product.price * item.quantity).toFixed(2)}
                      </span>
                      {revealedPhotoId === item.id && (
                        <img
                          className="reveal-photo"
                          src={item.product.imageUrl}
                          alt={item.product.name}
                        />
                      )}
                    </div>
                  ))}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;