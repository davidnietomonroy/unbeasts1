package Inventory;

import java.util.ArrayList;
import java.util.List;
import object.SuperObject;

public class Inventory_Darwin implements Inventory {

    private List<SuperObject> items;
    private final int maxSize;

    public Inventory_Darwin(int maxSize) {
        this.maxSize = maxSize;
        this.items = new ArrayList<>();
    }

    @Override
    public boolean addItem(SuperObject item) {
        if (isFull()) {
            System.out.println("El inventario del jugador está lleno.");
            return false;
        }
        items.add(item);
        return true;
    }

    @Override
    public void removeItem(SuperObject item) {
        items.remove(item);
    }

    @Override
    public List<SuperObject> getItems() {
        return items;
    }

    @Override
    public boolean isFull() {
        return items.size() >= maxSize;
        
    }
    @Override
    public void moveItemToFront(SuperObject item) {
        // 'items' es la List<SuperObject> que tienes en esta clase
        if (items.contains(item)) {
            items.remove(item);      // Primero, quita el objeto de su posición actual
            items.add(0, item);  // Luego, lo añade en la primera posición (índice 0)
        }
    }
}