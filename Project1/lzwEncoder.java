import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;



 /**
 * @author Maheswara Rao Lekkala
 * id : 801167702 
 *
 */
public class lzwEncoder {
	
	public static void main(String[] args) throws IOException {
		// checking input arguments
		//check for length of arguments
		if(args.length<2) {
			System.out.println("Please provide all the valid input arguments");
			return ;
		}
		// check for type of input file
		if(!args[0].endsWith(".txt")) {
			System.out.println("Please provide a valid text file as first input argument");
			return ;
		}
		//check for Integer number
		try 
	        { 
	            Integer.parseInt(args[1]); 
	        }  
	        catch (NumberFormatException e)  
	        { 
	            System.out.println("Please provide a valid integer number as second input argument");
	            return;
	        } 
		
		String input = readFileAsString(args[0]); //reading input file path from command line arguments
		String fileOPName=args[0].replace(".txt", ".lzw"); // creating output file path name
		
		FileOutputStream outputStream = new FileOutputStream(fileOPName);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-16BE")); // Encoding into lzw file using UTF-16BE format
		
		int max_table_size=(int) Math.pow(2, Integer.parseInt(args[1]));//bit_length is number of encoding bits
		HashMap<Integer, String> table=new HashMap<Integer, String>(); // Table to store the dictionary values
		
		//initializing the 255 characters in the table
		for (int i = 0; i <=255 ; i++) { 
					char c=(char) i;
					table.put(i, String.valueOf(c));
		}
				  
		String string = null; 
		int i=0; // Incrementor for while loop. 
		int tablekeyInc=255; // incrementor for table keys. Initialized from 255 as there were already 255 chars in table.
		
		//loop for encoding the text files into encoder
		while (i<input.length()) {
			String SYMBOL = String.valueOf(input.charAt(i));
			String concat="";
			
			// Elimating first char in concat as string will be null
			if(null==string) {
				concat=SYMBOL;
			}else {
				concat=string+SYMBOL;
			}
			if(table.containsValue(concat)) {
				string = concat;
			}else {
					uploadingToNewFile(out,getkeyByValue(table,string));//printing the values in encoded UTF-16BE lzw file
				if(table.size()<max_table_size) {
					table.put(++tablekeyInc, string+SYMBOL); // assigning the new values in dictionary/table.
				}
				string=SYMBOL;
			}
			i++;
		}
		uploadingToNewFile(out,getkeyByValue(table,string)); //printing the values in encoded UTF-16BE lzw file
		out.close();
		
	}
	
	/**
	 * @param out
	 * @param integer
	 * @throws IOException
	 * @Description Method to print the values in encoded UTF-16BE lzw file
	 */
	private static void uploadingToNewFile(BufferedWriter out, Integer integer) throws IOException {
		out.write(integer.intValue());
	}
	
	/**
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 * @Description To read the text file, given from command line arguments
	 */
	public static String readFileAsString(String fileName) throws IOException 
	  { 
	    String data = ""; 
	    data = new String(Files.readAllBytes(Paths.get(fileName))); 
	    return data; 
	  }
	/**
	 * @param binaryString
	 * @return
	 * @Description To convert binary bit to the length of 16 bits (Extra)
	 */
	private static String convertto16Bit(String binaryString) {
		if(binaryString.length()<16) {
			StringBuffer sb=new StringBuffer();
			for (int i = 0; i < 16-binaryString.length(); i++) {
				sb.append("0");
			}
			binaryString=sb+binaryString;
		}
		return binaryString;
	}
	/**
	 * @param tABLE
	 * @param value
	 * @return
	 * @Description To get the key for the given value in the table 
	 */
	private static Integer getkeyByValue(HashMap<Integer, String> tABLE,String value) {
		for(Map.Entry entry: tABLE.entrySet()){
            if(value.equals(entry.getValue())){
                return (Integer) entry.getKey();
            }
        }
	    return 0;
	}

}
