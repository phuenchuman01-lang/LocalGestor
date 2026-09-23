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
        this.cantidad = cantidad;
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
}