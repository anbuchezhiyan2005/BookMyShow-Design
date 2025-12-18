// Mock data matching backend models

export const mockUsers = [
  {
    userId: 'USER_1',
    name: 'John Doe',
    email: 'john@example.com',
    password: 'password123',
    phone: '1234567890',
    createdAt: '2025-12-18T10:00:00'
  }
];

export const mockMovies = [
  {
    movieId: 'MOVIE_1',
    title: 'Inception',
    genre: 'Sci-Fi',
    duration: 148,
    language: 'English',
    description: 'A thief who steals corporate secrets through dream-sharing technology',
    releaseDate: '2010-07-16'
  },
  {
    movieId: 'MOVIE_2',
    title: 'The Dark Knight',
    genre: 'Action',
    duration: 152,
    language: 'English',
    description: 'Batman faces the Joker in an epic battle for Gotham City',
    releaseDate: '2008-07-18'
  },
  {
    movieId: 'MOVIE_3',
    title: 'Interstellar',
    genre: 'Sci-Fi',
    duration: 169,
    language: 'English',
    description: 'A team of explorers travel through a wormhole in space',
    releaseDate: '2014-11-07'
  },
  {
    movieId: 'MOVIE_4',
    title: 'Avengers Endgame',
    genre: 'Action',
    duration: 181,
    language: 'English',
    description: 'The Avengers assemble one final time to undo Thanos\' actions',
    releaseDate: '2019-04-26'
  }
];

export const mockTheatres = [
  {
    theatreId: 'THEATRE_1',
    name: 'PVR Cinemas',
    location: 'MG Road',
    city: 'Bangalore',
    totalScreens: 6
  },
  {
    theatreId: 'THEATRE_2',
    name: 'INOX',
    location: 'Koramangala',
    city: 'Bangalore',
    totalScreens: 4
  },
  {
    theatreId: 'THEATRE_3',
    name: 'Cinepolis',
    location: 'Whitefield',
    city: 'Bangalore',
    totalScreens: 5
  }
];

export const mockShows = [
  {
    showId: 'SHOW_1',
    movieId: 'MOVIE_1',
    theatreId: 'THEATRE_1',
    showTime: '2025-12-19T10:00:00',
    price: 250,
    availableSeats: 48
  },
  {
    showId: 'SHOW_2',
    movieId: 'MOVIE_1',
    theatreId: 'THEATRE_1',
    showTime: '2025-12-19T14:00:00',
    price: 300,
    availableSeats: 45
  },
  {
    showId: 'SHOW_3',
    movieId: 'MOVIE_1',
    theatreId: 'THEATRE_2',
    showTime: '2025-12-19T18:00:00',
    price: 280,
    availableSeats: 50
  },
  {
    showId: 'SHOW_4',
    movieId: 'MOVIE_2',
    theatreId: 'THEATRE_1',
    showTime: '2025-12-19T11:00:00',
    price: 250,
    availableSeats: 47
  }
];

export const mockSeats = [
  // Regular seats for SHOW_1
  ...Array.from({ length: 30 }, (_, i) => ({
    seatId: `SEAT_SHOW1_${i + 1}`,
    showId: 'SHOW_1',
    seatNumber: `${String.fromCharCode(65 + Math.floor(i / 10))}${(i % 10) + 1}`,
    isAvailable: i < 28,
    seatType: 'REGULAR'
  })),
  // Premium seats for SHOW_1
  ...Array.from({ length: 15 }, (_, i) => ({
    seatId: `SEAT_SHOW1_PREMIUM_${i + 1}`,
    showId: 'SHOW_1',
    seatNumber: `${String.fromCharCode(68 + Math.floor(i / 10))}${(i % 10) + 1}`,
    isAvailable: i < 13,
    seatType: 'PREMIUM'
  })),
  // VIP seats for SHOW_1
  ...Array.from({ length: 5 }, (_, i) => ({
    seatId: `SEAT_SHOW1_VIP_${i + 1}`,
    showId: 'SHOW_1',
    seatNumber: `V${i + 1}`,
    isAvailable: i < 4,
    seatType: 'VIP'
  }))
];

export const mockBookings = [];
export const mockPayments = [];
export const mockReceipts = [];
