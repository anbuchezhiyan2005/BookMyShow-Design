import { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { bookingApi } from '../api/bookingApi';
import { movieApi } from '../api/movieApi';
import { theatreApi } from '../api/theatreApi';
import { showApi } from '../api/showApi';
import { receiptApi } from '../api/receiptApi';
import { AuthContext } from '../context/AuthContext';
import './BookingHistoryPage.css';

const BookingHistoryPage = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  
  const [bookings, setBookings] = useState([]);
  const [enrichedBookings, setEnrichedBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedReceipt, setSelectedReceipt] = useState(null);

  useEffect(() => {
    loadBookings();
  }, []);

  const loadBookings = async () => {
    try {
      const bookingData = await bookingApi.getUserBookings(user.userId);
      
      // Enrich bookings with movie, theatre, show details
      const enriched = await Promise.all(
        bookingData.map(async (booking) => {
          const show = await showApi.getShowById(booking.showId);
          const movie = await movieApi.getMovieById(show.movieId);
          const theatre = await theatreApi.getTheatreById(show.theatreId);
          
          return {
            ...booking,
            movie,
            theatre,
            show
          };
        })
      );
      
      setEnrichedBookings(enriched.sort((a, b) => 
        new Date(b.bookingTime) - new Date(a.bookingTime)
      ));
    } catch (error) {
      console.error('Error loading bookings:', error);
    } finally {
      setLoading(false);
    }
  };

  const viewReceipt = async (bookingId) => {
    try {
      const receipt = await receiptApi.getReceiptByBooking(bookingId);
      setSelectedReceipt(receipt);
    } catch (error) {
      // Receipt might not exist, generate it
      try {
        const receipt = await receiptApi.generateReceipt(bookingId);
        setSelectedReceipt(receipt);
      } catch (err) {
        console.error('Error viewing receipt:', err);
      }
    }
  };

  if (loading) return <div className="page-container">Loading bookings...</div>;

  return (
    <div className="page-container">
      <h1>My Bookings</h1>

      {enrichedBookings.length === 0 ? (
        <div className="no-bookings">
          <p>No bookings yet</p>
          <button onClick={() => navigate('/movies')} className="btn-primary">
            Browse Movies
          </button>
        </div>
      ) : (
        <div className="bookings-list">
          {enrichedBookings.map((booking) => (
            <div key={booking.bookingId} className="booking-card">
              <div className="booking-header">
                <h3>{booking.movie.title}</h3>
                <span className={`status-badge ${booking.bookingStatus.toLowerCase()}`}>
                  {booking.bookingStatus}
                </span>
              </div>
              
              <div className="booking-details">
                <div className="detail-row">
                  <span className="label">Theatre:</span>
                  <span>{booking.theatre.name}, {booking.theatre.location}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Show Time:</span>
                  <span>{new Date(booking.show.showTime).toLocaleString()}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Booking Date:</span>
                  <span>{new Date(booking.bookingTime).toLocaleString()}</span>
                </div>
                <div className="detail-row">
                  <span className="label">Seats:</span>
                  <span>{booking.seatIds.length} seat(s)</span>
                </div>
                <div className="detail-row">
                  <span className="label">Amount:</span>
                  <span className="amount">₹{booking.totalAmount}</span>
                </div>
              </div>

              <button 
                onClick={() => viewReceipt(booking.bookingId)}
                className="btn-view-receipt"
              >
                View Receipt
              </button>
            </div>
          ))}
        </div>
      )}

      {selectedReceipt && (
        <div className="receipt-modal" onClick={() => setSelectedReceipt(null)}>
          <div className="receipt-modal-content" onClick={(e) => e.stopPropagation()}>
            <button className="close-btn" onClick={() => setSelectedReceipt(null)}>×</button>
            <pre className="receipt-text">{selectedReceipt.receiptDetails}</pre>
          </div>
        </div>
      )}
    </div>
  );
};

export default BookingHistoryPage;
