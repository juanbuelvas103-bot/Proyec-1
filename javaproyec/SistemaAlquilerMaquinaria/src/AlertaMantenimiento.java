import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AlertaMantenimiento {

    // Verifica todas las máquinas y muestra alertas
    public static void verificarAlertas(ArrayList<Maquina> maquinas) {
        System.out.println("\n===== ⚠️  ALERTAS DE MANTENIMIENTO ⚠️  =====");
        boolean hayAlertas = false;

        for (Maquina m : maquinas) {
            // Alerta por días
            if (m.getFechaUltimoMantenimiento() != null) {
                long diasTranscurridos = diasDesde(m.getFechaUltimoMantenimiento());
                long diasRestantes = m.getDiasEntreMantenimientos() - diasTranscurridos;

                if (diasRestantes <= 5 && diasRestantes > 0) {
                    System.out.println("⚠️  ALERTA: " + m.getMarca() + " " + m.getModelo()
                        + " (Placa: " + m.getPlaca() + ")");
                    System.out.println("   Mantenimiento en " + diasRestantes + " días.");
                    hayAlertas = true;
                } else if (diasRestantes <= 0) {
                    System.out.println("🔴 URGENTE: " + m.getMarca() + " " + m.getModelo()
                        + " (Placa: " + m.getPlaca() + ")");
                    System.out.println("   Mantenimiento VENCIDO hace " + Math.abs(diasRestantes) + " días.");
                    hayAlertas = true;
                }
            }

            // Alerta por horas
            int horasRestantes = m.getHorasMaximas() - m.getHorasActuales();
            if (horasRestantes <= 20 && horasRestantes > 0) {
                System.out.println("⚠️  ALERTA: " + m.getMarca() + " " + m.getModelo()
                    + " (Placa: " + m.getPlaca() + ")");
                System.out.println("   Le quedan " + horasRestantes + " horas para mantenimiento.");
                hayAlertas = true;
            } else if (horasRestantes <= 0) {
                System.out.println("🔴 URGENTE: " + m.getMarca() + " " + m.getModelo()
                    + " (Placa: " + m.getPlaca() + ")");
                System.out.println("   Horas EXCEDIDAS en " + Math.abs(horasRestantes) + " horas.");
                hayAlertas = true;
            }
        }

        if (!hayAlertas) {
            System.out.println("✅ Todas las máquinas están al día con el mantenimiento.");
        }
        System.out.println("=============================================\n");
    }

    // Calcula cuántos días han pasado desde una fecha
    private static long diasDesde(String fecha) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaMantenimiento = LocalDate.parse(fecha, fmt);
        return ChronoUnit.DAYS.between(fechaMantenimiento, LocalDate.now());
    }
}