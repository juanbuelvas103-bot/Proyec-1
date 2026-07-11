import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/SistemaAlquilerMaquinaria";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "juanchopico30"; 

    public static Connection obtenerConexion() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            return conexion;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }
}
