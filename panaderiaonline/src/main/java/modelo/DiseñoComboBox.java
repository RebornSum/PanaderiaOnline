package modelo;

import javafx.scene.control.ListCell;
import services.dao.Producto;

public class DiseñoComboBox extends ListCell<Producto>{

    @Override
    public void updateItem(Producto item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {

            setText(item.getNombre());

        }
        else {
            setText(null);
        }
    }
}
