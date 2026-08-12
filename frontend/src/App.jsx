import { useState, useEffect } from 'react'

import './App.css'

function App() {
  const[user, setUser] = useState(null);
  const[authMode, setAuthMode] = useState('login');
  const[email, setEmail] = useState('');
  const[password, setPassword] = useState('');
  const[name, setName] = useState('');
  const[authError, setAuthError] = useState('');

  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [placedOrder, setPlacedOrder] = useState(null);
  const [orderHistory, setOrderHistory] = useState([]);

  useEffect(() => {
      fetch('http://localhost:8080/products')
      .then((response) => response.json())
      .then((data) => setProducts(data));
  }, []);

  useEffect(() =>{
    if(user){
      fetch(`http://localhost:8080/orders/user/${user.id}`)
        .then((response) => response.json())
        .then((data) => setOrderHistory(data));
    }
  }, [user, placedOrder])

  function handleAuth(e){
    e.preventDefault();
    setAuthError('');

    const endpoint = authMode === 'login' ? '/auth/login' : '/auth/register';
    const body = 
          authMode === 'login'
          ? {email, password}
          : {email, password, name};

    fetch(`http://localhost:8080${endpoint}`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(body),
    }).then((response) => {
      if(!response.ok){
        setAuthError('Invalid email or password');
        return;
      }
      response.json().then((data) => setUser(data));
    });
  }

  function logout(){
    setUser(null);
    setCart([]);
    setPlacedOrder(null);
    setOrderHistory([]);
  }

  function addToCart(product){
    const existingItem = cart.find((item) => item.product.id === product.id);
    if(existingItem){
      setCart(
        cart.map((item) =>
          item.product.id ===product.id
            ?{ ...item, quantity: item.quantity + 1 }
            :item)
      );
    } else{
      setCart([...cart, { product, quantity: 1 }]);
    }
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
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(orderRequest),
    })
    .then((response) => response.json())
    .then((data) => {
      setPlacedOrder(data);
      setCart([]);
    });
  }

  const cartTotal = cart.reduce(
    (sum, item) => sum + item.product.price * item.quantity, 0
  );

  if (!user) {
    return(
      <div>
        <h1>Kartly</h1>
        <form onSubmit={handleAuth}>
          <h2>{authMode === 'login' ? 'Log in' : 'Register'}</h2>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          {authMode ==='register' && (
            <input
              type="text"
              placeholder="Name"
              value={name}
              onChange={(e)=> setName(e.target.value)}
            />
          )}
          <button type="submit">
            {authMode === 'login' ? 'Log in' : 'Register'}
          </button>
      </form>
      {authError && <p style={{ color: 'red' }}>{authError}</p>}
      <button onClick={() => setAuthMode(authMode === 'login' ? 'register': 'login')}>
        {authMode === 'login' ? 'Need an account? Register' : 'Have an account? Log in'}
      </button>
      </div>
    );
  }

  return (
    <div>
      <h1>Kartly</h1>
      <p>
        Logged in as {user.name} <button onClick={logout}>Log out</button>
      </p>
      <h2>Products</h2>
      <ul>
        {products.map((product) => (
          <li key={product.id}>
            {product.name} - ${product.price} ({product.category })
            <button onClick={() => addToCart(product)}>Add To Cart</button>
          </li>
        ))}
      </ul>

      <h2>Cart</h2>
      <ul>
        {cart.map((item) => (
          <li key={item.product.id}>
            {item.product.name} X {item.quantity} = $
            {(item.product.price * item.quantity).toFixed(2)}
          </li>
        ))}
      </ul>
      <p> Total : ${cartTotal.toFixed(2)}</p>

      {cart.length > 0 && (
        <button onClick={placeOrder}>Place order</button>
      )}

      {placedOrder && (
        <div>
          <h2>Order confirmed!</h2>
          <p>Order #{placedOrder.id} - Total: ${placedOrder.totalAmount}</p>
        </div>
      )}

      <h2>Order history</h2>
      <ul>
        {orderHistory.map((order) => (
          <li key={order.id}>
            Order #{order.id} - ${order.totalAmount} -{' '}
            {new Date(order.createdAt).toLocaleString()}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App
