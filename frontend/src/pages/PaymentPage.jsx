import { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { paymentApi } from '../api/paymentApi';
import { bookingApi } from '../api/bookingApi';
import { BookingContext } from '../context/BookingContext';
import { UpiPaymentForm, CardPaymentForm, NetBankingForm } from '../components/payment/PaymentForms';
import './PaymentPage.css';

const PaymentPage = () => {
  const navigate = useNavigate();
  const { selectedMovie, selectedTheatre, selectedShow, selectedSeats, currentBooking } = useContext(BookingContext);
  
  const [paymentMethod, setPaymentMethod] = useState('UPI');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handlePayment = async (paymentDetails) => {
    setError('');
    setLoading(true);

    try {
      // Process payment
      await paymentApi.processPayment(
        currentBooking.bookingId,
        currentBooking.totalAmount,
        paymentMethod,
        paymentDetails
      );

      // Confirm booking
      await bookingApi.confirmBooking(currentBooking.bookingId);

      navigate('/confirmation');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (!currentBooking) {
    navigate('/movies');
    return null;
  }

  return (
    <div className="page-container">
      <h1>Complete Payment</h1>

      <div className="payment-container">
        <div className="booking-summary-card">
          <h2>Booking Summary</h2>
          <div className="summary-row">
            <span>Movie:</span>
            <strong>{selectedMovie?.title}</strong>
          </div>
          <div className="summary-row">
            <span>Theatre:</span>
            <strong>{selectedTheatre?.name}</strong>
          </div>
          <div className="summary-row">
            <span>Show Time:</span>
            <strong>{new Date(selectedShow?.showTime).toLocaleString()}</strong>
          </div>
          <div className="summary-row">
            <span>Seats:</span>
            <strong>{selectedSeats.map(s => s.seatNumber).join(', ')}</strong>
          </div>
          <div className="summary-row total-row">
            <span>Total Amount:</span>
            <strong className="total-price">₹{currentBooking.totalAmount}</strong>
          </div>
        </div>

        <div className="payment-card">
          <h2>Select Payment Method</h2>

          {error && <div className="error-message">{error}</div>}

          <div className="payment-methods">
            <button
              className={`method-btn ${paymentMethod === 'UPI' ? 'active' : ''}`}
              onClick={() => setPaymentMethod('UPI')}
            >
              UPI
            </button>
            <button
              className={`method-btn ${paymentMethod === 'CARD' ? 'active' : ''}`}
              onClick={() => setPaymentMethod('CARD')}
            >
              Card
            </button>
            <button
              className={`method-btn ${paymentMethod === 'NET_BANKING' ? 'active' : ''}`}
              onClick={() => setPaymentMethod('NET_BANKING')}
            >
              Net Banking
            </button>
          </div>

          <div className="payment-form-container">
            {paymentMethod === 'UPI' && (
              <UpiPaymentForm onSubmit={handlePayment} loading={loading} />
            )}
            {paymentMethod === 'CARD' && (
              <CardPaymentForm onSubmit={handlePayment} loading={loading} />
            )}
            {paymentMethod === 'NET_BANKING' && (
              <NetBankingForm onSubmit={handlePayment} loading={loading} />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PaymentPage;
