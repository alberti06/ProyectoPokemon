package util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioManager {
	
	    private static MediaPlayer mediaPlayer;
	    private static List<String> playlist = new ArrayList<>();
	    private static int currentIndex = 0;

	    public static void setPlaylist(List<String> canciones) {
	        playlist.clear();
	        playlist.addAll(canciones);
	        currentIndex = 0;
	    }

	    public static void reproducirActual() {
	        if (playlist.isEmpty()) return;
	        reproducirMusica(playlist.get(currentIndex));
	    }

	    public static void reproducirMusica(String ruta) {
	        try {
	            if (mediaPlayer != null) {
	                mediaPlayer.stop();
	            }

	            Media media = new Media(new File(ruta).toURI().toString());
	            mediaPlayer = new MediaPlayer(media);
	            mediaPlayer.play();

	            mediaPlayer.setOnEndOfMedia(() -> {
	                currentIndex = (currentIndex + 1) % playlist.size();
	                reproducirActual();
	                if (onTrackChanged != null) {
	                    javafx.application.Platform.runLater(onTrackChanged);
	                }
	            });

	        } catch (Exception e) {
	            System.out.println("Error al reproducir música: " + e.getMessage());
	        }
	    }

	    public static void bajarVolumen() {
	        if (mediaPlayer != null)
	            mediaPlayer.setVolume(Math.max(0, mediaPlayer.getVolume() - 0.1));
	    }

	    public static void subirVolumen() {
	        if (mediaPlayer != null)
	            mediaPlayer.setVolume(Math.min(1, mediaPlayer.getVolume() + 0.1));
	    }

	    public static void pausar() {
	        if (mediaPlayer != null)
	            mediaPlayer.pause();
	    }

	    public static void continuar() {
	        if (mediaPlayer != null)
	            mediaPlayer.play();
	    }

	    public static MediaPlayer getMediaPlayer() {
	        return mediaPlayer;
	    }

	    public static String getCancionActual() {
	        return !playlist.isEmpty() ? new File(playlist.get(currentIndex)).getName() : "Ninguna";
	    }
	    
	    private static Runnable onTrackChanged;  // esto se ejecutará cuando cambie la canción

	    public static void setOnTrackChanged(Runnable callback) {
	        onTrackChanged = callback;
	    }
	    
	    public static void setCurrentIndex(int index) {
	        if (index >= 0 && index < playlist.size()) {
	            currentIndex = index;
	        }
	    }
}
