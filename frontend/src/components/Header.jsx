function Header({ user, cartCount, currentView, onNavigate, onLogout }) {
  return (
    <header className="header">
      <div className="wordmark-small" onClick={() => onNavigate('products')}>
        Kartly
      </div>
      <nav className="nav">
        <button
          className={`nav-link ${currentView === 'history' ? 'active' : ''}`}
          onClick={() => onNavigate('history')}
        >
          Past orders
        </button>
        <button
          className={`nav-link cart-link ${currentView === 'cart' ? 'active' : ''}`}
          onClick={() => onNavigate('cart')}
        >
          Cart
          {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
        </button>
        <span className="user-name">{user.name}</span>
        <button className="btn-ghost" onClick={onLogout}>
          Log out
        </button>
      </nav>
    </header>
  );
}

export default Header;