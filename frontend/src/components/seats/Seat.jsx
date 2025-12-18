import './Seat.css';

const Seat = ({ seat, isSelected, onSelect }) => {
  const handleClick = () => {
    if (seat.isAvailable) {
      onSelect(seat);
    }
  };

  const getSeatClass = () => {
    let classes = 'seat';
    if (!seat.isAvailable) classes += ' seat-booked';
    else if (isSelected) classes += ' seat-selected';
    else classes += ' seat-available';
    
    classes += ` seat-${seat.seatType.toLowerCase()}`;
    return classes;
  };

  return (
    <div className={getSeatClass()} onClick={handleClick}>
      {seat.seatNumber}
    </div>
  );
};

export default Seat;
