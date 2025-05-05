package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConexionBD {

    public static Connection con;

    private static String url = "jdbc:mysql://localhost:3306/pokemon";
    private static String login = "root";
    private static String password = "";

    public static Connection conectar() {
        try {
            Connection con = DriverManager.getConnection(url, login, password);
            System.out.println("Conexión establecida correctamente");
            return con;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            mostrarVentanaError(e);
            printSQLException(e);
            return null;
        }
    }

    private static void mostrarVentanaError(SQLException ex) {
    	JOptionPane.showOptionDialog(
                null,
            "No se ha podido conectar con la base de datos. " +
            "Revisa si has encendido el servidor MySQL.",
            "Error de conexión",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            new Object[] { "Cerrar juego" },
            "Cerrar juego"
        );
        System.exit(1); 
    }

    public static void printSQLException(SQLException ex) {
        ex.printStackTrace(System.err);
        System.err.println("SQLState: " + ex.getSQLState());
        System.err.println("Error code: " + ex.getErrorCode());
        System.err.println("Message: " + ex.getMessage());
        Throwable t = ex.getCause();
        while (t != null) {
            System.err.println("Cause: " + t);
            t = t.getCause();
        }
    }
}