package controller;

import dao.ConexionBD;
import dao.EntrenadorDAO;
import dao.PokemonDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

public class PantallaChoosePokemonController {

    private int idEntrenador;
    private LoginController loginController;

    @FXML 
    private Button btnElegirBulbasur;
    
    @FXML 
    private Button btnElegirCharmander;
    
    @FXML
    private Button btnElegirSquirtle;

    @FXML 
    private ImageView imgBulbasur;
    
    @FXML 
    private ImageView imgCharmander;
    
    @FXML 
    private ImageView imgSquirtle;

    public void init(int idEntrenador, LoginController loginController) {
        this.idEntrenador = idEntrenador;
        this.loginController = loginController;
    }

    // ==========================
    // Hover - Mostrar imágenes
    // ==========================

    @FXML
    void mostrarBulbasur(MouseEvent event) {
        imgBulbasur.setVisible(true);
    }

    @FXML
    void quitarBulbasur(MouseEvent event) {
        imgBulbasur.setVisible(false);
    }

    @FXML
    void mostrarCharmander(MouseEvent event) {
        imgCharmander.setVisible(true);
    }

    @FXML
    void quitarCharmander(MouseEvent event) {
        imgCharmander.setVisible(false);
    }

    @FXML
    void mostrarSquirtle(MouseEvent event) {
        imgSquirtle.setVisible(true);
    }

    @FXML
    void quitarSquirtle(MouseEvent event) {
        imgSquirtle.setVisible(false);
    }

    // ==========================
    // Clic - Elegir Pokémon
    // ==========================

    @FXML
    void elegirBulbasur(MouseEvent event) {
        elegirPokemon("Bulbasaur");
    }

    @FXML
    void elegirCharmander(MouseEvent event) {
        elegirPokemon("Charmander");
    }

    @FXML
    void elegirSquirtle(MouseEvent event) {
        elegirPokemon("Squirtle");
    }

    private void elegirPokemon(String nombrePokemon) {
        try (Connection con = ConexionBD.conectar()) {
            Pokemon nuevo = PokemonDAO.generarPokemonPrincipalEspecifico(idEntrenador, nombrePokemon, con);

            // Mostrar género
            String generoTexto;
            if ("M".equalsIgnoreCase(nuevo.getSexo())) {
                generoTexto = "Macho";
            } else if ("F".equalsIgnoreCase(nuevo.getSexo())) {
                generoTexto = "Hembra";
            } else {
                generoTexto = "Desconocido";
            }

            JOptionPane.showMessageDialog(null,
                "¡Has elegido a " + nuevo.getNombre() + "!\nGénero: " + generoTexto,
                "¡Pokémon elegido!",
                JOptionPane.INFORMATION_MESSAGE
            );

            System.out.println("Pokémon elegido: " + nuevo.getNombre() + " | Género: " + generoTexto);
            abrirPantallaMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void abrirPantallaMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
            Parent root = loader.load();

            MenuController controller = loader.getController();
            Stage stageMenu = new Stage();
            stageMenu.setScene(new Scene(root));
            stageMenu.setTitle("Menú Principal");
            stageMenu.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));

            Entrenador entrenador = EntrenadorDAO.obtenerEntrenadorPorId(idEntrenador);
            controller.init(entrenador, stageMenu, loginController);

            stageMenu.show();

            Stage currentStage = (Stage) btnElegirBulbasur.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        imgBulbasur.setVisible(false);
        imgCharmander.setVisible(false);
        imgSquirtle.setVisible(false);

        try {
            imgBulbasur.setImage(new Image(new File("C:/ProyectoPokemon/resources/img/Pokemon/Front/001.png").toURI().toString()));
            imgCharmander.setImage(new Image(new File("C:/ProyectoPokemon/resources/img/Pokemon/Front/004.png").toURI().toString()));
            imgSquirtle.setImage(new Image(new File("C:/ProyectoPokemon/resources/img/Pokemon/Front/007.png").toURI().toString()));
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar una imagen de Pokémon.");
            e.printStackTrace();
        }
    }
}
