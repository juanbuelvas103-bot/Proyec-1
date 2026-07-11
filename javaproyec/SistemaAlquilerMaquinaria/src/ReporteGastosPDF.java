import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ReporteGastosPDF {

    private static final BaseColor VERDE_OSCURO = new BaseColor(179, 71, 0);
private static final BaseColor VERDE_MEDIO  = new BaseColor(224, 92, 0);
private static final BaseColor VERDE_CLARO  = new BaseColor(255, 240, 224);
private static final BaseColor DORADO       = new BaseColor(26, 26, 26);
private static final BaseColor BLANCO       = BaseColor.WHITE;
private static final BaseColor GRIS_TEXTO   = new BaseColor(50, 50, 50);


    private static final Font F_TITULO    = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,   BLANCO);
    private static final Font F_SUBTITULO = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, BLANCO);
    private static final Font F_SECCION   = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   BLANCO);
    private static final Font F_LABEL     = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,   GRIS_TEXTO);
    private static final Font F_VALOR     = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, GRIS_TEXTO);
    private static final Font F_TOTAL     = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   BLANCO);
    private static final Font F_TOTAL_V   = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,   DORADO);

    public static void generarReporte(Maquina maquina, ArrayList<GastoOperativo> gastos) {
        try {
            File carpeta = new File("Reportes");
            if (!carpeta.exists()) carpeta.mkdirs();

            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String nombreArchivo = "Reportes/Reporte_Gastos_"
                + maquina.getPlaca().replace(" ", "_")
                + "_" + fecha + ".pdf";

            Document doc = new Document(PageSize.A4, 40, 40, 40, 55);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(nombreArchivo));
            writer.setPageEvent(new PiePagina());
            doc.open();

            // ENCABEZADO
            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{55f, 45f});
            encabezado.setSpacingAfter(12);

            PdfPCell celdaEmpresa = new PdfPCell();
            celdaEmpresa.setBackgroundColor(VERDE_OSCURO);
            celdaEmpresa.setBorder(Rectangle.NO_BORDER);
            celdaEmpresa.setPadding(14);
            try {
                Image logo = Image.getInstance("logo.png");
                logo.scaleToFit(60, 60);
                celdaEmpresa.addElement(logo);
                celdaEmpresa.addElement(new Phrase(" ", F_SUBTITULO));
            } catch (Exception ex) { }
            celdaEmpresa.addElement(new Phrase("MAQUINARIA & ALQUILER", F_TITULO));
            celdaEmpresa.addElement(new Phrase("NIT: 123456789-0", F_SUBTITULO));
            celdaEmpresa.addElement(new Phrase("Tel: 3217230697  |  Cartagena, Colombia", F_SUBTITULO));
            encabezado.addCell(celdaEmpresa);

            PdfPCell celdaTitulo = new PdfPCell();
            celdaTitulo.setBackgroundColor(VERDE_MEDIO);
            celdaTitulo.setBorder(Rectangle.NO_BORDER);
            celdaTitulo.setPadding(14);
            celdaTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Font fTitRep = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BLANCO);
            Font fSubRep = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, BLANCO);
            Font fFecRep = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD, DORADO);
            celdaTitulo.addElement(new Phrase("REPORTE DE GASTOS", fTitRep));
            celdaTitulo.addElement(new Phrase(" ", fSubRep));
            celdaTitulo.addElement(new Phrase("Fecha: " + fecha.replace("-", "/"), fSubRep));
            celdaTitulo.addElement(new Phrase("Generado automáticamente", fFecRep));
            encabezado.addCell(celdaTitulo);
            doc.add(encabezado);

            // DATOS DE LA MÁQUINA
            doc.add(tituloSeccion("DATOS DE LA MÁQUINA"));
            PdfPTable tablaMaq = new PdfPTable(2);
            tablaMaq.setWidthPercentage(100);
            tablaMaq.setWidths(new float[]{50f, 50f});
            tablaMaq.setSpacingAfter(10);
            agregarPar(tablaMaq, "Marca:",  maquina.getMarca());
            agregarPar(tablaMaq, "Modelo:", maquina.getModelo());
            agregarPar(tablaMaq, "Placa:",  maquina.getPlaca());
            agregarPar(tablaMaq, "Estado:", maquina.getEstado().toString());
            doc.add(tablaMaq);

            // DETALLE DE GASTOS
            doc.add(tituloSeccion("DETALLE DE GASTOS OPERATIVOS"));
            PdfPTable tablaGastos = new PdfPTable(4);
            tablaGastos.setWidthPercentage(100);
            tablaGastos.setWidths(new float[]{10f, 25f, 40f, 25f});
            tablaGastos.setSpacingAfter(10);

            for (String col : new String[]{"#", "TIPO", "DESCRIPCIÓN", "COSTO"}) {
                PdfPCell h = new PdfPCell(new Phrase(col, F_SECCION));
                h.setBackgroundColor(VERDE_OSCURO);
                h.setPadding(7);
                h.setBorder(Rectangle.NO_BORDER);
                h.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaGastos.addCell(h);
            }

            NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            double totalGastos = 0;
            int fila = 1;

            for (GastoOperativo g : gastos) {
                BaseColor fondo = (fila % 2 == 0) ? VERDE_CLARO : BLANCO;

                PdfPCell cNum = new PdfPCell(new Phrase(String.valueOf(fila), F_VALOR));
                cNum.setBackgroundColor(fondo); cNum.setPadding(7); cNum.setBorder(Rectangle.NO_BORDER);
                cNum.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaGastos.addCell(cNum);

                PdfPCell cTipo = new PdfPCell(new Phrase(g.getTipo().toString(), F_VALOR));
                cTipo.setBackgroundColor(fondo); cTipo.setPadding(7); cTipo.setBorder(Rectangle.NO_BORDER);
                tablaGastos.addCell(cTipo);

                PdfPCell cDesc = new PdfPCell(new Phrase(g.getDescripcion(), F_VALOR));
                cDesc.setBackgroundColor(fondo); cDesc.setPadding(7); cDesc.setBorder(Rectangle.NO_BORDER);
                tablaGastos.addCell(cDesc);

                PdfPCell cCosto = new PdfPCell(new Phrase(fmt.format(g.getCosto()), F_VALOR));
                cCosto.setBackgroundColor(fondo); cCosto.setPadding(7); cCosto.setBorder(Rectangle.NO_BORDER);
                cCosto.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tablaGastos.addCell(cCosto);

                totalGastos += g.getCosto();
                fila++;
            }
            doc.add(tablaGastos);

            // DESGLOSE POR TIPO
            doc.add(tituloSeccion("DESGLOSE POR TIPO DE GASTO"));
            PdfPTable tablaDesglose = new PdfPTable(2);
            tablaDesglose.setWidthPercentage(60);
            tablaDesglose.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablaDesglose.setSpacingAfter(10);

            for (GastoOperativo.TipoGasto tipo : GastoOperativo.TipoGasto.values()) {
                double subtotal = 0;
                for (GastoOperativo g : gastos) {
                    if (g.getTipo() == tipo) subtotal += g.getCosto();
                }
                if (subtotal > 0) {
                    PdfPCell cTipo = new PdfPCell(new Phrase(tipo.toString(), F_LABEL));
                    cTipo.setBackgroundColor(VERDE_CLARO); cTipo.setPadding(7); cTipo.setBorder(Rectangle.NO_BORDER);
                    tablaDesglose.addCell(cTipo);

                    PdfPCell cVal = new PdfPCell(new Phrase(fmt.format(subtotal), F_VALOR));
                    cVal.setBackgroundColor(BLANCO); cVal.setPadding(7); cVal.setBorder(Rectangle.NO_BORDER);
                    cVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tablaDesglose.addCell(cVal);
                }
            }
            doc.add(tablaDesglose);

            // TOTAL FINAL
            PdfPTable tablaTotal = new PdfPTable(2);
            tablaTotal.setWidthPercentage(50);
            tablaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotal.setSpacingAfter(10);

            PdfPCell cTotalL = new PdfPCell(new Phrase("TOTAL GASTOS:", F_TOTAL));
            cTotalL.setBackgroundColor(VERDE_OSCURO); cTotalL.setPadding(10); cTotalL.setBorder(Rectangle.NO_BORDER);
            tablaTotal.addCell(cTotalL);

            PdfPCell cTotalV = new PdfPCell(new Phrase(fmt.format(totalGastos), F_TOTAL_V));
            cTotalV.setBackgroundColor(VERDE_OSCURO); cTotalV.setPadding(10); cTotalV.setBorder(Rectangle.NO_BORDER);
            cTotalV.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotal.addCell(cTotalV);
            doc.add(tablaTotal);

            doc.close();
            System.out.println("Reporte generado: " + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }

    private static PdfPTable tituloSeccion(String texto) throws DocumentException {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(8);
        t.setSpacingAfter(4);
        PdfPCell c = new PdfPCell(new Phrase(texto, F_SECCION));
        c.setBackgroundColor(VERDE_MEDIO); c.setPadding(7); c.setBorder(Rectangle.NO_BORDER);
        t.addCell(c);
        return t;
    }

    private static void agregarPar(PdfPTable tabla, String label, String valor) {
        PdfPCell cL = new PdfPCell(new Phrase(label, F_LABEL));
        cL.setBackgroundColor(VERDE_CLARO); cL.setPadding(7); cL.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(cL);

        PdfPCell cV = new PdfPCell(new Phrase(valor != null ? valor : "-", F_VALOR));
        cV.setBackgroundColor(BLANCO); cV.setPadding(7); cV.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(cV);
    }

    static class PiePagina extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font fPie = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC, new BaseColor(120, 120, 120));
            Phrase pie = new Phrase("MAQUINARIA & ALQUILER  |  Reporte de Gastos  |  Página "
                + writer.getPageNumber(), fPie);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pie,
                (document.left() + document.right()) / 2, document.bottom() - 10, 0);
        }
    }
}


