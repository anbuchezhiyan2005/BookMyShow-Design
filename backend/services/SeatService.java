package services;

import models.Seat;
import repositories.SeatRepository;

import java.util.ArrayList;
import java.util.List;

public class SeatService {
    private SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat addSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public List<Seat> getSeatsByShow(String showId) {
        return seatRepository.findByShowId(showId);
    }

    public List<Seat> getAvailableSeats(String showId) {
        return seatRepository.findAvailableByShowId(showId);
    }

    public boolean blockSeats(List<String> seatIds) {
        // Track successfully blocked seats for rollback if needed
        List<String> blockedSeats = new ArrayList<>();
        
        try {
            // Attempt to atomically block each seat
            for (String seatId : seatIds) {
                // atomicBlockSeat performs atomic check-and-update:
                // Only blocks seat if it exists AND is available in a single operation
                boolean blocked = seatRepository.atomicBlockSeat(seatId);
                
                if (!blocked) {
                    // Seat not available or doesn't exist - rollback all previously blocked seats
                    rollbackBlockedSeats(blockedSeats);
                    return false;
                }
                
                blockedSeats.add(seatId);
            }
            
            // All seats successfully blocked
            return true;
            
        } catch (Exception e) {
            // On any error, rollback all blocked seats
            rollbackBlockedSeats(blockedSeats);
            throw new RuntimeException("Failed to block seats: " + e.getMessage(), e);
        }
    }
    
    private void rollbackBlockedSeats(List<String> seatIds) {
        // Release all seats that were successfully blocked
        for (String seatId : seatIds) {
            seatRepository.updateAvailability(seatId, true);
        }
    }

    public boolean releaseSeats(List<String> seatIds) {
        boolean allReleased = true;
        for (String seatId : seatIds) {
            boolean updated = seatRepository.updateAvailability(seatId, true);
            if (!updated) {
                allReleased = false;
            }
        }
        return allReleased;
    }
}
