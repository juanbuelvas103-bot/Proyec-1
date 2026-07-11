import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Maquina {

    // atributos
    private String marca;
    private String modelo;
    private String placa;
    private double tarifaPorDia;
    private double tarifaPorMes;
    private double tarifaPorAño;
    private EstadoMaquina estado;
    private int diasEntreMantenimientos;  
    private int horasMaximas;             
    private int horasActuales;            
    private String fechaUltimoMantenimiento; 

// constructor
public Maquina(String marca, String modelo, String placa, double tarifaPorDia, double tarifaPorMes, double tarifaPorAño){

    this.marca = marca;
    this.modelo = modelo;
    this.placa = placa;
    this.tarifaPorDia = tarifaPorDia;
    this.tarifaPorMes = tarifaPorMes;
    this.tarifaPorAño = tarifaPorAño;
    this.estado = EstadoMaquina.DISPONIBLE; // Al momento de crear la maquina esta automaticamente sale disponible.
    this.diasEntreMantenimientos = 30;   // por defecto 30 días
    this.horasMaximas = 250;             // por defecto 250 horas
    this.horasActuales = 0;
    this.fechaUltimoMantenimiento = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

}
public String getMarca(){
    return  marca;
}
public String getModelo(){
    return modelo;
}
public String getPlaca(){
    return placa;
}
public double getTarifaPorDia(){
    return tarifaPorDia;
}
public double getTarifaPorMes(){
    return tarifaPorMes;
}
public double getTarifaPorAño(){
    return tarifaPorAño;
}

public void setEstado(EstadoMaquina estado) {
    this.estado = estado;
}

public EstadoMaquina getEstado() {
    return estado;
}


// para mostrar en consola
public void mostrarInformacion(){
    System.out.println("Marca: " + marca);
    System.out.println("Modelo: " + modelo);
    System.out.println("Placa: " + placa);
    System.out.printf("Tarifa por día: $%,.0f%n", tarifaPorDia);
    System.out.printf("Tarifa por mes: $%,.0f%n", tarifaPorMes);
    System.out.printf("Tarifa por año: $%,.0f%n", tarifaPorAño);
    System.out.println("Estado: " + estado);
    System.out.println("Último mantenimiento: " + fechaUltimoMantenimiento);
    System.out.println("Horas actuales: " + horasActuales + "/" + horasMaximas);

    

}
public int getDiasEntreMantenimientos() { return diasEntreMantenimientos; }
public int getHorasMaximas()            { return horasMaximas; }
public int getHorasActuales()           { return horasActuales; }
public String getFechaUltimoMantenimiento() { return fechaUltimoMantenimiento; }

public void setDiasEntreMantenimientos(int dias) { this.diasEntreMantenimientos = dias; }
public void setHorasMaximas(int horas)           { this.horasMaximas = horas; }
public void setHorasActuales(int horas)          { this.horasActuales = horas; }
public void agregarHoras(int horas)              { this.horasActuales += horas; }
public void setFechaUltimoMantenimiento(String fecha) { this.fechaUltimoMantenimiento = fecha; }

}

    















