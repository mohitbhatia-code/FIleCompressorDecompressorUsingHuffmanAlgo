import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DeCompressFile {
	private String encodedFile;
	private String huffManTreeFile;
	private String outputPath;

	public DeCompressFile(String encodedFile,String huffManTreeFile,String outputPath) {
		this.encodedFile = encodedFile;
		this.huffManTreeFile = huffManTreeFile;
		this.outputPath=outputPath;
	}

	public boolean decompress() {

		String bitsRepresentationForEncodedFile=""; 
		String bitsRepresentationForHuffManTreeFile="";

		//Getting bits representation for the encoded file
		File file = new File(encodedFile);
		File huffManFile = new File(huffManTreeFile);
		try {

			bitsRepresentationForEncodedFile = binaryRepresentationOfAFile(file);

			bitsRepresentationForHuffManTreeFile = binaryRepresentationOfAFile(huffManFile);


			//Creating huffman tree
			int[] SI = new int[1];
			SI[0]=0;
			HuffManNode root = createHuffManTreeFromItsBitsRepresentation(bitsRepresentationForHuffManTreeFile,SI);
			//		print(root,"");
			if (!decodeFileUsingHuffManTree(root,bitsRepresentationForEncodedFile)) {
				return false;
			}
			return true;

		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		return false;
	}


	public boolean decodeFileUsingHuffManTree(HuffManNode huffManNode,String bitsRepresentation) {
		HuffManNode temp = huffManNode;
		String actualData="";
		for(int i=0;i<bitsRepresentation.length();i++) {
			if (bitsRepresentation.charAt(i)=='0') {

				temp = temp.left;
				if (temp.left==null && temp.right==null) {
					actualData+=temp.c;
					temp = huffManNode;
				}


			}else {
				temp = temp.right;
				if (temp.left==null && temp.right==null) {
					actualData+=temp.c;
					temp = huffManNode;
				}

			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath+"_decoded.txt"));
			writer.write(actualData);
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return false;

	}

	public String binaryRepresentationOfAFile(File file) throws IOException {
		String bitsRepresentation="";

		//Scanner to get file content 
		FileInputStream fin = null;
		fin = new FileInputStream(file);
		
		byte[] fileContent = new byte[(int)file.length()];
		
		fin.read(fileContent);
//		StringBuilder stringBuilder = new StringBuilder();
//		while(in.hasNext()) {
//			stringBuilder.append(in.nextLine());
//		}
//		String string = stringBuilder.toString();
//		byte[] bytes = string.getBytes();
//	

		StringBuilder strBCodes = new StringBuilder();
		for(byte b:fileContent) {
			int val = b;  
			for (int i = 0; i < 8; i++)  
			{  
				strBCodes.append((val & 128) == 0 ? 0 : 1);  
				val <<= 1;  
			}  
		}
		String codes = strBCodes.toString();

		int end = codes.length()-1;
		String binaryRepresentationForExtraAdded0 = "";
		for(int i=end;i>end-8;i--) {
			binaryRepresentationForExtraAdded0 = codes.charAt(i)+binaryRepresentationForExtraAdded0;
		}
		int Extra0BitsAdded = Integer.parseInt(binaryRepresentationForExtraAdded0,2);

		int actualCodeLength = codes.length()-8-Extra0BitsAdded;

		for(int i=0;i<actualCodeLength;i++) {
			bitsRepresentation+=codes.charAt(i);
		}
		return bitsRepresentation;

	}
	public HuffManNode createHuffManTreeFromItsBitsRepresentation(String bits, int[] SI) {
		if (bits.charAt(SI[0])=='1') {
			HuffManNode internalNode = new HuffManNode();
			SI[0]=SI[0]+1;
			internalNode.left = createHuffManTreeFromItsBitsRepresentation(bits,SI);
			internalNode.right = createHuffManTreeFromItsBitsRepresentation(bits, SI);
			return internalNode;
		}else {
			HuffManNode leaf = new HuffManNode();
			String data = bits.substring(SI[0]+1,SI[0]+9);
			int parseInt = Integer.parseInt(data, 2);
			char c = (char)parseInt;
			leaf.c=c;
			SI[0]=SI[0]+9;
			return leaf;
		}
	}

	//	public void print(HuffManNode root,String opSoFar) {
	//		if (root.left==null && root.right==null) {
	//			System.out.println(root.c+":"+opSoFar);
	//			return;
	//		}
	//		print(root.left, opSoFar+0);
	//		print(root.right, opSoFar+1);
	//	}

}
