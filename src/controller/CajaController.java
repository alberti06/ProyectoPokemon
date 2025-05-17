package controller;

import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;

import dao.PokemonDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;

public class CajaController {
// Aqui ponemos los elementos de la vista
	 @FXML
	    private Button btnVolver;

	    @FXML
	    private GridPane gridCaja;

	    @FXML
	    private AnchorPane root;

	    @FXML
	    private ScrollPane scrollPane;
	    //metodo para volver al menu 
    @FXML
    void volverAlMenu(ActionEvent event) {
    	stage.close();

        if (equipoController != null) {
            equipoController.actualizarEquipo();
            equipoController.show();
        }

    }

    private Stage stage;
    private Entrenador entrenador;
    private EquipoController equipoController;

    public void init(Entrenador entrenador, Stage stage, EquipoController equipoController) {
        this.entrenador = entrenador;
        this.stage = stage;
        this.equipoController = equipoController;
        actualizarVistaCaja(); // Cargar la caja al iniciar
    }

    // Método público para que otros controladores puedan forzar la recarga
    public void actualizarVistaCaja() {
        gridCaja.getChildren().clear();
        List<Pokemon> caja = PokemonDAO.obtenerCaja(entrenador.getIdentrenador());

        int col = 0;
        int row = 0;

        for (Pokemon p : caja) {
            VBox slot = new VBox(5);
            slot.setAlignment(Pos.CENTER);

            String path = "C:/ProyectoPokemon/resources/img/Pokemon/Front/" +
                    String.format("%03d", p.getNumPokedex()) + ".png";
            File file = new File(path);
            Image img = file.exists()
                    ? new Image(file.toURI().toString())
                    : new Image(new File("C:/ProyectoPokemon/resources/img/Pokemon/Front/default.png").toURI().toString());

            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);

            Label nombre = new Label(p.getNombre());
            nombre.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            slot.getChildren().addAll(imageView, nombre);

            // Evento: mover Pokémon de la caja al equipo
            slot.setOnMouseClicked(e -> {
                int hueco = PokemonDAO.obtenerSiguienteHuecoEquipo(entrenador.getIdentrenador());
                if (hueco != -1) {
                    PokemonDAO.actualizarEquipo(p.getId(), hueco);
                    JOptionPane.showMessageDialog(null, p.getNombre() + " ha sido movido al equipo.");

                    //  Volver a la pantalla del equipo
                    volverAlMenu();

                } else {
                    JOptionPane.showMessageDialog(null, "Tu equipo está lleno.");
                }
            });

            gridCaja.add(slot, col, row);
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }
    }
    
    public void volverAlMenu() {
        stage.close();

        if (equipoController != null) {
            equipoController.actualizarEquipo();
            equipoController.show();
        }
    }

}
