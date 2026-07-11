import java.util.ArrayList;

public class GastoServicio {

    private ArrayList<GastoOperativo> gastos = GastoOperativoDAO.cargarTodos();

    public void registrar(String placaMaquina, GastoOperativo.TipoGasto tipo,
                          String descripcion, double costo) {
        GastoOperativo nuevo = new GastoOperativo(placaMaquina, tipo, descripcion, costo);
        gastos.add(nuevo);
        GastoOperativoDAO.guardar(nuevo);
        System.out.println("Gasto registrado con éxito. ID: " + nuevo.getId());
    }

    public ArrayList<GastoOperativo> listar() {
        return gastos;
    }

    public ArrayList<GastoOperativo> listarPorMaquina(String placa) {
        ArrayList<GastoOperativo> resultado = new ArrayList<>();
        for (GastoOperativo g : gastos) {
            if (g.getPlacaMaquina().equalsIgnoreCase(placa)) {
                resultado.add(g);
            }
        }
        return resultado;
    }

    public ArrayList<GastoOperativo> listarPorMes(String placa, String mes) {
        ArrayList<GastoOperativo> resultado = new ArrayList<>();
        for (GastoOperativo g : gastos) {
            if (g.getPlacaMaquina().equalsIgnoreCase(placa)
                    && g.getMes().equals(mes)) {
                resultado.add(g);
            }
        }
        return resultado;
    }

    public double totalPorMaquina(String placa) {
        double total = 0;
        for (GastoOperativo g : listarPorMaquina(placa)) {
            total += g.getCosto();
        }
        return total;
    }

    public double totalPorTipo(String placa, GastoOperativo.TipoGasto tipo) {
        double total = 0;
        for (GastoOperativo g : listarPorMaquina(placa)) {
            if (g.getTipo() == tipo) {
                total += g.getCosto();
            }
        }
        return total;
    }

    public boolean estaVacio() {
        return gastos.isEmpty();
    }

}
