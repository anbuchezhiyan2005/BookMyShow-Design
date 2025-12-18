import axios from './axiosConfig';

export const bookingApi = {
  async createBooking(userId, showId, seatIds) {
    const response = await axios.post('/bookings', {
      userId,
      showId,
      seatIds
    });
    return response.data;
  },

  async confirmBooking(bookingId) {
    const response = await axios.post(`/bookings/${bookingId}/confirm`);
    return response.data;
  },

  async cancelBooking(bookingId) {
    const response = await axios.post(`/bookings/${bookingId}/cancel`);
    return response.data;
  },

  async getBookingById(bookingId) {
    const response = await axios.get(`/bookings/${bookingId}`);
    return response.data;
  },

  async getUserBookings(userId) {
    const response = await axios.get(`/bookings?userId=${userId}`);
    return response.data;
  }
};
