import java.util.ArrayList;
import java.util.Scanner;

public class MenuGastos {

    private GastoServicio gastoServicio;
    private ArrayList<Maquina> maquinas;
    private Scanner sc;

    public MenuGastos(GastoServicio gastoServicio, ArrayList<Maquina> maquinas, Scanner sc) {
        this.gastoServicio = gastoServicio;
        this.maquinas = maquinas;
        this.sc = sc;
    }

    public void registrar() {
        if (maquinas.isEmpty()) {
            System.out.println("Debe registrar al menos una máquina primero.");
            return;
        }

        System.out.println("\n===== REGISTRAR GASTO OPERATIVO =====");

        // Elegir máquina
        System.out.println("\n--- MÁQUINAS ---");
        for (int i = 0; i < maquinas.size(); i++) {
            Maquina m = maquinas.get(i);
            System.out.println(i + ". " + m.getMarca() + " " + m.getModelo()
                + " - Placa: " + m.getPlaca());
        }
        System.out.print("Elige el número de la máquina: ");
        int idx = Integer.parseInt(sc.nextLine());
        String placa = maquinas.get(idx).getPlaca();

        // Elegir tipo de gasto
        System.out.println("\n--- TIPO DE GASTO ---");
        System.out.println("1. Combustible");
        System.out.println("2. Lubricantes");
        System.out.println("3. Salario operador");
        System.out.println("4. Viáticos");
        System.out.println("5. Peajes");
        System.out.println("6. Transporte");
        System.out.println("7. Herramientas e insumos");
        System.out.println("8. Otro");
        System.out.print("Elige: ");
        int opTipo = Integer.parseInt(sc.nextLine());
        GastoOperativo.TipoGasto tipo = mapearTipo(opTipo);

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        System.out.print("Costo: ");
        double costo = Double.parseDouble(sc.nextLine().trim().replace(",", "."));

        gastoServicio.registrar(placa, tipo, descripcion, costo);
    }

    public void listarPorMaquina() {
        if (maquinas.isEmpty()) {
            System.out.println("No hay máquinas registradas.");
            return;
        }

        System.out.println("\n--- MÁQUINAS ---");
        for (int i = 0; i < maquinas.size(); i++) {
            Maquina m = maquinas.get(i);
            System.out.println(i + ". " + m.getMarca() + " " + m.getModelo()
                + " - Placa: " + m.getPlaca());
        }
        System.out.print("Elige el número de la máquina: ");
        int idx = Integer.parseInt(sc.nextLine());
        String placa = maquinas.get(idx).getPlaca();

        System.out.println("\n===== GASTOS DE " + placa + " =====");
        ArrayList<GastoOperativo> lista = gastoServicio.listarPorMaquina(placa);

        if (lista.isEmpty()) {
            System.out.println("Esta máquina no tiene gastos registrados.");
            return;
        }

        for (GastoOperativo g : lista) {
            g.mostrarInformacion();
            System.out.println("------");
        }

        System.out.printf("TOTAL GASTADO: $ %,.0f%n",
            gastoServicio.totalPorMaquina(placa));

        // Desglose por tipo
        System.out.println("\n--- DESGLOSE POR TIPO ---");
        for (GastoOperativo.TipoGasto t : GastoOperativo.TipoGasto.values()) {
            double total = gastoServicio.totalPorTipo(placa, t);
            if (total > 0) {
                System.out.printf("%-25s: $ %,.0f%n", t, total);
            }
        }
    }

    private GastoOperativo.TipoGasto mapearTipo(int opcion) {
        switch (opcion) {
            case 1: return GastoOperativo.TipoGasto.COMBUSTIBLE;
            case 2: return GastoOperativo.TipoGasto.LUBRICANTES;
            case 3: return GastoOperativo.TipoGasto.SALARIO_OPERADOR;
            case 4: return GastoOperativo.TipoGasto.VIATICOS;
            case 5: return GastoOperativo.TipoGasto.PEAJES;
            case 6: return GastoOperativo.TipoGasto.TRANSPORTE;
            case 7: return GastoOperativo.TipoGasto.HERRAMIENTAS_INSUMOS;
            default: return GastoOperativo.TipoGasto.OTRO;
        }
    }
    public void generarReportePDF() {
    if (maquinas.isEmpty()) {
        System.out.println("No hay máquinas registradas.");
        return;
    }

    System.out.println("\n--- MÁQUINAS ---");
    for (int i = 0; i < maquinas.size(); i++) {
        Maquina m = maquinas.get(i);
        System.out.println(i + ". " + m.getMarca() + " " + m.getModelo()
            + " - Placa: " + m.getPlaca());
    }
    System.out.print("Elige el número de la máquina: ");
    int idx = Integer.parseInt(sc.nextLine());
    Maquina maquina = maquinas.get(idx);

    ArrayList<GastoOperativo> gastos = gastoServicio.listarPorMaquina(maquina.getPlaca());

    if (gastos.isEmpty()) {
        System.out.println("Esta máquina no tiene gastos registrados.");
        return;
    }

    ReporteGastosPDF.generarReporte(maquina, gastos);
}
}