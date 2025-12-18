import Seat from './Seat';
import './SeatLayout.css';

const SeatLayout = ({ seats, selectedSeats, onSeatSelect }) => {
  // Group seats by row
  const groupSeatsByRow = () => {
    const rows = {};
    seats.forEach(seat => {
      const row = seat.seatNumber.charAt(0);
      if (!rows[row]) {
        rows[row] = [];
      }
      rows[row].push(seat);
    });
    return rows;
  };

  const seatRows = groupSeatsByRow();

  return (
    <div className="seat-layout">
      <div className="screen">SCREEN THIS WAY</div>
      
      <div className="seats-container">
        {Object.entries(seatRows).map(([row, rowSeats]) => (
          <div key={row} className="seat-row">
            <div className="row-label">{row}</div>
            <div className="seats">
              {rowSeats.map(seat => (
                <Seat
                  key={seat.seatId}
                  seat={seat}
                  isSelected={selectedSeats.some(s => s.seatId === seat.seatId)}
                  onSelect={onSeatSelect}
                />
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SeatLayout;
