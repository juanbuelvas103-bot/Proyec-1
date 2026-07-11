import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

    public static void guardar(Cliente c) {
        String sql = "INSERT INTO clientes (nombre, identificacion, telefono, direccion, nombre_obra, proyecto, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getIdentificacion());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getNombreDeLaObra());
            ps.setString(6, c.getProyecto());
            ps.setString(7, c.getEmail());
            ps.executeUpdate();
            System.out.println("Cliente guardado en base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al guardar cliente: " + e.getMessage());
             e.printStackTrace();
        }
    }

    public static ArrayList<Cliente> cargarTodos() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("nombre"),
                    rs.getString("identificacion"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("nombre_obra"),
                    rs.getString("proyecto"),
                    rs.getString("email")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar clientes: " + e.getMessage());
        }
        return lista;
    }
}