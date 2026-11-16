import java.util.*;
public class RefactoredStoreDemo {
    interface StockListener { void stockChanged(String item, int remaining); }
    static class Inventory {
        private final List<StockListener> listeners = new ArrayList<>();
        void subscribe(StockListener listener) { listeners.add(listener); }
        void setStock(String item,int remaining) {
            for (StockListener listener : List.copyOf(listeners)) listener.stockChanged(item,remaining);
        }
    }
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.subscribe((name, count) -> System.out.println("Dashboard: "+name+" = "+count));
        inventory.subscribe((name, count) -> { if (count < 5) System.out.println("Alert: reorder "+name); });
        inventory.setStock("Notebook", 3);
    }
}