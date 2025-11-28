package co.edu.uv.vinzstock.service;

import co.edu.uv.vinzstock.dto.ProductoConInventarioDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfService {

    public byte[] generarReporteInventario(List<ProductoConInventarioDTO> productos) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reporte de Inventario - VinzStock", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Fecha
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            Paragraph fechaParrafo = new Paragraph("Fecha de generacion: " + fecha);
            fechaParrafo.setAlignment(Element.ALIGN_RIGHT);
            fechaParrafo.setSpacingAfter(20);
            document.add(fechaParrafo);
            
            // Tabla
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Ancho de columnas
            float[] columnWidths = {1f, 3f, 4f, 2f, 1.5f, 2f, 2f};
            table.setWidths(columnWidths);
            
            // Headers
            addTableHeader(table, "ID");
            addTableHeader(table, "Nombre");
            addTableHeader(table, "Descripcion");
            addTableHeader(table, "Precio");
            addTableHeader(table, "IVA");
            addTableHeader(table, "Precio Final");
            addTableHeader(table, "Stock");
            
            // Datos
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            
            for (ProductoConInventarioDTO producto : productos) {
                addTableCell(table, String.valueOf(producto.getId()));
                addTableCell(table, producto.getNombre());
                addTableCell(table, producto.getDescripcion());
                addTableCell(table, currencyFormat.format(producto.getPrecioVenta()));
                addTableCell(table, producto.getIva() + "%");
                addTableCell(table, currencyFormat.format(producto.getPrecioFinal()));
                addTableCell(table, String.valueOf(producto.getCantidad()));
            }
            
            document.add(table);
            
            // Resumen
            Paragraph resumen = new Paragraph("\nTotal de productos: " + productos.size());
            resumen.setSpacingBefore(20);
            document.add(resumen);
            
            long totalStock = productos.stream().mapToLong(ProductoConInventarioDTO::getCantidad).sum();
            document.add(new Paragraph("Stock total: " + totalStock + " unidades"));
            
            long productosBajos = productos.stream().filter(p -> p.getCantidad() <= 5).count();
            if (productosBajos > 0) {
                Paragraph alerta = new Paragraph("\nProductos con stock bajo (menor o igual a 5): " + productosBajos);
                alerta.getFont().setColor(BaseColor.RED);
                document.add(alerta);
            }
            
            document.close();
            
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }
    
    private void addTableHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorderWidth(2);
        cell.setPhrase(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }
    
    private void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 9)));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}