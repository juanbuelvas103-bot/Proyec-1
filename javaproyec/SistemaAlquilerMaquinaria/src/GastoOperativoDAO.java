import java.sql.*;
import java.util.ArrayList;

public class GastoOperativoDAO {

    public static void guardar(GastoOperativo g) {
        String sql = "INSERT INTO gastos_operativos (placa_maquina, tipo, descripcion, costo, fecha, mes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, g.getPlacaMaquina());
            ps.setString(2, g.getTipo().toString());
            ps.setString(3, g.getDescripcion());
            ps.setDouble(4, g.getCosto());
            ps.setString(5, g.getFecha());
            ps.setString(6, g.getMes());
            ps.executeUpdate();
            System.out.println("Gasto guardado en base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al guardar gasto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<GastoOperativo> cargarTodos() {
        ArrayList<GastoOperativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM gastos_operativos";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                GastoOperativo g = new GastoOperativo(
                    rs.getString("placa_maquina"),
                    GastoOperativo.TipoGasto.valueOf(rs.getString("tipo")),
                    rs.getString("descripcion"),
                    rs.getDouble("costo")
                );
                lista.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar gastos: " + e.getMessage());
        }
        return lista;
    }
}