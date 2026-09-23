package ModeloTest;

import Modelo.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testCreacionItemAsignaIdUnico() {
        Item item1 = new Item("Microscopio", 1);
        Item item2 = new Item("Microscopio", 1);

        assertNotNull(item1.getIdItem());
        assertNotEquals(item1.getIdItem(), item2.getIdItem());
    }

    @Test
    void testSetCantidadEvitaNegativos() {
        Item item = new Item("Reactivo A", 5);
        item.setCantidad(-3);

        assertEquals(0, item.getCantidad());
    }
}