package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import util.AudioManager;

public class ConfiguracionController {
//Añadidos los elementos de la vista 
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

    private final Map<String, String> canciones = new HashMap<>();
    private String ultimaCancionMostrada = "";

    @FXML
    public void initialize() {
        canciones.put("Música Intro", "Musica-‐-Hecho-con-Clipchamp.mp3");
        canciones.put("Himno Real Murcia", "Himno-del-Centenario-Real-Murcia-CF.wav");
        canciones.put("Coldplay - Viva La Vida", "Coldplay - Viva La Vida (Official Video).mp3");
        canciones.put("La Roja Baila", "La Roja Baila (Himno Oficial de la Selección Española) (Videoclip Oficial).mp3");
        canciones.put("Imagine", "John Lennon - Imagine - 1971.mp3");
        canciones.put("Too Much Love Will Kill You", "Queen - Too Much Love Will Kill You (Official Video).mp3");
        canciones.put("Faro de Lisboa", "Faro de Lisboa (feat. Bunbury).mp3");
        canciones.put("Master of Puppets", "Metallica - Master of Puppets (Live) [Quebec Magnetic].mp3");

        choiceCanciones.getItems().addAll(canciones.keySet());

        sliderVolumen.setValue(AudioManager.getVolumenGlobal() * 100);
        sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
            double vol = newVal.doubleValue() / 100;
            AudioManager.setVolumenGlobal(vol);
            if (AudioManager.getMediaPlayer() != null) {
                AudioManager.getMediaPlayer().setVolume(vol);
            }
        });

        if (AudioManager.getMediaPlayer() != null) {
            String actual = AudioManager.getCancionActual();
            canciones.forEach((nombre, archivo) -> {
                if (actual.contains(archivo)) {
                    choiceCanciones.getSelectionModel().select(nombre);
                }
            });
        } else {
            choiceCanciones.getSelectionModel().selectFirst();
        }

        actualizarIconoPlay();

        AudioManager.setOnStatusChanged(() -> Platform.runLater(this::actualizarIconoPlay));
        AudioManager.setOnTrackChanged(() -> Platform.runLater(() -> {
            actualizarNombreCancion();
            actualizarIconoPlay();
        }));

        actualizarNombreCancion();

        choiceCanciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String archivo = canciones.get(newVal);
                String ruta = "./sonidos/" + archivo;

                List<String> lista = new ArrayList<>();
                for (String value : canciones.values()) {
                    lista.add("./sonidos/" + value);
                }

                int index = lista.indexOf(ruta);

                if (!AudioManager.getCancionActual().equals(new File(ruta).getName())) {
                    AudioManager.setPlaylist(lista);
                    AudioManager.setCurrentIndex(index);
                    AudioManager.reproducirActual();
                }

                actualizarNombreCancion();
                actualizarIconoPlay();
            }
        });
    }
    //metodo para pausar o play de la musica

    private void actualizarIconoPlay() {
        if (AudioManager.getMediaPlayer() != null &&
            AudioManager.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            btnPlay.setImage(new Image(new File("./img/imagenesExtra/pause.png").toURI().toString()));
        } else {
            btnPlay.setImage(new Image(new File("./img/imagenesExtra/play.png").toURI().toString()));
        }
    }

    @FXML
    void cerrarVentana(MouseEvent event) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void togglePlay(MouseEvent event) {
        if (AudioManager.getMediaPlayer() != null) {
            if (AudioManager.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                AudioManager.pausar();
            } else {
                AudioManager.continuar();
            }
        } else {
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
            }
        }

        actualizarIconoPlay();
        actualizarNombreCancion();
    }

    @FXML
    void siguienteCancion(MouseEvent event) {
        AudioManager.siguiente();
        actualizarNombreCancion();
        actualizarIconoPlay();
    }

    @FXML
    void anteriorCancion(MouseEvent event) {
        AudioManager.anterior();
        actualizarIconoPlay();
        actualizarNombreCancion();
    }

    private void actualizarNombreCancion() {
        String nombre = AudioManager.getCancionActual();
        lblCancionActual.setText("Reproduciendo: " + nombre);

        for (Map.Entry<String, String> entry : canciones.entrySet()) {
            if (nombre.equals(entry.getValue())) {
                choiceCanciones.getSelectionModel().select(entry.getKey());
                break;
            }
        }
    }
}