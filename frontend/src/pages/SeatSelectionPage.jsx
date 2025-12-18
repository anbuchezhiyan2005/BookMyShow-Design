import { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { seatApi } from '../api/seatApi';
import { bookingApi } from '../api/bookingApi';
import { AuthContext } from '../context/AuthContext';
import { BookingContext } from '../context/BookingContext';
import SeatLayout from '../components/seats/SeatLayout';
import SeatLegend from '../components/seats/SeatLegend';
import './SeatSelectionPage.css';

const SeatSelectionPage = () => {
  const { showId } = useParams();
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const { selectedMovie, selectedTheatre, selectedShow, setSelectedSeats, setCurrentBooking } = useContext(BookingContext);
  
  const [seats, setSeats] = useState([]);
  const [selected, setSelected] = useState([]);
  const [loading, setLoading] = useState(true);
  const [booking, setBooking] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadSeats();
  }, [showId]);

  const loadSeats = async () => {
    try {
      const data = await seatApi.getSeatsByShow(showId);
      setSeats(data);
    } catch (error) {
      console.error('Error loading seats:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSeatSelect = (seat) => {
    const isAlreadySelected = selected.some(s => s.seatId === seat.seatId);
    
    if (isAlreadySelected) {
      setSelected(selected.filter(s => s.seatId !== seat.seatId));
    } else {
      setSelected([...selected, seat]);
    }
  };

  const calculateTotal = () => {
    const priceMultipliers = { REGULAR: 1.0, PREMIUM: 1.5, VIP: 2.0 };
    return selected.reduce((total, seat) => {
      return total + (selectedShow.price * priceMultipliers[seat.seatType]);
    }, 0);
  };

  const handleProceed = async () => {
    if (selected.length === 0) {
      setError('Please select at least one seat');
      return;
    }

    setError('');
    setBooking(true);

    try {
      const seatIds = selected.map(s => s.seatId);
      const booking = await bookingApi.createBooking(user.userId, showId, seatIds);
      
      setSelectedSeats(selected);
      setCurrentBooking(booking);
      navigate('/payment');
    } catch (error) {
      setError(error.message);
      // Reload seats to reflect any changes
      loadSeats();
    } finally {
      setBooking(false);
    }
  };

  if (loading) return <div className="page-container">Loading seats...</div>;

  return (
    <div className="page-container">
      <div className="booking-info">
        <h2>{selectedMovie?.title}</h2>
        <p>{selectedTheatre?.name} | {new Date(selectedShow?.showTime).toLocaleString()}</p>
      </div>

      <SeatLegend />

      {error && <div className="error-banner">{error}</div>}

      <SeatLayout 
        seats={seats} 
        selectedSeats={selected} 
        onSeatSelect={handleSeatSelect} 
      />

      {selected.length > 0 && (
        <div className="booking-summary">
          <div className="summary-details">
            <h3>Selected Seats</h3>
            <p>{selected.map(s => s.seatNumber).join(', ')}</p>
            <div className="total-amount">
              Total: <strong>₹{calculateTotal()}</strong>
            </div>
          </div>
          <button 
            onClick={handleProceed} 
            disabled={booking}
            className="btn-proceed"
          >
            {booking ? 'Processing...' : 'Proceed to Payment'}
          </button>
        </div>
      )}
    </div>
  );
};

export default SeatSelectionPage;
