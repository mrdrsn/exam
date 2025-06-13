
package model;

/**
 *
 * @author Nastya
 */

public class Measurement {
    
    private double temperature; //температура тела (°C)
    private int heartRate; //сердечный ритм (ударов/мин)
    private int cvp; //центральное венозное давление (мм рт. ст.)

    public Measurement(double temperature, int heartRate, int cvp) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.cvp = cvp;
    }
   
    public double getTemperature() {
        return temperature;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getCvp() {
        return cvp;
    }
    
    @Override
    public String toString(){
        return this.temperature + " " + this.heartRate + " " + this.cvp;
    }
    

}