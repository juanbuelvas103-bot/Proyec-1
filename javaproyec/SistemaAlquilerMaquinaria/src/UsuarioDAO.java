import java.sql.*;


public class UsuarioDAO {

    // Verificar login y devolver el rol
    public static String login(String username, String password) {
        String sql = "SELECT rol FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("rol");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
        }
        return null;
    }

    // Registrar un nuevo usuario
    public static boolean registrar(String username, String password, String rol) {
        String sql = "INSERT INTO usuarios (username, password, rol) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, rol);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // Verificar si un usuario ya existe
    public static boolean existeUsuario(String username) {
        String sql = "SELECT id FROM usuarios WHERE username = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            return false;
        }
    }

    // Cambiar contraseña
    public static boolean cambiarPassword(String username, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE username = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevaPassword);
            ps.setString(2, username);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }

    // Listar todos los usuarios
    public static void listarUsuarios() {
        String sql = "SELECT id, username, rol FROM usuarios";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n===== USUARIOS REGISTRADOS =====");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id")
                    + " | Usuario: " + rs.getString("username")
                    + " | Rol: " + rs.getString("rol"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
    }
}
