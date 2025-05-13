package dao;

import model.Pokemon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonDAO {

    public static Pokemon generarPokemonPrincipalEspecifico(int idEntrenador, String nombrePokemon, Connection conexion) throws SQLException {
        String queryPokedex = "SELECT NUM_POKEDEX, TIPO1, TIPO2, IMG_FRONTAL, IMG_TRASERA, SONIDO, NIVEL_EVOLUCION FROM POKEDEX WHERE NOM_POKEMON = ?";
        PreparedStatement stPokedex = conexion.prepareStatement(queryPokedex);
        stPokedex.setString(1, nombrePokemon);
        ResultSet rsPokedex = stPokedex.executeQuery();

        if (!rsPokedex.next()) {
            throw new SQLException("Pokémon no encontrado en la POKEDEX: " + nombrePokemon);
        }

        int numPokedex = rsPokedex.getInt("NUM_POKEDEX");
        String tipo1 = rsPokedex.getString("TIPO1");
        String tipo2 = rsPokedex.getString("TIPO2");
        String imgFrontal = rsPokedex.getString("IMG_FRONTAL");
        String imgTrasera = rsPokedex.getString("IMG_TRASERA");
        String sonido = rsPokedex.getString("SONIDO");
        Integer nivelEvolucion = rsPokedex.getObject("NIVEL_EVOLUCION", Integer.class);

        Random rd = new Random();
        int idPokemon = generarIdUnico(conexion);
        int vitalidad = 15 + rd.nextInt(16);
        int ataque = 5 + rd.nextInt(6);
        int defensa = 5 + rd.nextInt(6);
        int atEspecial = 5 + rd.nextInt(6);
        int defEspecial = 5 + rd.nextInt(6);
        int velocidad = 5 + rd.nextInt(11);
        int nivel = 1;
        int fertilidad = 1 + rd.nextInt(5);
        String sexo = rd.nextBoolean() ? "M" : "F"; // Corrigido: usar String
        String estado = "NORMAL";
        int equipo = 1;

        String insertSQL = "INSERT INTO POKEMON (ID_POKEMON, FKID_ENTRENADOR, FK_NUM_POKEDEX, NOMBRE, VITALIDAD, ATAQUE, DEFENSA, AT_ESPECIAL, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO, ESTADO, EQUIPO) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conexion.prepareStatement(insertSQL);
        stmt.setInt(1, idPokemon);
        stmt.setInt(2, idEntrenador);
        stmt.setInt(3, numPokedex);
        stmt.setString(4, nombrePokemon);
        stmt.setInt(5, vitalidad);
        stmt.setInt(6, ataque);
        stmt.setInt(7, defensa);
        stmt.setInt(8, atEspecial);
        stmt.setInt(9, defEspecial);
        stmt.setInt(10, velocidad);
        stmt.setInt(11, nivel);
        stmt.setInt(12, fertilidad);
        stmt.setString(13, sexo); // Corrección aquí
        stmt.setString(14, estado);
        stmt.setInt(15, equipo);

        stmt.executeUpdate();

        return new Pokemon(
            idPokemon, idEntrenador, numPokedex, nombrePokemon, tipo1, tipo2,
            vitalidad, ataque, defensa, atEspecial, defEspecial, velocidad, nivel,
            fertilidad, sexo.charAt(0), estado, equipo, imgFrontal, imgTrasera, sonido, nivelEvolucion
        );
    }

    private static int generarIdUnico(Connection conexion) throws SQLException {
        String query = "SELECT MAX(ID_POKEMON) AS MAX_ID FROM POKEMON";
        PreparedStatement statement = conexion.prepareStatement(query);
        ResultSet resultado = statement.executeQuery();
        return resultado.next() ? resultado.getInt("MAX_ID") + 1 : 1;
    }

    public static void insertarPokemon(int idEntrenador, Pokemon p) {
        try (Connection conexion = ConexionBD.conectar()) {
            int nuevoId = generarIdUnico(conexion);

            String insertSQL = """
                INSERT INTO POKEMON (ID_POKEMON, FKID_ENTRENADOR, FK_NUM_POKEDEX, NOMBRE, VITALIDAD, ATAQUE,
                                     DEFENSA, AT_ESPECIAL, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO,
                                     ESTADO, EQUIPO)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";

            PreparedStatement stmt = conexion.prepareStatement(insertSQL);
            stmt.setInt(1, nuevoId);
            stmt.setInt(2, idEntrenador);
            stmt.setInt(3, p.getNumPokedex());
            stmt.setString(4, p.getNombre());
            stmt.setInt(5, p.getVitalidad());
            stmt.setInt(6, p.getAtaque());
            stmt.setInt(7, p.getDefensa());
            stmt.setInt(8, p.getAtEspecial());
            stmt.setInt(9, p.getDefEspecial());
            stmt.setInt(10, p.getVelocidad());
            stmt.setInt(11, p.getNivel());
            stmt.setInt(12, p.getFertilidad());
            stmt.setString(13, String.valueOf(p.getSexo()));
            stmt.setString(14, p.getEstado());
            stmt.setInt(15, p.getEquipo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Pokemon> obtenerEquipo(int idEntrenador) {
        List<Pokemon> equipo = new ArrayList<>();
        try (Connection con = ConexionBD.conectar()) {
            String sql = """
                SELECT p.*, d.TIPO1, d.TIPO2, d.IMG_FRONTAL, d.IMG_TRASERA, d.SONIDO, d.NIVEL_EVOLUCION
                FROM pokemon p
                JOIN pokedex d ON p.FK_NUM_POKEDEX = d.NUM_POKEDEX
                WHERE p.FKID_ENTRENADOR = ? AND p.EQUIPO > 0 AND p.EQUIPO <= 6
            """;

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idEntrenador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pokemon p = new Pokemon(
                    rs.getInt("ID_POKEMON"),
                    rs.getInt("FKID_ENTRENADOR"),
                    rs.getInt("FK_NUM_POKEDEX"),
                    rs.getString("NOMBRE"),
                    rs.getString("TIPO1"),
                    rs.getString("TIPO2"),
                    rs.getInt("VITALIDAD"),
                    rs.getInt("ATAQUE"),
                    rs.getInt("DEFENSA"),
                    rs.getInt("AT_ESPECIAL"),
                    rs.getInt("DEF_ESPECIAL"),
                    rs.getInt("VELOCIDAD"),
                    rs.getInt("NIVEL"),
                    rs.getInt("FERTILIDAD"),
                    rs.getString("SEXO").charAt(0),
                    rs.getString("ESTADO"),
                    rs.getInt("EQUIPO"),
                    rs.getString("IMG_FRONTAL"),
                    rs.getString("IMG_TRASERA"),
                    rs.getString("SONIDO"),
                    rs.getObject("NIVEL_EVOLUCION", Integer.class)
                );
                equipo.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipo;
    }
}
