import java.util.*;
public class WeatherStationDemo {
    interface Observer { void update(double temperature, double humidity, double pressure); }
    static class WeatherData {
        private final List<Observer> observers = new ArrayList<>();
        void addObserver(Observer o) { observers.add(Objects.requireNonNull(o)); }
        void removeObserver(Observer o) { observers.remove(o); }
        void measurementsChanged(double t, double h, double p) {
            for (Observer o : List.copyOf(observers)) o.update(t,h,p);
        }
    }
    static class CurrentConditions implements Observer {
        public void update(double t,double h,double p) { System.out.printf("Current: %.1f C, %.0f%% RH, %.0f hPa%n",t,h,p); }
    }
    static class StatisticsDisplay implements Observer {
        private double sum; private int count;
        public void update(double t,double h,double p) { sum += t; System.out.printf("Average temperature: %.1f C%n",sum / ++count); }
    }
    static class ForecastDisplay implements Observer {
        private double previous = Double.NaN;
        public void update(double t,double h,double p) {
            System.out.println("Forecast: " + (Double.isNaN(previous) ? "First reading" : p > previous ? "Improving" : "Stable or worsening"));
            previous = p;
        }
    }
    public static void main(String[] args) {
        WeatherData station = new WeatherData(); station.addObserver(new CurrentConditions());
        station.addObserver(new StatisticsDisplay()); station.addObserver(new ForecastDisplay());
        station.measurementsChanged(26, 55, 1013); station.measurementsChanged(27.5, 60, 1018);
    }
}