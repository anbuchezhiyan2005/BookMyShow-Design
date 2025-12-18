import axios from './axiosConfig';

export const showApi = {
  async getShowsByMovie(movieId) {
    const response = await axios.get(`/shows?movieId=${movieId}`);
    return response.data;
  },

  async getShowsByTheatre(theatreId) {
    const response = await axios.get(`/shows?theatreId=${theatreId}`);
    return response.data;
  },

  async getShowsByMovieAndTheatre(movieId, theatreId) {
    const response = await axios.get(`/shows?movieId=${movieId}&theatreId=${theatreId}`);
    return response.data;
  },

  async getShowById(showId) {
    const response = await axios.get(`/shows/${showId}`);
    return response.data;
  }
};
