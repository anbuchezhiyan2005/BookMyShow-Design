import { createContext, useState } from 'react';

export const BookingContext = createContext();

export const BookingProvider = ({ children }) => {
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [selectedTheatre, setSelectedTheatre] = useState(null);
  const [selectedShow, setSelectedShow] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [currentBooking, setCurrentBooking] = useState(null);

  const resetBooking = () => {
    setSelectedMovie(null);
    setSelectedTheatre(null);
    setSelectedShow(null);
    setSelectedSeats([]);
    setCurrentBooking(null);
  };

  return (
    <BookingContext.Provider 
      value={{ 
        selectedMovie, 
        setSelectedMovie,
        selectedTheatre,
        setSelectedTheatre,
        selectedShow,
        setSelectedShow,
        selectedSeats,
        setSelectedSeats,
        currentBooking,
        setCurrentBooking,
        resetBooking
      }}
    >
      {children}
    </BookingContext.Provider>
  );
};
