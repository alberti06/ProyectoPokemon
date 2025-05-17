package controller;


import java.io.File;
import java.io.IOException;

import dao.EntrenadorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entrenador;
import dao.PokemonDAO;


public class MenuController {
	
	private Entrenador entrenador;
    private Stage stage;
    private LoginController loginController;
    private EquipoController equipoController;
    
    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private VBox Box;

    @FXML
    private ImageView btnConf;

    @FXML
    private ImageView imgBolsa;

    @FXML
    private ImageView imgCaptura;

    @FXML
    private ImageView imgCenPokemon;

    @FXML
    private ImageView imgCombate;

    @FXML
    private ImageView imgCrianza;

    @FXML
    private ImageView imgEquipo;

    @FXML
    private ImageView imgFondo;

    @FXML
    private ImageView imgSalir;
    
    @FXML
    private ImageView imgTienda;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblJugador;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPesetas;
    
    public void init(Entrenador ent, Stage stage, LoginController loginController) {
        this.loginController = loginController;
        this.stage = stage;
        this.entrenador = ent;

        // ✅ AÑADIR ESTA LÍNEA PARA CARGAR EL EQUIPO
        entrenador.setPokemons(dao.PokemonDAO.obtenerEquipo(entrenador.getIdentrenador()));

        lblNombre.setText(ent.getUsuario());
        lblCantidad.setText(Integer.toString(ent.getPokedolares()));
        
        /*
        Box.prefWidthProperty().bind(stage.widthProperty());
        Box.prefHeightProperty().bind(stage.heightProperty());*/
    }
    
    public void actualizarDinero() {
        lblCantidad.setText(String.valueOf(entrenador.getPokedolares()));
        lblPesetas.setVisible(false);
        //lblPesetas.setText(entrenador.getPokedolares() + " $");
    }
    
    @FXML
    void salirInicio(MouseEvent event) {
        if (loginController != null && stage != null) {
            loginController.show();
            stage.close();
            System.out.println("Has cerrado sesion correctamente");
        }
    }
    
    //REVISAR
    public void show() {
        if (stage != null) {
            stage.show();
        }
    }

    @FXML
    void abrirConf(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuConfiguracion.fxml"));
            Parent root = loader.load();

            Stage confStage = new Stage();
            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Configuración");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    @FXML
    void abrirBolsa(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BolsaPokemon.fxml"));
            Parent root = loader.load(); // ⚠️ primero carga

            BolsaController controller = loader.getController(); // ✅ luego obtén el controller

            Stage confStage = new Stage();
            controller.init(entrenador, confStage, this, loginController);

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Bolsa");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void abrirCaptura(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/captura.fxml"));
            Parent root = loader.load(); // ⚠️ primero carga

            CapturaController controller = loader.getController(); // ✅ luego obtén el controller

            Stage confStage = new Stage();
            controller.init(entrenador, confStage, this, loginController);

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Captura");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void abrirCentropoke(MouseEvent event) {
        try {
            // Cargar el equipo antes de abrir el centro
            entrenador.setEquipo(PokemonDAO.obtenerEquipo(entrenador.getIdentrenador()));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CentroPokemon.fxml"));
            Parent root = loader.load();

            CentropokeController controller = loader.getController();
            Stage confStage = new Stage();
            controller.init(entrenador, confStage, this, loginController);

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Centro Pokemon");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   
    @FXML
    void abrirCombate(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Combate.fxml"));
            Parent root = loader.load();

            CombateController combateController = loader.getController();
            
            Stage confStage = new Stage(); // <-- crea primero la nueva ventana
            combateController.init(entrenador, confStage, this, loginController); // <-- pasar confStage aquí

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Combate");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @FXML
    void abrirCrianza(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Crianza.fxml"));
            Parent root = loader.load();

            CrianzaController crianzaController = loader.getController();
            
            Stage confStage = new Stage(); // <-- crea primero la nueva ventana
            crianzaController.init(entrenador, confStage, this, loginController); // <-- pasar confStage aquí

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Crianza");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void abrirEquipo(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Equipo.fxml"));
            Parent root = loader.load();

            equipoController = loader.getController(); // ← guardamos la referencia

            Stage confStage = new Stage();
            equipoController.init(entrenador, confStage, this, loginController);

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Equipo");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirPokedex(MouseEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Entrenamiento.fxml"));
    		Parent root = loader.load();

    		EntrenamientoController entrenamientoController = loader.getController(); 
    		Scene scene = new Scene(root);
    		Stage pokedexStage = new Stage();

    		pokedexStage.setScene(scene);
    		pokedexStage.setTitle("Pokédex");
    		entrenamientoController.init(entrenador, pokedexStage, this, loginController);
    		pokedexStage.show();
    		  this.stage.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void abrirTienda(MouseEvent event) {
    	    try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tienda.fxml"));
    	        Parent root = loader.load();

    	        TiendaController tiendaController = loader.getController();

    	        Stage confStage = new Stage();
    	        tiendaController.init(entrenador, confStage, this, loginController); 

    	        confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
    	        confStage.setTitle("Tienda");
    	        confStage.setScene(new Scene(root));
    	        confStage.setResizable(false);
    	        confStage.show();

    	        this.stage.close();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
    
    public void initEntrenadorDesdeId(int idEntrenador) {
        try {
            Entrenador ent = EntrenadorDAO.obtenerEntrenadorPorId(idEntrenador);
            this.entrenador = ent;
            lblNombre.setText(ent.getUsuario());
            lblCantidad.setText(String.valueOf(ent.getPokedolares()));
            lblPesetas.setText(ent.getPokedolares() + " $");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizarVistaEquipo() {
        if (equipoController != null) {
            equipoController.actualizarEquipo(); // ← método que debe estar en EquipoController
        } else {
            System.out.println("No hay equipo cargado aún.");
        }
    }

    
    
}