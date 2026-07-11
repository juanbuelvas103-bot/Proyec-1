import java.sql.*;
import java.util.ArrayList;

public class MantenimientoDAO {

    public static void guardar(Mantenimiento m) {
        String sql = "INSERT INTO mantenimientos (placa_maquina, tipo, descripcion, tecnico, costo, fecha_inicio, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getPlacaMaquina());
            ps.setString(2, m.getTipo().toString());
            ps.setString(3, m.getDescripcion());
            ps.setString(4, m.getTecnico());
            ps.setDouble(5, m.getCosto());
            ps.setString(6, m.getFechaInicio());
            ps.setBoolean(7, m.isActivo());
            ps.executeUpdate();
            System.out.println("Mantenimiento guardado en base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al guardar mantenimiento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void finalizar(int id, String observaciones, String fechaFin) {
        String sql = "UPDATE mantenimientos SET activo = FALSE, fecha_fin = ?, observaciones = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaFin);
            ps.setString(2, observaciones);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al finalizar mantenimiento: " + e.getMessage());
        }
    }

    public static ArrayList<Mantenimiento> cargarTodos() {
        ArrayList<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM mantenimientos";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Mantenimiento m = new Mantenimiento(
                    rs.getString("placa_maquina"),
                    Mantenimiento.TipoMantenimiento.valueOf(rs.getString("tipo")),
                    rs.getString("descripcion"),
                    rs.getString("tecnico"),
                    rs.getDouble("costo")
                );
                if (!rs.getBoolean("activo")) {
                    m.finalizar(rs.getString("observaciones"));
                }
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar mantenimientos: " + e.getMessage());
        }
        return lista;
    }
}
