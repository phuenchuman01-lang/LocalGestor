package Modelo;

import java.util.UUID;

public class Item {
    private final String idItem;
    private String nombre;
    private int cantidad;

    public Item(String nombre, int cantidad) {
        this.idItem = UUID.randomUUID().toString();
        this.nombre = nombre;
        setCantidad(cantidad);
    }

    public Item(String idItem, String nombre, int cantidad) {
        this.idItem = idItem;
        this.nombre = nombre;
        setCantidad(cantidad);
    }

    public String getIdItem() {
        return idItem;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = Math.max(0, cantidad);
    }

    @Override
    public String toString() {
        return String.format("%s (Cant: %d) [ID: %s]", nombre, cantidad, idItem.substring(0, Math.min(6, idItem.length())));
    }
}