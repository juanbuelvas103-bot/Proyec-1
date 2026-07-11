import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cotizacion {

    private static int contadorCotizaciones = 1;

    private int numeroCotizacion;
    private Cliente cliente;
    private Maquina maquina;
    private int cantidad;
    private String tipoTarifa;       // dia / mes / año
    private String condicionesPago;  // ej: "50% anticipo, 50% al finalizar"
    private String observaciones;
    private String fechaEmision;
    private String fechaVencimiento; // cotización válida hasta
    private double subtotal;
    private double iva;
    private double total;

    public Cotizacion(Cliente cliente, Maquina maquina, int cantidad,
                      String tipoTarifa, double iva,
                      String condicionesPago, String observaciones,
                      int diasVigencia) {

        this.numeroCotizacion = contadorCotizaciones++;
        this.cliente = cliente;
        this.maquina = maquina;
        this.cantidad = cantidad;
        this.tipoTarifa = tipoTarifa;
        this.condicionesPago = condicionesPago;
        this.observaciones = observaciones;
        this.iva = iva;

        // Fecha automática del sistema
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaEmision = LocalDate.now().format(fmt);
        this.fechaVencimiento = LocalDate.now().plusDays(diasVigencia).format(fmt);

        // Calcular subtotal según tarifa
        double tarifa = 0;
        if (tipoTarifa.equalsIgnoreCase("dia")) {
            tarifa = maquina.getTarifaPorDia();
        } else if (tipoTarifa.equalsIgnoreCase("mes")) {
            tarifa = maquina.getTarifaPorMes();
        } else if (tipoTarifa.equalsIgnoreCase("año")) {
            tarifa = maquina.getTarifaPorAño();
        }

        this.subtotal = tarifa * cantidad;
        this.total = subtotal + (subtotal * iva);
    }

    // Getters
    public int getNumeroCotizacion()  { return numeroCotizacion; }
    public Cliente getCliente()       { return cliente; }
    public Maquina getMaquina()       { return maquina; }
    public int getCantidad()          { return cantidad; }
    public String getTipoTarifa()     { return tipoTarifa; }
    public String getCondicionesPago(){ return condicionesPago; }
    public String getObservaciones()  { return observaciones; }
    public String getFechaEmision()   { return fechaEmision; }
    public String getFechaVencimiento(){ return fechaVencimiento; }
    public double getSubtotal()       { return subtotal; }
    public double getIva()            { return iva; }
    public double getTotal()          { return total; }
}
