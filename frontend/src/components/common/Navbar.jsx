import { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          🎬 BookMyShow
        </Link>
        
        {user && (
          <div className="nav-menu">
            <Link to="/movies" className="nav-link">Movies</Link>
            <Link to="/bookings" className="nav-link">My Bookings</Link>
            <span className="nav-user">Hi, {user.name}</span>
            <button onClick={handleLogout} className="btn-logout">Logout</button>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
