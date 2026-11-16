import java.beans.*;
public class PropertyChangeDemo {
    static class Sensor {
        private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
        private int temperature;
        void addListener(PropertyChangeListener listener) { changes.addPropertyChangeListener(listener); }
        void setTemperature(int value) { int old = temperature; temperature = value; changes.firePropertyChange("temperature",old,value); }
    }
    public static void main(String[] args) {
        Sensor sensor = new Sensor();
        sensor.addListener(e -> System.out.println(e.getPropertyName()+": "+e.getOldValue()+" -> "+e.getNewValue()));
        sensor.setTemperature(22); sensor.setTemperature(24);
    }
}