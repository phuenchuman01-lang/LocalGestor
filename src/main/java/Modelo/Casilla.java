package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Casilla {
    private final String idCasilla;
    private String idUsuarioAsignado;
    private final List<Item> items;

    public Casilla(String idCasilla) {
        this.idCasilla = idCasilla;
        this.idUsuarioAsignado = null;
        this.items = new ArrayList<>();
    }

    public String getIdCasilla() {
        return idCasilla;
    }

    public String getIdUsuarioAsignado() {
        return idUsuarioAsignado;
    }

    public void setIdUsuarioAsignado(String idUsuarioAsignado) {
        this.idUsuarioAsignado = idUsuarioAsignado;
    }

    public List<Item> getItems() {
        return items;
    }

    public void agregarItem(Item item) {
        this.items.add(item);
    }

    public boolean removerItem(String idItem) {
        return this.items.removeIf(item -> item.getIdItem().equals(idItem));
    }
}