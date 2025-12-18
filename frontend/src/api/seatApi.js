import axios from './axiosConfig';

export const seatApi = {
  async getSeatsByShow(showId) {
    const response = await axios.get(`/seats?showId=${showId}`);
    return response.data;
  },

  async getAvailableSeats(showId) {
    const response = await axios.get(`/seats?showId=${showId}&available=true`);
    return response.data;
  },

  async blockSeats(seatIds) {
    const response = await axios.post('/seats/block', { seatIds });
    return response.data;
  },

  async releaseSeats(seatIds) {
    const response = await axios.post('/seats/release', { seatIds });
    return response.data;
  }
};
