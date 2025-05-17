package controller;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import dao.PokemonDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;

public class CentropokeController {

    @FXML
    private ImageView btnRestaurar;

    @FXML
    private ImageView btnSalir;

    @FXML
    private Label lblDialogo;

    private MenuController menuController;
    
    private Stage stage;
    
    private Entrenador entrenador;
    
    private LoginController loginController;

    @FXML
    void btnRestaurar(MouseEvent event) {
        try {
            // Curar todos los Pokémon del equipo
            for (Pokemon p : entrenador.getEquipo()) {
                p.setVidaActual(p.getVitalidad());
                PokemonDAO.actualizarVida(p);
            }

            // Mostrar ventana emergente de confirmación
          JOptionPane.showMessageDialog(null, "¡Todos tus Pokémon han sido curados con éxito!", 
                                                      "Centro Pokémon",JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error al curar los Pokémon.", 
                                                      "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @FXML
    void salirMenupoke(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            menuController = loader.getController();

            Scene sc = new Scene(root);
            Stage st = new Stage();

            st.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            st.setTitle("Proyecto Pokémon los 3 mosqueteros");
            st.setScene(sc);
            menuController.init(entrenador, st, loginController);

            st.show();
            this.stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {
        this.menuController = menuController;
        this.stage = stage;
        this.entrenador = entrenador;
        this.loginController = loginController;


    }

}
