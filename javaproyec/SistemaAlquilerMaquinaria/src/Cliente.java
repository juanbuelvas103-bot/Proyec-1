public class Cliente {

// atributos del cliente
    private String nombre;
    private String identificacion;
    private String telefono;
    private String direccion;
    private String nombreDeLaObra;
    private String proyecto;
    private String email;


// contructor cliente
public Cliente(String nombre, String identificacion, String telefono, String direccion, String nombreDeLaObra, String proyecto, String email){

this.nombre = nombre;
this.identificacion = identificacion;
this.telefono = telefono;
this.direccion = direccion;
this.nombreDeLaObra = nombreDeLaObra;
this.proyecto = proyecto;
this.email = email;

}

public String getNombre(){
    return nombre;
}
public String getIdentificacion(){
    return identificacion;
}
public String getTelefono(){
    return telefono;
}
public String getDireccion(){
    return direccion;
}
public String getNombreDeLaObra(){
    return nombreDeLaObra;
}
public String getProyecto(){
    return proyecto;
}
public String getEmail(){
    return email;
}

public void mostrarInformacion(){
    System.out.println("Nombre: " + nombre);
    System.out.println("Identificacion: " + identificacion);
    System.out.println("Telefono: " + telefono);
    System.out.println("Direccion: " + direccion);
    System.out.println("NombreDeLaObra: " + nombreDeLaObra);
    System.out.println("Proyecto: " + proyecto);
    System.out.println("Email: " + email);
    
}

}














