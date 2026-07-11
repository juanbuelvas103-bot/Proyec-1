import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GastoOperativo {

    public enum TipoGasto {
        COMBUSTIBLE,
        LUBRICANTES,
        SALARIO_OPERADOR,
        VIATICOS,
        PEAJES,
        TRANSPORTE,
        HERRAMIENTAS_INSUMOS,
        OTRO
    }

    private static int contador = 1;

    private int id;
    private String placaMaquina;
    private TipoGasto tipo;
    private String descripcion;
    private double costo;
    private String fecha;
    private String mes;

    public GastoOperativo(String placaMaquina, TipoGasto tipo,
                          String descripcion, double costo) {
        this.id = contador++;
        this.placaMaquina = placaMaquina;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.costo = costo;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fecha = LocalDate.now().format(fmt);
        this.mes = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public int getId()              { return id; }
    public String getPlacaMaquina() { return placaMaquina; }
    public TipoGasto getTipo()      { return tipo; }
    public String getDescripcion()  { return descripcion; }
    public double getCosto()        { return costo; }
    public String getFecha()        { return fecha; }
    public String getMes()          { return mes; }

    public void mostrarInformacion() {
        System.out.println("ID: "          + id);
        System.out.println("Tipo: "        + tipo);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Costo: $"      + String.format("%,.0f", costo));
        System.out.println("Fecha: "       + fecha);
    }
}