import axios from './axiosConfig';

export const movieApi = {
  async getAllMovies() {
    const response = await axios.get('/movies');
    return response.data;
  },

  async getMovieById(movieId) {
    const response = await axios.get(`/movies/${movieId}`);
    return response.data;
  },

  async searchMoviesByTitle(title) {
    const response = await axios.get(`/movies?title=${encodeURIComponent(title)}`);
    return response.data;
  },

  async getMoviesByGenre(genre) {
    const response = await axios.get(`/movies?genre=${encodeURIComponent(genre)}`);
    return response.data;
  }
};
