package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;

import dao.PokemonDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;

public class EquipoController {

    private MenuController menuController;
    private Stage stage;
    private Entrenador entrenador;
    private LoginController loginController;

    @FXML private ProgressBar barPokemon1, barPokemon2, barPokemon3, barPokemon4, barPokemon5, barPokemon6;
    @FXML private ImageView btnSalir;
    @FXML private AnchorPane imgFondo;
    @FXML private ImageView imgPokemon1, imgPokemon2, imgPokemon3, imgPokemon4, imgPokemon5, imgPokemon6;
    @FXML private Label lblNombre1, lblNombre2, lblNombre3, lblNombre4, lblNombre5, lblNombre6;
    @FXML private Label lblPS2, lblPS3, lblPS4, lblPS5, lblPS6, lblPs1;
    @FXML private Label lblNivel1, lblNivel2, lblNivel3, lblNivel4, lblNivel5, lblNivel6;
    @FXML private Button btnCaja;

    public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {
        this.menuController = menuController;
        this.stage = stage;
        this.entrenador = entrenador;
        this.loginController = loginController;
        cargarEquipo();
    }

    private void cargarImagen(ImageView imageView, String nombreArchivo) {
        String ruta = "C:/ProyectoPokemon/resources/img/Pokemon/Front/" + nombreArchivo;
        File archivo = new File(ruta);
        if (archivo.exists()) {
            imageView.setImage(new Image(archivo.toURI().toString()));
        } else {
            imageView.setImage(null);
        }
    }

    private void cargarEquipo() {
        List<Pokemon> equipo = PokemonDAO.obtenerEquipo(entrenador.getIdentrenador());

        ImageView[] imagenes = { imgPokemon1, imgPokemon2, imgPokemon3, imgPokemon4, imgPokemon5, imgPokemon6 };
        Label[] nombres = { lblNombre1, lblNombre2, lblNombre3, lblNombre4, lblNombre5, lblNombre6 };
        Label[] niveles = { lblNivel1, lblNivel2, lblNivel3, lblNivel4, lblNivel5, lblNivel6 };
        Label[] ps = { lblPs1, lblPS2, lblPS3, lblPS4, lblPS5, lblPS6 };
        ProgressBar[] barras = { barPokemon1, barPokemon2, barPokemon3, barPokemon4, barPokemon5, barPokemon6 };

        for (int i = 0; i < 6; i++) {
            if (i < equipo.size()) {
                Pokemon p = equipo.get(i);
                nombres[i].setText(p.getNombre());
                niveles[i].setText("Nv. " + p.getNivel());
                ps[i].setText(p.getVitalidad() + " PS");
                barras[i].setProgress(p.getVitalidad() / 100.0);
                cargarImagen(imagenes[i], p.getImgFrontal());

                imagenes[i].setOnMouseClicked(e -> {
                    PokemonDAO.actualizarEquipo(p.getId(), 0);
                    JOptionPane.showMessageDialog(null, p.getNombre() + " ha sido enviado a la caja.");
                    cargarEquipo();
                });
            } else {
                nombres[i].setText("");
                niveles[i].setText("");
                ps[i].setText("");
                barras[i].setProgress(0);
                imagenes[i].setImage(null);
                imagenes[i].setOnMouseClicked(null);
            }
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
            st.setTitle("Proyecto Pokemon los 3 mosqueteros");
            st.setScene(sc);
            menuController.init(entrenador, st, loginController);
            st.show();
            this.stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirCaja(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Caja.fxml"));
            Parent root = loader.load();

            CajaController cajaController = loader.getController();

            Stage cajaStage = new Stage();
            cajaStage.setTitle("Caja de Pokémon");
            cajaStage.setScene(new Scene(root));
            cajaStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));

            // ✅ PASA THIS → EquipoController
            cajaController.init(entrenador, cajaStage, this);

            cajaStage.show();
            stage.close(); // cerrar la ventana del equipo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEquipo() {
        cargarEquipo(); // Método que repinta visualmente los Pokémon del equipo
    }

    public void show() {
        if (stage != null) {
            stage.show();
        }
    }
}
