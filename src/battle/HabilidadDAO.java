package battle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabilidadDAO {
	private static final String DB_URL = "jdbc:sqlite:C:/SQLite_Prueba/BasesDatos/habilidades_unbeast.db";

	public static List<Habilidad> obtenerHabilidades() {
        List<Habilidad> habilidades = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM habilidades";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Habilidad h = new Habilidad(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo1"),
                    rs.getString("tipo2"),
                    rs.getInt("damage"),
                    rs.getInt("coste_mana")
                );
                habilidades.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return habilidades;
    }
	
	
}