package services;

import models.*;
import repositories.*;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ReceiptService {
    private ReceiptRepository receiptRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ShowRepository showRepository;
    private MovieRepository movieRepository;
    private TheatreRepository theatreRepository;
    private PaymentRepository paymentRepository;

    public ReceiptService(ReceiptRepository receiptRepository, 
                         BookingRepository bookingRepository,
                         UserRepository userRepository,
                         ShowRepository showRepository,
                         MovieRepository movieRepository,
                         TheatreRepository theatreRepository,
                         PaymentRepository paymentRepository) {
        this.receiptRepository = receiptRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.paymentRepository = paymentRepository;
    }

    public Receipt generateReceipt(String bookingId) {
        // Fetch all related data
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        User user = userRepository.findById(booking.getUserId());
        Show show = showRepository.findById(booking.getShowId());
        Movie movie = movieRepository.findById(show.getMovieId());
        Theatre theatre = theatreRepository.findById(show.getTheatreId());
        Payment payment = paymentRepository.findByBookingId(bookingId);

        // Format receipt details
        StringBuilder details = new StringBuilder();
        details.append("\n========================================\n");
        details.append("           BOOKMYSHOW RECEIPT          \n");
        details.append("========================================\n\n");
        details.append("Booking ID: ").append(booking.getBookingId()).append("\n");
        details.append("User: ").append(user.getName()).append("\n");
        details.append("Email: ").append(user.getEmail()).append("\n\n");
        details.append("Movie: ").append(movie.getTitle()).append("\n");
        details.append("Genre: ").append(movie.getGenre()).append("\n");
        details.append("Duration: ").append(movie.getDuration()).append(" mins\n\n");
        details.append("Theatre: ").append(theatre.getName()).append("\n");
        details.append("Location: ").append(theatre.getLocation()).append(", ").append(theatre.getCity()).append("\n\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        details.append("Show Time: ").append(show.getShowTime().format(formatter)).append("\n");
        details.append("Seats: ").append(String.join(", ", booking.getSeatIds())).append("\n");
        details.append("Number of Seats: ").append(booking.getSeatIds().size()).append("\n\n");
        details.append("Total Amount: ?").append(booking.getTotalAmount()).append("\n");
        details.append("Payment Method: ").append(payment.getPaymentMethod()).append("\n");
        details.append("Transaction ID: ").append(payment.getTransactionId()).append("\n");
        details.append("Payment Status: ").append(payment.getPaymentStatus()).append("\n\n");
        details.append("Booking Date: ").append(booking.getBookingDate().format(formatter)).append("\n");
        details.append("Booking Status: ").append(booking.getBookingStatus()).append("\n\n");
        details.append("========================================\n");
        details.append("     Thank you for booking with us!    \n");
        details.append("========================================\n");

        String receiptId = "REC_" + UUID.randomUUID().toString().substring(0, 8);
        Receipt receipt = new Receipt(receiptId, bookingId, details.toString());
        return receiptRepository.save(receipt);
    }

    public Receipt getReceiptByBooking(String bookingId) {
        Receipt receipt = receiptRepository.findByBookingId(bookingId);
        if (receipt == null) {
            throw new RuntimeException("Receipt not found for booking ID: " + bookingId);
        }
        return receipt;
    }

    public String printReceipt(String receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId);
        if (receipt == null) {
            throw new RuntimeException("Receipt not found with ID: " + receiptId);
        }
        return receipt.getReceiptDetails();
    }
}
