import { useState, useEffect } from 'react'

import './App.css'

function App() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
      fetch('http://localhost:8080/products')
      .then((response) => response.json())
      .then((data) => setProducts(data));
  }, []);


  return (
    <div>
      <h1>Kartly</h1>
      <h2>Products</h2>
      <ul>
        {products.map((product) => (
          <li key={product.id}>
            {product.name} - ${product.price} ({product.category })
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App
