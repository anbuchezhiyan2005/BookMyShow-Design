# BookMyShow Testing Guide

This guide will help you test the complete BookMyShow system.

## Prerequisites

1. **MongoDB** running on `localhost:27017`
2. **Backend server** running on `http://localhost:8080`
3. **Frontend dev server** running on `http://localhost:3000`

## Starting the System

### 1. Start MongoDB
```bash
# Windows
mongod --dbpath "C:\data\db"

# Linux/Mac
mongod --dbpath /data/db
```

### 2. Start Backend
```bash
cd backend
# Windows
start-server.bat

# Linux/Mac
chmod +x start-server.sh
./start-server.sh
```

Wait for the message: `🚀 BookMyShow API Server started on http://localhost:8080`

### 3. Start Frontend
```bash
cd frontend
npm run dev
```

Open `http://localhost:3000` in your browser.

## Testing Flow

### Phase 1: User Registration and Login

1. **Register a New User**
   - Navigate to `http://localhost:3000/register`
   - Fill in:
     - Name: "John Doe"
     - Email: "john@example.com"
     - Password: "password123"
     - Phone: "9876543210"
   - Click "Register"
   - Should redirect to movies page

2. **Logout and Login**
   - Click logout in navbar
   - Navigate to `/login`
   - Login with registered credentials
   - Should redirect to movies page

### Phase 2: Seeding Test Data

Before testing the booking flow, you need to add some test data to MongoDB.

#### Add Test Movies
```javascript
// Open MongoDB shell or MongoDB Compass
use bookmyshow

db.movies.insertMany([
  {
    movieId: "MOVIE_1",
    title: "Avengers: Endgame",
    genre: "Action",
    description: "Marvel superhero movie",
    duration: 181,
    language: "English",
    releaseDate: new Date("2019-04-26"),
    posterUrl: "https://example.com/avengers.jpg"
  },
  {
    movieId: "MOVIE_2",
    title: "Inception",
    genre: "Sci-Fi",
    description: "Dream within a dream",
    duration: 148,
    language: "English",
    releaseDate: new Date("2010-07-16"),
    posterUrl: "https://example.com/inception.jpg"
  }
])
```

#### Add Test Theatres
```javascript
db.theatres.insertMany([
  {
    theatreId: "THEATRE_1",
    name: "PVR Phoenix",
    city: "Mumbai",
    address: "Phoenix Mall, Lower Parel",
    screens: 6,
    facilities: ["Parking", "Food Court", "Wheelchair Access"]
  },
  {
    theatreId: "THEATRE_2",
    name: "INOX Megaplex",
    city: "Mumbai",
    address: "R City Mall, Ghatkopar",
    screens: 8,
    facilities: ["Parking", "Food Court", "Arcade"]
  }
])
```

#### Add Test Shows
```javascript
db.shows.insertMany([
  {
    showId: "SHOW_1",
    movieId: "MOVIE_1",
    theatreId: "THEATRE_1",
    showTime: new Date("2025-12-20T14:00:00"),
    price: 250,
    language: "English",
    format: "2D"
  },
  {
    showId: "SHOW_2",
    movieId: "MOVIE_1",
    theatreId: "THEATRE_1",
    showTime: new Date("2025-12-20T18:00:00"),
    price: 300,
    language: "English",
    format: "IMAX"
  },
  {
    showId: "SHOW_3",
    movieId: "MOVIE_2",
    theatreId: "THEATRE_2",
    showTime: new Date("2025-12-20T15:30:00"),
    price: 200,
    language: "English",
    format: "2D"
  }
])
```

#### Add Test Seats
```javascript
// Add seats for SHOW_1
let seats = [];
const rows = ['A', 'B', 'C', 'D', 'E'];
const seatsPerRow = 10;

rows.forEach(row => {
  for (let i = 1; i <= seatsPerRow; i++) {
    seats.push({
      seatId: `SEAT_SHOW1_${row}${i}`,
      showId: "SHOW_1",
      seatNumber: `${row}${i}`,
      seatType: i <= 3 ? "VIP" : (i <= 7 ? "PREMIUM" : "REGULAR"),
      isAvailable: true,
      isBlocked: false
    });
  }
});

db.seats.insertMany(seats);

// Repeat similar for other shows
```

### Phase 3: Testing Movie Browsing

1. **View All Movies**
   - Should see movies on the movies page
   - Test search functionality
   - Test genre filter

2. **View Movie Details**
   - Click on a movie card
   - Should show movie details
   - Should show available theatres
   - Should show available show times

### Phase 4: Testing Booking Flow

1. **Select Show**
   - On movie details page, select a theatre
   - Select a show time
   - Click "Book Tickets"
   - Should navigate to seat selection page

2. **Select Seats**
   - Available seats should be shown in green
   - Booked seats in red (if any)
   - Click to select seats (should turn blue)
   - Click again to deselect
   - See selected seats count and total price
   - Click "Proceed to Payment"

3. **Make Payment**
   - Should show booking summary
   - Select payment method (UPI/Card/Net Banking)
   - Fill in payment details:
     - **UPI**: upiId (e.g., "john@paytm")
     - **Card**: cardNumber, cvv, expiry
     - **Net Banking**: bankName, accountNumber
   - Click "Pay Now"
   - Should navigate to confirmation page

4. **View Confirmation**
   - Should show booking confirmation
   - Booking ID
   - Movie, theatre, show, and seat details
   - Payment details
   - Option to download receipt

### Phase 5: Testing Booking History

1. **View My Bookings**
   - Click "My Bookings" in navbar
   - Should show all user's bookings
   - Each booking should show:
     - Movie name
     - Theatre and show details
     - Seats
     - Total amount
     - Booking status
   - Click on a booking to view details

### Phase 6: Testing Cancellation

1. **Cancel a Booking**
   - Go to booking history
   - Click "Cancel" on a pending booking
   - Confirm cancellation
   - Seats should be released
   - Booking status should change to "CANCELLED"

## API Testing with cURL

### Test User Registration
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"password\",\"phoneNumber\":\"1234567890\"}"
```

### Test Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"test@example.com\",\"password\":\"password\"}"
```

### Get All Movies
```bash
curl http://localhost:8080/api/movies
```

### Get Shows by Movie
```bash
curl "http://localhost:8080/api/shows?movieId=MOVIE_1"
```

### Get Seats for Show
```bash
curl "http://localhost:8080/api/seats?showId=SHOW_1"
```

### Create Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d "{\"userId\":\"USER_123\",\"showId\":\"SHOW_1\",\"seatIds\":[\"SEAT_SHOW1_A1\",\"SEAT_SHOW1_A2\"]}"
```

## Expected Behaviors

### Successful Scenarios

1. **User can register** → User object returned with userId
2. **User can login** → User object returned
3. **Movies are displayed** → List of movies shown
4. **Seats can be selected** → UI updates, selected seats highlighted
5. **Booking created** → Booking object with bookingId returned
6. **Payment processed** → Payment successful, booking confirmed
7. **Receipt generated** → Receipt details displayed

### Error Scenarios

1. **Duplicate email registration** → Error: "Email already exists"
2. **Invalid login** → Error: "Invalid email or password"
3. **Selecting unavailable seat** → Seat disabled, cannot select
4. **Payment failure** → Error message, seats released, can retry
5. **Network error** → "No response from server" message

## Troubleshooting

### Frontend Issues

1. **"No response from server"**
   - Check if backend is running on port 8080
   - Check browser console for CORS errors
   - Verify API URL in axiosConfig.js

2. **Movies not loading**
   - Check if MongoDB has movie data
   - Check backend logs for errors
   - Check Network tab in browser DevTools

3. **Login not working**
   - Verify user exists in database
   - Check password (case-sensitive)
   - Check backend UserHandler logs

### Backend Issues

1. **Port 8080 already in use**
   - Kill existing process: `netstat -ano | findstr :8080`
   - Change port in ApiServer.java

2. **MongoDB connection failed**
   - Ensure MongoDB is running
   - Check connection string in mongodb.properties
   - Verify database name

3. **ClassNotFoundException**
   - Ensure all JAR files are in lib/
   - Recompile with correct classpath
   - Check MANIFEST.MF if using JAR

## Performance Testing

1. **Load Test**: Create 100 bookings simultaneously
2. **Cache Test**: Check if repeated movie queries are faster
3. **Concurrent Booking**: Try booking same seats from 2 users

## Security Testing

1. **SQL Injection**: Try SQL in input fields (should be safe with MongoDB)
2. **XSS**: Try `<script>` tags in inputs (React escapes by default)
3. **Authorization**: Try accessing other users' bookings (should implement)

## Database Verification

Check data after each operation:

```javascript
// View all bookings
db.bookings.find().pretty()

// View all payments
db.payments.find().pretty()

// View seat status
db.seats.find({ showId: "SHOW_1" }).pretty()

// View user bookings
db.bookings.find({ userId: "USER_123" }).pretty()
```

## Success Criteria

✅ User can register and login  
✅ Movies are displayed correctly  
✅ User can browse and search movies  
✅ User can view movie details and shows  
✅ User can select and book seats  
✅ User can complete payment  
✅ Booking is confirmed and receipt is generated  
✅ User can view booking history  
✅ User can cancel bookings  
✅ Seats are properly blocked/released  
✅ No duplicate bookings for same seats  
✅ CORS works correctly  
✅ Error messages are user-friendly  

## Next Steps

After successful testing:

1. Add more movies, theatres, and shows
2. Implement seat layout visualization improvements
3. Add email notifications
4. Add booking reminders
5. Implement real payment gateway integration
6. Add admin panel for theatre management
7. Add reviews and ratings
