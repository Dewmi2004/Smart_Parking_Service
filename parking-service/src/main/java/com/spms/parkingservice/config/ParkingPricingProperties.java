package com.spms.parkingservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Binds the "parking.pricing" section of application.yml (or the Config
 * Server's parking-service.yml) so pricing rules can be tuned without a
 * code change or redeploy.
 */
@Component
@ConfigurationProperties(prefix = "parking.pricing")
public class ParkingPricingProperties {

    /** Occupancy ratio (0.0-1.0) in a zone above which surge pricing kicks in. */
    private double highOccupancyThreshold = 0.8;

    /** Multiplier applied to base price when a zone is above the threshold. */
    private double highOccupancyMultiplier = 1.5;

    /** Master switch for peak-hour pricing. */
    private boolean peakHourEnabled = true;

    /** Multiplier applied to base price during any configured peak window. */
    private double peakHourMultiplier = 1.2;

    /** Time-of-day windows treated as "peak" (e.g. morning + evening rush). */
    private List<PeakWindow> peakHours = new ArrayList<>();

    public double getHighOccupancyThreshold() {
        return highOccupancyThreshold;
    }

    public void setHighOccupancyThreshold(double highOccupancyThreshold) {
        this.highOccupancyThreshold = highOccupancyThreshold;
    }

    public double getHighOccupancyMultiplier() {
        return highOccupancyMultiplier;
    }

    public void setHighOccupancyMultiplier(double highOccupancyMultiplier) {
        this.highOccupancyMultiplier = highOccupancyMultiplier;
    }

    public boolean isPeakHourEnabled() {
        return peakHourEnabled;
    }

    public void setPeakHourEnabled(boolean peakHourEnabled) {
        this.peakHourEnabled = peakHourEnabled;
    }

    public double getPeakHourMultiplier() {
        return peakHourMultiplier;
    }

    public void setPeakHourMultiplier(double peakHourMultiplier) {
        this.peakHourMultiplier = peakHourMultiplier;
    }

    public List<PeakWindow> getPeakHours() {
        return peakHours;
    }

    public void setPeakHours(List<PeakWindow> peakHours) {
        this.peakHours = peakHours;
    }

    /** One "start-end" time-of-day window, e.g. 08:00-10:00. */
    public static class PeakWindow {
        private LocalTime start;
        private LocalTime end;

        public LocalTime getStart() {
            return start;
        }

        public void setStart(LocalTime start) {
            this.start = start;
        }

        public LocalTime getEnd() {
            return end;
        }

        public void setEnd(LocalTime end) {
            this.end = end;
        }

        public boolean contains(LocalTime time) {
            return start != null && end != null && !time.isBefore(start) && !time.isAfter(end);
        }
    }
}
