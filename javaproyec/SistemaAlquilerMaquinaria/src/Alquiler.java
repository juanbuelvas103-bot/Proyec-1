public class Alquiler {

    private Cliente  cliente;
    private Maquina maquina;
    private int cantidad;
    private String tipoTarifa; // Dia, Mes, Año.

    // Constructor
    public Alquiler( Cliente cliente, Maquina maquina, int cantidad, String tipoTarifa){
this.cliente = cliente;
this.maquina = maquina;
this.cantidad = cantidad;
this.tipoTarifa = tipoTarifa;

}

// Calcula el total a pagar según el tipo de tarifa (dia, mes, año)
public double calcularTotal() {
    double total = 0;

    if (tipoTarifa.equals("dia")) {
        total = cantidad * maquina.getTarifaPorDia();
    } else if (tipoTarifa.equals("mes")) {
        total = cantidad * maquina.getTarifaPorMes();
    } else if (tipoTarifa.equals("año")) {
        total = cantidad * maquina.getTarifaPorAño();
    }

    return total;
}

public void mostrarInformacion(){
    System.out.println("Cliente: " + cliente.getNombre());
    System.out.println("Maquina: " + maquina.getMarca() +" " + maquina.getModelo());
System.out.println("Cantidad: " + cantidad + " (" + tipoTarifa + ")");
System.out.println("Total a pagar: " + calcularTotal());

}
public void generarFactura() {
    System.out.println("\n========================================");
    System.out.println("           FACTURA DE ALQUILER          ");
    System.out.println("========================================");

    // Datos del cliente
    System.out.println("\n--- DATOS DEL CLIENTE ---");
    System.out.println("Nombre:           " + cliente.getNombre());
    System.out.println("Identificación:   " + cliente.getIdentificacion());
    System.out.println("Teléfono:         " + cliente.getTelefono());
    System.out.println("Dirección:        " + cliente.getDireccion());
    System.out.println("Obra:             " + cliente.getNombreDeLaObra());
    System.out.println("Proyecto:         " + cliente.getProyecto());
    System.out.println("Email:            " + cliente.getEmail());

    // Datos de la máquina
    System.out.println("\n--- DATOS DE LA MÁQUINA ---");
    System.out.println("Marca:            " + maquina.getMarca());
    System.out.println("Modelo:           " + maquina.getModelo());
    System.out.println("Placa:            " + maquina.getPlaca());

    // Detalle del alquiler
    System.out.println("\n--- DETALLE DEL ALQUILER ---");
    System.out.println("Tipo de tarifa:   " + tipoTarifa);
    System.out.println("Cantidad:         " + cantidad + " " + tipoTarifa + "(s)");

    double tarifa = 0;
    if (tipoTarifa.equals("dia")) {
        tarifa = maquina.getTarifaPorDia();
    } else if (tipoTarifa.equals("mes")) {
        tarifa = maquina.getTarifaPorMes();
    } else if (tipoTarifa.equals("año")) {
        tarifa = maquina.getTarifaPorAño();
    }

    System.out.println("Tarifa unitaria:  $" + tarifa);
    System.out.println("----------------------------------------");
    System.out.println("TOTAL A PAGAR:    $" + calcularTotal());
    System.out.println("========================================\n");
}
public Cliente getCliente() {  
    return cliente;
}

public Maquina getMaquina() {  
    return maquina;
}
public String getTipoTarifa() {
    return tipoTarifa;
}

public int getCantidad() {
    return cantidad;
}

}  









