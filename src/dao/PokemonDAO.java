package dao;

import model.Ataque;
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
	    String sexo = rd.nextBoolean() ? "M" : "F";
	    if (!sexo.equals("M") && !sexo.equals("F")) sexo = "M";
	    String estado = "NORMAL";
	    int equipo = obtenerSiguienteHuecoEquipo(idEntrenador);
	    if (equipo == -1) equipo = 0;

	    // ✅ INSERT con EXP añadido
	    String insertSQL = """
	        INSERT INTO POKEMON (ID_POKEMON, FKID_ENTRENADOR, FK_NUM_POKEDEX, NOMBRE, VITALIDAD, VIDA_ACTUAL, ATAQUE,
	                             DEFENSA, AT_ESPECIAL, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO,
	                             ESTADO, EQUIPO, EXP)
	        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	    """;

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
	    stmt.setInt(17, 0); // ✅ EXP inicial = 0
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


    public static int generarIdUnico(Connection conexion) throws SQLException {
        String query = "SELECT MAX(ID_POKEMON) AS MAX_ID FROM POKEMON";
        try (PreparedStatement stmt = conexion.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("MAX_ID") + 1;
            }
        }
        return 1;
    }

    public static int obtenerSiguienteHuecoEquipo(int idEntrenador) {
        try (Connection conexion = ConexionBD.conectar()) {
            String query = "SELECT EQUIPO FROM POKEMON WHERE FKID_ENTRENADOR = ? AND EQUIPO BETWEEN 1 AND 6";
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setInt(1, idEntrenador);
            ResultSet rs = stmt.executeQuery();

            boolean[] ocupado = new boolean[6];
            while (rs.next()) {
                int eq = rs.getInt("EQUIPO");
                if (eq >= 1 && eq <= 6) {
                    ocupado[eq - 1] = true;
                }
            }
            for (int i = 0; i < 6; i++) {
                if (!ocupado[i]) return i + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void guardarPokemon(Pokemon p) {
        try (Connection conexion = ConexionBD.conectar()) {
            int nuevoId = generarIdUnico(conexion);
            int equipo = obtenerSiguienteHuecoEquipo(p.getIdEntrenador());
            if (equipo == -1) equipo = 0;

          
            String insertSQL = """
                INSERT INTO POKEMON (ID_POKEMON, FKID_ENTRENADOR, FK_NUM_POKEDEX, NOMBRE, VITALIDAD, VIDA_ACTUAL, ATAQUE,
                                     DEFENSA, AT_ESPECIAL, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO,
                                     ESTADO, EQUIPO, EXP)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement stmt = conexion.prepareStatement(insertSQL);
            stmt.setInt(1, nuevoId);
            stmt.setInt(2, p.getIdEntrenador());
            stmt.setInt(3, p.getNumPokedex());
            stmt.setString(4, p.getNombre());
            stmt.setInt(5, p.getVitalidad());
            stmt.setInt(6, p.getVidaActual());
            stmt.setInt(7, p.getAtaque());
            stmt.setInt(8, p.getDefensa());
            stmt.setInt(9, p.getAtEspecial());
            stmt.setInt(10, p.getDefEspecial());
            stmt.setInt(11, p.getVelocidad());
            stmt.setInt(12, p.getNivel());
            stmt.setInt(13, p.getFertilidad());
            stmt.setString(14, p.getSexo());
            stmt.setString(15, p.getEstado());
            stmt.setInt(16, equipo);
            stmt.setInt(17, 0); // ✅ EXP inicial = 0

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int obtenerExp(int idPokemon) {
        int exp = 0;
        String query = "SELECT EXP FROM POKEMON WHERE ID_POKEMON = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idPokemon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exp = rs.getInt("EXP");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exp;
    }

    public static Pokemon generarPokemonSalvaje(Connection con, int nivel) throws SQLException {
        Random rand = new Random();

        PreparedStatement ps = con.prepareStatement("""
            SELECT NUM_POKEDEX, NOM_POKEMON, TIPO1, TIPO2, IMG_FRONTAL, IMG_TRASERA, SONIDO, NIVEL_EVOLUCION
            FROM POKEDEX
            ORDER BY RAND()
            LIMIT 1
        """);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) throw new SQLException("No se pudo generar un Pokémon salvaje.");

        int numPokedex = rs.getInt("NUM_POKEDEX");
        String nombre = rs.getString("NOM_POKEMON");
        String tipo1 = rs.getString("TIPO1");
        String tipo2 = rs.getString("TIPO2");
        String imgFrontal = rs.getString("IMG_FRONTAL");
        String imgTrasera = rs.getString("IMG_TRASERA");
        String sonido = rs.getString("SONIDO");
        int nivelEvolucion = rs.getInt("NIVEL_EVOLUCION");

        // Generar estadísticas escaladas
        int base = 50 + nivel * 2;
        int vitalidad = base + rand.nextInt(10);
        int ataque = base / 2 + rand.nextInt(5);
        int defensa = base / 2 + rand.nextInt(5);
        int atEspecial = base / 2 + rand.nextInt(5);
        int defEspecial = base / 2 + rand.nextInt(5);
        int velocidad = base / 2 + rand.nextInt(5);

        String sexo = rand.nextBoolean() ? "Macho" : "Hembra";
        int fertilidad = 3;

        Pokemon p = new Pokemon(
            -1, // ID no asignado
            -1, // ID del entrenador (rival ficticio)
            numPokedex,
            nombre,
            tipo1,
            tipo2,
            vitalidad,
            ataque,
            defensa,
            atEspecial,
            defEspecial,
            velocidad,
            nivel,
            fertilidad,
            sexo,
            null,
            0,
            imgFrontal,
            imgTrasera,
            sonido,
            nivelEvolucion
        );
        p.setVidaActual(vitalidad);
        return p;
    }

    public static void actualizarVida(Pokemon p) {
        String sql = "UPDATE POKEMON SET VIDA_ACTUAL = ? WHERE ID_POKEMON = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, p.getVidaActual());
            pst.setInt(2, p.getId());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Pokemon> obtenerEquipo(int idEntrenador) {
        List<Pokemon> equipo = new ArrayList<>();
        String sql = """
            SELECT p.*, d.TIPO1, d.TIPO2, d.IMG_FRONTAL, d.IMG_TRASERA, d.SONIDO, d.NIVEL_EVOLUCION
            FROM POKEMON p
            JOIN POKEDEX d ON p.FK_NUM_POKEDEX = d.NUM_POKEDEX
            WHERE p.FKID_ENTRENADOR = ? AND p.EQUIPO BETWEEN 1 AND 6
        """;
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

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
                    rs.getInt("VIDA_ACTUAL"),
                    rs.getInt("ATAQUE"),
                    rs.getInt("DEFENSA"),
                    rs.getInt("AT_ESPECIAL"),
                    rs.getInt("DEF_ESPECIAL"),
                    rs.getInt("VELOCIDAD"),
                    rs.getInt("NIVEL"),
                    rs.getInt("FERTILIDAD"),
                    rs.getString("SEXO"),
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

    public static List<Pokemon> obtenerCaja(int idEntrenador) {
        List<Pokemon> caja = new ArrayList<>();
        String sql = """
            SELECT p.*, d.TIPO1, d.TIPO2, d.IMG_FRONTAL, d.IMG_TRASERA, d.SONIDO, d.NIVEL_EVOLUCION
            FROM POKEMON p
            JOIN POKEDEX d ON p.FK_NUM_POKEDEX = d.NUM_POKEDEX
            WHERE p.FKID_ENTRENADOR = ? AND (p.EQUIPO = 0 OR p.EQUIPO IS NULL)
        """;
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

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
                    rs.getInt("VIDA_ACTUAL"),
                    rs.getInt("ATAQUE"),
                    rs.getInt("DEFENSA"),
                    rs.getInt("AT_ESPECIAL"),
                    rs.getInt("DEF_ESPECIAL"),
                    rs.getInt("VELOCIDAD"),
                    rs.getInt("NIVEL"),
                    rs.getInt("FERTILIDAD"),
                    rs.getString("SEXO"),
                    rs.getString("ESTADO"),
                    rs.getInt("EQUIPO"),
                    rs.getString("IMG_FRONTAL"),
                    rs.getString("IMG_TRASERA"),
                    rs.getString("SONIDO"),
                    rs.getObject("NIVEL_EVOLUCION", Integer.class)
                );
                caja.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caja;
    }

    public static void actualizarEquipo(int idPokemon, int nuevoEquipo) {
        String sql = "UPDATE POKEMON SET EQUIPO = ? WHERE ID_POKEMON = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, nuevoEquipo);
            stmt.setInt(2, idPokemon);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int obtenerUltimoIdPokemonInsertado(Connection conexion) throws SQLException {
        String query = "SELECT MAX(ID_POKEMON) AS ULTIMO_ID FROM POKEMON";
        try (PreparedStatement stmt = conexion.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ULTIMO_ID");
            } else {
                throw new SQLException("No se pudo obtener el último ID de Pokémon.");
            }
        }
    }
    
    public static void insertarPokemon(int idEntrenador, Pokemon p) {
        String sql = """
            INSERT INTO POKEMON (ID_POKEMON, FKID_ENTRENADOR, FK_NUM_POKEDEX, NOMBRE, VITALIDAD, VIDA_ACTUAL, ATAQUE,
                                 DEFENSA, AT_ESPECIAL, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO,
                                 ESTADO, EQUIPO, IMG_FRONTAL, IMG_TRASERA, SONIDO, NIVEL_EVOLUCION, EXP)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            int nuevoId = generarIdUnico(conexion);

            stmt.setInt(1, nuevoId);
            stmt.setInt(2, idEntrenador);
            stmt.setInt(3, p.getNumPokedex());
            stmt.setString(4, p.getNombre());
            stmt.setInt(5, p.getVitalidad());
            stmt.setInt(6, p.getVidaActual());
            stmt.setInt(7, p.getAtaque());
            stmt.setInt(8, p.getDefensa());
            stmt.setInt(9, p.getAtEspecial());
            stmt.setInt(10, p.getDefEspecial());
            stmt.setInt(11, p.getVelocidad());
            stmt.setInt(12, p.getNivel());
            stmt.setInt(13, p.getFertilidad());
            stmt.setString(14, p.getSexo());
            stmt.setString(15, p.getEstado());
            stmt.setInt(16, p.getEquipo());
            stmt.setString(17, p.getImgFrontal());
            stmt.setString(18, p.getImgTrasera());
            stmt.setString(19, p.getSonido());

            if (p.getNivelEvolucion() != null) {
                stmt.setInt(20, p.getNivelEvolucion());
            } else {
                stmt.setNull(20, java.sql.Types.INTEGER);
            }

            stmt.setInt(21, 0); // ✅ EXP inicial en 0

            stmt.executeUpdate();

            // Insertar ataque básico: Placaje (ID 31)
            PreparedStatement ps = conexion.prepareStatement("""
                INSERT INTO MOVIMIENTO_POKEMON (ID_POKEMON, ID_MOVIMIENTO, PP_RESTANTES)
                VALUES (?, 31, (SELECT PP_MAX FROM MOVIMIENTOS WHERE ID_MOVIMIENTO = 31))
            """);
            ps.setInt(1, nuevoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
    public static void actualizarFertilidad(int idPokemon, int nuevaFertilidad) {
        String sql = "UPDATE POKEMON SET FERTILIDAD = ? WHERE ID_POKEMON = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, nuevaFertilidad);
            stmt.setInt(2, idPokemon);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Ataque> obtenerAtaques(int idPokemon) {
        List<Ataque> lista = new ArrayList<>();
        try (Connection con = ConexionBD.conectar()) {
            PreparedStatement ps = con.prepareStatement("""
                SELECT m.ID_MOVIMIENTO, m.NOM_MOVIMIENTO, m.NIVEL_APRENDIZAJE, m.PP_MAX,
                       mp.PP_ACTUALES, m.TIPO, m.POTENCIA, m.TIPO_MOV, m.ESTADO,
                       m.TURNOS, m.MEJORA, m.NUM
                FROM MOVIMIENTO_POKEMON mp
                JOIN MOVIMIENTOS m ON mp.ID_MOVIMIENTO = m.ID_MOVIMIENTO
                WHERE mp.ID_POKEMON = ?
            """);
            ps.setInt(1, idPokemon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ataque atk = new Ataque(
                    rs.getInt("ID_MOVIMIENTO"),
                    rs.getString("NOM_MOVIMIENTO"),
                    rs.getInt("NIVEL_APRENDIZAJE"),
                    rs.getInt("PP_MAX"),
                    rs.getInt("PP_ACTUALES"),
                    rs.getString("TIPO"),
                    rs.getObject("POTENCIA") != null ? rs.getInt("POTENCIA") : null,
                    rs.getString("TIPO_MOV"),
                    rs.getString("ESTADO"),
                    rs.getObject("TURNOS") != null ? rs.getInt("TURNOS") : null,
                    rs.getString("MEJORA"),
                    rs.getObject("NUM") != null ? rs.getInt("NUM") : null
                );
                lista.add(atk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


}
