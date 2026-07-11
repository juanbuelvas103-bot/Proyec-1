import java.sql.*;
import java.util.ArrayList;

public class MaquinaDAO {

    // Guardar una máquina nueva en la base de datos
    public static void guardar(Maquina m) {
        String sql = "INSERT INTO maquinas (marca, modelo, placa, tarifa_por_dia, tarifa_por_mes, tarifa_por_año, estado, horas_actuales, horas_maximas, dias_entre_mantenimientos, fecha_ultimo_mantenimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getMarca());
            ps.setString(2, m.getModelo());
            ps.setString(3, m.getPlaca());
            ps.setDouble(4, m.getTarifaPorDia());
            ps.setDouble(5, m.getTarifaPorMes());
            ps.setDouble(6, m.getTarifaPorAño());
            ps.setString(7, m.getEstado().toString());
            ps.setInt(8, m.getHorasActuales());
            ps.setInt(9, m.getHorasMaximas());
            ps.setInt(10, m.getDiasEntreMantenimientos());
            ps.setString(11, m.getFechaUltimoMantenimiento());
            ps.executeUpdate();
            System.out.println("Máquina guardada en base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al guardar máquina: " + e.getMessage());
        }
    }

    // Cargar todas las máquinas desde la base de datos
    public static ArrayList<Maquina> cargarTodas() {
        ArrayList<Maquina> lista = new ArrayList<>();
        String sql = "SELECT * FROM maquinas";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Maquina m = new Maquina(
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("placa"),
                    rs.getDouble("tarifa_por_dia"),
                    rs.getDouble("tarifa_por_mes"),
                    rs.getDouble("tarifa_por_año")
                );
                m.setEstado(EstadoMaquina.valueOf(rs.getString("estado")));
                m.setHorasActuales(rs.getInt("horas_actuales"));
                m.setHorasMaximas(rs.getInt("horas_maximas"));
                m.setDiasEntreMantenimientos(rs.getInt("dias_entre_mantenimientos"));
                m.setFechaUltimoMantenimiento(rs.getString("fecha_ultimo_mantenimiento"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar máquinas: " + e.getMessage());
        }
        return lista;
    }

    // Actualizar estado de una máquina
    public static void actualizarEstado(String placa, EstadoMaquina estado) {
        String sql = "UPDATE maquinas SET estado = ? WHERE placa = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado.toString());
            ps.setString(2, placa);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
        }
    }
}