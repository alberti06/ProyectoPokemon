package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MochilaDAO {

    public static void agregarObjeto(Connection con, int idEntrenador, int idObjeto, int cantidad) {
        try {
            // Ver si ya existe
            PreparedStatement select = con.prepareStatement(
                "SELECT NUMERO_OBJETOS FROM MOCHILA WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?"
            );
            select.setInt(1, idEntrenador);
            select.setInt(2, idObjeto);
            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                int actual = rs.getInt("NUMERO_OBJETOS");
                PreparedStatement update = con.prepareStatement(
                    "UPDATE MOCHILA SET NUMERO_OBJETOS = ? WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?"
                );
                update.setInt(1, actual + cantidad);
                update.setInt(2, idEntrenador);
                update.setInt(3, idObjeto);
                update.executeUpdate();
            } else {
                PreparedStatement insert = con.prepareStatement(
                    "INSERT INTO MOCHILA (ID_ENTRENADOR, ID_OBJETO, NUMERO_OBJETOS) VALUES (?, ?, ?)"
                );
                insert.setInt(1, idEntrenador);
                insert.setInt(2, idObjeto);
                insert.setInt(3, cantidad);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int obtenerCantidadObjeto(Connection con, int idEntrenador, int idObjeto) throws SQLException {
        String sql = "SELECT NUMERO_OBJETOS FROM mochila WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            ps.setInt(2, idObjeto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("NUMERO_OBJETOS");
            }
        }
        return 0;
    }

    public static void restarObjeto(Connection con, int idEntrenador, int idObjeto, int cantidad) throws SQLException {
        String sql = "UPDATE mochila SET NUMERO_OBJETOS = NUMERO_OBJETOS - ? WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idEntrenador);
            ps.setInt(3, idObjeto);
            ps.executeUpdate();
        }
    }
}