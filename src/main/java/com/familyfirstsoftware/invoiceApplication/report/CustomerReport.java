package com.familyfirstsoftware.invoiceApplication.report;

import com.familyfirstsoftware.invoiceApplication.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

// TODO : add their picture onto the report
@Slf4j
public class CustomerReport {

    public static final String DATE_FORMATER = "yyyy-MM-dd HH:mm:ss";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Customer> customers;

    private static String[] HEADERS = {
            "ID",
            "Name",
            "Email",
            "Type",
            "Status",
            "Address",
            "Phone",
            "Created At"
    };

    public CustomerReport(List<Customer> customers) {
        this.customers = customers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Customers");
        setHeaders();

    }

    private void setHeaders() {
        log.info("Creating header");
        Row headerRow = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        // loop through the headers
        IntStream.range(0, HEADERS.length).forEach(index -> {
            headerRow.createCell(index).setCellValue(HEADERS[index]);
            headerRow.getCell(index).setCellStyle(style);

            /*Cell cell = headerRow.getCell(index);
            cell.setCellValue(HEADERS[index]);
            cell.setCellStyle(style);
            */

        });
    }
    public InputStreamResource export(){
        return generateReport();
    }

    private InputStreamResource generateReport() {
        log.info("Generating report");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(10);
            style.setFont(font);
            int rowIndex = 1;
            for(Customer customer : customers){
                Row headerRow = sheet.createRow(rowIndex++);
                headerRow.createCell(0).setCellValue(customer.getId());
                headerRow.createCell(1).setCellValue(customer.getName());
                headerRow.createCell(2).setCellValue(customer.getEmail());
                headerRow.createCell(3).setCellValue(customer.getType());
                headerRow.createCell(4).setCellValue(customer.getStatus());
                headerRow.createCell(5).setCellValue(customer.getAddress());
                headerRow.createCell(6).setCellValue(customer.getPhone());
                // TODO : check to see if this is null;
                headerRow.createCell(7).setCellValue(DateFormatUtils.format(customer.getCreatedAt(), DATE_FORMATER).toString());

            }
            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (IOException e) {
            log.error("An error occurred while generating the report");
            throw new RuntimeException("An error occurred while generating the report");

        }
    }
}
