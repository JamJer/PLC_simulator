import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Lexical analyzer
 */
public class LexicalAnalyzer {
	public static LinkedList<Symbol> SymbolTable=new LinkedList();	//Symbol Table
	public LinkedList<String> temp_t=new LinkedList();				//temp data
	static final String BEGIN_TOKEN = "BEGIN_TOKEN";				//begin token
	static final String COLON_TOKEN = "COLON_TOKEN";		 		//: token
	static final String SEMICOLON_TOKEN = "SEMICOLON_TOKEN";		//; token
	static final String LEFTPAREN_TOKEN = "LEFTPAREN_TOKEN";		//( token
	static final String RIGHTPAREN_TOKEN = "RIGHTPAREN_TOKEN";		//) token
	static final String LEFTBRACE_TOKEN = "LEFTBRACE_TOKEN";		//{ token
	static final String RIGHTBRACE_TOKEN = "RIGHTBRACE_TOKEN";		//} token
	static final String MOD_TOKEN = "MOD_TOKEN";					//% token
	static final String EOF_TOKEN = "EOF_TOKEN";					//end of stream token
	static final String TYPE_TOKEN =  "TYPE_TOKEN";					//int token
	static final String IF_TOKEN = "IF_TOKEN";						//if token
	static final String ELSE_TOKEN = "ELSE_TOKEN";					//else token
	static final String WHILE_TOKEN = "WHILE_TOKEN";				//while token
	static final String FOR_TOKEN = "FOR_TOKEN";					//for token
	static final String IDENTIFIER_TOKEN = "IDENTIFIER_TOKEN";		// 英文字母 及數字token
	static final String PLUS_TOKEN = "PLUS_TOKEN";					//+ token
	static final String MINUS_TOKEN = "MINUS_TOKEN";				//- token
	static final String MULT_TOKEN = "MULT_TOKEN";					//* token
	static final String DIV_TOKEN = "DIV_TOKEN";					//"/" token 
	static final String EQUAL_TOKEN = "EQUAL_TOKEN";				//==token
	static final String ASSIGN_TOKEN = "ASSIGN_TOKEN";				//= token
	static final String GREATER_TOKEN = "GREATER_TOKEN";			//> token
	static final String GREATEREQUAL_TOKEN = "GREATEREQUAL_TOKEN";	//>=token
	static final String LESS_TOKEN = "LESS_TOKEN";					//< token
	static final String LESSEQUAL_TOKEN = "LESSEQUAL_TOKEN";		//<=token
	static final String AND_TOKEN = "AND_TOKEN";					//and token
	static final String OR_TOKEN = "OR_TOKEN";						//or token
	static final String HASHTAG_TOKEN = "HASHTAG_TOKEN";			//# token
	public LexicalAnalyzer(){
		
	}
	public boolean checkExist(String s){							//判斷是否有重複的Symbol		
		boolean flag=false;
		int i;
		for(i=0;i<temp_t.size();i++){
			if(temp_t.get(i).equals(s)){
				flag=true;
				break;
			}
			else
				continue;
		}
		return flag;
	}
	/**這個function會產生出SymbolTable,
	 * 如果讀到x=x+1會生成
	 * 	x IDENTIFIER_TOKEN
	 *	= ASSIGN_TOKEN
	 *	+ PLUS_TOKEN
	 *	1 IDENTIFIER_TOKEN
	 *	每個Symbol只會出現一次*/
    public void genSymbolTable(String line){						//line是輸入字串
    	LinkedList<String> temp_e=new LinkedList();					//存分析好的文字檔
    	StringTokenizer st=new StringTokenizer(line,"()><=,;+-*/ ",true);
    	String [] str=new String[2];								//為了判斷==,<=,>=
    	for(int n=0;n<2;n++){										//初始str
    		str[n]=null;
    	}
    	//int j=0;
    	while(st.hasMoreElements()){
			 temp_e.add(st.nextToken());
		}
    	int j=0;
    	for(int i=0;i<temp_e.size();i++){
    		str[j]=temp_e.get(i).toString();
    		if(temp_e.get(i).toString().equals(":")){				//read ":"
    			if(checkExist(":")==false){
    				temp_t.add(":");
    				Symbol temp=new Symbol(":",COLON_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals(";")){			//read ";"
    			if(checkExist(";")==false){
    				temp_t.add(";");
    				Symbol temp=new Symbol(";",SEMICOLON_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals(" ")){			//read " "
    			continue;
    		}else if(temp_e.get(i).toString().equals("(")){			//read "("
    			if(checkExist("(")==false){
    				temp_t.add("(");
    				Symbol temp=new Symbol("(",LEFTPAREN_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals(")")){			//read ")"
    			if(checkExist(")")==false){
    				temp_t.add(")");
    				Symbol temp=new Symbol(")",RIGHTPAREN_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("{")){			//read "{"
    			if(checkExist("{")==false){
    				temp_t.add("{");
    				Symbol temp=new Symbol("{",LEFTBRACE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("}")){			//read "}"
    			if(checkExist("}")==false){
    				temp_t.add("}");
    				Symbol temp=new Symbol("}",RIGHTBRACE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("%")){			//read "%"
    			if(checkExist("%")==false){
    				temp_t.add("%");
    				Symbol temp=new Symbol("%",MOD_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("end")){		//read "end"
    			if(checkExist("end")==false){
    				temp_t.add("end");
    				Symbol temp=new Symbol("end",EOF_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("int")){		//read "int"
    			if(checkExist("int")==false){
    				temp_t.add("int");
    				Symbol temp=new Symbol("int",TYPE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("if")){		//read "if"
    			if(checkExist("if")==false){
    				temp_t.add("if");
    				Symbol temp=new Symbol("if",IF_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("else")){		//read "else"
    			if(checkExist("else")==false){
    				temp_t.add("else");
    				Symbol temp=new Symbol("else",ELSE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("while")){		//read "while"
    			if(checkExist("while")==false){
    				temp_t.add("while");
    				Symbol temp=new Symbol("while",WHILE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("for")){		//read "for"
    			if(checkExist("for")==false){
    				temp_t.add("for");
    				Symbol temp=new Symbol("for",FOR_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("+")){			//read "+"
    			if(checkExist("+")==false){
    				temp_t.add("+");
    				Symbol temp=new Symbol("+",PLUS_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("-")){			//read "-"
    			if(checkExist("-")==false){
    				temp_t.add("-");
    				Symbol temp=new Symbol("-",MINUS_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("*")){			//read "*"
    			if(checkExist("*")==false){
    				temp_t.add("*");
    				Symbol temp=new Symbol("*",MULT_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("/")){			//read "/"
    			if(checkExist("/")==false){
    				temp_t.add("/");
    				Symbol temp=new Symbol("/",DIV_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("and")){		//read "and"
    			if(checkExist("and")==false){
    				temp_t.add("and");
    				Symbol temp=new Symbol("and",AND_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("or")){		//read "or"
    			if(checkExist("or")==false){
    				temp_t.add("or");
    				Symbol temp=new Symbol("or",OR_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("=")){			//read "="
    			if(str[1]==null){
    				//System.out.println("280");
    				j=1;
    				//continue;
    			}
    			else if(str[1].equals("=")){						//判斷是==,<=,>=
    				j=0;
    				if(str[0].equals("=")){
    					if(checkExist("==")==false){
    	    				temp_t.add("==");
    	    				Symbol temp=new Symbol("==",EQUAL_TOKEN);
    	    				SymbolTable.add(temp);
    	    			}
    				}else if(str[0].equals("<")){
    					j=0;
    					if(checkExist("<=")==false){
    	    				temp_t.add("<=");
    	    				Symbol temp=new Symbol("<=",LESSEQUAL_TOKEN);
    	    				SymbolTable.add(temp);
    	    			}
    				}else if(str[0].equals(">")){
    					j=0;
    					if(checkExist(">=")==false){
    	    				temp_t.add(">=");
    	    				Symbol temp=new Symbol(">=",GREATEREQUAL_TOKEN);
    	    				SymbolTable.add(temp);
    	    			}
    				}
    			}
    		}else if(temp_e.get(i).toString().equals(">")){			//read ">"
    			if(str[1]==null){
    				j=1;
    				//continue;
    			}
    		}else if(temp_e.get(i).toString().equals("<")){			//read "<"
    			if(str[1]==null){
    				j=1;
    				//continue;
    			}
    		}else if(temp_e.get(i).toString().equals("begin")){		//read "begin"
    			if(checkExist("begin")==false){
    				temp_t.add("begin");
    				Symbol temp=new Symbol("begin",BEGIN_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(temp_e.get(i).toString().equals("#")){			//read "#"
    			if(checkExist("#")==false){
    				temp_t.add("#");
    				Symbol temp=new Symbol("#",HASHTAG_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else{													//read other symbol
    			if(str[0].equals("=")){
    				j=0;
    				if(checkExist("=")==false){
    					temp_t.add("=");
    					Symbol temp=new Symbol("=",ASSIGN_TOKEN);
    					SymbolTable.add(temp);
    				}
    				str[1]=null;
    			}
    			if(str[0].equals(">")){
    				j=0;
    				if(checkExist(">")==false){
    					temp_t.add(">");
    					Symbol temp=new Symbol(">",GREATER_TOKEN);
    					SymbolTable.add(temp);
    				}
    				str[1]=null;
    			}
    			if(str[0].equals("<")){
    				j=0;
    				if(checkExist("<")==false){
    					temp_t.add("<");
    					Symbol temp=new Symbol("<",LESS_TOKEN);
    					SymbolTable.add(temp);
    				}
    				str[1]=null;
    			}
    			if(checkExist(temp_e.get(i).toString())==false){
    				temp_t.add(temp_e.get(i).toString());
    				Symbol temp=new Symbol(temp_e.get(i).toString(),IDENTIFIER_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}
		}
	 }
	public static void main(String[] args) {
		LinkedList<Symbol> s=new LinkedList();
		LexicalAnalyzer a=new LexicalAnalyzer();
		String line;
		try{
			FileReader fr = new FileReader("test_p.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				a.genSymbolTable(line);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0;i<SymbolTable.size();i++){
    		System.out.println(SymbolTable.get(i).toString());
    	}
	}
}