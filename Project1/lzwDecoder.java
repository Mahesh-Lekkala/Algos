import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author Maheswara Rao Lekkala
 * id : 801167702 
 *
 */
public class lzwDecoder {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// checking input arguments
				//check for length of arguments
				if(args.length<2) {
					System.out.println("Please provide all the valid input arguments");
					return ;
				}
				// check for type of input file
				if(!args[0].endsWith(".lzw")) {
					System.out.println("Please provide a valid lzw file as first input argument");
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
				
		
		String inputFileName=args[0]; //reading input file path from command line arguments
		String fileOPName=inputFileName.replace(".lzw", "_decoded.txt"); // creating output file path name
		
		FileOutputStream out = new FileOutputStream(fileOPName); 
		
		int max_table_size=(int) Math.pow(2, Integer.parseInt(args[1])); //bit_length is number of encoding bits 
		HashMap<Integer, String> table=new HashMap<Integer, String>(); // Table to store the dictionary values
		
		//initializing the 255 characters in the table
		for (int i = 0; i <=255 ; i++) {
					char c=(char) i;
					table.put(i, String.valueOf(c));
		}
		
		//Reader to read UTF-16BE encoded lzw file		  
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-16BE"));
		
		String val="";
		int samp;
		//Storing the encoded UTF-16BE values into String
		while ((samp= reader.read()) != -1) {
			val=val+(char)samp;
		}
		int code =val.charAt(0);
		String outputString = table.get(code); // Retrieving the first char from the encoded UTF-16BE lzw file
		
		//printing first char into the output text file 
		//(this is not inserted into the table, as this is a char and will be already present in the initialized 255 chars)
		out.write(outputString.getBytes());
		
		int i=1; // Incrementor for while loop. Initialized from 1 cause first char was already printed.
		int tablekeyInc=255; // Incrementor for table keys. Initialized from 255 as there were already 255 chars in table
		
		//loop for decoding the remaining characters
		while (i<val.length()) {
			String new_string;
			code=val.charAt(i++); //everytime i value was taken, it will be increamented too
			if(!table.containsKey(code)) {
				 new_string = outputString + outputString.charAt(0);
			}else {
				new_string=table.get(code);
			}
			//Printing the decoded values
			out.write(new_string.getBytes());
			if(table.size()<max_table_size) {
				table.put(++tablekeyInc, outputString+new_string.charAt(0));
			}
			outputString=new_string;
			
		}
		
		reader.close();
		out.close();
	}
	
	
	

}
