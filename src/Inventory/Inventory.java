package Inventory;

import java.util.List;
import object.SuperObject;

/**
 * Interfaz que define el comportamiento estándar para cualquier inventario en el juego.
 * Todos los inventarios (jugador, NPCs, cofres) deben implementar esta interfaz para
 * garantizar que tengan los mismos métodos básicos.
 */
public interface Inventory {

    /**
     * Añade un objeto al inventario.
     * * @param item El SuperObject a añadir.
     * @return true si el objeto fue añadido con éxito, false si el inventario estaba lleno.
     */
    boolean addItem(SuperObject item);

    /**
     * Elimina un objeto del inventario.
     * * @param item El SuperObject a eliminar.
     */
    void removeItem(SuperObject item);

    /**
     * Devuelve la lista de todos los objetos que se encuentran actualmente en el inventario.
     * * @return Una lista de SuperObject.
     */
    List<SuperObject> getItems();

    /**
     * Comprueba si el inventario ha alcanzado su capacidad máxima.
     * * @return true si no se pueden añadir más objetos, false en caso contrario.
     */
    boolean isFull();
}