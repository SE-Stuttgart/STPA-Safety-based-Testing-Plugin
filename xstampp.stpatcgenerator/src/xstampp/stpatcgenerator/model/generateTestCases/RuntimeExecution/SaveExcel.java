package xstampp.stpatcgenerator.model.generateTestCases.RuntimeExecution;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import xstampp.stpatcgenerator.model.generateTestCases.TestCase;
import xstampp.stpatcgenerator.model.generateTestCases.TestSuite;
public class SaveExcel {
	public void toCSVFile( Map<String,Integer> map, String name){
	    //write to file : "fileone"
	    try{
	    File fileTwo=new File("STPAfrequency"+name+".csv");
	    
	    FileOutputStream fos=new FileOutputStream(fileTwo);
	        PrintWriter pw=new PrintWriter(fos);

	        for(Map.Entry<String,Integer> m :map.entrySet()){
	            pw.println(m.getKey()+","+m.getValue());
	        }

	        pw.flush();
	        pw.close();
	        fos.close();
	    }catch(Exception e){}
	    }
	    
	   public void writeCSVfile(JTable table, String name) throws IOException, ClassNotFoundException {
	        Writer writer = null;
	        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
	        int nRow = dtm.getRowCount();
	        int nCol = dtm.getColumnCount();
	        try {
	            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Alltestcases"+name+".txt"), "utf-8"));

	            //write the header information
	            StringBuffer bufferHeader = new StringBuffer();
	            for (int j = 0; j < nCol; j++) {
	                bufferHeader.append(dtm.getColumnName(j));
	                if (j!=nCol) bufferHeader.append(", ");
	            }
	            writer.write(bufferHeader.toString() + "\r\n");

	           //write row information
	            for (int i = 0 ; i < nRow ; i++){
	                 StringBuffer buffer = new StringBuffer();
	                for (int j = 0 ; j < nCol ; j++){
	                    buffer.append(dtm.getValueAt(i,j));
	                    if (j!=nCol) buffer.append(", ");
	                }
	                writer.write(buffer.toString() + "\r\n");
	            }
	        } finally {
	              writer.close();
	        }
	    }
	   
	    
	    public void toCSVFile( JTable table, String name){
	    //write to file : "fileone"
	    try{
	    File fileTwo=new File("Alltestcases"+name+".csv");
	    
	    FileOutputStream fos=new FileOutputStream(fileTwo);
	        PrintWriter pw=new PrintWriter(fos);

	         TableModel model = table.getModel(); //Table model

	        String str="";
	        for (int headings = 0; headings < model.getColumnCount(); headings++) { //For each column
	             str +=model.getColumnName(headings)+ ";";//Write column name
	        }
	       pw.println(str);
	        pw.println("\n");
	        for (int rows = 0; rows < model.getRowCount(); rows++) { //For each table row
	          String data="";  
	         
	            for (int cols = 0; cols < table.getColumnCount(); cols++) { //For each table column
	              data+= model.getValueAt(rows, cols).toString()+ ";"; //Write value
	            }
	            pw.println("\n");
	            pw.println(data);
	        }

	        pw.flush();
	        pw.close();
	        fos.close();
	    }catch(Exception e){}
	    }
	    public  String toCSVString(Map<String, Integer> frequency) {
	        StringBuilder csv = new StringBuilder(512);

	        // write each phoneme line
	        for (String entry : frequency.keySet()) {

	            csv.append(System.lineSeparator());
	            csv.append(entry);

	            Integer value = frequency.get(entry).intValue();
	            csv.append(",");
	            if (value != null) {
	                csv.append((value / 100d) + "%");
	            }
	        }

	        return csv.toString();
	    }

	    public void exportTable(JTable table, File file) throws IOException {
	        TableModel model = table.getModel();
	        FileWriter out = new FileWriter(file);

	        for (int i = 0; i < model.getColumnCount(); i++) {
	            out.write(model.getColumnName(i) + "\t");
	        }
	        out.write("\n");
	        for (int i = 0; i < model.getRowCount(); i++) {
	            for (int j = 0; j < model.getColumnCount(); j++) {
	//   I added this check for the ISBN conversion
	                if (j == 0) {
//	    the book Title
	                    out.write(model.getValueAt(i, j).toString() + "\t");
	                }
	            }

	        }
	    }

	    public void toExcel(JTable table, File file) {
	        try {
	            TableModel model = table.getModel();
	            FileWriter excel = new FileWriter(file);

	            for (int i = 0; i < model.getColumnCount(); i++) {
	                excel.write(model.getColumnName(i) + "\t");
	            }

	            excel.write("\n");

	            for (int i = 0; i < model.getRowCount(); i++) {
	                for (int j = 0; j < model.getColumnCount(); j++) {
	                    excel.write(model.getValueAt(i, j).toString() + "\t");
	                }
	                excel.write("\n");
	            }

	            excel.close();

	        } catch (IOException e) {
	            System.out.println(e);
	        }
	    }

	    
	    
	    public void writeToExcell(JTable table, String path) throws FileNotFoundException, IOException {
	        new WorkbookFactory();
	        Workbook wb = new XSSFWorkbook(); //Excell workbook
	        Sheet sheet = wb.createSheet(); //WorkSheet
	        Row row = sheet.createRow(2); //Row created at line 3
	        TableModel model = table.getModel(); //Table model

	        Row headerRow = sheet.createRow(0); //Create row at line 0
	        for (int headings = 0; headings < model.getColumnCount(); headings++) { //For each column
	            headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
	        }

	        for (int rows = 0; rows < model.getRowCount(); rows++) { //For each table row
	            for (int cols = 0; cols < table.getColumnCount(); cols++) { //For each table column
	                row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
	            }

	            //Set the row to the next one in the sequence 
	            row = sheet.createRow((rows + 3));

	        }
	        wb.write(new FileOutputStream(toString()));//Save the file     
	    }

	    public String createExecl(TreeSet<TestSuite> treeSuites) {
	        String result = "";
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("Test Cases sheet");
	        Map<String, Object[]> data = new HashMap<String, Object[]>();

	        for (TestSuite ts : treeSuites) {
	            for (TestCase tc : ts.getTestCase()) {
	                data.put(String.valueOf(ts.getId()), new Object[]{tc.getId(), tc.getPreConditions(), tc.getPostConditions(), ""});
	            }

	        }

	        data.put("-", new Object[]{"Test Suite No.", "TestCase No.", "Pre-Conditions and Actions", "Post-Conditions", "Results"});

	        Set<String> keyset = data.keySet();
	        int rownum = 0;
	        for (String key : keyset) {
	            Row row = sheet.createRow(rownum++);
	            Object[] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr) {
	                Cell cell = row.createCell(cellnum++);
	                if (obj instanceof Date) {
	                    cell.setCellValue((Date) obj);
	                } else if (obj instanceof Boolean) {
	                    cell.setCellValue((Boolean) obj);
	                } else if (obj instanceof String) {
	                    cell.setCellValue((String) obj);
	                } else if (obj instanceof Double) {
	                    cell.setCellValue((Double) obj);
	                }
	            }
	        }

	        try {
	            FileOutputStream out
	                    = new FileOutputStream(new File("testcases.xls"));
	            workbook.write(out);
	            out.close();
	            result = "Excel written successfully..";

	        } catch (FileNotFoundException e) {
	            result = "File Not Found Exception";
	            e.printStackTrace();
	        } catch (IOException e) {
	            result = "Tool can not create an excel sheets of test cases ";
	            e.printStackTrace();
	        }
	        return result;
	    }
}
