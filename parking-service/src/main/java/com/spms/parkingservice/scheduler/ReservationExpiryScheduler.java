package com.spms.parkingservice.scheduler;

import com.spms.parkingservice.config.ParkingReservationProperties;
import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.ParkingStatus;
import com.spms.parkingservice.repository.ParkingSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Periodically sweeps for parking spaces that have been RESERVED longer
 * than the configured expiry window (parking.reservation.expiry-minutes,
 * default 15) without the driver actually showing up, and auto-releases
 * them back to AVAILABLE so the space isn't held forever by a no-show.
 */
@Component
public class ReservationExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReservationExpiryScheduler.class);

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingReservationProperties reservationProperties;

    public ReservationExpiryScheduler(ParkingSpaceRepository parkingSpaceRepository,
                                       ParkingReservationProperties reservationProperties) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.reservationProperties = reservationProperties;
    }

    // Runs on a fixed delay (default: every 60s) rather than fixed rate, so
    // sweeps never overlap even if one run happens to take a while.
    @Scheduled(fixedDelayString = "${parking.reservation.expiry-check-interval-ms:60000}")
    public void releaseExpiredReservations() {
        int released = releaseExpiredReservationsNow();
        if (released > 0) {
            log.info("Reservation expiry sweep: auto-released {} expired reservation(s)", released);
        }
    }

    /**
     * The actual release logic, pulled out so it can also be triggered
     * on-demand (see ParkingSpaceController's manual test endpoint) without
     * waiting for the scheduler's next tick.
     */
    public int releaseExpiredReservationsNow() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(reservationProperties.getExpiryMinutes());

        List<ParkingSpace> expired = parkingSpaceRepository.findByStatusAndReservedAtBefore(
                ParkingStatus.RESERVED, cutoff);

        for (ParkingSpace space : expired) {
            log.debug("Auto-releasing expired reservation on space {} (reserved at {})",
                    space.getId(), space.getReservedAt());
            space.setStatus(ParkingStatus.AVAILABLE);
            space.setReservedByUserId(null);
            space.setReservedVehicleId(null);
            space.setReservedAt(null);
        }

        if (!expired.isEmpty()) {
            parkingSpaceRepository.saveAll(expired);
        }

        return expired.size();
    }
}
