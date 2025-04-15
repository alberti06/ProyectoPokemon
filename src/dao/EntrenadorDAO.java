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
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, entrenador.getUsuario());
				ps.setString(2, entrenador.getPass());

				int filasAfectadas = ps.executeUpdate();
				return filasAfectadas > 0;

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
		            String usuario1 = rs.getString("usuario");
		            String pass1 = rs.getString("pass");
		            int pokedolares = rs.getInt("pokedolares");

		            return new Entrenador(usuario1, pass1, pokedolares);
		        }

		    } catch (SQLException e) {
		        ConexionBD.printSQLException(e);
		    }

		    return null;
		}

}
