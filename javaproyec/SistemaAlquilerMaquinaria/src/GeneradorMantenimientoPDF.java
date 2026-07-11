import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

public class GeneradorMantenimientoPDF {

    private static BaseColor GRIS_CLARO = new BaseColor(238, 233, 225);
    private static BaseColor BLANCO = BaseColor.WHITE;

    // Paleta tierra/industrial, distinta por tipo de mantenimiento
    private static BaseColor obtenerColorPrincipal(Mantenimiento.TipoMantenimiento tipo) {
        switch (tipo) {
            case PREVENTIVO:             return new BaseColor(90, 74, 58);    // marrón oscuro
            case CORRECTIVO:             return new BaseColor(139, 69, 19);   // óxido/marrón rojizo
            case CAMBIO_ACEITE_FILTROS:  return new BaseColor(85, 87, 83);    // gris grafito
            case CAMBIO_LLANTAS:         return new BaseColor(74, 60, 45);    // marrón tabaco
            case COMPRA_REPUESTOS:       return new BaseColor(112, 91, 64);  // caqui oscuro
            case REPARACION:             return new BaseColor(120, 47, 20);   // óxido intenso
            default:                     return new BaseColor(90, 74, 58);
        }
    }

    private static BaseColor obtenerColorSecundario(Mantenimiento.TipoMantenimiento tipo) {
        switch (tipo) {
            case PREVENTIVO:             return new BaseColor(140, 118, 93);
            case CORRECTIVO:             return new BaseColor(184, 115, 51);
            case CAMBIO_ACEITE_FILTROS:  return new BaseColor(130, 132, 128);
            case CAMBIO_LLANTAS:         return new BaseColor(120, 100, 78);
            case COMPRA_REPUESTOS:       return new BaseColor(163, 137, 98);
            case REPARACION:             return new BaseColor(178, 90, 51);
            default:                     return new BaseColor(140, 118, 93);
        }
    }

    public static void generarReporte(Mantenimiento m, Maquina maquina) {
        Document documento = null;
        File carpeta = new File("Mantenimientos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        String nombreArchivo = "Mantenimientos" + File.separator
            + "Mantenimiento_" + m.getId() + "_" + maquina.getPlaca() + ".pdf";

        try {
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

            BaseColor colorPrincipal = obtenerColorPrincipal(m.getTipo());
            BaseColor colorSecundario = obtenerColorSecundario(m.getTipo());

            Font fuenteEmpresa = new Font(baseFont, 16, Font.BOLD, BLANCO);
            Font fuenteDatosEmpresa = new Font(baseFont, 9, Font.NORMAL, BLANCO);
            Font fuenteTitulo = new Font(baseFont, 14, Font.BOLD, BLANCO);
            Font fuenteFecha = new Font(baseFont, 10, Font.NORMAL, BLANCO);
            Font fuenteSeccion = new Font(baseFont, 12, Font.BOLD, BLANCO);
            Font fuenteNormal = new Font(baseFont, 10, Font.NORMAL);
            Font fuenteTotal = new Font(baseFont, 12, Font.BOLD, BLANCO);
            Font fuentePie = new Font(baseFont, 8, Font.ITALIC, colorSecundario);

            documento = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // ===== ENCABEZADO =====
            PdfPTable encabezado = new PdfPTable(3);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{20f, 40f, 40f});

            try {
                Image logo = Image.getInstance("logo.png");
                logo.scaleToFit(80, 80);
                PdfPCell celdaLogo = new PdfPCell(logo);
                celdaLogo.setBackgroundColor(colorPrincipal);
                celdaLogo.setBorder(Rectangle.NO_BORDER);
                celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaLogo.setPadding(5);
                encabezado.addCell(celdaLogo);
            } catch (Exception e) {
                PdfPCell celdaLogo = new PdfPCell(new Phrase(""));
                celdaLogo.setBackgroundColor(colorPrincipal);
                celdaLogo.setBorder(Rectangle.NO_BORDER);
                encabezado.addCell(celdaLogo);
            }

            PdfPCell celdaEmpresa = new PdfPCell();
            celdaEmpresa.setBackgroundColor(colorPrincipal);
            celdaEmpresa.setPadding(10);
            celdaEmpresa.setBorder(Rectangle.NO_BORDER);
            celdaEmpresa.addElement(new Phrase("MAQUINARIA & ALQUILER", fuenteEmpresa));
            celdaEmpresa.addElement(new Phrase("NIT: 123456789-0", fuenteDatosEmpresa));
            celdaEmpresa.addElement(new Phrase("Tel: 3217230697", fuenteDatosEmpresa));
            celdaEmpresa.addElement(new Phrase("Cartagena, Colombia", fuenteDatosEmpresa));
            encabezado.addCell(celdaEmpresa);

            PdfPCell celdaTitulo = new PdfPCell();
            celdaTitulo.setBackgroundColor(colorSecundario);
            celdaTitulo.setPadding(15);
            celdaTitulo.setBorder(Rectangle.NO_BORDER);
            celdaTitulo.addElement(new Phrase("ORDEN DE MANTENIMIENTO", fuenteTitulo));
            celdaTitulo.addElement(new Phrase("ID: " + m.getId(), fuenteFecha));
            celdaTitulo.addElement(new Phrase("Tipo: " + m.getTipo(), fuenteFecha));
            celdaTitulo.addElement(new Phrase("Fecha: " + m.getFechaInicio(), fuenteFecha));
            encabezado.addCell(celdaTitulo);

            documento.add(encabezado);
            documento.add(new Paragraph(" "));

            // ===== DATOS DE LA MÁQUINA =====
            PdfPTable tablaMaquina = new PdfPTable(1);
            tablaMaquina.setWidthPercentage(100);

            PdfPCell tituloMaquina = new PdfPCell(new Phrase("DATOS DE LA MÁQUINA", fuenteSeccion));
            tituloMaquina.setBackgroundColor(colorPrincipal);
            tituloMaquina.setPadding(8);
            tituloMaquina.setBorder(Rectangle.NO_BORDER);
            tablaMaquina.addCell(tituloMaquina);

            PdfPCell datosMaquina = new PdfPCell();
            datosMaquina.setBackgroundColor(GRIS_CLARO);
            datosMaquina.setPadding(10);
            datosMaquina.setBorder(Rectangle.NO_BORDER);
            datosMaquina.addElement(new Phrase("Marca: " + maquina.getMarca(), fuenteNormal));
            datosMaquina.addElement(new Phrase("Modelo: " + maquina.getModelo(), fuenteNormal));
            datosMaquina.addElement(new Phrase("Placa: " + maquina.getPlaca(), fuenteNormal));
            datosMaquina.addElement(new Phrase("Horas actuales: " + maquina.getHorasActuales(), fuenteNormal));
            tablaMaquina.addCell(datosMaquina);

            documento.add(tablaMaquina);
            documento.add(new Paragraph(" "));

            // ===== DETALLE DEL MANTENIMIENTO =====
            PdfPTable tablaDetalle = new PdfPTable(1);
            tablaDetalle.setWidthPercentage(100);

            PdfPCell tituloDetalle = new PdfPCell(new Phrase("DETALLE DEL MANTENIMIENTO", fuenteSeccion));
            tituloDetalle.setBackgroundColor(colorPrincipal);
            tituloDetalle.setPadding(8);
            tituloDetalle.setBorder(Rectangle.NO_BORDER);
            tablaDetalle.addCell(tituloDetalle);

            PdfPCell datosDetalle = new PdfPCell();
            datosDetalle.setBackgroundColor(GRIS_CLARO);
            datosDetalle.setPadding(10);
            datosDetalle.setBorder(Rectangle.NO_BORDER);
            datosDetalle.addElement(new Phrase("Tipo: " + m.getTipo(), fuenteNormal));
            datosDetalle.addElement(new Phrase("Descripción: " + m.getDescripcion(), fuenteNormal));
            datosDetalle.addElement(new Phrase("Técnico: " + m.getTecnico(), fuenteNormal));
            datosDetalle.addElement(new Phrase("Fecha inicio: " + m.getFechaInicio(), fuenteNormal));
            datosDetalle.addElement(new Phrase("Fecha fin: " + (m.getFechaFin() != null ? m.getFechaFin() : "En curso"), fuenteNormal));
            datosDetalle.addElement(new Phrase("Estado: " + (m.isActivo() ? "ACTIVO" : "FINALIZADO"), fuenteNormal));
            if (!m.getObservaciones().isEmpty()) {
                datosDetalle.addElement(new Phrase("Observaciones: " + m.getObservaciones(), fuenteNormal));
            }
            tablaDetalle.addCell(datosDetalle);

            documento.add(tablaDetalle);
            documento.add(new Paragraph(" "));

            // ===== COSTO =====
            NumberFormat formatoPesos = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            PdfPTable tablaCosto = new PdfPTable(2);
            tablaCosto.setWidthPercentage(50);
            tablaCosto.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell celdaLabel = new PdfPCell(new Phrase("COSTO TOTAL:", fuenteTotal));
            celdaLabel.setBackgroundColor(colorPrincipal);
            celdaLabel.setPadding(10);
            celdaLabel.setBorder(Rectangle.NO_BORDER);
            tablaCosto.addCell(celdaLabel);

            PdfPCell celdaValor = new PdfPCell(new Phrase(formatoPesos.format(m.getCosto()), fuenteTotal));
            celdaValor.setBackgroundColor(colorPrincipal);
            celdaValor.setPadding(10);
            celdaValor.setBorder(Rectangle.NO_BORDER);
            celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaCosto.addCell(celdaValor);

            documento.add(tablaCosto);
            documento.add(new Paragraph(" "));

            // ===== PIE DE PÁGINA =====
            Paragraph pie = new Paragraph("MAQUINARIA & ALQUILER - Registro generado el " + m.getFechaInicio(), fuentePie);
            pie.setAlignment(Element.ALIGN_CENTER);
            documento.add(pie);

            System.out.println("PDF de mantenimiento generado: " + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al generar PDF: " + e.getMessage());
        } finally {
            if (documento != null && documento.isOpen()) {
                documento.close();
            }
        }
    }
}