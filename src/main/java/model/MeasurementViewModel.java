
package model;

/**
 *
 * @author Nastya
 */
public class MeasurementViewModel {

    private double temperature;
    private int heartRate;
    private int cvp;

    public MeasurementViewModel(double temperature, int heartRate, int cvp) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.cvp = cvp;
    }

    public String getTemperatureText() {
        return String.format("%.1f°C", temperature);
    }

    public String getHeartRateText() {
        return String.valueOf(heartRate);
    }

    public String getCvpText() {
        return String.valueOf(cvp);
    }
}
