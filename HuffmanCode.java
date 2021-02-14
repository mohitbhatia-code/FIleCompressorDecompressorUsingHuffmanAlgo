import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

class HuffManNode{
	char c;
	int freq;
	HuffManNode left;
	HuffManNode right;
}

class CompareUsingFreq implements Comparator<HuffManNode>{

	@Override
	public int compare(HuffManNode o1, HuffManNode o2) {
		// TODO Auto-generated method stub
		return o1.freq-o2.freq;
	}
	
}
public class HuffmanCode {
	
	
	int n;
	char[] charArray;
	int[] freqArray;
	private HashMap<Character,String> huffManCodeForEachCharacter = new HashMap<Character, String>();
	PriorityQueue<HuffManNode> priorityQueue;
	
	
	HuffmanCode(int n, char[] charArray,int[] freqArray){
		this.n= n;
		this.charArray=charArray;
		this.freqArray=freqArray;
		priorityQueue = new PriorityQueue<HuffManNode>(n,new CompareUsingFreq());
		
	}
	public HuffManNode createHuffManTree() {
		for(int i=0;i<n;i++) {
			HuffManNode huffManNode = new HuffManNode();
			huffManNode.c = charArray[i];
			huffManNode.freq = freqArray[i];
			priorityQueue.add(huffManNode);	
		}
		HuffManNode root = null;
		while(priorityQueue.size()>1) {
			HuffManNode a = priorityQueue.peek();
			priorityQueue.poll();
			
			HuffManNode b = priorityQueue.peek();
			priorityQueue.poll();
			
			HuffManNode huffManNode = new HuffManNode();
			huffManNode.freq = a.freq+b.freq;
			huffManNode.c='+';
			
			huffManNode.left = a;
			huffManNode.right = b;
			
			root = huffManNode;
			
			priorityQueue.add(huffManNode);
		}
		return root;
	}
	
	public void print(HuffManNode root,String opSoFar) {
		if (root.left==null && root.right==null) {
			System.out.println(root.c+":"+opSoFar);
			return;
		}
		print(root.left, opSoFar+0);
		print(root.right, opSoFar+1);
	}
	
	public void putHashMapKeyValue(HuffManNode root, String opSoFar) {
		if (root.left==null && root.right==null) {
			huffManCodeForEachCharacter.put(root.c, opSoFar);
			return;
		}
		putHashMapKeyValue(root.left, opSoFar+0);
		putHashMapKeyValue(root.right, opSoFar+1);
	}
	
	
	public HashMap<Character, String> getHuffManCodeForEachCharacter() {
		
		return huffManCodeForEachCharacter;
	}
	
	

}
