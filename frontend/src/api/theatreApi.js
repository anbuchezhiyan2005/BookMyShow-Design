import axios from './axiosConfig';

export const theatreApi = {
  async getAllTheatres() {
    const response = await axios.get('/theatres');
    return response.data;
  },

  async getTheatreById(theatreId) {
    const response = await axios.get(`/theatres/${theatreId}`);
    return response.data;
  },

  async getTheatresByMovie(movieId) {
    const response = await axios.get(`/theatres?movieId=${movieId}`);
    return response.data;
  },

  async getTheatresByCity(city) {
    const response = await axios.get(`/theatres?city=${encodeURIComponent(city)}`);
    return response.data;
  }
};
