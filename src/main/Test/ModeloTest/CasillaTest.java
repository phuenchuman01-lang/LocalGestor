package ModeloTest;

import Modelo.Casilla;
import Modelo.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CasillaTest {

    private Casilla casilla;

    @BeforeEach
    void setUp() {
        casilla = new Casilla("C-01");
    }

    @Test
    void testAgregarItem() {
        Item item = new Item("Placa Petri", 10);
        casilla.agregarItem(item);

        assertEquals(1, casilla.getItems().size());
    }

    @Test
    void testRemoverItemExistente() {
        Item item = new Item("Sensor", 2);
        casilla.agregarItem(item);

        boolean removido = casilla.removerItem(item.getIdItem());
        assertTrue(removido);
        assertTrue(casilla.getItems().isEmpty());
    }
}
