import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Mantenimiento {

    public enum TipoMantenimiento {
        PREVENTIVO,
        CORRECTIVO,
        CAMBIO_ACEITE_FILTROS,
        CAMBIO_LLANTAS,
        COMPRA_REPUESTOS,
        REPARACION
    }

    private static int contador = 1;

    private int id;
    private String placaMaquina;
    private TipoMantenimiento tipo;
    private String descripcion;
    private String tecnico;
    private double costo;
    private String fechaInicio;
    private String fechaFin;
    private boolean activo;
    private String observaciones;

    public Mantenimiento(String placaMaquina, TipoMantenimiento tipo,
                         String descripcion, String tecnico, double costo) {
        this.id = contador++;
        this.placaMaquina = placaMaquina;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.tecnico = tecnico;
        this.costo = costo;
        this.activo = true;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaInicio = LocalDate.now().format(fmt);
        this.fechaFin = null;
        this.observaciones = "";
    }

    public void finalizar(String observaciones) {
        if (!activo) {
            System.out.println("Este mantenimiento ya fue cerrado.");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaFin = LocalDate.now().format(fmt);
        this.observaciones = observaciones;
        this.activo = false;
    }

    public int getId()                    { return id; }
    public String getPlacaMaquina()       { return placaMaquina; }
    public TipoMantenimiento getTipo()    { return tipo; }
    public String getDescripcion()        { return descripcion; }
    public String getTecnico()            { return tecnico; }
    public double getCosto()              { return costo; }
    public String getFechaInicio()        { return fechaInicio; }
    public String getFechaFin()           { return fechaFin; }
    public boolean isActivo()             { return activo; }
    public String getObservaciones()      { return observaciones; }

    public void mostrarInformacion() {
        System.out.println("ID: "          + id);
        System.out.println("Tipo: "        + tipo);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Técnico: "     + tecnico);
        System.out.println("Costo: $"      + String.format("%,.0f", costo));
        System.out.println("Inicio: "      + fechaInicio);
        System.out.println("Fin: "         + (fechaFin != null ? fechaFin : "En curso"));
        System.out.println("Estado: "      + (activo ? "ACTIVO" : "FINALIZADO"));
        if (!observaciones.isEmpty()) {
            System.out.println("Observaciones: " + observaciones);
        }
    }
}