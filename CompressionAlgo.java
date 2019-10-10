package thread_code;

import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) {
    	frequency = freq; 
    }
    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends HuffmanTree {
	
	public final char value; // the character this leaf represents
	public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}
 
class HuffmanNode extends HuffmanTree {
    
	public final HuffmanTree left, right; // subtrees
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

class details_character{
    int size;
    String final_state_value;
    String type;
    int ht1;
    details_character(){
        size=0;
        type="\0";
        final_state_value="\0";
        ht1=0;
    }
    void display_character_details(){
        System.out.println("\ntype:"+type);
        System.out.println("\nsize:"+size);
    }
}

public class CompressionAlgo {
    
	public static String temp25;
    static FileReader decode_file;
    static FileWriter decode_file_write;
    
    //huffman tree construction______________________
    public static HuffmanTree buildTree(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        
        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++)
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char)i));

        assert trees.size() > 0;
        
        // loop until there is only one tree left
        while (trees.size() > 1) {
        
        	// two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
 
            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        
        return trees.poll();
    }
    
    // Compression
    public static void compress(HuffmanTree tree, StringBuffer prefix,char alpha1) {
        
    	assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
 
            // print out character, frequency, and code for this leaf (which is just the prefix)
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
            if(leaf.value==alpha1){
                temp25=prefix.toString();
            }
        }
        else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // traverse left
            prefix.append('0');
            compress(node.left, prefix,alpha1);
            prefix.deleteCharAt(prefix.length()-1);
 
            // traverse right
            prefix.append('1');
            compress(node.right, prefix,alpha1);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }
    
    //decompress
    public static void decompress(HuffmanTree tree) {
        
    	char alpha='\0';    
        assert tree != null;
        
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
 
            // print out character, frequency, and code for this leaf (which is just the prefix)
            try{ decode_file_write.write(leaf.value);
            }catch(Exception e){
            	e.printStackTrace();
            }
        } else if (tree instanceof HuffmanNode) {
            try{
                int character;
                if((character=decode_file.read())!=-1){
                	alpha=(char)character;
                }
            }catch(Exception e){
            	e.printStackTrace();
            }
            HuffmanNode node = (HuffmanNode)tree;
 
            // traverse left
            if(alpha=='0'){
            	decompress(node.left);
            }
            // traverse right
            if(alpha=='1'){
            	decompress(node.right);
            }}
    }
    
    public static void main(String[] args) {
        int i,j;
        details_character[] dc1=new details_character[8];
        for(i=0;i<8;i++)
        {
            dc1[i]=new details_character();
        }
        int count=0,original_size=0;
        int c_lower,c_upper,c_symbol,c_space,c_number,c_newline,c_horizon,c_punct;
        c_lower=c_horizon=c_upper=c_symbol=c_space=c_number=c_newline=c_punct=0;
        try {
        	FileReader reader = new FileReader("sample1.txt");
            int character,sl_present;
            char alpha;
            FileWriter w1 = new FileWriter("capitals.txt", true);
            FileWriter w2 = new FileWriter("smallcharacters.txt", true);
            FileWriter w3 = new FileWriter("numerics.txt", true);
            FileWriter w4 = new FileWriter("punctuations.txt", true);
            FileWriter w5 = new FileWriter("symbols.txt", true);
            
            while ((character = reader.read()) != -1) {
            	System.out.print((char) character);
            	alpha=(char)character;

            	count++;
            	sl_present=0;
            	if(alpha==' '){
            		c_space++;
            		sl_present=1;
            	}
            	if(alpha == '\n'){
            		c_newline++;
            		sl_present=1;
            	}
            	if(alpha == '\t'){
            		c_horizon++;
            		sl_present=1;
            	}
            	if(alpha >= 'A'&& alpha <= 'Z'){
            		c_upper++;
            		sl_present=1;
            		w1.write(alpha);
            	}
            	if(alpha >= 'a'&& alpha <= 'z'){
            		c_lower++;
            		sl_present=1;
            		w2.write(alpha);
            	}
            	if(alpha >= '0'&&alpha <= '9'){
            		c_number++;
            		sl_present=1;
            		w3.write(alpha);
            	}
            	if(alpha=='.'||alpha==','||alpha=='"'||alpha==';'||alpha==':'||alpha=='?'){
            		c_punct++;
            		sl_present=1;
            		w4.write(alpha);
            	}
            	if(alpha=='{'||alpha=='}'||alpha=='['||alpha==']'||alpha=='('||alpha==')'){
            		c_punct++;
            		sl_present=1;
            		w4.write(alpha);
            	}
            	if(alpha=='-'||alpha=='_'||alpha=='!'||alpha=='\''){
            		c_punct++;
            		sl_present=1;
            		w4.write(alpha);
            	}
            	if(alpha=='@'||alpha=='#'||alpha=='$'||alpha=='%'||alpha=='^'||alpha=='&'||alpha=='*'){
            		c_symbol++;
            		sl_present=1;
            		w5.write(alpha);
            	}
            	if(alpha=='+'||alpha=='='||alpha=='>'||alpha=='<'||alpha=='|'||alpha=='/'){
            		c_symbol++;
            		sl_present=1;
            		w5.write(alpha);
            	}
            	if(sl_present==0){
            		c_symbol++;
            		w5.write(alpha);
            	}
            }
            c_symbol=c_symbol-c_newline;
            
            //objects to store details of the characters
            dc1[0].type="space";
            dc1[0].ht1=10;
            dc1[0].size=c_space;
            dc1[1].type="small";
            dc1[1].ht1=2;
            dc1[1].size=c_lower;
            dc1[2].type="capital";
            dc1[2].ht1=1;
            dc1[2].size=c_upper;
            dc1[3].type="number";
            dc1[3].ht1=3;
            dc1[3].size=c_number;
            dc1[4].type="punctuation";
            dc1[4].ht1=4;
            dc1[4].size=c_punct;
            dc1[5].type="symbol";
            dc1[5].ht1=5;
            dc1[5].size=c_symbol;
            dc1[6].type="newline";
            dc1[6].ht1=20;
            dc1[6].size=c_newline;
            dc1[7].type="tab";
            dc1[7].ht1=30;
            dc1[7].size=c_horizon;
            
            // States
            dc1[0].final_state_value="0";
            dc1[1].final_state_value="10";
            dc1[2].final_state_value="110";
            dc1[3].final_state_value="1110";
            dc1[4].final_state_value="11110";
            dc1[5].final_state_value="111110";
            dc1[6].final_state_value="1111110";
            dc1[7].final_state_value="1111111";
            
            //sort the character groups based on their occurrence
            int temp=0,temp45=0;
            String temp1;
            for(i=0;i<8;i++){
                for(j=0;j<7;j++){
                    if(dc1[j].size < dc1[j+1].size){
                        temp=dc1[j].size;
                        temp1=dc1[j].type;
                        temp45=dc1[j].ht1;
                        dc1[j].size=dc1[j+1].size;
                        dc1[j].type=dc1[j+1].type;
                        dc1[j].ht1=dc1[j+1].ht1;
                        dc1[j+1].size=temp;
                        dc1[j+1].type=temp1;
                        dc1[j+1].ht1=temp45;
                    }
                }
            }
            for(i=0;i<8;i++){
            	System.out.println(dc1[i].size+","+dc1[i].type+","+dc1[i].final_state_value+","+dc1[i].ht1);
            }
            reader.close();
            w1.close();
            w2.close();
            w3.close();
            w4.close();
            w5.close();
            
            // Huffman tree construction
            int[] cfreq1=new int[256];
            int[] cfreq2=new int[256];
            int[] cfreq3=new int[256];
            int[] cfreq4=new int[256];
            int[] cfreq5=new int[256];
            int character12;
            
            // Capital
            FileReader r1=new FileReader("capitals.txt");
            while((character12=r1.read())!=-1){
                cfreq1[(char)character12]++;
            }     
            
            HuffmanTree tree1 = buildTree(cfreq1);
            
            //Small characters
            FileReader r2=new FileReader("smallcharacters.txt");
            while((character12=r2.read())!=-1){
                cfreq2[(char)character12]++;
            }
            
            HuffmanTree tree2 = buildTree(cfreq2);
            
            //Numerics
            FileReader r3=new FileReader("numerics.txt");
            while((character12=r3.read())!=-1){
                cfreq3[(char)character12]++;
            }
            
            HuffmanTree tree3 = buildTree(cfreq3);
            
            //Punctuations
            FileReader r4=new FileReader("punctuations.txt");
            while((character12=r4.read())!=-1){
                cfreq4[(char)character12]++;
            }
            
            HuffmanTree tree4 = buildTree(cfreq4);
            
            //Symbols
            FileReader r5=new FileReader("symbols.txt");
            while((character12=r5.read())!=-1){
                cfreq5[(char)character12]++;
            }
            HuffmanTree tree5 = buildTree(cfreq5);
            
            //--------------
            //encoding part
            //--------------
            
            int character1;
            FileWriter encode_file=new FileWriter("encoded_file.txt",true);
            FileReader reader1=new FileReader("sample1.txt");
            while ((character1 = reader1.read()) != -1) {
            	alpha=(char)character1;
            	if(alpha==' '){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("space")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            	}
            	if(alpha == '\n'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("newline")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            	}
            	if(alpha == '\t'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("tab")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            	}
            	if(alpha >= 'A'&& alpha <= 'Z'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("capital")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree1, new StringBuffer(),alpha);
            		System.out.println(temp25);
            		encode_file.write(temp25);
            	}
            	if(alpha >= 'a'&& alpha <= 'z'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("small")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree2, new StringBuffer(),alpha);
            		System.out.println(temp25);
            		encode_file.write(temp25);
            	}
            	if(alpha >= '0'&&alpha <= '9'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("number")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree3, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            
            	// Punctuations
            	if(alpha=='.'||alpha==','||alpha=='"'||alpha==';'||alpha==':'||alpha=='?'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("punctuation")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree4, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            
            	if(alpha=='{'||alpha=='}'||alpha=='['||alpha==']'||alpha=='('||alpha==')'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("punctuation")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree4, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            
            	if(alpha=='-'||alpha=='_'||alpha=='!'||alpha=='\''){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("punctuation")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree4, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            
            	// 	Symbols
            	if(alpha=='@'||alpha=='#'||alpha=='$'||alpha=='%'||alpha=='^'||alpha=='&'||alpha=='*'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("symbol")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree5, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            
            	if(alpha=='+'||alpha=='='||alpha=='>'||alpha=='<'||alpha=='|'||alpha=='/'){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("symbol")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree5, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            
            	// 	Backslash
            	if(alpha==92){
            		for(i=0;i<8;i++)
            		{
            			if(dc1[i].type.equals("symbol")){
            				encode_file.write(dc1[i].final_state_value);
            			}
            		}
            		compress(tree5, new StringBuffer(),alpha);
            		encode_file.write(temp25);
            	}
            }
            reader1.close();
            encode_file.close();
            
            //-------------
            //decoding part
            //-------------
            
            int character3;
            int choice;
            int m,n;
            decode_file = new FileReader("encoded_file.txt");
            decode_file_write=new FileWriter("decoded_file.txt",true);
            while ((character3 = decode_file.read()) != -1) {
            System.out.print((char) character3);
            alpha=(char)character3;
            
            if(alpha=='0'){
            for(m=0;m<8;m++){
                if(dc1[m].final_state_value=="0"){
                    switch(dc1[m].ht1){
                        case 1: decompress(tree1);
                            break;
                        case 2: decompress(tree2);
                            break;
                        case 3: decompress(tree3);
                            break;
                        case 4: decompress(tree4);
                            break;
                        case 5: decompress(tree5);
                            break;
                        case 10: decode_file_write.write(" ");
                            break;
                        case 20: decode_file_write.write("\n");
                            break;
                        case 30: decode_file_write.write("\t");
                            break;
                        }
                }
            }
            }else{
                if(alpha=='1'){
                    if((character3 = decode_file.read())!=-1){
                        if((char)character3=='0'){
                             for(m=0;m<8;m++){
                if(dc1[m].final_state_value=="10"){
                    switch(dc1[m].ht1){
                        case 1: decompress(tree1);
                            break;
                        case 2: decompress(tree2);
                            break;
                        case 3: decompress(tree3);
                            break;
                        case 4: decompress(tree4);
                            break;
                        case 5: decompress(tree5);
                            break;
                        case 10: decode_file_write.write(" ");
                            break;
                        case 20: decode_file_write.write("\n");
                            break;
                        case 30: decode_file_write.write("\t");
                            break;
                    }}}
                        }else{
                            if((char)character3=='1'){
                                if((character3=decode_file.read())!=-1){
                                    if((char)character3=='0'){
                                         for(m=0;m<8;m++){
                                        	 if(dc1[m].final_state_value=="110"){
                                        		 switch(dc1[m].ht1){
                                        		 	case 1: decompress(tree1);
                                        		 		break;
                                        		 	case 2: decompress(tree2);
                                        		 		break;
                                        		 	case 3: decompress(tree3);
                                        		 		break;
                                        		 	case 4: decompress(tree4);
                                        		 		break;
                                        		 	case 5: decompress(tree5);
                                        		 		break;
                                        		 	case 10: decode_file_write.write(" ");
                                        		 		break;
                                        		 	case 20: decode_file_write.write("\n");
                                        		 		break;
                                        		 	case 30: decode_file_write.write("\t");
                                        		 		break;
                                        		 }
                                        	 }
                                         }
                                    }else{
                                        if((char)character3=='1'){
                                            if((character3=decode_file.read())!=-1){
                                                if((char)character3=='0'){
                                                     for(m=0;m<8;m++){
                                                    	 if(dc1[m].final_state_value=="1110"){
                                                    		 switch(dc1[m].ht1){
                                                    		 	case 1: decompress(tree1);
                                                    		 		break;
                                                    		 	case 2: decompress(tree2);
                                                    		 		break;
                                                    		 	case 3: decompress(tree3);
                                                    		 		break;
                                                    		 	case 4: decompress(tree4);
                                                    		 		break;
                                                    		 	case 5: decompress(tree5);
                                                    		 		break;
                                                    		 	case 10: decode_file_write.write(" ");
                                                    		 		break;
                                                    		 	case 20: decode_file_write.write("\n");
                                                    		 		break;
                                                    		 	case 30: decode_file_write.write("\t");
                                                    		 		break;
                                                    		 }
                                                    	 }
                                                     }
                                                }else{
                                                    if((char)character3=='1'){
                                                        if((character3=decode_file.read())!=-1){
                                                            if((char)character3=='0'){
                                                                 for(m=0;m<8;m++){
                                                                	 if(dc1[m].final_state_value=="11110"){
                    switch(dc1[m].ht1){
                        case 1: decompress(tree1);
                            break;
                        case 2: decompress(tree2);
                            break;
                        case 3: decompress(tree3);
                            break;
                        case 4: decompress(tree4);
                            break;
                        case 5: decompress(tree5);
                            break;
                        case 10: decode_file_write.write(" ");
                            break;
                        case 20: decode_file_write.write("\n");
                            break;
                        case 30: decode_file_write.write("\t");
                            break;
                        }
                }
            }
                                                            }else{
                                                                if((char)character3=='1'){
                                                                    if((character3=decode_file.read())!=-1){
                                                                        if((char)character3=='0'){
                                                                             for(m=0;m<8;m++){
                if(dc1[m].final_state_value=="111110"){
                    switch(dc1[m].ht1){
                        case 1: decompress(tree1);
                            break;
                        case 2: decompress(tree2);
                            break;
                        case 3: decompress(tree3);
                            break;
                        case 4: decompress(tree4);
                            break;
                        case 5: decompress(tree5);
                            break;
                        case 10: decode_file_write.write(" ");
                            break;
                        case 20: decode_file_write.write("\n");
                            break;
                        case 30: decode_file_write.write("\t");
                            break;
                        }
                }
            }
                                                                        }else{
                                                                            if((char)character3=='1'){
                                                                                if((character3=decode_file.read())!=-1){
                                                                                    if((char)character3=='0'){
                                                                                         for(m=0;m<8;m++){
                if(dc1[m].final_state_value=="1111110"){
                    switch(dc1[m].ht1){
                        case 1: decompress(tree1);
                            break;
                        case 2: decompress(tree2);
                            break;
                        case 3: decompress(tree3);
                            break;
                        case 4: decompress(tree4);
                            break;
                        case 5: decompress(tree5);
                            break;
                        case 10: decode_file_write.write(" ");
                            break;
                        case 20: decode_file_write.write("\n");
                            break;
                        case 30: decode_file_write.write("\t");
                            break;
                        }
                }
            }
                                                                                    }else{
                                                                                        if(((char)character3=='1')){
                                                                                             for(m=0;m<8;m++){
                if(dc1[m].final_state_value=="1111111"){
                    switch(dc1[m].ht1){
                        case 1: decompress(tree1);
                            break;
                        case 2: decompress(tree2);
                            break;
                        case 3: decompress(tree3);
                            break;
                        case 4: decompress(tree4);
                            break;
                        case 5: decompress(tree5);
                            break;
                        case 10: decode_file_write.write(" ");
                            break;
                        case 20: decode_file_write.write("\n");
                            break;
                        case 30: decode_file_write.write("\t");
                            break;
                        }
                }}}}}}}}}}}}}}}}}}}}}
            }
            }
            decode_file.close();
            decode_file_write.close();
            System.out.println("\nthe number characters in the given text file:"+count);
            original_size=count*8;
            System.out.println("the space needed before compression:"+original_size);
            System.out.println("the character in the text file will be read and written in their respective group");
            System.out.println("for example A will be in group capital_char and 9 will be numberic character");
                    
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("this is temp value:"+temp25);//checking
    }
}
