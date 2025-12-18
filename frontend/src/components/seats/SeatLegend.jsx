import './SeatLegend.css';

const SeatLegend = () => {
  return (
    <div className="seat-legend">
      <div className="legend-item">
        <div className="legend-box available"></div>
        <span>Available</span>
      </div>
      <div className="legend-item">
        <div className="legend-box selected"></div>
        <span>Selected</span>
      </div>
      <div className="legend-item">
        <div className="legend-box booked"></div>
        <span>Booked</span>
      </div>
      <div className="legend-item">
        <div className="legend-box premium"></div>
        <span>Premium</span>
      </div>
      <div className="legend-item">
        <div className="legend-box vip"></div>
        <span>VIP</span>
      </div>
    </div>
  );
};

export default SeatLegend;
