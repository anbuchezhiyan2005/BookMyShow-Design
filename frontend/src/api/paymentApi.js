import axios from './axiosConfig';

export const paymentApi = {
  async processPayment(bookingId, amount, paymentMethod, paymentDetails) {
    const response = await axios.post('/payments', {
      bookingId,
      amount,
      paymentMethod,
      paymentDetails
    });
    return response.data;
  },

  async getPaymentByBooking(bookingId) {
    const response = await axios.get(`/payments?bookingId=${bookingId}`);
    return response.data;
  }
};
