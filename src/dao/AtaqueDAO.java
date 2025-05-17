package dao;

import model.Ataque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtaqueDAO {

    // Obtener ataque por ID
    public static Ataque obtenerAtaquePorId(int idMovimiento, Connection conexion) throws SQLException {
        String query = "SELECT * FROM MOVIMIENTOS WHERE ID_MOVIMIENTO = ?";
        try (PreparedStatement st = conexion.prepareStatement(query)) {
            st.setInt(1, idMovimiento);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Ataque(
                        rs.getInt("ID_MOVIMIENTO"),
                        rs.getString("NOM_MOVIMIENTO"),
                        rs.getInt("NIVEL_APRENDIZAJE"),
                        rs.getInt("PP_MAX"),
                        rs.getInt("PP_MAX"), 
                        rs.getString("TIPO"),
                        rs.getObject("POTENCIA", Integer.class) != null ? rs.getInt("POTENCIA") : 0,
                        rs.getString("TIPO_MOV"),
                        rs.getString("ESTADO"),
                        rs.getObject("TURNOS", Integer.class) != null ? rs.getInt("TURNOS") : null,
                        rs.getString("MEJORA"),
                        rs.getObject("NUM", Integer.class) != null ? rs.getInt("NUM") : null
                    );
                }
            }
        }
        return null;
    }

    // Obtener ataques que tiene un Pokémon
    public static List<Ataque> obtenerAtaquesDePokemon(int idPokemon, Connection conexion) throws SQLException {
        List<Ataque> lista = new ArrayList<>();
        String query = "SELECT A.*, MP.PP_ACTUALES FROM MOVIMIENTOS A " +
                       "JOIN MOVIMIENTO_POKEMON MP ON A.ID_MOVIMIENTO = MP.ID_MOVIMIENTO " +
                       "WHERE MP.ID_POKEMON = ?";
        try (PreparedStatement st = conexion.prepareStatement(query)) {
            st.setInt(1, idPokemon);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Ataque ataque = new Ataque(
                        rs.getInt("ID_MOVIMIENTO"),
                        rs.getString("NOM_MOVIMIENTO"),
                        rs.getInt("NIVEL_APRENDIZAJE"),
                        rs.getInt("PP_MAX"),
                        rs.getInt("PP_ACTUALES"),
                        rs.getString("TIPO"),
                        rs.getObject("POTENCIA", Integer.class) != null ? rs.getInt("POTENCIA") : 0,
                        rs.getString("TIPO_MOV"),
                        rs.getString("ESTADO"),
                        rs.getObject("TURNOS", Integer.class) != null ? rs.getInt("TURNOS") : null,
                        rs.getString("MEJORA"),
                        rs.getObject("NUM", Integer.class) != null ? rs.getInt("NUM") : null
                    );
                    lista.add(ataque);
                }
            }
        }
        return lista;
    }

    // Obtener todos los ataques disponibles
    public static List<Ataque> obtenerTodosLosAtaques(Connection conexion) throws SQLException {
        List<Ataque> lista = new ArrayList<>();
        String query = "SELECT * FROM MOVIMIENTOS";
        try (PreparedStatement st = conexion.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Ataque ataque = new Ataque(
                    rs.getInt("ID_MOVIMIENTO"),
                    rs.getString("NOM_MOVIMIENTO"),
                    rs.getInt("NIVEL_APRENDIZAJE"),
                    rs.getInt("PP_MAX"),
                    rs.getInt("PP_MAX"),
                    rs.getString("TIPO"),
                    rs.getObject("POTENCIA", Integer.class) != null ? rs.getInt("POTENCIA") : 0,
                    rs.getString("TIPO_MOV"),
                    rs.getString("ESTADO"),
                    rs.getObject("TURNOS", Integer.class) != null ? rs.getInt("TURNOS") : null,
                    rs.getString("MEJORA"),
                    rs.getObject("NUM", Integer.class) != null ? rs.getInt("NUM") : null
                );
                lista.add(ataque);
            }
        }
        return lista;
    }

    // Asignar un ataque a un Pokémon con PP iniciales
    public static void insertarMovimientoParaPokemon(int idPokemon, int idMovimiento, int ppActuales, Connection conexion) throws SQLException {
        String query = "INSERT INTO MOVIMIENTO_POKEMON (ID_POKEMON, ID_MOVIMIENTO, PP_ACTUALES) VALUES (?, ?, ?)";
        try (PreparedStatement st = conexion.prepareStatement(query)) {
            st.setInt(1, idPokemon);
            st.setInt(2, idMovimiento);
            st.setInt(3, ppActuales);
            st.executeUpdate();
        }
    }

    // Actualizar los PP actuales de un ataque
    public static void actualizarPPMovimiento(int idPokemon, int idMovimiento, int nuevosPP, Connection conexion) throws SQLException {
        String query = "UPDATE MOVIMIENTO_POKEMON SET PP_ACTUALES = ? WHERE ID_POKEMON = ? AND ID_MOVIMIENTO = ?";
        try (PreparedStatement st = conexion.prepareStatement(query)) {
            st.setInt(1, nuevosPP);
            st.setInt(2, idPokemon);
            st.setInt(3, idMovimiento);
            st.executeUpdate();
        }
    }

    // Eliminar un ataque de un Pokémon
    public static void eliminarMovimientoDePokemon(int idPokemon, int idMovimiento, Connection conexion) throws SQLException {
        String query = "DELETE FROM MOVIMIENTO_POKEMON WHERE ID_POKEMON = ? AND ID_MOVIMIENTO = ?";
        try (PreparedStatement st = conexion.prepareStatement(query)) {
            st.setInt(1, idPokemon);
            st.setInt(2, idMovimiento);
            st.executeUpdate();
        }
    }

    // Restaurar todos los PP de los ataques de un Pokémon (por ejemplo, al curarse)
    public static void restaurarPPDePokemon(int idPokemon, Connection conexion) throws SQLException {
        String query = "UPDATE MOVIMIENTO_POKEMON MP " +
                       "JOIN MOVIMIENTOS M ON MP.ID_MOVIMIENTO = M.ID_MOVIMIENTO " +
                       "SET MP.PP_ACTUALES = M.PP_MAX " +
                       "WHERE MP.ID_POKEMON = ?";
        try (PreparedStatement st = conexion.prepareStatement(query)) {
            st.setInt(1, idPokemon);
            st.executeUpdate();
        }
    }
}
