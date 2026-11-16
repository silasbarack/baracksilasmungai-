import java.util.*;
@SuppressWarnings("deprecation")
public class LegacyObserverDemo {
    static class LegacyWeather extends Observable {
        void setTemperature(double temperature) { setChanged(); notifyObservers(temperature); }
    }
    public static void main(String[] args) {
        LegacyWeather weather = new LegacyWeather();
        Observer display = (subject, value) -> System.out.println("Legacy observed temperature: " + value);
        weather.addObserver(display); weather.setTemperature(24.5);
        weather.deleteObserver(display);
    }
}