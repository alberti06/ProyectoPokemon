package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Objeto;

public class ObjetoDAO {

    public static Objeto obtenerObjetoPorId(Connection con, int id) throws SQLException {
        String sql = "SELECT * FROM objeto WHERE ID_OBJETO = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Objeto(
                    rs.getInt("ID_OBJETO"),
                    rs.getString("NOM_OBJETO"),
                    rs.getInt("ATAQUE"),
                    rs.getInt("AT_ESPECIAL"),
                    rs.getInt("DEFENSA"),
                    rs.getInt("DEF_ESPECIAL"),
                    rs.getInt("VELOCIDAD"),
                    rs.getInt("PRECIO")
                );
            }
        }
        return null;
    }

    public static List<Objeto> obtenerTodosLosObjetos(Connection con) throws SQLException {
        List<Objeto> lista = new ArrayList<>();
        String sql = "SELECT * FROM objeto";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Objeto(
                    rs.getInt("ID_OBJETO"),
                    rs.getString("NOM_OBJETO"),
                    rs.getInt("ATAQUE"),
                    rs.getInt("AT_ESPECIAL"),
                    rs.getInt("DEFENSA"),
                    rs.getInt("DEF_ESPECIAL"),
                    rs.getInt("VELOCIDAD"),
                    rs.getInt("PRECIO")
                ));
            }
        }
        return lista;
    }
    public static boolean restarObjeto(int idEntrenador, String objeto) {
        String sql = """
            UPDATE MOCHILA
            SET CANTIDAD = CANTIDAD - 1
            WHERE ID_ENTRENADOR = ? AND NOM_OBJETO = ? AND CANTIDAD > 0
        """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEntrenador);
            ps.setString(2, objeto.toLowerCase());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}