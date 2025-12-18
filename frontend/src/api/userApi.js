import axios from './axiosConfig';

let currentUser = null;

export const userApi = {
  async registerUser(userData) {
    const response = await axios.post('/users/register', userData);
    const user = response.data;
    currentUser = user;
    localStorage.setItem('currentUser', JSON.stringify(user));
    return user;
  },

  async loginUser(email, password) {
    const response = await axios.post('/users/login', { email, password });
    const user = response.data;
    currentUser = user;
    localStorage.setItem('currentUser', JSON.stringify(user));
    return user;
  },

  async getUserById(userId) {
    const response = await axios.get(`/users/${userId}`);
    return response.data;
  },

  getCurrentUser() {
    if (currentUser) return currentUser;
    
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      currentUser = JSON.parse(stored);
      return currentUser;
    }
    return null;
  },

  logout() {
    currentUser = null;
    localStorage.removeItem('currentUser');
  }
};
