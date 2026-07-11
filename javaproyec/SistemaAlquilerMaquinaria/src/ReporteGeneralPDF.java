import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ReporteGeneralPDF {

    private static BaseColor VERDE_OSCURO = new BaseColor(27, 67, 50);
    private static BaseColor VERDE_MEDIO = new BaseColor(45, 106, 79);
    private static BaseColor DORADO = new BaseColor(183, 121, 31);
    private static BaseColor VERDE_CLARO = new BaseColor(240, 250, 244);
    private static BaseColor BLANCO = BaseColor.WHITE;
    private static BaseColor ROJO_OSCURO = new BaseColor(123, 29, 29);
    private static BaseColor NARANJA_OSCURO = new BaseColor(146, 64, 14);
    private static BaseColor AMARILLO_CLARO = new BaseColor(255, 243, 205);
    private static BaseColor ROJO_CLARO = new BaseColor(255, 230, 230);

    public static void generarReporte(
            ArrayList<Maquina> maquinas,
            ArrayList<Cliente> clientes,
            ArrayList<Alquiler> alquileres,
            ArrayList<GastoOperativo> gastos,
            ArrayList<Mantenimiento> mantenimientos) {

        try {
            File carpeta = new File("Reportes");
            if (!carpeta.exists()) carpeta.mkdirs();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            String fechaHora = LocalDateTime.now().format(fmt);
            String nombreArchivo = "Reportes\\ReporteGeneral_" + fechaHora + ".pdf";

            Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            NumberFormat fmt2 = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            Font fuenteSeccion = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BLANCO);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteTotal = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BLANCO);

            // ===== ENCABEZADO =====
            PdfPTable encabezado = new PdfPTable(3);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{20f, 40f, 40f});

            try {
                Image logo = Image.getInstance("logo.png");
                logo.scaleToFit(80, 80);
                PdfPCell celdaLogo = new PdfPCell(logo);
                celdaLogo.setBackgroundColor(VERDE_OSCURO);
                celdaLogo.setBorder(Rectangle.NO_BORDER);
                celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaLogo.setPadding(5);
                encabezado.addCell(celdaLogo);
            } catch (Exception e) {
                PdfPCell celdaLogo = new PdfPCell(new Phrase(""));
                celdaLogo.setBackgroundColor(VERDE_OSCURO);
                celdaLogo.setBorder(Rectangle.NO_BORDER);
                encabezado.addCell(celdaLogo);
            }

            Font fuenteEmpresa = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BLANCO);
            Font fuenteDatos = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BLANCO);
            PdfPCell celdaEmpresa = new PdfPCell();
            celdaEmpresa.setBackgroundColor(VERDE_OSCURO);
            celdaEmpresa.setPadding(10);
            celdaEmpresa.setBorder(Rectangle.NO_BORDER);
            celdaEmpresa.addElement(new Phrase("MAQUINARIA & ALQUILER", fuenteEmpresa));
            celdaEmpresa.addElement(new Phrase("NIT: 123456789-0", fuenteDatos));
            celdaEmpresa.addElement(new Phrase("Tel: 3217230697", fuenteDatos));
            celdaEmpresa.addElement(new Phrase("Cartagena, Colombia", fuenteDatos));
            encabezado.addCell(celdaEmpresa);

            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BLANCO);
            Font fuenteFecha = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BLANCO);
            PdfPCell celdaTitulo = new PdfPCell();
            celdaTitulo.setBackgroundColor(VERDE_MEDIO);
            celdaTitulo.setPadding(15);
            celdaTitulo.setBorder(Rectangle.NO_BORDER);
            celdaTitulo.addElement(new Phrase("REPORTE GENERAL", fuenteTitulo));
            celdaTitulo.addElement(new Phrase("Fecha: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")), fuenteFecha));
            celdaTitulo.addElement(new Phrase("Hora: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")), fuenteFecha));
            encabezado.addCell(celdaTitulo);

            documento.add(encabezado);
            documento.add(new Paragraph(" "));

            // ===== MÁQUINAS =====
            agregarTituloSeccion(documento, "MAQUINAS (" + maquinas.size() + " en total)", fuenteSeccion, VERDE_OSCURO);
            PdfPTable tablaMaquinas = new PdfPTable(4);
            tablaMaquinas.setWidthPercentage(100);
            tablaMaquinas.setWidths(new float[]{30f, 25f, 20f, 25f});

            agregarEncabezadoTabla(tablaMaquinas, new String[]{"MAQUINA", "PLACA", "ESTADO", "TARIFA/DIA"}, fuenteSeccion, DORADO);

            for (Maquina m : maquinas) {
                BaseColor colorEstado = m.getEstado() == EstadoMaquina.DISPONIBLE ? VERDE_CLARO :
                                        m.getEstado() == EstadoMaquina.ALQUILADA ? ROJO_CLARO :
                                        AMARILLO_CLARO;

                agregarCeldaColor(tablaMaquinas, m.getMarca() + " " + m.getModelo(), fuenteNormal, colorEstado);
                agregarCeldaColor(tablaMaquinas, m.getPlaca(), fuenteNormal, colorEstado);
                agregarCeldaColor(tablaMaquinas, m.getEstado().toString(), fuenteNegrita, colorEstado);
                agregarCeldaColor(tablaMaquinas, fmt2.format(m.getTarifaPorDia()), fuenteNormal, colorEstado);
            }
            documento.add(tablaMaquinas);
            documento.add(new Paragraph(" "));

            // ===== CLIENTES =====
            agregarTituloSeccion(documento, "CLIENTES (" + clientes.size() + " en total)", fuenteSeccion, VERDE_OSCURO);
            PdfPTable tablaClientes = new PdfPTable(3);
            tablaClientes.setWidthPercentage(100);
            tablaClientes.setWidths(new float[]{40f, 30f, 30f});

            agregarEncabezadoTabla(tablaClientes, new String[]{"NOMBRE", "IDENTIFICACION", "TELEFONO"}, fuenteSeccion, DORADO);

            for (Cliente c : clientes) {
                agregarCeldaColor(tablaClientes, c.getNombre(), fuenteNormal, VERDE_CLARO);
                agregarCeldaColor(tablaClientes, c.getIdentificacion(), fuenteNormal, VERDE_CLARO);
                agregarCeldaColor(tablaClientes, c.getTelefono(), fuenteNormal, VERDE_CLARO);
            }
            documento.add(tablaClientes);
            documento.add(new Paragraph(" "));

            // ===== ALQUILERES ACTIVOS =====
            agregarTituloSeccion(documento, "ALQUILERES ACTIVOS (" + alquileres.size() + ")", fuenteSeccion, VERDE_OSCURO);
            if (alquileres.isEmpty()) {
                documento.add(new Paragraph("No hay alquileres activos.", fuenteNormal));
            } else {
                PdfPTable tablaAlquileres = new PdfPTable(4);
                tablaAlquileres.setWidthPercentage(100);
                tablaAlquileres.setWidths(new float[]{30f, 25f, 20f, 25f});

                agregarEncabezadoTabla(tablaAlquileres, new String[]{"CLIENTE", "MAQUINA", "CANTIDAD", "TOTAL"}, fuenteSeccion, DORADO);

                double totalIngresos = 0;
                for (Alquiler a : alquileres) {
                    agregarCeldaColor(tablaAlquileres, a.getCliente().getNombre(), fuenteNormal, VERDE_CLARO);
                    agregarCeldaColor(tablaAlquileres, a.getMaquina().getMarca() + " " + a.getMaquina().getModelo(), fuenteNormal, VERDE_CLARO);
                    agregarCeldaColor(tablaAlquileres, a.getCantidad() + " " + a.getTipoTarifa(), fuenteNormal, VERDE_CLARO);
                    agregarCeldaColor(tablaAlquileres, fmt2.format(a.calcularTotal()), fuenteNormal, VERDE_CLARO);
                    totalIngresos += a.calcularTotal();
                }
                documento.add(tablaAlquileres);

                PdfPTable tablaTotal = new PdfPTable(2);
                tablaTotal.setWidthPercentage(40);
                tablaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                agregarFilaResumen(tablaTotal, "TOTAL INGRESOS:", fmt2.format(totalIngresos), VERDE_OSCURO, fuenteTotal);
                documento.add(tablaTotal);
            }
            documento.add(new Paragraph(" "));

            // ===== GASTOS OPERATIVOS =====
            agregarTituloSeccion(documento, "GASTOS OPERATIVOS (" + gastos.size() + ")", fuenteSeccion, VERDE_OSCURO);
            if (gastos.isEmpty()) {
                documento.add(new Paragraph("No hay gastos registrados.", fuenteNormal));
            } else {
                PdfPTable tablaGastos = new PdfPTable(4);
                tablaGastos.setWidthPercentage(100);
                tablaGastos.setWidths(new float[]{20f, 25f, 35f, 20f});

                agregarEncabezadoTabla(tablaGastos, new String[]{"PLACA", "TIPO", "DESCRIPCION", "COSTO"}, fuenteSeccion, DORADO);

                double totalGastos = 0;
                for (GastoOperativo g : gastos) {
                    agregarCeldaColor(tablaGastos, g.getPlacaMaquina(), fuenteNormal, ROJO_CLARO);
                    agregarCeldaColor(tablaGastos, g.getTipo().toString(), fuenteNormal, ROJO_CLARO);
                    agregarCeldaColor(tablaGastos, g.getDescripcion(), fuenteNormal, ROJO_CLARO);
                    agregarCeldaColor(tablaGastos, fmt2.format(g.getCosto()), fuenteNormal, ROJO_CLARO);
                    totalGastos += g.getCosto();
                }
                documento.add(tablaGastos);

                PdfPTable tablaGastoTotal = new PdfPTable(2);
                tablaGastoTotal.setWidthPercentage(40);
                tablaGastoTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                agregarFilaResumen(tablaGastoTotal, "TOTAL GASTOS:", fmt2.format(totalGastos), ROJO_OSCURO, fuenteTotal);
                documento.add(tablaGastoTotal);
            }
            documento.add(new Paragraph(" "));

            // ===== MANTENIMIENTOS =====
            agregarTituloSeccion(documento, "MANTENIMIENTOS (" + mantenimientos.size() + ")", fuenteSeccion, VERDE_OSCURO);
            if (mantenimientos.isEmpty()) {
                documento.add(new Paragraph("No hay mantenimientos registrados.", fuenteNormal));
            } else {
                PdfPTable tablaMantenimientos = new PdfPTable(5);
                tablaMantenimientos.setWidthPercentage(100);
                tablaMantenimientos.setWidths(new float[]{20f, 20f, 25f, 20f, 15f});

                agregarEncabezadoTabla(tablaMantenimientos, new String[]{"PLACA", "TIPO", "DESCRIPCION", "TECNICO", "COSTO"}, fuenteSeccion, DORADO);

                double totalMantenimientos = 0;
                for (Mantenimiento m : mantenimientos) {
                    BaseColor color = m.isActivo() ? AMARILLO_CLARO : VERDE_CLARO;
                    agregarCeldaColor(tablaMantenimientos, m.getPlacaMaquina(), fuenteNormal, color);
                    agregarCeldaColor(tablaMantenimientos, m.getTipo().toString(), fuenteNormal, color);
                    agregarCeldaColor(tablaMantenimientos, m.getDescripcion(), fuenteNormal, color);
                    agregarCeldaColor(tablaMantenimientos, m.getTecnico(), fuenteNormal, color);
                    agregarCeldaColor(tablaMantenimientos, fmt2.format(m.getCosto()), fuenteNormal, color);
                    totalMantenimientos += m.getCosto();
                }
                documento.add(tablaMantenimientos);

                PdfPTable tablaMantenimientoTotal = new PdfPTable(2);
                tablaMantenimientoTotal.setWidthPercentage(40);
                tablaMantenimientoTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                agregarFilaResumen(tablaMantenimientoTotal, "TOTAL MANTENIMIENTOS:", fmt2.format(totalMantenimientos), NARANJA_OSCURO, fuenteTotal);
                documento.add(tablaMantenimientoTotal);
            }

            documento.close();
            System.out.println("Reporte general generado: " + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }

    private static void agregarTituloSeccion(Document doc, String titulo, Font fuente, BaseColor color) throws DocumentException {
        PdfPTable tabla = new PdfPTable(1);
        tabla.setWidthPercentage(100);
        PdfPCell celda = new PdfPCell(new Phrase(titulo, fuente));
        celda.setBackgroundColor(color);
        celda.setPadding(8);
        celda.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celda);
        doc.add(tabla);
    }

    private static void agregarEncabezadoTabla(PdfPTable tabla, String[] titulos, Font fuente, BaseColor color) {
        for (String titulo : titulos) {
            PdfPCell celda = new PdfPCell(new Phrase(titulo, fuente));
            celda.setBackgroundColor(color);
            celda.setPadding(7);
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
        }
    }

    private static void agregarCeldaColor(PdfPTable tabla, String texto, Font fuente, BaseColor color) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(color);
        celda.setPadding(6);
        celda.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celda);
    }

    private static void agregarFilaResumen(PdfPTable tabla, String label, String valor, BaseColor color, Font fuente) {
        PdfPCell celdaLabel = new PdfPCell(new Phrase(label, fuente));
        celdaLabel.setBackgroundColor(color);
        celdaLabel.setPadding(8);
        celdaLabel.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaLabel);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor, fuente));
        celdaValor.setBackgroundColor(color);
        celdaValor.setPadding(8);
        celdaValor.setBorder(Rectangle.NO_BORDER);
        celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(celdaValor);
    }
}