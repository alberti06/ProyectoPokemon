package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.AudioManager;
import javafx.event.ActionEvent;

public class ConfiguracionController {


    @FXML
    private ImageView btnCerrar;
    
    @FXML
    private ImageView btnForward;

    @FXML
    private ImageView btnPlay;
    
    @FXML
    private ImageView btnRewind;

    @FXML
    private ChoiceBox<String> choiceCanciones;

    @FXML
    private ImageView imgFondo;
    
    @FXML
    private Label lblCancionActual;
    
    @FXML
    private Slider sliderVolumen;

    
    // Mapa para traducir el nombre visible a la ruta real
    private final Map<String, String> canciones = new HashMap<>();

    @FXML
    public void initialize() {
        canciones.put("Música Intro", "Musica-‐-Hecho-con-Clipchamp.mp3");
        canciones.put("Himno Real Murcia", "Himno-del-Centenario-Real-Murcia-CF.wav");
        canciones.put("Coldplay - Viva La Vida", "Coldplay - Viva La Vida (Official Video).mp3");
        canciones.put("La Roja Baila", "La Roja Baila (Himno Oficial de la Selección Española) (Videoclip Oficial).mp3");
        canciones.put("Imagine", "John Lennon - Imagine - 1971.mp3");
        canciones.put("Too Much Love Will Kill You", "Queen - Too Much Love Will Kill You (Official Video).mp3");
        canciones.put("Faro de Lisboa", "Faro de Lisboa (feat. Bunbury).mp3");
        
        
        
        choiceCanciones.getItems().addAll(canciones.keySet());
        choiceCanciones.getSelectionModel().selectFirst();

        if (AudioManager.getMediaPlayer() != null) {
            sliderVolumen.setValue(AudioManager.getMediaPlayer().getVolume() * 100);
        }

        sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (AudioManager.getMediaPlayer() != null) {
                AudioManager.getMediaPlayer().setVolume(newVal.doubleValue() / 100);
            }
        });

        AudioManager.setOnTrackChanged(() -> actualizarNombreCancion());
        actualizarNombreCancion();
    }

    @FXML
    void cerrarVentana(MouseEvent event) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void togglePlay(MouseEvent event) {
        if (AudioManager.getMediaPlayer() != null) {
            if (AudioManager.getMediaPlayer().getStatus() == javafx.scene.media.MediaPlayer.Status.PLAYING) {
                AudioManager.pausar();
                btnPlay.setImage(new Image(new File("./img/imagenesExtra/play.png").toURI().toString()));
            } else {
                // 🎯 Aquí revisamos si ya hay una canción cargada (mediaPlayer no null)
                AudioManager.continuar(); // Continúa la canción actual
                btnPlay.setImage(new Image(new File("./img/imagenesExtra/pause.png").toURI().toString()));
            }
        } else {
            // Si por alguna razón no hay mediaPlayer (primera vez), reproducir seleccionada
            String seleccion = choiceCanciones.getValue();
            if (seleccion != null) {
                String archivo = canciones.get(seleccion);
                String ruta = "./sonidos/" + archivo;

                List<String> lista = new ArrayList<>();
                for (String value : canciones.values()) {
                    lista.add("./sonidos/" + value);
                }

                int index = lista.indexOf(ruta);
                AudioManager.setPlaylist(lista);
                AudioManager.setCurrentIndex(index);
                AudioManager.reproducirActual();
                btnPlay.setImage(new Image(new File("./img/imagenesExtra/pause.png").toURI().toString()));
            }
        }

        actualizarNombreCancion();
    }

    @FXML
    void siguienteCancion(MouseEvent event) {
        AudioManager.siguiente();
        actualizarNombreCancion();
    }

    @FXML
    void anteriorCancion(MouseEvent event) {
        AudioManager.anterior();
        actualizarNombreCancion();
    }

    private void actualizarNombreCancion() {
        String nombre = AudioManager.getCancionActual();
        lblCancionActual.setText("Reproduciendo: " + nombre);
        System.out.println("Musica actualizada: " + nombre);
    }
}
