package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Pokemon;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ResourceBundle;

import dao.ConexionBD;
import dao.EntrenadorDAO;
import dao.PokemonDAO;

public class PantallaChoosePokemonController implements Initializable{
	
	private LoginController loginController;
	private int idEntrenador;

    @FXML
    private Button btnElegirpoke1;

    @FXML
    private Button btnElegirpoke2;

    @FXML
    private Button btnElegirpoke3;

    @FXML
    private ImageView imgBulbasur;

    @FXML
    private ImageView imgCharmander;

    @FXML
    private ImageView imgSquirtle;

    @FXML
    private Label lblTexto;

    @FXML
    private Label lblTexto1;

    @FXML
    private Label lblTexto11;
    

    // Este método se llama desde el controlador anterior (registro)
    public void setIdEntrenador(int id) {
        this.idEntrenador = id;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ocultar imágenes al inicio
        imgBulbasur.setVisible(false);
        imgCharmander.setVisible(false);
        imgSquirtle.setVisible(false);

        // Hover: mostrar imagen
        btnElegirpoke1.setOnMouseEntered(e -> imgBulbasur.setVisible(true));
        btnElegirpoke1.setOnMouseExited(e -> imgBulbasur.setVisible(false));

        btnElegirpoke2.setOnMouseEntered(e -> imgCharmander.setVisible(true));
        btnElegirpoke2.setOnMouseExited(e -> imgCharmander.setVisible(false));

        btnElegirpoke3.setOnMouseEntered(e -> imgSquirtle.setVisible(true));
        btnElegirpoke3.setOnMouseExited(e -> imgSquirtle.setVisible(false));

        // Click: elegir Pokémon
        btnElegirpoke1.setOnAction(e -> elegirPokemon("Bulbasaur"));
        btnElegirpoke2.setOnAction(e -> elegirPokemon("Charmander"));
        btnElegirpoke3.setOnAction(e -> elegirPokemon("Squirtle"));
    }

    private void elegirPokemon(String nombrePokemon) {
        try (Connection con = ConexionBD.conectar()) {
            Pokemon elegido = PokemonDAO.generarPokemonPrincipalEspecifico(idEntrenador, nombrePokemon, con);
            System.out.println("Pokémon elegido y guardado: " + elegido.getNombre());

            // Cierra la ventana actual
            Stage currentStage = (Stage) btnElegirpoke1.getScene().getWindow();
            currentStage.close();

            // Abre la pantalla del menú principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
            Parent root = loader.load();

            MenuController controller = loader.getController();
            Stage stage = new Stage();
            controller.init(EntrenadorDAO.obtenerEntrenadorPorId(idEntrenador), stage, loginController); // Usa el método correcto

            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
