package trie;

import java.util.ArrayList;


public class Trie {
	

	private Trie() { }
	
	
	public static TrieNode buildTrie(String[] allWords) {
		
		if(allWords.length < 1) {
			return null;
		}
		
		Indexes fc = new Indexes(0, (short)0, (short)(allWords[0].length() - 1));
		TrieNode firChild = new TrieNode(fc, null, null);
		TrieNode root = new TrieNode(null, firChild, null);
		

		for(int i = 1; i < allWords.length; i++) {
			addWord(i, allWords, firChild);
		}
		return root;
		
	}
	
	private static void addWord(int thisWordPosition,  String[] allWords, TrieNode root) {
		String thisWord = allWords[thisWordPosition];
		if( root.substr == null  )
		{
			addWord( thisWordPosition, allWords, root.firstChild );
		}


		short begin = root.substr.startIndex;
		short end = root.substr.endIndex;
		int rootWordNum = root.substr.wordIndex;
		int i = 0;
		
		while((thisWord.substring(i, i + 1)).equals(allWords[rootWordNum].substring(i, i + 1))) {
			i++;
		}
		if(i > begin) {
			if((i - 1 - begin) >= end - begin) {
				addWord( thisWordPosition, allWords, root.firstChild );
			}
			else {
				shift(thisWordPosition, allWords, root, i);
			}
			return;
		}
		
		else if(root.sibling == null) {
			Indexes t = new Indexes(thisWordPosition, begin, (short)(thisWord.length() - 1));
			TrieNode temp = new TrieNode(t, null, null);
			root.sibling = temp;
			return;
		}
		
		else {
			addWord( thisWordPosition, allWords, root.sibling );
		}
		
		
		
	}
	
	private static void shift(int thisWordPosition, String[] allWords, TrieNode root, int lettersInCommon) {
		String thisWord = allWords[thisWordPosition];
		
		TrieNode finalChild = root.firstChild;
		TrieNode child;
		int rootWordNum = root.substr.wordIndex;
		
		short childEnd = root.substr.endIndex;
		
		Indexes childSub = new Indexes(rootWordNum, (short)lettersInCommon , childEnd);
		child = new TrieNode(childSub, null, null);
		
		
		root.substr.endIndex = (short)(lettersInCommon - 1);
		root.firstChild = child;
		
		
		Indexes sibSub = new Indexes(thisWordPosition, (short)lettersInCommon, (short)(thisWord.length() - 1));
		TrieNode sib = new TrieNode(sibSub, null, null);
		
		
		child.sibling = sib;
		child.firstChild = finalChild;
		
		
	}
	
	
	
	
	
	
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {

		ArrayList<TrieNode> a = new ArrayList<>();
		if(root == null || root.firstChild == null) {
			return null;
		}
		
		TrieNode fir = firstOne(prefix, allWords, root.firstChild);
		if(fir == null) {
			return null;
		}
		addAll(fir.firstChild, a);
		
		
		
		return a;
	}
	
	private static void addAll(TrieNode root, ArrayList<TrieNode> a) {
		if(root.firstChild == null) {
			a.add(root);
		}
		else if(root.firstChild != null) {
			addAll(root.firstChild, a);
		}
		if(root.sibling != null) {
			addAll(root.sibling, a);
		}
		
	}
	
	private static TrieNode firstOne(String prefix,  String[] allWords, TrieNode root) {
		if( root.substr == null  )
		{
			return firstOne( prefix, allWords, root.firstChild );
		}

		short begin = root.substr.startIndex;
		short end = root.substr.endIndex;
		int rootWordNum = root.substr.wordIndex;
		int i = 0;
		
		while(prefix.length() > i + begin && begin + i < end + 1 && (prefix.substring(begin + i, begin + i + 1)).equals(allWords[rootWordNum].substring(begin + i, begin + i + 1))) {
			i++;
		}
		
		if(i > 0) {
			if(i + begin >= prefix.length()) {
				return root;
			}
			else {
				return firstOne(prefix, allWords, root.firstChild);
			}
		}
		
		else if(root.sibling == null) {
			return null;
		}
		
		else {
			return firstOne(prefix, allWords, root.sibling);
		}
		
		
		
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
