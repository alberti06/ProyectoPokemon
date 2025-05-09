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
            Class.forName("com.mysql.cj.jdbc.Driver"); // Asegura que el driver se carga
            con = DriverManager.getConnection(url, login, password);
            System.out.println("Conexión establecida correctamente");
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC no encontrado");
            e.printStackTrace();
            System.exit(1);
            return null; // Esto no se ejecuta, pero requerido por el compilador
        } catch (SQLException e) {
            mostrarVentanaError(e);
            return null; // Esto tampoco se ejecuta, porque el método termina con System.exit
        }
    }

    private static void mostrarVentanaError(SQLException ex) {
    	 System.out.println("Error con la conexion de la base de datos.");
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