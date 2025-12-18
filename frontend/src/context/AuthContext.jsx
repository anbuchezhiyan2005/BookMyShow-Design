import { createContext, useState, useEffect } from 'react';
import { userApi } from '../api/userApi';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const currentUser = userApi.getCurrentUser();
    setUser(currentUser);
    setLoading(false);
  }, []);

  const register = async (userData) => {
    const newUser = await userApi.registerUser(userData);
    setUser(newUser);
    return newUser;
  };

  const login = async (email, password) => {
    const loggedInUser = await userApi.loginUser(email, password);
    setUser(loggedInUser);
    return loggedInUser;
  };

  const logout = () => {
    userApi.logout();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, register, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
