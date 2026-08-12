import { useState, useEffect } from 'react'

import './App.css'

function App() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);

  useEffect(() => {
      fetch('http://localhost:8080/products')
      .then((response) => response.json())
      .then((data) => setProducts(data));
  }, []);

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

  const cartTotal = cart.reduce(
    (sum, item) => sum + item.product.price * item.quantity, 0
  );

  return (
    <div>
      <h1>Kartly</h1>
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
    </div>
  );
}

export default App
