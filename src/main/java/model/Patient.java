
package model;

import model.Measurement;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Nastya
 */
public class Patient {
    private String id;
    private String name;
    private List<Measurement> measurement;
    
    //подумать над другим паттерном
    public Patient(String id, String name){
        this.id = id;
        this.name = name;
        this.measurement = new ArrayList<>();
    }
    
    public void addMeasurement(Measurement measurement){
        this.measurement.add(measurement);
    }
    
    public List<Measurement> getLastMeasurements(int limit) {
        int start = Math.max(0, this.measurement.size() - limit);
        return new ArrayList<>(this.measurement.subList(start, this.measurement.size()));
    }
    
    public String getId() {
        return id;
    }

    public String getFullName() {
        return this.name;
    }

    public List<Measurement> getMeasurements() {
        return this.measurement;
    }
}
