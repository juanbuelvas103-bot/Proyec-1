import java.util.Scanner;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in, "Cp850");

        // ===== LOGIN =====
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE ALQUILER DE MAQUINARIA  ║");
        System.out.println("╚══════════════════════════════════════╝");

        String rolUsuario = null;
        int intentos = 0;

        while (rolUsuario == null && intentos < 3) {
            System.out.print("Usuario: ");
            String username = sc.nextLine().trim();
            System.out.print("Contraseña: ");
            String password = sc.nextLine().trim();

            rolUsuario = UsuarioDAO.login(username, password);

            if (rolUsuario == null) {
                intentos++;
                System.out.println("Credenciales incorrectas. Intento " + intentos + "/3");
            }
        }

        if (rolUsuario == null) {
            System.out.println("Acceso denegado. Demasiados intentos fallidos.");
            sc.close();
            return;
        }

        System.out.println("Bienvenido. Rol: " + rolUsuario);

    
       ArrayList<Maquina> maquinas = MaquinaDAO.cargarTodas();
        ArrayList<Cliente> clientes = ClienteDAO.cargarTodos();
        ArrayList<Alquiler> alquileres = AlquilerDAO.cargarTodos(clientes, maquinas);
        MenuCotizaciones menuCotizaciones = new MenuCotizaciones(maquinas, clientes, sc);
        GastoServicio gastoServicio = new GastoServicio();
        MenuGastos menuGastos = new MenuGastos(gastoServicio, maquinas, sc);
        MantenimientoServicio mantenimientoServicio = new MantenimientoServicio();
        MenuMantenimiento menuMantenimiento = new MenuMantenimiento(mantenimientoServicio, maquinas, sc);

        int opcion;

        do {
            AlertaMantenimiento.verificarAlertas(maquinas);
            System.out.println("\n===== SISTEMA DE ALQUILER DE MAQUINARIA =====");
            System.out.println("1. Registrar máquina");
            System.out.println("2. Ver máquinas registradas");
            System.out.println("3. Registrar cliente");
            System.out.println("4. Ver clientes registrados");
            System.out.println("5. Crear alquiler");
            System.out.println("6. Ver historial de alquileres");
            System.out.println("7. Devolver maquina");
            System.out.println("8. Buscar cliente o Maquina");
            System.out.println("9. Generar factura PDF");
            System.out.println("10. Generar cotización");
            System.out.println("11. Registrar gasto operativo");
            System.out.println("12. Ver gastos de una máquina");
            System.out.println("13. Reporte de gastos en PDF");
            System.out.println("14. Registrar mantenimiento");
            System.out.println("15. Ver mantenimientos de una máquina");
            System.out.println("16. Finalizar mantenimiento");
            System.out.println("17. Reporte general del negocio");
            System.out.println("18. Salir");
            System.out.print("Elige una opción: ");
            try {
    opcion = Integer.parseInt(sc.nextLine().trim());
} catch (NumberFormatException e) {
    opcion = 0;
}

            switch (opcion) {

                case 1:
                     
    System.out.print("Marca: ");
    String marca = sc.nextLine().trim();
    System.out.print("Modelo: ");
    String modelo = sc.nextLine().trim();
    System.out.print("Placa: ");
    String placa = sc.nextLine().trim().toUpperCase();

    // Validar campos vacíos
    if (marca.isEmpty() || modelo.isEmpty() || placa.isEmpty()) {
        System.out.println("Error: todos los campos son obligatorios.");
        break;
    }

    // Validar placa duplicada
    boolean placaExiste = false;
    for (Maquina m : maquinas) {
        if (m.getPlaca().equalsIgnoreCase(placa)) {
            placaExiste = true;
            break;
        }
    }
    if (placaExiste) {
        System.out.println("Error: ya existe una máquina con esa placa.");
        break;
    }

    System.out.print("Tarifa por día: ");
    double tarifaPorDia = Double.parseDouble(sc.nextLine().replace(",", "."));
    System.out.print("Tarifa por mes: ");
    double tarifaPorMes = Double.parseDouble(sc.nextLine().replace(",", "."));
    System.out.print("Tarifa por año: ");
    double tarifaPorAño = Double.parseDouble(sc.nextLine().replace(",", "."));
   Maquina nuevaMaquina = new Maquina(marca, modelo, placa, tarifaPorDia, tarifaPorMes, tarifaPorAño);
   maquinas.add(nuevaMaquina);
   MaquinaDAO.guardar(nuevaMaquina);
   System.out.println("Maquina registrada con éxito.");
    break;

                case 2:
                    System.out.println("\n===== MÁQUINAS REGISTRADAS =====");
                    if (maquinas.isEmpty()) {
                        System.out.println("No hay máquinas registradas todavía.");
                    } else {
                        for (Maquina m : maquinas) {
                            m.mostrarInformacion();
                            System.out.println("------");
                        }
                    }
                    break;

                case 3:
       
    System.out.print("Nombre: ");
    String nombre = sc.nextLine().trim();

    System.out.print("Identificación: ");
    String identificacion = sc.nextLine().trim();

    // Validar inmediatamente después de identificación
    if (nombre.isEmpty() || identificacion.isEmpty()) {
        System.out.println("Error: nombre e identificación son obligatorios.");
        break;
    }

    boolean idExiste = false;
    for (Cliente c : clientes) {
        if (c.getIdentificacion().equals(identificacion)) {
            idExiste = true;
            break;
        }
    }
    if (idExiste) {
        System.out.println("Error: ya existe un cliente con esa identificación.");
        break;
    }

    System.out.print("Teléfono: ");
    String telefono = sc.nextLine().trim();
    System.out.print("Dirección: ");
    String direccion = sc.nextLine().trim();
    System.out.print("Nombre de la obra: ");
    String nombreDeLaObra = sc.nextLine().trim();
    System.out.print("Proyecto: ");
    String proyecto = sc.nextLine().trim();
    System.out.print("Email: ");
    String email = sc.nextLine().trim();

   Cliente nuevoCliente = new Cliente(nombre, identificacion, telefono, direccion, nombreDeLaObra, proyecto, email);
   clientes.add(nuevoCliente);
   ClienteDAO.guardar(nuevoCliente);
    System.out.println("Cliente registrado con éxito.");
    break;         

                case 4:
                    System.out.println("\n===== CLIENTES REGISTRADOS =====");
                    if (clientes.isEmpty()) {
                        System.out.println("No hay clientes registrados todavía.");
                    } else {
                        for (Cliente c : clientes) {
                            c.mostrarInformacion();
                            System.out.println("------");
                        }
                    }
                    break;

                case 5:
                   
    if (maquinas.isEmpty() || clientes.isEmpty()) {
        System.out.println("Debe registrar al menos una máquina y un cliente primero.");
        break;
    }
    System.out.println("\n===== CLIENTES =====");
    for (int i = 0; i < clientes.size(); i++) {
        System.out.println(i + ". " + clientes.get(i).getNombre());
    }
    System.out.print("Elige el número del cliente: ");
    int indiceCliente = Integer.parseInt(sc.nextLine());

    // Filtrar solo máquinas disponibles
    ArrayList<Maquina> maquinasDisponibles = new ArrayList<>();
    for (Maquina m : maquinas) {
        if (m.getEstado() == EstadoMaquina.DISPONIBLE) {
            maquinasDisponibles.add(m);
        }
    }

    if (maquinasDisponibles.isEmpty()) {
        System.out.println("No hay máquinas disponibles en este momento.");
        break;
    }

    System.out.println("\n===== MÁQUINAS DISPONIBLES =====");
    for (int i = 0; i < maquinasDisponibles.size(); i++) {
        System.out.println(i + ". " + maquinasDisponibles.get(i).getMarca() + " " + maquinasDisponibles.get(i).getModelo());
    }
    System.out.print("Elige el número de la máquina: ");
    int indiceMaquina = Integer.parseInt(sc.nextLine());

    System.out.print("Cantidad (días/meses/años): ");
    int cantidad = Integer.parseInt(sc.nextLine());
    System.out.print("Tipo de tarifa (dia/mes/año): ");
    String tipoTarifa = sc.nextLine();

    Cliente clienteElegido = clientes.get(indiceCliente);
    Maquina maquinaElegida = maquinasDisponibles.get(indiceMaquina);
    maquinaElegida.setEstado(EstadoMaquina.ALQUILADA);
    MaquinaDAO.actualizarEstado(maquinaElegida.getPlaca(), EstadoMaquina.ALQUILADA);
    Alquiler nuevoAlquiler = new Alquiler(clienteElegido, maquinaElegida, cantidad, tipoTarifa);
    alquileres.add(nuevoAlquiler);
   AlquilerDAO.guardar(nuevoAlquiler);

   
    System.out.println("Alquiler creado con éxito.");
    System.out.printf("Total a pagar: $%,.0f%n", nuevoAlquiler.calcularTotal());
    break;

                case 6:
                    System.out.println("\n===== HISTORIAL DE ALQUILERES =====");
    if (alquileres.isEmpty()) {
        System.out.println("No hay alquileres registrados todavía.");
    } else {
        for (int i = 0; i < alquileres.size(); i++) {
            System.out.println("\n--- Alquiler #" + (i + 1) + " ---");
            alquileres.get(i).mostrarInformacion();
            System.out.println("------");
        }
    }
    break;


case 7:
    if (alquileres.isEmpty()) {
        System.out.println("No hay alquileres registrados.");
        break;
    }
    System.out.println("\n===== ALQUILERES ACTIVOS =====");
    for (int i = 0; i < alquileres.size(); i++) {
        System.out.println(i + ". " + alquileres.get(i).getCliente().getNombre()
            + " - " + alquileres.get(i).getMaquina().getMarca()
            + " " + alquileres.get(i).getMaquina().getModelo());
    }
    System.out.print("Elige el número del alquiler a devolver: ");
    int indiceDevolver = Integer.parseInt(sc.nextLine());

    System.out.print("¿Cuántas horas trabajó la máquina durante este alquiler? ");
    int horasTrabajadas = Integer.parseInt(sc.nextLine());
    alquileres.get(indiceDevolver).getMaquina().agregarHoras(horasTrabajadas);

    alquileres.get(indiceDevolver).getMaquina().setEstado(EstadoMaquina.DISPONIBLE);
    alquileres.remove(indiceDevolver);
    System.out.println("Máquina devuelta y disponible nuevamente.");
    break;

 
    case 8:
    
    System.out.println("\n¿Qué deseas buscar?");
    System.out.println("1. Cliente por nombre");
    System.out.println("2. Máquina por placa");
    System.out.print("Elige: ");
    int tipoBusqueda = Integer.parseInt(sc.nextLine());

    if (tipoBusqueda == 1) {
        System.out.print("Ingresa el nombre del cliente: ");
        String nombreBuscar = sc.nextLine();
        boolean encontradoCliente = false;
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(nombreBuscar)) {
                c.mostrarInformacion();
                encontradoCliente = true;
            }
        }
        if (!encontradoCliente) {
            System.out.println("No se encontró ningún cliente con ese nombre.");
        }

    } else if (tipoBusqueda == 2) {
        System.out.print("Ingresa la placa de la máquina: ");
        String placaBuscar = sc.nextLine();
        boolean encontradaMaquina = false;
        for (Maquina m : maquinas) {
            if (m.getPlaca().equalsIgnoreCase(placaBuscar)) {
                m.mostrarInformacion();
                encontradaMaquina = true;
            }
        }
        if (!encontradaMaquina) {
            System.out.println("No se encontró ninguna máquina con esa placa.");
        }
    } else {
        System.out.println("Opción no válida.");
    }
    break;

case 9:
    if (alquileres.isEmpty()) {
        System.out.println("No hay alquileres registrados.");
        break;
    }

    // Mostrar alquileres disponibles
    System.out.println("\n===== ALQUILERES =====");
    for (int i = 0; i < alquileres.size(); i++) {
        System.out.println(i + ". " + alquileres.get(i).getCliente().getNombre()
            + " - " + alquileres.get(i).getMaquina().getMarca()
            + " " + alquileres.get(i).getMaquina().getModelo());
    }
    System.out.print("Elige el número del alquiler: ");
    int indiceFactura = Integer.parseInt(sc.nextLine());
    

    System.out.print("Fecha (dd/mm/yyyy): ");
    String fechaFactura = sc.nextLine();

    System.out.print("Método de pago (Efectivo/Transferencia/Tarjeta): ");
    String metodoPagoFactura = sc.nextLine();

    System.out.print("IVA (escribe 0.19 para 19%, 0 si no aplica): ");
    double ivaFactura = Double.parseDouble(sc.nextLine().replace(",", "."));
    

    System.out.print("Observaciones: ");
    String observacionesFactura = sc.nextLine();

    // Crear la factura y generar el PDF
    Factura nuevaFactura = new Factura(
        alquileres.get(indiceFactura),
        alquileres.size(),
        fechaFactura,
        metodoPagoFactura,
        ivaFactura,
        observacionesFactura
    );

    GeneradorPDF.generarFactura(nuevaFactura);
    break;               
    
 case 10:
      
    menuCotizaciones.generar();
    break;

    case 11:
    
    menuGastos.registrar();
    break;
case 12:
    
    menuGastos.listarPorMaquina();
    break;

    case 13:
    menuGastos.generarReportePDF();
    break;

   case 14:
    menuMantenimiento.registrar();
    break;

case 15:
    menuMantenimiento.verPorMaquina();
    break;

case 16:
    menuMantenimiento.finalizar();
    break;

case 17:
   
    ReporteGeneralPDF.generarReporte(
        maquinas,
        clientes,
        alquileres,
        gastoServicio.listar(),
        mantenimientoServicio.listar()
    );
    break;

case 18:
    System.out.println("Saliendo del sistema...");
    break;      

                default:
                    System.out.println("Opción no válida");
            }



        } while (opcion != 18);

        sc.close();
    }
}


