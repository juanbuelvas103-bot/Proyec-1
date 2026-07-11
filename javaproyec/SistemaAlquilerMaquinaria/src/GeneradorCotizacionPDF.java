import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

public class GeneradorCotizacionPDF {

    // ── Paleta de colores propia (distinta a la factura) ──────────────────────
    private static final BaseColor VERDE_OSCURO   = new BaseColor(18, 84, 55);   // encabezado
    private static final BaseColor VERDE_MEDIO    = new BaseColor(39, 124, 85);  // secciones
    private static final BaseColor VERDE_CLARO    = new BaseColor(214, 240, 226); // fondo celdas
    private static final BaseColor DORADO         = new BaseColor(198, 155, 46); // acento totales
    private static final BaseColor BLANCO         = BaseColor.WHITE;
    private static final BaseColor GRIS_TEXTO     = new BaseColor(50, 50, 50);

    // ── Fuentes ───────────────────────────────────────────────────────────────
    private static final Font F_TITULO     = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD,  BLANCO);
    private static final Font F_SUBTITULO  = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BLANCO);
    private static final Font F_SECCION    = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,  BLANCO);
    private static final Font F_LABEL      = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,  GRIS_TEXTO);
    private static final Font F_VALOR      = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, GRIS_TEXTO);
    private static final Font F_TOTAL_L    = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,  BLANCO);
    private static final Font F_TOTAL_V    = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,  DORADO);
    private static final Font F_PIE        = new Font(Font.FontFamily.HELVETICA, 8,  Font.ITALIC, VERDE_MEDIO);
    private static final Font F_VIGENCIA   = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,  DORADO);

    public static void generarCotizacion(Cotizacion cotizacion) {
        try {
            // ── Crear carpeta Cotizaciones/ si no existe ──────────────────────
            File carpeta = new File("Cotizaciones");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
                System.out.println("Carpeta 'Cotizaciones/' creada.");
            }

            String nombreArchivo = "Cotizaciones/COT-"
                + String.format("%03d", cotizacion.getNumeroCotizacion())
                + "_" + cotizacion.getCliente().getNombre().replace(" ", "_")
                + ".pdf";

            Document doc = new Document(PageSize.A4, 45, 45, 45, 55);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(nombreArchivo));

            // Pie de página automático
            writer.setPageEvent(new PiesDePagina(cotizacion));

            doc.open();

            // ═══════════════════════════════════════════════════════════════
            // ENCABEZADO: logo + nombre empresa | datos cotización
            // ═══════════════════════════════════════════════════════════════
            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{55f, 45f});
            encabezado.setSpacingAfter(12);

            // Celda izquierda: logo + nombre empresa
            PdfPCell celdaEmpresa = new PdfPCell();
            celdaEmpresa.setBackgroundColor(VERDE_OSCURO);
            celdaEmpresa.setBorder(Rectangle.NO_BORDER);
            celdaEmpresa.setPadding(14);

            try {
                Image logo = Image.getInstance("logo.png");
                logo.scaleToFit(70, 70);
                celdaEmpresa.addElement(logo);
                celdaEmpresa.addElement(new Phrase(" ", F_SUBTITULO));
            } catch (Exception ex) {
                // sin logo la celda sigue generándose
            }

            celdaEmpresa.addElement(new Phrase("MAQUINARIA & ALQUILER", F_TITULO));
            celdaEmpresa.addElement(new Phrase("NIT: 123456789-0", F_SUBTITULO));
            celdaEmpresa.addElement(new Phrase("Tel: 3217230697  |  Cartagena, Colombia", F_SUBTITULO));
            encabezado.addCell(celdaEmpresa);

            // Celda derecha: número y fechas de cotización
            PdfPCell celdaCot = new PdfPCell();
            celdaCot.setBackgroundColor(VERDE_MEDIO);
            celdaCot.setBorder(Rectangle.NO_BORDER);
            celdaCot.setPadding(14);
            celdaCot.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Font fNumCot = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BLANCO);
            Font fDetCot = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, BLANCO);
            Font fVig    = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD, DORADO);

            celdaCot.addElement(new Phrase("COTIZACIÓN", fDetCot));
            celdaCot.addElement(new Phrase("N° " + String.format("%03d", cotizacion.getNumeroCotizacion()), fNumCot));
            celdaCot.addElement(new Phrase(" ", fDetCot));
            celdaCot.addElement(new Phrase("Fecha emisión:     " + cotizacion.getFechaEmision(), fDetCot));
            celdaCot.addElement(new Phrase("Válida hasta:         " + cotizacion.getFechaVencimiento(), fVig));
            encabezado.addCell(celdaCot);

            doc.add(encabezado);

            // ═══════════════════════════════════════════════════════════════
            // DATOS DEL CLIENTE
            // ═══════════════════════════════════════════════════════════════
            doc.add(tituloSeccion("DATOS DEL CLIENTE"));

            PdfPTable tablaCliente = new PdfPTable(2);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.setWidths(new float[]{50f, 50f});
            tablaCliente.setSpacingAfter(10);

            agregarParCelda(tablaCliente, "Nombre:",       cotizacion.getCliente().getNombre());
            agregarParCelda(tablaCliente, "Identificación:", cotizacion.getCliente().getIdentificacion());
            agregarParCelda(tablaCliente, "Teléfono:",     cotizacion.getCliente().getTelefono());
            agregarParCelda(tablaCliente, "Email:",        cotizacion.getCliente().getEmail());
            agregarParCelda(tablaCliente, "Dirección:",    cotizacion.getCliente().getDireccion());
            agregarParCelda(tablaCliente, "Obra:",         cotizacion.getCliente().getNombreDeLaObra());

            doc.add(tablaCliente);

            // ═══════════════════════════════════════════════════════════════
            // DETALLE DE LA MAQUINA
            // ═══════════════════════════════════════════════════════════════
            doc.add(tituloSeccion("DETALLE DEL SERVICIO COTIZADO"));

            PdfPTable tablaDetalle = new PdfPTable(4);
            tablaDetalle.setWidthPercentage(100);
            tablaDetalle.setWidths(new float[]{40f, 20f, 20f, 20f});
            tablaDetalle.setSpacingAfter(10);

            // Encabezados de columna
            String[] cols = {"DESCRIPCIÓN", "TARIFA", "CANTIDAD", "VALOR ESTIMADO"};
            for (String col : cols) {
                PdfPCell h = new PdfPCell(new Phrase(col, F_SECCION));
                h.setBackgroundColor(VERDE_OSCURO);
                h.setPadding(8);
                h.setBorder(Rectangle.NO_BORDER);
                h.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaDetalle.addCell(h);
            }

            // Fila de datos
            NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

            String desc = "Alquiler " + cotizacion.getMaquina().getMarca()
                + " " + cotizacion.getMaquina().getModelo()
                + "\nPlaca: " + cotizacion.getMaquina().getPlaca();

            PdfPCell cDesc = new PdfPCell(new Phrase(desc, F_VALOR));
            cDesc.setBackgroundColor(VERDE_CLARO);
            cDesc.setPadding(8);
            cDesc.setBorder(Rectangle.NO_BORDER);
            tablaDetalle.addCell(cDesc);

            PdfPCell cTarifa = celdaCentro(cotizacion.getTipoTarifa());
            tablaDetalle.addCell(cTarifa);

            PdfPCell cCant = celdaCentro(String.valueOf(cotizacion.getCantidad()));
            tablaDetalle.addCell(cCant);

            PdfPCell cValor = celdaDerecha(fmt.format(cotizacion.getSubtotal()));
            tablaDetalle.addCell(cValor);

            doc.add(tablaDetalle);

            // ═══════════════════════════════════════════════════════════════
            // TOTALES
            // ═══════════════════════════════════════════════════════════════
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(45);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.setSpacingAfter(14);

            filaTotal(tablaTotales, "Subtotal:",
                fmt.format(cotizacion.getSubtotal()), VERDE_CLARO, F_VALOR, F_VALOR);
            filaTotal(tablaTotales, "IVA " + (int)(cotizacion.getIva() * 100) + "%:",
                fmt.format(cotizacion.getSubtotal() * cotizacion.getIva()), VERDE_CLARO, F_VALOR, F_VALOR);
            filaTotal(tablaTotales, "TOTAL ESTIMADO:",
                fmt.format(cotizacion.getTotal()), VERDE_OSCURO, F_TOTAL_L, F_TOTAL_V);

            doc.add(tablaTotales);

            // ═══════════════════════════════════════════════════════════════
            // CONDICIONES DE PAGO
            // ═══════════════════════════════════════════════════════════════
            if (cotizacion.getCondicionesPago() != null && !cotizacion.getCondicionesPago().isEmpty()) {
                doc.add(tituloSeccion("CONDICIONES DE PAGO"));
                PdfPTable tablaPago = new PdfPTable(1);
                tablaPago.setWidthPercentage(100);
                tablaPago.setSpacingAfter(10);
                PdfPCell cPago = new PdfPCell(new Phrase(cotizacion.getCondicionesPago(), F_VALOR));
                cPago.setBackgroundColor(VERDE_CLARO);
                cPago.setPadding(10);
                cPago.setBorder(Rectangle.NO_BORDER);
                tablaPago.addCell(cPago);
                doc.add(tablaPago);
            }

            // ═══════════════════════════════════════════════════════════════
            // OBSERVACIONES
            // ═══════════════════════════════════════════════════════════════
            if (cotizacion.getObservaciones() != null && !cotizacion.getObservaciones().isEmpty()) {
                doc.add(tituloSeccion("OBSERVACIONES"));
                PdfPTable tablaObs = new PdfPTable(1);
                tablaObs.setWidthPercentage(100);
                tablaObs.setSpacingAfter(10);
                PdfPCell cObs = new PdfPCell(new Phrase(cotizacion.getObservaciones(), F_VALOR));
                cObs.setBackgroundColor(VERDE_CLARO);
                cObs.setPadding(10);
                cObs.setBorder(Rectangle.NO_BORDER);
                tablaObs.addCell(cObs);
                doc.add(tablaObs);
            }

            // ═══════════════════════════════════════════════════════════════
            // VIGENCIA (recordatorio visual destacado)
            // ═══════════════════════════════════════════════════════════════
            PdfPTable tablaVig = new PdfPTable(1);
            tablaVig.setWidthPercentage(100);
            tablaVig.setSpacingAfter(6);
            PdfPCell cVig = new PdfPCell(
                new Phrase("⚠  Esta cotización es válida hasta el "
                    + cotizacion.getFechaVencimiento()
                    + ". Pasada esta fecha los precios pueden variar.", F_VIGENCIA));
            cVig.setBackgroundColor(VERDE_OSCURO);
            cVig.setPadding(10);
            cVig.setBorder(Rectangle.NO_BORDER);
            cVig.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaVig.addCell(cVig);
            doc.add(tablaVig);

            // Separador decorativo
            doc.add(new Chunk(new LineSeparator(1f, 100f, VERDE_MEDIO, Element.ALIGN_CENTER, -2)));
            doc.add(new Paragraph(" "));

            // Agradecimiento
            Paragraph gracias = new Paragraph(
                "Gracias por confiar en MAQUINARIA & ALQUILER.\n"
                + "Para aceptar esta cotización o solicitar información adicional, contáctenos.", F_PIE);
            gracias.setAlignment(Element.ALIGN_CENTER);
            doc.add(gracias);

            doc.close();
            System.out.println("Cotizacion generada: " + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al generar cotizacion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── Helpers de construcción ───────────────────────────────────────────────

    private static PdfPTable tituloSeccion(String texto) throws DocumentException {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(8);
        t.setSpacingAfter(4);
        PdfPCell c = new PdfPCell(new Phrase(texto, F_SECCION));
        c.setBackgroundColor(VERDE_MEDIO);
        c.setPadding(7);
        c.setBorder(Rectangle.NO_BORDER);
        t.addCell(c);
        return t;
    }

    private static void agregarParCelda(PdfPTable tabla, String label, String valor) {
        PdfPCell cLabel = new PdfPCell(new Phrase(label, F_LABEL));
        cLabel.setBackgroundColor(VERDE_CLARO);
        cLabel.setPadding(7);
        cLabel.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(cLabel);

        PdfPCell cValor = new PdfPCell(new Phrase(valor != null ? valor : "-", F_VALOR));
        cValor.setBackgroundColor(BLANCO);
        cValor.setPadding(7);
        cValor.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(cValor);
    }

    private static PdfPCell celdaCentro(String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto, F_VALOR));
        c.setBackgroundColor(VERDE_CLARO);
        c.setPadding(8);
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private static PdfPCell celdaDerecha(String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto, F_VALOR));
        c.setBackgroundColor(VERDE_CLARO);
        c.setPadding(8);
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }

    private static void filaTotal(PdfPTable tabla, String label, String valor,
                                   BaseColor color, Font fLabel, Font fValor) {
        PdfPCell cL = new PdfPCell(new Phrase(label, fLabel));
        cL.setBackgroundColor(color);
        cL.setPadding(8);
        cL.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(cL);

        PdfPCell cV = new PdfPCell(new Phrase(valor, fValor));
        cV.setBackgroundColor(color);
        cV.setPadding(8);
        cV.setBorder(Rectangle.NO_BORDER);
        cV.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tabla.addCell(cV);
    }

    // ── Pie de página con número de página ───────────────────────────────────
    static class PiesDePagina extends PdfPageEventHelper {

        private Cotizacion cotizacion;

        public PiesDePagina(Cotizacion cotizacion) {
            this.cotizacion = cotizacion;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font fPie = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC, new BaseColor(120, 120, 120));
            Phrase pie = new Phrase(
                "MAQUINARIA & ALQUILER  |  COT-"
                + String.format("%03d", cotizacion.getNumeroCotizacion())
                + "  |  Página " + writer.getPageNumber(), fPie);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pie,
                (document.left() + document.right()) / 2, document.bottom() - 10, 0);
        }
    }
}
