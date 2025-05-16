package controller;

import dao.PokemonDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Entrenador;
import model.Pokemon;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CrianzaController {

    @FXML private ChoiceBox<Pokemon> choiceHembra;
    @FXML private ChoiceBox<Pokemon> choiceMacho;
    @FXML private ImageView imgCria;
    @FXML private ImageView imgHuevoMasc;
    @FXML private ImageView imgPokeF;
    @FXML private ImageView imgPokeM;
    @FXML private Label lblText1;
    @FXML private Label lblText2;
    @FXML private Label lblText3;

    private Pokemon padreMacho;
    private Pokemon madreHembra;
    private Pokemon nuevoHijo;

    private Entrenador entrenador;
    private Stage stage;
    private MenuController menuController;
    private LoginController loginController;

    public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {
        this.entrenador = entrenador;
        this.stage = stage;
        this.menuController = menuController;
        this.loginController = loginController;

        List<Pokemon> equipo = PokemonDAO.obtenerEquipo(entrenador.getIdentrenador());
        List<Pokemon> caja = PokemonDAO.obtenerCaja(entrenador.getIdentrenador());

        equipo.addAll(caja);
        inicializarCrianza(equipo);
    }

    public void inicializarCrianza(List<Pokemon> equipoYCaja) {
        List<Pokemon> machos = equipoYCaja.stream()
                .filter(p -> "M".equalsIgnoreCase(p.getSexo()) && p.getFertilidad() > 0)
                .collect(Collectors.toList());

        List<Pokemon> hembras = equipoYCaja.stream()
                .filter(p -> "F".equalsIgnoreCase(p.getSexo()) && p.getFertilidad() > 0)
                .collect(Collectors.toList());

        StringConverter<Pokemon> converter = new StringConverter<>() {
            @Override
            public String toString(Pokemon p) {
                if (p == null) return "";
                return p.getNombre() + " (Nv. " + p.getNivel() + ", Fertilidad: " + p.getFertilidad() + ")";
            }

            @Override
            public Pokemon fromString(String string) {
                return null;
            }
        };

        choiceMacho.setConverter(converter);
        choiceHembra.setConverter(converter);

        choiceMacho.getItems().addAll(machos);
        choiceHembra.getItems().addAll(hembras);

        choiceMacho.setOnAction(e -> {
            comprobarCompatibilidad();
            actualizarImagenMacho();
        });

        choiceHembra.setOnAction(e -> {
            comprobarCompatibilidad();
            actualizarImagenHembra();
        });

        imgCria.setVisible(false);
    }

    private void comprobarCompatibilidad() {
        padreMacho = choiceMacho.getValue();
        madreHembra = choiceHembra.getValue();
    }

    private void actualizarImagenMacho() {
        if (padreMacho != null && padreMacho.getImgFrontal() != null) {
            String ruta = "file:/C:/ProyectoPokemon/resources/img/Pokemon/Front/" + padreMacho.getImgFrontal();
            imgPokeM.setImage(new Image(ruta));
        }
    }

    private void actualizarImagenHembra() {
        if (madreHembra != null && madreHembra.getImgFrontal() != null) {
            String ruta = "file:/C:/ProyectoPokemon/resources/img/Pokemon/Front/" + madreHembra.getImgFrontal();
            imgPokeF.setImage(new Image(ruta));
        }
    }

    @FXML
    void fusionarHuevo(MouseEvent event) {
        padreMacho = choiceMacho.getValue();
        madreHembra = choiceHembra.getValue();

        if (padreMacho == null || madreHembra == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un macho y una hembra.");
            return;
        }
        if (!padreMacho.getTipo1().equalsIgnoreCase(madreHembra.getTipo1())) {
            JOptionPane.showMessageDialog(null, "Deben compartir el mismo tipo primario.");
            return;
        }

        String tipo1 = padreMacho.getTipo1();
        String tipo2 = padreMacho.getTipo2();
        String nombre = padreMacho.getNombre();

        int vitalidad = Math.max(padreMacho.getVitalidad(), madreHembra.getVitalidad());
        int ataque = Math.max(padreMacho.getAtaque(), madreHembra.getAtaque());
        int defensa = Math.max(padreMacho.getDefensa(), madreHembra.getDefensa());
        int atEspecial = Math.max(padreMacho.getAtEspecial(), madreHembra.getAtEspecial());
        int defEspecial = Math.max(padreMacho.getDefEspecial(), madreHembra.getDefEspecial());
        int velocidad = Math.max(padreMacho.getVelocidad(), madreHembra.getVelocidad());

        String sexoHijo = Math.random() < 0.5 ? "M" : "F";
        Pokemon parentVisual = Math.random() < 0.5 ? padreMacho : madreHembra;

        nuevoHijo = new Pokemon(
        	    0,
        	    padreMacho.getIdEntrenador(),
        	    padreMacho.getNumPokedex(), // ya debería ser el mismo para ambos en este punto
        	    nombre,
        	    tipo1,
        	    tipo2,
        	    vitalidad,
        	    ataque,
        	    defensa,
        	    atEspecial,
        	    defEspecial,
        	    velocidad,
        	    1,
        	    5,
        	    sexoHijo,
        	    "Sano",
        	    0,
        	    parentVisual.getImgFrontal(),
        	    parentVisual.getImgTrasera(),
        	    parentVisual.getSonido(),
        	    parentVisual.getNivelEvolucion()
        	);

        padreMacho.setFertilidad(padreMacho.getFertilidad() - 1);
        madreHembra.setFertilidad(madreHembra.getFertilidad() - 1);
        PokemonDAO.actualizarFertilidad(padreMacho.getId(), padreMacho.getFertilidad());
        PokemonDAO.actualizarFertilidad(madreHembra.getId(), madreHembra.getFertilidad());

        String rutaMacho = "file:/C:/ProyectoPokemon/resources/img/Pokemon/Front/" + padreMacho.getImgFrontal();
        String rutaHembra = "file:/C:/ProyectoPokemon/resources/img/Pokemon/Front/" + madreHembra.getImgFrontal();
        String rutaCria = "file:/C:/ProyectoPokemon/resources/img/Pokemon/Front/" + nuevoHijo.getImgFrontal();

        imgPokeM.setImage(new Image(rutaMacho));
        imgPokeF.setImage(new Image(rutaHembra));
        imgCria.setImage(new Image(rutaCria));
        imgCria.setVisible(true);
        lblText3.setText("¡Ha nacido un " + nuevoHijo.getNombre() + "!");

        PokemonDAO.insertarPokemon(entrenador.getIdentrenador(), nuevoHijo);

        JOptionPane.showMessageDialog(null, "🐣 ¡Ha nacido un " + nuevoHijo.getNombre() + "!", "Crianza completada", JOptionPane.INFORMATION_MESSAGE);
    }

    @FXML
    void abrirHuevo(MouseEvent event) {
        if (nuevoHijo != null) {
            String rutaCria = "file:/C:/ProyectoPokemon/resources/img/Pokemon/Front/" + nuevoHijo.getImgFrontal();
            lblText3.setText("¡Tu Pokémon ha nacido!");
            imgCria.setImage(new Image(rutaCria));
        }
    }

    @FXML
    void salirMenupoke(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            menuController = loader.getController();
            Scene sc = new Scene(root);
            Stage st = new Stage();
            st.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            st.setTitle("Proyecto Pokemon los 3 mosqueteros");
            st.setScene(sc);
            menuController.init(entrenador, st, loginController);
            st.show();
            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}