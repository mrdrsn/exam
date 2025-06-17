
package main.java.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

public class DataCalculations {

    private DescriptiveStatistics stats;

    public DataCalculations(List<Double> values) {
        stats = new DescriptiveStatistics();
        for (Double v : values) {
            stats.addValue(v);
        }
    }

    public double getMean() {
        return stats.getMean(); 
    }

    public double getExpectedValue() {
        return stats.getMean(); 
    }

    public double getVariance() {
        return stats.getVariance();
    }

    public double getFirstQuartile() {
        return stats.getPercentile(25);
    }

    public double getFourthQuartile() {
        return stats.getPercentile(75);
    }
}
