import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection con = ConexionDB.obtenerConexion();
        if (con != null) {
            System.out.println("Conexion exitosa con la base de datos.");
        } else {
            System.out.println("No se pudo conectar.");
        }
    }
}