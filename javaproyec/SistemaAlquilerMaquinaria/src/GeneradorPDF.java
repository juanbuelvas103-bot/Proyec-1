
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

public class GeneradorPDF {

    // Colores principales
    private static BaseColor AZUL_OSCURO = new BaseColor(0, 51, 102);
    private static BaseColor AZUL_MEDIO = new BaseColor(51, 102, 153);
    private static BaseColor GRIS_CLARO = new BaseColor(242, 242, 242);
    private static BaseColor BLANCO = BaseColor.WHITE;

    public static void generarFactura(Factura factura) {
        try {
            File carpeta = new File("Facturas");
if (!carpeta.exists()) {
    carpeta.mkdirs();
}
String nombreArchivo = "Facturas/Factura_"
    + String.format("%03d", factura.getNumeroFactura())
    + "_" + factura.getAlquiler().getCliente().getNombre().replace(" ", "_")
    + ".pdf";
Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
documento.open();
            


// ===== ENCABEZADO =====
PdfPTable encabezado = new PdfPTable(3);
encabezado.setWidthPercentage(100);
encabezado.setWidths(new float[]{20f, 40f, 40f});

// Celda logo
Image logo = Image.getInstance("logo.png");
logo.scaleToFit(80, 80);
PdfPCell celdaLogo = new PdfPCell(logo);
celdaLogo.setBackgroundColor(AZUL_OSCURO);
celdaLogo.setBorder(Rectangle.NO_BORDER);
celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
celdaLogo.setPadding(5);
encabezado.addCell(celdaLogo);

// Celda nombre empresa y datos
Font fuenteEmpresa = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BLANCO);
Font fuenteDatosEmpresa = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BLANCO);
PdfPCell celdaEmpresa = new PdfPCell();
celdaEmpresa.setBackgroundColor(AZUL_OSCURO);
celdaEmpresa.setPadding(10);
celdaEmpresa.setBorder(Rectangle.NO_BORDER);
celdaEmpresa.addElement(new Phrase("MAQUINARIA & ALQUILER", fuenteEmpresa));
celdaEmpresa.addElement(new Phrase("NIT: 123456789-0", fuenteDatosEmpresa));
celdaEmpresa.addElement(new Phrase("Tel: 3217230697", fuenteDatosEmpresa));
celdaEmpresa.addElement(new Phrase("Cartagena, Colombia", fuenteDatosEmpresa));
encabezado.addCell(celdaEmpresa);

// Celda número de factura
Font fuenteFactura = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BLANCO);
Font fuenteFecha = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BLANCO);
PdfPCell celdaFactura = new PdfPCell();
celdaFactura.setBackgroundColor(AZUL_MEDIO);
celdaFactura.setPadding(15);
celdaFactura.setBorder(Rectangle.NO_BORDER);
celdaFactura.setHorizontalAlignment(Element.ALIGN_RIGHT);
celdaFactura.addElement(new Phrase("FACTURA N° " + String.format("%03d", factura.getNumeroFactura()), fuenteFactura));
celdaFactura.addElement(new Phrase("Fecha: " + factura.getFecha(), fuenteFecha));
celdaFactura.addElement(new Phrase("Método de pago: " + factura.getMetodoPago(), fuenteFecha));
encabezado.addCell(celdaFactura);

documento.add(encabezado);
documento.add(new Paragraph(" "));
            

            
            // ===== DATOS DEL CLIENTE =====
            Font fuenteSeccion = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BLANCO);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            

            PdfPTable tablaCliente = new PdfPTable(1);
            tablaCliente.setWidthPercentage(100);

            PdfPCell tituloCliente = new PdfPCell(new Phrase("DATOS DEL CLIENTE", fuenteSeccion));
            tituloCliente.setBackgroundColor(AZUL_OSCURO);
            tituloCliente.setPadding(8);
            tituloCliente.setBorder(Rectangle.NO_BORDER);
            tablaCliente.addCell(tituloCliente);

            PdfPCell datosCliente = new PdfPCell();
            datosCliente.setBackgroundColor(GRIS_CLARO);
            datosCliente.setPadding(10);
            datosCliente.setBorder(Rectangle.NO_BORDER);
            datosCliente.addElement(new Phrase("Nombre: " + factura.getAlquiler().getCliente().getNombre(), fuenteNormal));
            datosCliente.addElement(new Phrase("Identificación: " + factura.getAlquiler().getCliente().getIdentificacion(), fuenteNormal));
            datosCliente.addElement(new Phrase("Teléfono: " + factura.getAlquiler().getCliente().getTelefono(), fuenteNormal));
            datosCliente.addElement(new Phrase("Dirección: " + factura.getAlquiler().getCliente().getDireccion(), fuenteNormal));
            datosCliente.addElement(new Phrase("Obra: " + factura.getAlquiler().getCliente().getNombreDeLaObra(), fuenteNormal));
            tablaCliente.addCell(datosCliente);

            documento.add(tablaCliente);
            documento.add(new Paragraph(" "));

            // ===== DETALLE DEL ALQUILER =====
            PdfPTable tablaDetalle = new PdfPTable(4);
            tablaDetalle.setWidthPercentage(100);
            tablaDetalle.setWidths(new float[]{40f, 20f, 20f, 20f});

            // Encabezados de la tabla
            String[] encabezados = {"DESCRIPCIÓN", "TARIFA", "CANTIDAD", "TOTAL"};
            for (String enc : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(enc, fuenteSeccion));
                celda.setBackgroundColor(AZUL_MEDIO);
                celda.setPadding(8);
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaDetalle.addCell(celda);
            }

            // Fila de datos
            String descripcion = "Alquiler " + factura.getAlquiler().getMaquina().getMarca()
                + " " + factura.getAlquiler().getMaquina().getModelo()
                + " - Placa: " + factura.getAlquiler().getMaquina().getPlaca();

            PdfPCell celdaDesc = new PdfPCell(new Phrase(descripcion, fuenteNormal));
            celdaDesc.setBackgroundColor(GRIS_CLARO);
            celdaDesc.setPadding(8);
            celdaDesc.setBorder(Rectangle.NO_BORDER);
            tablaDetalle.addCell(celdaDesc);

            PdfPCell celdaTarifa = new PdfPCell(new Phrase(factura.getAlquiler().getTipoTarifa(), fuenteNormal));
            celdaTarifa.setBackgroundColor(GRIS_CLARO);
            celdaTarifa.setPadding(8);
            celdaTarifa.setBorder(Rectangle.NO_BORDER);
            celdaTarifa.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaDetalle.addCell(celdaTarifa);

            PdfPCell celdaCantidad = new PdfPCell(new Phrase(String.valueOf(factura.getAlquiler().getCantidad()), fuenteNormal));
            celdaCantidad.setBackgroundColor(GRIS_CLARO);
            celdaCantidad.setPadding(8);
            celdaCantidad.setBorder(Rectangle.NO_BORDER);
            celdaCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaDetalle.addCell(celdaCantidad);

            NumberFormat formatoPesos = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            PdfPCell celdaTotal = new PdfPCell(new Phrase(formatoPesos.format(factura.getSubTotal()), fuenteNormal));
            celdaTotal.setBackgroundColor(GRIS_CLARO);
            celdaTotal.setPadding(8);
            celdaTotal.setBorder(Rectangle.NO_BORDER);
            celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaDetalle.addCell(celdaTotal);

            documento.add(tablaDetalle);
            documento.add(new Paragraph(" "));

            // ===== TOTALES =====
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(50);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);

            agregarFilaTotal(tablaTotales, "Subtotal:", formatoPesos.format(factura.getSubTotal()), GRIS_CLARO, fuenteNormal);
            agregarFilaTotal(tablaTotales, "IVA " + (int)(factura.getIva() * 100) + "%:", formatoPesos.format(factura.getSubTotal() * factura.getIva()), GRIS_CLARO, fuenteNormal);

            Font fuenteTotal = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BLANCO);
            agregarFilaTotal(tablaTotales, "TOTAL:", formatoPesos.format(factura.getTotalConIva()), AZUL_OSCURO, fuenteTotal);

            documento.add(tablaTotales);
            documento.add(new Paragraph(" "));

            // ===== OBSERVACIONES =====
            if (!factura.getObservaciones().isEmpty()) {
                Font fuenteObs = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                Paragraph obs = new Paragraph("Observaciones: " + factura.getObservaciones(), fuenteObs);
                documento.add(obs);
            }

            // ===== PIE DE PÁGINA =====
            documento.add(new Paragraph(" "));
            Font fuentePie = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, AZUL_MEDIO);
            Paragraph pie = new Paragraph("Gracias por confiar en MAQUINARIA & ALQUILER", fuentePie);
            pie.setAlignment(Element.ALIGN_CENTER);
            documento.add(pie);

            documento.close();
            System.out.println("✅ Factura PDF generada: " + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al generar PDF: " + e.getMessage());
        }
    }

    // Método auxiliar para filas de totales
    private static void agregarFilaTotal(PdfPTable tabla, String label, String valor, BaseColor color, Font fuente) {
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
