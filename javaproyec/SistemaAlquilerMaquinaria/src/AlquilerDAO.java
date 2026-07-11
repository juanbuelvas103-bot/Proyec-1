import java.sql.*;
import java.util.ArrayList;

public class AlquilerDAO {

    public static void guardar(Alquiler a) {
        // Primero obtenemos el id del cliente y la máquina
        String sqlCliente = "SELECT id FROM clientes WHERE identificacion = ?";
        String sqlMaquina = "SELECT id FROM maquinas WHERE placa = ?";
        String sql = "INSERT INTO alquileres (cliente_id, maquina_id, cantidad, tipo_tarifa) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtenerConexion()) {

            // Obtener id del cliente
            int clienteId = -1;
            try (PreparedStatement ps = con.prepareStatement(sqlCliente)) {
                ps.setString(1, a.getCliente().getIdentificacion());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) clienteId = rs.getInt("id");
            }

            // Obtener id de la máquina
            int maquinaId = -1;
            try (PreparedStatement ps = con.prepareStatement(sqlMaquina)) {
                ps.setString(1, a.getMaquina().getPlaca());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) maquinaId = rs.getInt("id");
            }

            if (clienteId == -1 || maquinaId == -1) {
                System.out.println("Error: no se encontró el cliente o la máquina en la base de datos.");
                return;
            }

            // Guardar el alquiler
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, clienteId);
                ps.setInt(2, maquinaId);
                ps.setInt(3, a.getCantidad());
                ps.setString(4, a.getTipoTarifa());
                ps.executeUpdate();
                System.out.println("Alquiler guardado en base de datos.");
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar alquiler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Alquiler> cargarTodos(ArrayList<Cliente> clientes, ArrayList<Maquina> maquinas) {
        ArrayList<Alquiler> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.identificacion, m.placa FROM alquileres a " +
                     "JOIN clientes c ON a.cliente_id = c.id " +
                     "JOIN maquinas m ON a.maquina_id = m.id";

        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Buscar cliente y máquina en las listas
                Cliente cliente = null;
                for (Cliente c : clientes) {
                    if (c.getIdentificacion().equals(rs.getString("identificacion"))) {
                        cliente = c;
                        break;
                    }
                }

                Maquina maquina = null;
                for (Maquina m : maquinas) {
                    if (m.getPlaca().equals(rs.getString("placa"))) {
                        maquina = m;
                        break;
                    }
                }

                if (cliente != null && maquina != null) {
                    Alquiler a = new Alquiler(cliente, maquina, rs.getInt("cantidad"), rs.getString("tipo_tarifa"));
                    lista.add(a);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar alquileres: " + e.getMessage());
        }
        return lista;
    }
}