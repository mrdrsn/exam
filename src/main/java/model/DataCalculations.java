/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        return stats.getMean(); // Среднее арифметическое
    }

    public double getExpectedValue() {
        return stats.getMean(); // Мат. ожидание = среднее для равновероятных данных
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
