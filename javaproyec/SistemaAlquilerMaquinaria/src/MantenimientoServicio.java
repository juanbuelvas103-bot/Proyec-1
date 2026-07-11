import java.util.ArrayList;

public class MantenimientoServicio {

    private ArrayList<Mantenimiento> mantenimientos = new ArrayList<>();

    public Mantenimiento registrar(String placaMaquina, Mantenimiento.TipoMantenimiento tipo,
                                    String descripcion, String tecnico, double costo) {
        Mantenimiento nuevo = new Mantenimiento(placaMaquina, tipo, descripcion, tecnico, costo);
        mantenimientos.add(nuevo);
        MantenimientoDAO.guardar(nuevo);
        return nuevo;
    }

    public void finalizar(int id, String observaciones, Maquina maquina) {
        for (Mantenimiento m : mantenimientos) {
            if (m.getId() == id && m.isActivo()) {
                m.finalizar(observaciones);
                MantenimientoDAO.finalizar(id, observaciones, m.getFechaFin());
                // actualizar fecha ultimo mantenimiento en la maquina
                maquina.setFechaUltimoMantenimiento(m.getFechaFin());
                maquina.setHorasActuales(0); // reinicia contador de horas
                System.out.println("Mantenimiento finalizado correctamente.");
                return;
            }
        }
        System.out.println("No se encontró mantenimiento activo con ese ID.");
    }

    public ArrayList<Mantenimiento> listarPorMaquina(String placa) {
        ArrayList<Mantenimiento> resultado = new ArrayList<>();
        for (Mantenimiento m : mantenimientos) {
            if (m.getPlacaMaquina().equalsIgnoreCase(placa)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    public ArrayList<Mantenimiento> listarActivos() {
        ArrayList<Mantenimiento> resultado = new ArrayList<>();
        for (Mantenimiento m : mantenimientos) {
            if (m.isActivo()) resultado.add(m);
        }
        return resultado;
    }

    public double totalPorMaquina(String placa) {
        double total = 0;
        for (Mantenimiento m : listarPorMaquina(placa)) {
            total += m.getCosto();
        }
        return total;
    }
public ArrayList<Mantenimiento> listar() {
    return mantenimientos;
}
    public boolean estaVacio() {
        return mantenimientos.isEmpty();
    }
}