import java.util.ArrayList;
import java.util.Scanner;

public class MenuCotizaciones {

    private ArrayList<Maquina> maquinas;
    private ArrayList<Cliente> clientes;
    private Scanner sc;

    public MenuCotizaciones(ArrayList<Maquina> maquinas, ArrayList<Cliente> clientes, Scanner sc) {
        this.maquinas = maquinas;
        this.clientes = clientes;
        this.sc = sc;
    }

    public void generar() {
        if (maquinas.isEmpty() || clientes.isEmpty()) {
            System.out.println("Debe registrar al menos una máquina y un cliente primero.");
            return;
        }

        System.out.println("\n===== GENERAR COTIZACIÓN =====");

        // Elegir cliente
        System.out.println("\n--- CLIENTES ---");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println(i + ". " + clientes.get(i).getNombre());
        }
        System.out.print("Elige el número del cliente: ");
        int idxCliente = Integer.parseInt(sc.nextLine());
        Cliente cliente = clientes.get(idxCliente);

        // Elegir máquina
        System.out.println("\n--- MÁQUINAS ---");
        for (int i = 0; i < maquinas.size(); i++) {
            Maquina m = maquinas.get(i);
            System.out.println(i + ". " + m.getMarca() + " " + m.getModelo()
                + " | Día: $" + m.getTarifaPorDia()
                + " | Mes: $" + m.getTarifaPorMes()
                + " | Año: $" + m.getTarifaPorAño());
        }
        System.out.print("Elige el número de la máquina: ");
        int idxMaquina = Integer.parseInt(sc.nextLine());
        Maquina maquina = maquinas.get(idxMaquina);

        // Datos del alquiler cotizado
        System.out.print("Tipo de tarifa (dia/mes/año): ");
        String tipoTarifa = sc.nextLine().trim().toLowerCase();

        System.out.print("Cantidad (" + tipoTarifa + "s): ");
        int cantidad = Integer.parseInt(sc.nextLine());

        System.out.print("IVA (0.19 para 19%, 0 si no aplica): ");
        double iva = Double.parseDouble(sc.nextLine().trim().replace(",", "."));

        System.out.print("Condiciones de pago (ej: 50% anticipo, 50% al finalizar): ");
        String condiciones = sc.nextLine();

        System.out.print("Observaciones (Enter para omitir): ");
        String observaciones = sc.nextLine();

        System.out.print("Días de vigencia de la cotización (ej: 15): ");
        int diasVigencia = Integer.parseInt(sc.nextLine());

        // Crear cotización y generar PDF
        Cotizacion cotizacion = new Cotizacion(
            cliente, maquina, cantidad, tipoTarifa,
            iva, condiciones, observaciones, diasVigencia
        );

        GeneradorCotizacionPDF.generarCotizacion(cotizacion);
    }
}
