package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dao.PokemonDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @FXML 
    private ProgressBar barPokemon1;
    
    @FXML 
    private ProgressBar barPokemon2;
    
    @FXML 
    private ProgressBar barPokemon3;
    
    @FXML 
    private ProgressBar barPokemon4;
    
    @FXML 
    private ProgressBar barPokemon5;
    
    @FXML 
    private ProgressBar barPokemon6;

    @FXML
    private ImageView btnSalir;

    @FXML
    private AnchorPane imgFondo;

    @FXML 
    private ImageView imgPokemon1;
    
    @FXML 
    private ImageView imgPokemon2;
    
    @FXML 
    private ImageView imgPokemon3;
    
    @FXML 
    private ImageView imgPokemon4;
    
    @FXML 
    private ImageView imgPokemon5;
    
    @FXML 
    private ImageView imgPokemon6;

    @FXML 
    private Label lblNombre1;
    
    @FXML 
    private Label lblNombre2;
    
    @FXML 
    private Label lblNombre3;
    
    @FXML 
    private Label lblNombre4;
    
    @FXML 
    private Label lblNombre5;
    
    @FXML 
    private Label lblNombre6;

    @FXML 
    private Label lblPS2;
    
    @FXML 
    private Label lblPS3;
    
    @FXML 
    private Label lblPS4;
    
    @FXML 
    private Label lblPS5;
    
    @FXML 
    private Label lblPS6;
    
    @FXML 
    private Label lblPs1;

    @FXML 
    private Label lblNivel1;
    
    @FXML 
    private Label lblNivel2;
    
    @FXML 
    private Label lblNivel3;
    
    @FXML 
    private Label lblNivel4;
    
    @FXML 
    private Label lblNivel5;
    
    @FXML 
    private Label lblNivel6;

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

    public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {
        this.menuController = menuController;
        this.stage = stage;
        this.entrenador = entrenador;
        this.loginController = loginController;
        cargarEquipo();
    }

    private void cargarImagen(ImageView imageView, String nombreArchivo) {
        String ruta = "/Pokemon/Front/" + nombreArchivo; // sin "img" al principio
        InputStream is = getClass().getResourceAsStream(ruta);
        System.out.println("🔍 Buscando imagen: " + ruta + " | Existe: " + (is != null));
        if (is != null) {
            imageView.setImage(new Image(is));
        } else {
            System.out.println("⚠ Imagen no encontrada en: " + ruta);
        }
    }

    private void cargarEquipo() {
        List<Pokemon> equipo = PokemonDAO.obtenerEquipo(entrenador.getIdentrenador());

        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            String nombre = p.getNombre();
            int vitalidad = p.getVitalidad();
            int nivel = p.getNivel();
            double progreso = vitalidad / 100.0;
            String imagenArchivo = p.getImgFrontal();

            switch (i) {
                case 0:
                    lblNombre1.setText(nombre);
                    lblPs1.setText(vitalidad + " PS");
                    lblNivel1.setText("Nv. " + nivel);
                    barPokemon1.setProgress(progreso);
                    cargarImagen(imgPokemon1, imagenArchivo);
                    break;
                case 1:
                    lblNombre2.setText(nombre);
                    lblPS2.setText(vitalidad + " PS");
                    lblNivel2.setText("Nv. " + nivel);
                    barPokemon2.setProgress(progreso);
                    cargarImagen(imgPokemon2, imagenArchivo);
                    break;
                case 2:
                    lblNombre3.setText(nombre);
                    lblPS3.setText(vitalidad + " PS");
                    lblNivel3.setText("Nv. " + nivel);
                    barPokemon3.setProgress(progreso);
                    cargarImagen(imgPokemon3, imagenArchivo);
                    break;
                case 3:
                    lblNombre4.setText(nombre);
                    lblPS4.setText(vitalidad + " PS");
                    lblNivel4.setText("Nv. " + nivel);
                    barPokemon4.setProgress(progreso);
                    cargarImagen(imgPokemon4, imagenArchivo);
                    break;
                case 4:
                    lblNombre5.setText(nombre);
                    lblPS5.setText(vitalidad + " PS");
                    lblNivel5.setText("Nv. " + nivel);
                    barPokemon5.setProgress(progreso);
                    cargarImagen(imgPokemon5, imagenArchivo);
                    break;
                case 5:
                    lblNombre6.setText(nombre);
                    lblPS6.setText(vitalidad + " PS");
                    lblNivel6.setText("Nv. " + nivel);
                    barPokemon6.setProgress(progreso);
                    cargarImagen(imgPokemon6, imagenArchivo);
                    break;
            }
        }
    }

}
