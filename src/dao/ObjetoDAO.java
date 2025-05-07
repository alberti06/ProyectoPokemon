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
}