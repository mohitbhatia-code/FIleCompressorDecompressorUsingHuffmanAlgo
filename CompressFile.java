import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class CompressFile {
	char[] inputFileContent;
	HashMap<Character,String> huffmanCodes;
	private String directoryPath;
	private HuffManNode huffManTree;

	public CompressFile(char[] inputFileContent, HashMap<Character, String> huffmanCodes,String path,HuffManNode huffmantree) {
		this.inputFileContent = inputFileContent;
		this.huffmanCodes = huffmanCodes;
		this.directoryPath=path;
		this.huffManTree = huffmantree;
	}


	public boolean compress() {

		String codes="";
		for(Character c:inputFileContent) {
			String code = huffmanCodes.get(c);  //huffman code corresponding to each character in input file
			codes+=code;
		}
		/*We can only 
		write whole number of 
		bytes (8 bits) in a file
		length of codes may not be the multiple of 8
		 */

		int byteArrayLength = (int)Math.ceil(codes.length()/8.0); /*Number
		of bytes which is to be a whole number
		 */
		
		int extra0BitsAdded = (byteArrayLength*8-codes.length());
		for(int i=codes.length();i<byteArrayLength*8;i++) {
			codes+=0;
		}

		//System.out.println(codes);


		byte[] byteArray = new byte[byteArrayLength+1];
		int i=0;
		for(int j=0;j<byteArrayLength;j++) {
			byteArray[j] =(byte) Integer.parseInt(codes.substring(i,i+8),2);  //Converting 8 bits into corresponding byte
			i=i+8;
		}

		byteArray[byteArrayLength] = (byte)extra0BitsAdded;
		/*Writing byteArray into the output file which is compressed i.e. having less number of bytes 
		than the number of bytes in input file*/

		try {
			FileOutputStream fos = new FileOutputStream(directoryPath+"/"+"encoded.txt");
			fos.write(byteArray);
			
			if (!write_tree_to_file()) {
				return false;
			}
		
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

		return false;
	}

	
	
	//Writing huffman tree into a file so that it can be used for decoding purpose
	public boolean write_tree_to_file() {
		String bitsRepresentation = bitsRepresentationOfHuffManTree(huffManTree);
		
		
		int tree_byte_array_length = (int)Math.ceil(bitsRepresentation.length()/8.0);
		

		int extra0bitsAdded = tree_byte_array_length*8-bitsRepresentation.length();
		for(int j=bitsRepresentation.length();j<tree_byte_array_length*8;j++) {
			bitsRepresentation+=0;
		}
		
		
		byte[] tree_byte_Array = new byte[tree_byte_array_length+1];
		
		int k=0;
		
		for(int j=0;j<tree_byte_array_length;j++) {
			tree_byte_Array[j] = (byte)Integer.parseInt(bitsRepresentation.substring(k,k+8),2);
			k+=8;
		}
		
		
		tree_byte_Array[tree_byte_array_length] = (byte)extra0bitsAdded;
		
		try {
			FileOutputStream fos = new FileOutputStream(directoryPath+"/"+"huffmanTree.txt");
			fos.write(tree_byte_Array);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return false;
		
	}

	public String bitsRepresentationOfHuffManTree(HuffManNode huffManNode) {
		if (huffManNode.left==null && huffManNode.right==null) {
	        String str = huffManNode.c+"";  
			byte[] bytes = str.getBytes();  
	        StringBuilder binary = new StringBuilder();  
	        for (byte b : bytes)  
	        {  
	        	int val = b;  
	        	for (int i = 0; i < 8; i++)  
	        	{  
	        		binary.append((val & 128) == 0 ? 0 : 1);  
	        		val <<= 1;  
	        	}  
	        	// binary.append(' ');  
	        }  

			return "0"+binary.toString();
		}
		String op="1";
		op+=bitsRepresentationOfHuffManTree(huffManNode.left);
		op+=bitsRepresentationOfHuffManTree(huffManNode.right);
		return op;

	}
}
