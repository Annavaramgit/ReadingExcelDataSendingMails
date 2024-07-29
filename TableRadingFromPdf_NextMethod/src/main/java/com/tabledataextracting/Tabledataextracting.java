package com.tabledataextracting;

import java.util.*;
import com.aspose.pdf.*;

public class Tabledataextracting {
    public static void main(String[] args) {
        // Load source PDF document        
        String filePath = "C:\\Users\\Sreenivas Bandaru\\Documents\\Intellije_workspace\\res.pdf";
        Document pdfDocument = new Document(filePath);

        // This Loop is for no of pages 
        for (int pageIndex = 1; pageIndex <= pdfDocument.getPages().size(); pageIndex++) {
        	
            Page page = pdfDocument.getPages().get_Item(pageIndex);
            //TableAbsorber used for extract tables 
           TableAbsorber absorber = new TableAbsorber();

            // Visit method present inside for  extract tables
            absorber.visit(page);
            
           //this list of list used for store table data of current page
            //the inner lsit used for to represent rows of that table
            List<List<String>> tableData = new ArrayList<>();

            // this loop is for all the tables in the current page
            for (AbsorbedTable table : absorber.getTableList()) {
            	//this list of list for inside table no.of rows will be there for that purpose 
            	//the inner loop is repersent cell in the each row
                List<List<String>> rows = new ArrayList<>(); 
                
                // this loop is for to iterate each row in the table
                for (AbsorbedRow row : table.getRowList()) {
                	//this list is for to  store all cells of row
                    List<String> cells = new ArrayList<>(); 
                    
                    // Iterate over the cells in the current row
                    for (AbsorbedCell cell : row.getCellList()) {
                        StringBuilder cellText = new StringBuilder();
                        String v=String.valueOf(cell);
                      
                     
                        for (TextFragment fragment : cell.getTextFragments()) {
                         
                                cellText.append(fragment.getText());
                          
                        }
                        
                        cells.add(cellText.toString()); // Add cell text to cells list
                    }
                    
                    rows.add(cells); // Add row to rows list
                }
                
                tableData.addAll(rows); // Add rows to tableData list
            }

            // Print table data for the current page
           // System.out.println("Table data for Page " + pageIndex + ":");
            tableData.forEach(row -> {
                row.forEach(cell -> {
                    System.out.print(cell + "         "); // Print cell with separator
                });
              System.out.println(" "); // Newline after row
            });
            System.out.println(); // Newline after all tables on the current page
        }
    }
}
