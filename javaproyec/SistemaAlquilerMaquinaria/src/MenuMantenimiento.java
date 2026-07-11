import java.util.ArrayList;
import java.util.Scanner;

public class MenuMantenimiento {

    private MantenimientoServicio servicio;
    private ArrayList<Maquina> maquinas;
    private Scanner sc;

    public MenuMantenimiento(MantenimientoServicio servicio, ArrayList<Maquina> maquinas, Scanner sc) {
        this.servicio = servicio;
        this.maquinas = maquinas;
        this.sc = sc;
    }

    // Registrar un mantenimiento nuevo
    public void registrar() {
        if (maquinas.isEmpty()) {
            System.out.println("Debe registrar al menos una máquina primero.");
            return;
        }

        System.out.println("\n===== REGISTRAR MANTENIMIENTO =====");

        // Elegir máquina
        System.out.println("\n--- MÁQUINAS ---");
        for (int i = 0; i < maquinas.size(); i++) {
            Maquina m = maquinas.get(i);
            System.out.println(i + ". " + m.getMarca() + " " + m.getModelo()
                + " - Placa: " + m.getPlaca());
        }
       int idx = leerEntero("Elige el número de la máquina: ");
        Maquina maquina = maquinas.get(idx);

        // Tipo de mantenimiento
        System.out.println("\n--- TIPO DE MANTENIMIENTO ---");
        System.out.println("1. Preventivo");
        System.out.println("2. Correctivo");
        System.out.println("3. Cambio aceite y filtros");
        System.out.println("4. Cambio llantas");
        System.out.println("5. Compra repuestos");
        System.out.println("6. Reparación");
        int opTipo = leerEntero("Elige: ");
        Mantenimiento.TipoMantenimiento tipo = mapearTipo(opTipo);

        System.out.print("Descripción del trabajo: ");
        String descripcion = sc.nextLine();

        System.out.print("Nombre del técnico: ");
        String tecnico = sc.nextLine();

        System.out.print("Costo estimado: ");
        double costo = Double.parseDouble(sc.nextLine().replace(",", "."));

        // Cambiar estado de la máquina
        maquina.setEstado(EstadoMaquina.EN_MANTENIMIENTO);

        Mantenimiento nuevo = servicio.registrar(maquina.getPlaca(), tipo, descripcion, tecnico, costo);
        System.out.println("Mantenimiento registrado con ID: " + nuevo.getId());
        System.out.println("Máquina marcada como EN MANTENIMIENTO.");
        GeneradorMantenimientoPDF.generarReporte(nuevo, maquina);
    }

    // Ver mantenimientos de una máquina
    public void verPorMaquina() {
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
       int idx = leerEntero("Elige el número de la máquina: ");
        String placa = maquinas.get(idx).getPlaca();

        System.out.println("\n===== MANTENIMIENTOS DE " + placa + " =====");
        ArrayList<Mantenimiento> lista = servicio.listarPorMaquina(placa);

        if (lista.isEmpty()) {
            System.out.println("Esta máquina no tiene mantenimientos registrados.");
            return;
        }

        for (Mantenimiento m : lista) {
            m.mostrarInformacion();
            System.out.println("------");
        }

        System.out.printf("TOTAL INVERTIDO EN MANTENIMIENTOS: $%,.0f%n",
            servicio.totalPorMaquina(placa));
    }

    // Finalizar un mantenimiento activo
    public void finalizar() {
        ArrayList<Mantenimiento> activos = servicio.listarActivos();

        if (activos.isEmpty()) {
            System.out.println("No hay mantenimientos activos.");
            return;
        }

        System.out.println("\n===== MANTENIMIENTOS ACTIVOS =====");
        for (Mantenimiento m : activos) {
            System.out.println("ID: " + m.getId() + " | " + m.getPlacaMaquina()
                + " | " + m.getTipo() + " | Inicio: " + m.getFechaInicio());
        }

        int id = leerEntero("Ingresa el ID del mantenimiento a finalizar: ");

        System.out.print("Observaciones finales: ");
        String observaciones = sc.nextLine();

        // Buscar la máquina por placa y actualizar
        for (Mantenimiento m : activos) {
            if (m.getId() == id) {
                for (Maquina maq : maquinas) {
                    if (maq.getPlaca().equalsIgnoreCase(m.getPlacaMaquina())) {
                        servicio.finalizar(id, observaciones, maq);
                        maq.setEstado(EstadoMaquina.DISPONIBLE);
                        System.out.println("Máquina disponible nuevamente.");
                        return;
                    }
                }
            }
        }
        System.out.println("No se encontró el mantenimiento.");
    }

      private int leerEntero(String mensaje) {
        int valor;
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                valor = Integer.parseInt(entrada);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
            }
        }
        return valor;
    }


    private Mantenimiento.TipoMantenimiento mapearTipo(int opcion) {
        switch (opcion) {
            case 1: return Mantenimiento.TipoMantenimiento.PREVENTIVO;
            case 2: return Mantenimiento.TipoMantenimiento.CORRECTIVO;
            case 3: return Mantenimiento.TipoMantenimiento.CAMBIO_ACEITE_FILTROS;
            case 4: return Mantenimiento.TipoMantenimiento.CAMBIO_LLANTAS;
            case 5: return Mantenimiento.TipoMantenimiento.COMPRA_REPUESTOS;
            default: return Mantenimiento.TipoMantenimiento.REPARACION;
        }
    }
}