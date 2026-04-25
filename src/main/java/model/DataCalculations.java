package main.java.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

/**
 * Класс, предоставляющий методы для статистической обработки набора числовых
 * данных. Использует библиотеку Apache Commons Math для вычисления основных
 * статистических показателей.
 *
 * <p>
 * Поддерживаемые операции:
 * <ul>
 * <li>Вычисление среднего значения (среднего арифметического)</li>
 * <li>Вычисление дисперсии</li>
 * <li>Определение квартилей (25-й и 75-й процентили)</li>
 * </ul>
 *
 * @author nsoko
 */
public class DataCalculations {

    private DescriptiveStatistics stats;

    /**
     * Создаёт новый экземпляр класса на основе списка числовых значений. Все
     * значения добавляются во внутренний объект {@link DescriptiveStatistics}
     * для последующего анализа.
     *
     * @param values список числовых значений для анализа
     */
    public DataCalculations(List<Double> values) {
        stats = new DescriptiveStatistics();
        for (Double v : values) {
            stats.addValue(v);
        }
    }

    /**
     * Возвращает среднее значение (среднее арифметическое) всех элементов в
     * наборе данных.
     *
     * @return среднее значение
     */
    public double getMean() {
        return stats.getMean();
    }

    /**
     * Возвращает математическое ожидание, которое в данном случае совпадает со
     * средним значением.
     *
     * @return математическое ожидание (среднее)
     */
    public double getExpectedValue() {
        return stats.getMean();
    }

    /**
     * Возвращает дисперсию значений — меру разброса данных относительно
     * среднего.
     *
     * @return дисперсия
     */
    public double getVariance() {
        return stats.getVariance();
    }

    /**
     * Возвращает первый квартиль (25-й процентиль) — значение, ниже которого
     * находится 25% данных.
     *
     * @return первый квартиль
     */
    public double getFirstQuartile() {
        return stats.getPercentile(25);
    }

    /**
     * Возвращает четвёртый квартиль (75-й процентиль) — значение, ниже которого
     * находится 75% данных.
     *
     * @return четвёртый квартиль
     */
    public double getFourthQuartile() {
        return stats.getPercentile(75);
    }
}
