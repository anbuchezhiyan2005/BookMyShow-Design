# BookMyShow - Movie Ticket Booking System

A full-stack movie ticket booking application with Java backend and React frontend.

## Project Structure

```
BookMyShow/
├── backend/          # Java backend with HTTP server
│   ├── api/          # API servlets and HTTP utilities
│   ├── models/       # Data models (Movie, Theatre, Show, Booking, etc.)
│   ├── repositories/ # Database access layer (MongoDB)
│   ├── services/     # Business logic
│   ├── strategy/     # Payment strategies
│   └── config/       # MongoDB configuration
│
└── frontend/         # React + Vite frontend
    └── src/
        ├── api/      # API client functions
        ├── pages/    # Page components
        ├── components/ # Reusable components
        └── context/  # React context providers
```

## Prerequisites

### Backend
- Java 17 or higher
- MongoDB 4.4 or higher running on `localhost:27017`
- Required JAR files in `backend/lib/`:
  - MongoDB Java Driver
  - Gson (for JSON serialization)

### Frontend
- Node.js 18 or higher
- npm or yarn

## Setup Instructions

### 1. MongoDB Setup

Start MongoDB server:
```bash
# Windows
mongod --dbpath "path/to/data/directory"

# Linux/Mac
mongod --dbpath /path/to/data/directory
```

The application will automatically create a database named `bookmyshow`.

### 2. Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Ensure all required JAR files are in the `lib/` directory:
   - `mongo-java-driver-*.jar`
   - `gson-*.jar`

3. Compile the Java code:
```bash
javac -cp "lib/*" -d bin $(find . -name "*.java")
```

4. Run the API server:
```bash
java -cp "lib/*;bin" api.ApiServer
```

The backend server will start on `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Users
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Login user
- `GET /api/users/{userId}` - Get user by ID

### Movies
- `GET /api/movies` - Get all movies
- `GET /api/movies/{movieId}` - Get movie by ID
- `GET /api/movies?title={title}` - Search movies by title
- `GET /api/movies?genre={genre}` - Get movies by genre
- `POST /api/movies` - Add new movie

### Theatres
- `GET /api/theatres` - Get all theatres
- `GET /api/theatres/{theatreId}` - Get theatre by ID
- `GET /api/theatres?movieId={movieId}` - Get theatres showing a movie
- `GET /api/theatres?city={city}` - Get theatres by city
- `POST /api/theatres` - Add new theatre

### Shows
- `GET /api/shows/{showId}` - Get show by ID
- `GET /api/shows?movieId={movieId}` - Get shows for a movie
- `GET /api/shows?theatreId={theatreId}` - Get shows for a theatre
- `GET /api/shows?movieId={movieId}&theatreId={theatreId}` - Get shows for movie and theatre
- `POST /api/shows` - Add new show

### Seats
- `GET /api/seats?showId={showId}` - Get all seats for a show
- `GET /api/seats?showId={showId}&available=true` - Get available seats
- `POST /api/seats/block` - Block seats (temporary hold)
- `POST /api/seats/release` - Release blocked seats
- `POST /api/seats` - Add new seat

### Bookings
- `POST /api/bookings` - Create new booking
- `POST /api/bookings/{bookingId}/confirm` - Confirm booking
- `POST /api/bookings/{bookingId}/cancel` - Cancel booking
- `GET /api/bookings/{bookingId}` - Get booking by ID
- `GET /api/bookings?userId={userId}` - Get user bookings

### Payments
- `POST /api/payments` - Process payment
- `GET /api/payments?bookingId={bookingId}` - Get payment for booking

### Receipts
- `POST /api/receipts` - Generate receipt
- `GET /api/receipts?bookingId={bookingId}` - Get receipt for booking

## Features

### Backend
- RESTful API with Java HttpServer
- MongoDB integration with caching layer
- CORS support for frontend integration
- Payment strategy pattern (UPI, Card, Net Banking)
- Seat blocking/release mechanism
- Receipt generation
- Error handling and logging

### Frontend
- User authentication (Register/Login)
- Browse movies
- View movie details
- Select theatre and show time
- Interactive seat selection
- Multiple payment methods
- Booking confirmation
- Booking history
- Responsive design

## Architecture

### Backend Patterns
- **Repository Pattern**: Abstraction layer for database operations
- **Cached Repository**: Performance optimization with in-memory caching
- **Service Layer**: Business logic separation
- **Strategy Pattern**: Flexible payment method handling

### Frontend Architecture
- **React Context API**: Global state management (Auth, Booking)
- **Axios**: HTTP client with interceptors
- **React Router**: Client-side routing
- **Protected Routes**: Authentication-based access control

## Configuration

### Backend Configuration
MongoDB settings in `backend/config/mongodb.properties`:
```properties
mongodb.uri=mongodb://localhost:27017
mongodb.database=bookmyshow
```

### Frontend Configuration
API base URL in `frontend/src/api/axiosConfig.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

Vite proxy configuration in `frontend/vite.config.js` automatically forwards `/api` requests to the backend.

## Development

### Backend Development
- Make changes to Java files
- Recompile: `javac -cp "lib/*" -d bin $(find . -name "*.java")`
- Restart server

### Frontend Development
- Frontend uses Vite's hot module replacement (HMR)
- Changes are reflected instantly without restart
- Proxy configuration routes API calls to backend

## Troubleshooting

### Backend Issues
- **Port 8080 already in use**: Stop other processes or change port in `ApiServer.java`
- **MongoDB connection failed**: Ensure MongoDB is running on port 27017
- **ClassNotFoundException**: Check if all JAR files are in `lib/` directory

### Frontend Issues
- **Network Error**: Ensure backend is running on port 8080
- **CORS Error**: Verify CORS headers are set correctly in backend
- **npm install fails**: Try deleting `node_modules` and `package-lock.json`, then reinstall

## Production Build

### Frontend
```bash
cd frontend
npm run build
```
Output will be in `frontend/dist/` directory.

### Backend
Package as JAR:
```bash
jar -cvf bookmyshow.jar -C bin .
```

## License

This project is created for educational purposes.
