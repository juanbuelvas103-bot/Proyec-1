public class Factura {

    // atributos de factura
    private Alquiler alquiler;
    private int numeroFactura;
    private String fecha;
    private boolean pagada;
    private String metodoPago;
    private double iva;
    private double subTotal;
    private double totalConIva;
    private String observaciones;


    // Constructor: crea una factura a partir de un alquiler ya realizado
public Factura(Alquiler alquiler, int numeroFactura, String fecha, String metodoPago, double iva, String observaciones) {
    this.alquiler = alquiler;
    this.numeroFactura = numeroFactura;
    this.fecha = fecha;
    this.metodoPago = metodoPago;
    this.iva = iva;
    this.observaciones = observaciones;
    this.pagada = false; // toda factura nueva empieza sin pagar

    this.subTotal = alquiler.calcularTotal(); // toma el total ya calculado en Alquiler
    this.totalConIva = subTotal + (subTotal * iva); // suma el iva al subtotal
}

public Alquiler getAlquiler(){
    return alquiler;
}
public int getNumeroFactura(){
    return numeroFactura;
}
public String getFecha(){
    return fecha;
}
public boolean isPagada(){
    return pagada;
}
public String getMetodoPago(){
    return metodoPago;
}
public double getIva(){
    return iva;
}
public double getSubTotal(){
    return subTotal;
}
public double getTotalConIva(){
    return totalConIva;
}
public String getObservaciones(){
    return observaciones;
}

public void marcarComoPagada() {
    this.pagada = true;
}

public void mostrarInformacion() {
    System.out.println("===== FACTURA N° " + numeroFactura + " =====");
    System.out.println("Fecha: " + fecha);
    alquiler.mostrarInformacion(); // reutiliza el método de Alquiler
    System.out.println("Subtotal: " + subTotal);
    System.out.println("IVA: " + iva);
    System.out.println("Total con IVA: " + totalConIva);
    System.out.println("Método de pago: " + metodoPago);
    System.out.println("Pagada: " + pagada);
    System.out.println("Observaciones: " + observaciones);
}

}








