package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Entrenador;

public class EntrenadorDAO {


		// Añadir un nuevo entrenador
	public static boolean anyadirEntrenador(Connection con, Entrenador entrenador) {
	    String query = "INSERT INTO entrenador (usuario, pass) VALUES (?, ?)";

	    try {
	        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
	        ps.setString(1, entrenador.getUsuario());
	        ps.setString(2, entrenador.getPass());

	        int filasAfectadas = ps.executeUpdate();

	        if (filasAfectadas > 0) {
	            ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int idGenerado = generatedKeys.getInt(1);
	                entrenador.setIdentrenador(idGenerado); // ← actualizamos el objeto con el ID
	            }
	            return true;
	        } else {
	            return false;
	        }

	    } catch (SQLException e) {
	        ConexionBD.printSQLException(e);
	        return false;
	    }
	}

		// Verificar si un entrenador ya existe
		public static boolean existeEntrenador(Connection con, String nombre) {
			String query = "SELECT * FROM entrenador WHERE usuario = ?"; 


			try {
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, nombre);
				ResultSet rs = ps.executeQuery();

				return rs.next(); // true si existe, false si no

			} catch (SQLException e) {
				ConexionBD.printSQLException(e);
				return false;
			}
		}

		// Comprobar login
		public static Entrenador login(Connection con, String usuario, String pass) {
		    String query = "SELECT * FROM entrenador WHERE usuario = ? AND pass = ?";

		    try {
		        PreparedStatement ps = con.prepareStatement(query);
		        ps.setString(1, usuario);
		        ps.setString(2, pass);

		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            int id = rs.getInt("ID_ENTRENADOR");
		            String usuario1 = rs.getString("usuario");
		            String pass1 = rs.getString("pass");
		            int pokedolares = rs.getInt("pokedolares");

		            return new Entrenador(usuario1, pass1, pokedolares, id);
		        }

		    } catch (SQLException e) {
		        ConexionBD.printSQLException(e);
		    }

		    return null;
		}
		
		public static boolean actualizarDinero(Connection con, int idEntrenador, int nuevoDinero) {
		    String query = "UPDATE entrenador SET pokedolares = ? WHERE ID_ENTRENADOR = ?";
		    try {
		        PreparedStatement ps = con.prepareStatement(query);
		        ps.setInt(1, nuevoDinero);
		        ps.setInt(2, idEntrenador);
		        return ps.executeUpdate() > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}

}
