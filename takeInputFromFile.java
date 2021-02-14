import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class takeInputFromFile {
	private char[] alphabets = null;
	private int[] freq = null;
	private char[] fileContent = null;
	private File file;
	
	public char[] getAlphabets() {
		return alphabets;
	}


	public int[] getFreq() {
		return freq;
	}

	public char[] getFileContent(){
		return fileContent;
	}
	public File getFile() {
		return file;
	}
	public void takeInput() {
		Scanner s = new Scanner(System.in);
		System.out.print("Enter file path: ");
		String fileName = s.nextLine();

		try {
			
			file = new File(fileName);
			
			//Scanner to get file content 
			Scanner in = new Scanner(file);
			StringBuilder stringBuilder = new StringBuilder();
			while(in.hasNext()) {
				stringBuilder.append(in.nextLine()+"\n");
			}
			stringBuilder.deleteCharAt(stringBuilder.length()-1);

			fileContent = stringBuilder.toString().toCharArray();


			ArrayList<Character> alphabetList = new ArrayList<Character>();
			ArrayList<Integer> freqList = new ArrayList<Integer>();

			int index;
			for(int i=0;i<fileContent.length;i++) {
				if ((index=alphabetList.indexOf(fileContent[i]))>=0){
					// Alphabet is present in the list
					freqList.set(index, freqList.get(index)+1);
				}else {
					//Alphabet has occured for the first time
					alphabetList.add(fileContent[i]);
					freqList.add(1);
				}
			}

			alphabets = new char[alphabetList.size()];
			freq = new int[freqList.size()];

			for(int i=0;i<alphabetList.size();i++) {
				alphabets[i]=alphabetList.get(i);
				freq[i] = freqList.get(i);
			}	
		}catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		}

	}
	

}
