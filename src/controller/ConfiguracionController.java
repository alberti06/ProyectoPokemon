package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.AudioManager;
import javafx.event.ActionEvent;

public class ConfiguracionController {

    @FXML
    private ImageView btnBajarVolumen;

    @FXML
    private ImageView btnCerrar;

    @FXML
    private Button btnReproducir;

    @FXML
    private ImageView btnSubirVolumen;

    @FXML
    private ChoiceBox<String> choiceCanciones;

    @FXML
    private ImageView imgFondo;
    
    @FXML
    private Label lblCancionActual;

    
    // Mapa para traducir el nombre visible a la ruta real
    private final Map<String, String> canciones = new HashMap<>();

    @FXML
    public void initialize() {
    	canciones.put("Música Intro", "Musica-‐-Hecho-con-Clipchamp.mp3");
        canciones.put("Himno Real Murcia", "Himno-del-Centenario-Real-Murcia-CF.wav");
        canciones.put("Coldplay - Viva La Vida", "Coldplay - Viva La Vida (Official Video).mp3");
        canciones.put("La Roja Baila", "La Roja Baila (Himno Oficial de la Selección Española) (Videoclip Oficial).mp3");

        choiceCanciones.getItems().addAll(canciones.keySet());
        choiceCanciones.getSelectionModel().selectFirst();

        AudioManager.setOnTrackChanged(() -> {
            System.out.println("Callback recibido por ConfiguracionController");
            actualizarNombreCancion();
        });

        actualizarNombreCancion(); 
        
            }


    @FXML
    void cerrarVentana(MouseEvent event) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void bajarVolumen(MouseEvent event) {
        AudioManager.bajarVolumen();
    }

    @FXML
    void subirVolumen(MouseEvent event) {
        AudioManager.subirVolumen();
    }

    @FXML
    void reproducirCancion(ActionEvent event) {
        String seleccion = choiceCanciones.getValue();
        if (seleccion != null) {
            String archivo = canciones.get(seleccion);
            String ruta = "./sonidos/" + archivo;

            // Crear nueva playlist con todas las canciones disponibles
            List<String> lista = new ArrayList<>();
            for (String value : canciones.values()) {
                lista.add("./sonidos/" + value);
            }

            int index = lista.indexOf(ruta);
            AudioManager.setPlaylist(lista);
            AudioManager.setCurrentIndex(index); // 🔥 NUEVO
            AudioManager.reproducirActual();

            actualizarNombreCancion(); // Mostramos la canción actual

            // 🔁 Configurar el listener para actualizar el label al cambiar de canción
            AudioManager.getMediaPlayer().setOnEndOfMedia(() -> {
                AudioManager.reproducirActual(); // siguiente canción
                javafx.application.Platform.runLater(() -> actualizarNombreCancion());
            });
        }
    }
        
    private void actualizarNombreCancion() {
        String nombre = AudioManager.getCancionActual();
        lblCancionActual.setText("Reproduciendo: " + nombre);
        System.out.println("Label actualizado con: " + nombre);
    }
}
