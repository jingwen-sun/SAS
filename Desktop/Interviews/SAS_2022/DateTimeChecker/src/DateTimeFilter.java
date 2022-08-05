import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
public class DateTimeFilter {
	/**
	 * Read in a file of date-time values, check if they are valid, 
	 * only write unique valid date-time values to a separate file
	 * @param inputPath
	 * @param outputPath
	 */
	public void checkDateTime(String inputPath, String outputPath) {
			//File inputFile = new File(inputPath);			
			FileReader fr = null;
			BufferedReader br = null;
			// we only want unique date-time values
			// use set's property for this purpose
			Set<String> uniqueTime = new HashSet<String>();
			
			// open and read a file
			try {
				fr = new FileReader(inputPath);
				br = new BufferedReader(fr);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// read one line at a time and iterate thru the whole file
			try {
				String line = br.readLine();
				while (line != null) {
					// System.out.println(line.length());
					// call helper function to check if this date-time value is valid
					if (isValidDateTime(line)) {
						// add to HashSet only if this date-time value is in valid format
						uniqueTime.add(line);
					}
					line = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// close FileReader and BufferedReader to release resource
				try {
					fr.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//File outputFile = new File(outputPath);
			FileWriter fw = null;
			BufferedWriter bw = null;
			
			try {
				fw = new FileWriter(outputPath);
				bw = new BufferedWriter(fw);
				// write out unique date-time values to the output file
				for (String st : uniqueTime) {
					bw.write(st);
					bw.write("\n");
				}
				// force any data left in the memory to be written to the file
				fw.flush();
				bw.flush();
				// close FileWriter and BufferedWriter to release resource
				fw.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	}
	
	/**
	 * private helper function, check if the date-time value is of correct ISO 8601 format
	 * @param line
	 * @return true if the input date time format is valid
	 * 		false otherwise
	 */
	private boolean isValidDateTime(String line) {
		// check length of input first
		if (line.length() != 25 && line.length() != 20) {
			// YYYY-MM-DDThh:mm:ss+/-hh:mm has length 25
			// YYYY-MM-DDThh:mm:ssZ has length 20
			return false;
		} 
		// check the first 19 characters (no TZD)
		char[] arr = line.toCharArray();
		for (int i = 0; i < 19; i++) {
			if (i == 4 || i == 7) {
				if (arr[i] != '-') {
				return false;
				}
			} else if (i == 10) {
				if (arr[i] != 'T') {
					return false;
				}
			} else if (i == 13 || i == 16) {
				if (arr[i] != ':') {
					return false;
				}
			} else {
				if (!Character.isDigit(arr[i])) {
					return false;
				}
			}
		}
		// when TZD == "Z" for GMT
		if (arr.length == 20) {
			if (arr[arr.length - 1] != 'Z') {
				return false;
			}
		} else {
			// TZD is in +hh:mm or -hh:mm format
			for (int i = 19; i < arr.length; i++) {
				if (i == 19) {
					if (arr[i] != '+' && arr[i] != '-') {
						return false;
					}
				} else if (i == 22) {
					if (arr[i] != ':') {
						return false;
					}
				} else {
					if (!Character.isDigit(arr[i])) {
						return false;
					}
				}
			}
		}
		return true;
	}
		
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		DateTimeFilter dt1 = new DateTimeFilter();
		dt1.checkDateTime("TestData/testInput.txt", "TestData/testOutput.txt");
	}
}
