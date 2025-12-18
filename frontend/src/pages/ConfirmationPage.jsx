import { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { receiptApi } from '../api/receiptApi';
import { BookingContext } from '../context/BookingContext';
import './ConfirmationPage.css';

const ConfirmationPage = () => {
  const navigate = useNavigate();
  const { currentBooking, resetBooking } = useContext(BookingContext);
  
  const [receipt, setReceipt] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!currentBooking) {
      navigate('/movies');
      return;
    }

    loadReceipt();
  }, []);

  const loadReceipt = async () => {
    try {
      const receiptData = await receiptApi.generateReceipt(currentBooking.bookingId);
      setReceipt(receiptData);
    } catch (error) {
      console.error('Error generating receipt:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleNewBooking = () => {
    resetBooking();
    navigate('/movies');
  };

  const handleViewBookings = () => {
    resetBooking();
    navigate('/bookings');
  };

  if (loading) return <div className="page-container">Generating receipt...</div>;

  return (
    <div className="page-container">
      <div className="success-message">
        <div className="success-icon">✓</div>
        <h1>Booking Confirmed!</h1>
        <p>Your tickets have been booked successfully</p>
      </div>

      {receipt && (
        <div className="receipt-card">
          <pre className="receipt-text">{receipt.receiptDetails}</pre>
        </div>
      )}

      <div className="action-buttons">
        <button onClick={handleNewBooking} className="btn-primary">
          Book Another Movie
        </button>
        <button onClick={handleViewBookings} className="btn-secondary">
          View My Bookings
        </button>
      </div>
    </div>
  );
};

export default ConfirmationPage;
