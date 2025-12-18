import axios from './axiosConfig';

export const receiptApi = {
  async generateReceipt(bookingId) {
    const response = await axios.post('/receipts', { bookingId });
    return response.data;
  },

  async getReceiptByBooking(bookingId) {
    const response = await axios.get(`/receipts?bookingId=${bookingId}`);
    return response.data;
  }
};
