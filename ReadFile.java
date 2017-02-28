package taf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	private String path;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		// consider defining this outside of the function?
		String filePath = "C:/Program Files/HP/QuickTest Professional/Tests/config/winrunner pack run ini.txt";
		
		try{
			ReadFile file = new ReadFile(filePath);
			String[] fileOut = file.OpenFile();
			
			int fileLineCount = fileOut.length;
			int line;
			for (line = 0; line < fileLineCount; line++){
				System.out.println(fileOut[line]);
			}
			
			//System.out.println(fileOut);
		}
		catch (IOException e){
			System.out.println( e.getMessage());
		}
		
		
		//String[] fileOutputArray = openFile(filePath);
		
	}

	
	public ReadFile(String filePath){
		path = filePath;
	}

	public String[] OpenFile() throws IOException{ // opens AND reads, loads into array.
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readLines();
		System.out.println("number of lines in file = " + numberOfLines);
		
		String[] fileStoreArray = new String[numberOfLines];
		
		int line;
		for (line = 0; line < numberOfLines; line++){
			fileStoreArray[line] = textReader.readLine();
			
		}
		
		textReader.close();
		return fileStoreArray;
		
	}
	
	int readLines() throws IOException{
		FileReader fileToRead = new FileReader(path);
		BufferedReader bf = new BufferedReader(fileToRead);
		
		String aLine;
		int numberOfLines = 0;
		
		while ((aLine = bf.readLine()) != null){
			numberOfLines++;
		}
		
		bf.close();
		return numberOfLines;
		
	}
	
}