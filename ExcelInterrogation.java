package taf;
//import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
//import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sourceforge.htmlunit.corejs.javascript.Evaluator;

import org.apache.poi.*;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.usermodel.HSSFWorkbook;

//import com.jniwrapper.win32.excel.*;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;

//import jexcel.*;


public class ExcelInterrogation {
	static InputStream inp;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//classLoaderCheck();
		
	}

	
//	public static void classLoaderCheck(){
//		ClassLoader classloader = 
//				org.apache.poi.poifs.filesystem.POIFSFileSystem.class.getClassLoader();
//		URL res classloader.getResource("org/apache/poi/poifs/filesystem/POIFFileSystem.class");
//		String path = res.getPath();
//		System.out.println("Core POI came from " + path);
//		
//	}
//	
	
//public static String readValFromExcel(int x, int y){
	
	public static String generateV11(){
		String val = null;
		
		// open workbook
		
		String V11Filename = ActorLibrary.containerFilespace + "VRN Generator.xls";
		System.out.println("V11 calc spreadsheet string = " + V11Filename);
		
		Workbook workbookObj = openWorkbook(V11Filename);
		// set cell values
		val = ExcelInterrogation.readValFromExcel(workbookObj,"L2"); // get existing v11
		System.out.println(val);
		
		String monthDigits = ActorLibrary.scenarioInfoContainer.get("CALC_CURRENT_MONTH_MM");
		setValInExcel(workbookObj, "B2", monthDigits); // CALC_CURRENT_MONTH_MM
		
		String yearDigits = ActorLibrary.scenarioInfoContainer.get("CALC_CURRENT_YEAR_YYYY");
		setValInExcel(workbookObj, "C2", yearDigits); // CALC_CURRENT_YEAR_YYYY
		setValInExcel(workbookObj, "E2", ActorLibrary.scenarioInfoContainer.get("VRM")); //VRM
		
		val = ExcelInterrogation.readValFromExcel(workbookObj,"L2"); // read new v11
		System.out.println(val);
		
		val = apacheCellValClearup(val);
		
		ActorLibrary.scenarioInfoContainer.put("V11",val);
		
		closeXLSheet(inp); // close.
		
		return val;
		
	}
	
	public static Workbook openWorkbook(String fileName){
		
		Workbook wb = null;
		
		try {
			
			//System.out.println("excel read: try");
				
			inp = new FileInputStream(fileName);
			//HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
			//String xlsFile = "C:/wr_code/VRN Generator.xls";
			wb = WorkbookFactory.create(inp);			
			//wb.Close;
			
			} 
			catch (FileNotFoundException e)
			{
				System.out.println(e);
				//return wb;
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
			catch (InvalidFormatException e)
			{
				System.out.println(e);
			}

		
		return wb;
		
	}
	
	public static void closeXLSheet(InputStream inp){
		
		try {
		inp.close();
		} 
		catch (FileNotFoundException e)
		{
			System.out.println(e);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
//		catch (InvalidFormatException e)
//		{
//			System.out.println(e);
//		}
		
		
	}
	
	public static String readValFromExcel(Workbook workbook, String cellRef){
		String excelVal = "nothing";
		
		System.out.println("excel read: start");
		
		CellReference cellReference = new CellReference(cellRef);
		
		//try {
		//System.out.println("excel read: try");
			
		Sheet sheet = workbook.getSheetAt(0);
		//System.out.println(sheet);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		
		Row row = sheet.getRow(cellReference.getRow());
		//System.out.println(row);
		
		Cell cell = row.getCell(cellReference.getCol());
		//System.out.println(cell);
		
		CellValue cellValue = evaluator.evaluate(cell);
				
		if (cellValue != null){
			//System.out.println(cell.toString());
				
			excelVal = cellValue.toString();
			
			//switch (cell.getCellType()){
			
			//}
		
		}
		
		//excelVal = cell;
//		} catch (InvalidFormatException e)
//		{
//			System.out.println(e);
//		}
		
		
		return excelVal;
	}

	public static void setValInExcel(Workbook workbook, String cellRef, String val){
		//String excelVal = "nothing";
		
		System.out.println("excel write: start");
		System.out.println("excel write: " + val);
		
		
		CellReference cellReference = new CellReference(cellRef);
		
		
		Sheet sheet = workbook.getSheetAt(0);
		//System.out.println(sheet);
		//FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		
		Row row = sheet.getRow(cellReference.getRow());
		//System.out.println(row);
		
		Cell cell = row.getCell(cellReference.getCol());
		cell.setCellValue(val);
		//System.out.println(cell);
		
		
		
		//excelVal = cell;
		
		//return excelVal;
	}

	public static String apacheCellValClearup(String CellVal){ // get rid of the apache gumpf that is included with the
		//cell value.
		String cleanedVal;
		
		cleanedVal = CellVal.replace("org.apache.poi.ss.usermodel.CellValue [","");
		cleanedVal = cleanedVal.replace("]","");
		cleanedVal = cleanedVal.replace("\"","");
		
		
		return cleanedVal;
	}
	

}