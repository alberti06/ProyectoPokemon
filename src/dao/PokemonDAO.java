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
        int vidaActual = vitalidad;
        int ataque = 5 + rd.nextInt(6);
        int defensa = 5 + rd.nextInt(6);
        int atEspecial = 5 + rd.nextInt(6);
        int defEspecial = 5 + rd.nextInt(6);
        int velocidad = 5 + rd.nextInt(11);
        int nivel = 1;
        int fertilidad = 1 + rd.nextInt(5);
        String sexo = rd.nextBoolean() ? "M" : "F"; // Aseguramos que sea M o F
        if (!sexo.equals("M") && !sexo.equals("F")) sexo = "M"; // Seguridad extra
        String estado = "NORMAL";
        int equipo = obtenerSiguienteHuecoEquipo(idEntrenador);
        if (equipo == -1) equipo = 0; // caja

        String insertSQL = """
            INSERT INTO POKEMON (ID_POKEMON, FKID_ENTRENADOR, FK_NUM_POKEDEX, NOMBRE, VITALIDAD, VIDA_ACTUAL, ATAQUE,
                                 DEFENSA, AT_ESPECIAL, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO,
                                 ESTADO, EQUIPO)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";

        PreparedStatement stmt = conexion.prepareStatement(insertSQL);
        stmt.setInt(1, idPokemon);
        stmt.setInt(2, idEntrenador);
        stmt.setInt(3, numPokedex);
        stmt.setString(4, nombrePokemon);
        stmt.setInt(5, vitalidad);
        stmt.setInt(6, vidaActual);
        stmt.setInt(7, ataque);
        stmt.setInt(8, defensa);
        stmt.setInt(9, atEspecial);
        stmt.setInt(10, defEspecial);
        stmt.setInt(11, velocidad);
        stmt.setInt(12, nivel);
        stmt.setInt(13, fertilidad);
        stmt.setString(14, sexo);
        stmt.setString(15, estado);
        stmt.setInt(16, equipo);
        stmt.executeUpdate();

        PreparedStatement ps = conexion.prepareStatement("""
            INSERT INTO MOVIMIENTO_POKEMON (ID_POKEMON, ID_MOVIMIENTO, PP_RESTANTES)
            VALUES (?, 31, (SELECT PP_MAX FROM MOVIMIENTOS WHERE ID_MOVIMIENTO = 31))
        """);
        ps.setInt(1, idPokemon);
        ps.executeUpdate();

        return new Pokemon(idPokemon, idEntrenador, numPokedex, nombrePokemon, tipo1, tipo2, vitalidad, vidaActual,
                ataque, defensa, atEspecial, defEspecial, velocidad, nivel, fertilidad, sexo, estado, equipo,
                imgFrontal, imgTrasera, sonido, nivelEvolucion);
    }

    // ... (resto del código no modificado)

}
