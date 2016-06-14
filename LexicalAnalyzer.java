import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Lexical analyzer
 */
public class LexicalAnalyzer {
	public int acl = 1;
	public int for_Num=0;
	public int while_Num=0;
	public int if_Num=0;
	public int end_Num=0;
	public int begin_Num=0;
	public static LinkedList<String>ErrorMessage=new LinkedList();
	public LinkedList<Symbol> SymbolTable=new LinkedList();			//Symbol Table
	public LinkedList<String> temp_t=new LinkedList();				//temp data
	LinkedList<varValue> varValue=new LinkedList();					//var Value
	public boolean error_Var=true;									/**this 4 boolean respective 4 error**/
	public boolean error_Type=true;
	public boolean error_Flag=true;
	public boolean error_Loop=true;
	public boolean error_Begin=true;
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
	static final String COMMA_TOKEN = "COMMA_TOKEN";				//, token
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
	public boolean checkVar(String s){								//check var是否是城市可以辨別的型態
		boolean flag=false;
		int i;
		for(i=0;i<varValue.size();i++){
			if(varValue.get(i).var.equals(s)){
				flag=true;
				break;
			}
			else{
				continue;
			}
		}
		return flag;
	}
	public boolean Numornot(String msg){							//check msg 是否為整數
        try{
               Integer.parseInt(msg);
               return true; 
              }catch(Exception e){
               return false;
              }
    }
	public varValue addVar(String s,int num){						//判斷是否有重複的變數(var)
		int i;														//如果有則在後面加_i(i為重複的個數)
		for(i=0;i<varValue.size();i++){
			if(varValue.get(i).getVar().equals(s)){
				String t="";
				if(s.contains("_"))
				{
					s=s.replace("_"+(acl-1), "");
					t=s+"_"+acl++;
				}
				else{
					t=s+"_"+acl++;
				}
				return addVar(t,num);
			}
			else{
				;
			}
		}
		acl=1;
		varValue var=new varValue(s,num);
		return var;
	}
	public LinkedList<String> genLexeme(String line){						//產生Lexeme
		LinkedList<String> lex=new LinkedList();	
		if(line.contains("#")){
			String[] sub=line.split("#");
			line=sub[0];
			StringTokenizer st=new StringTokenizer(line,"()><=,;+-*/ ",true);
			while(st.hasMoreElements()){
				 lex.add(st.nextToken());
			}
			return lex;
		}
		else{
			StringTokenizer st=new StringTokenizer(line,"()><=,;+-*/ ",true);
			while(st.hasMoreElements()){
				lex.add(st.nextToken());
			}
			return lex;
		}
		
	}
	/**這個function會產生出SymbolTable,
	 * 如果讀到x=x+1會生成
	 * 	x IDENTIFIER_TOKEN
	 *	= ASSIGN_TOKEN
	 *	+ PLUS_TOKEN
	 *	1 IDENTIFIER_TOKEN
	 *	每個Symbol只會出現一次*/
    public LinkedList<Symbol> genSymbolTable(LinkedList<String> lex){			//lex是LexemeLinkedList
    	String [] str=new String[2];											//為了判斷==,<=,>=
    	for(int n=0;n<2;n++){													//初始str
    		str[n]=null;
    	}
    	int j=0;
    	for(int i=0;i<lex.size();i++){
    		str[j]=lex.get(i).toString();
    		if(lex.get(i).toString().equals(":")){				//read ":"
    			if(checkExist(":")==false){
    				temp_t.add(":");
    				Symbol temp=new Symbol(":",COLON_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals(";")){			//read ";"
    			if(checkExist(";")==false){
    				temp_t.add(";");
    				Symbol temp=new Symbol(";",SEMICOLON_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals(" ")){			//read " "
    			continue;
    		}else if(lex.get(i).toString().equals(",")){			//read " "
    			if(checkExist(",")==false){
    				temp_t.add(",");
    				Symbol temp=new Symbol(",",COMMA_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("(")){			//read "("
    			if(checkExist("(")==false){
    				temp_t.add("(");
    				Symbol temp=new Symbol("(",LEFTPAREN_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals(")")){			//read ")"
    			if(checkExist(")")==false){
    				temp_t.add(")");
    				Symbol temp=new Symbol(")",RIGHTPAREN_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("{")){			//read "{"
    			if(checkExist("{")==false){
    				temp_t.add("{");
    				Symbol temp=new Symbol("{",LEFTBRACE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("}")){			//read "}"
    			if(checkExist("}")==false){
    				temp_t.add("}");
    				Symbol temp=new Symbol("}",RIGHTBRACE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("%")){			//read "%"
    			if(checkExist("%")==false){
    				temp_t.add("%");
    				Symbol temp=new Symbol("%",MOD_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("end")){		//read "end"
    			if(checkExist("end")==false){
    				temp_t.add("end");
    				Symbol temp=new Symbol("end",EOF_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("int")){		//read "int"
    			if(checkExist("int")==false){
    				temp_t.add("int");
    				Symbol temp=new Symbol("int",TYPE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("if")){		//read "if"
    			if(checkExist("if")==false){
    				temp_t.add("if");
    				Symbol temp=new Symbol("if",IF_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("else")){		//read "else"
    			if(checkExist("else")==false){
    				temp_t.add("else");
    				Symbol temp=new Symbol("else",ELSE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("while")){	//read "while"
    			if(checkExist("while")==false){
    				temp_t.add("while");
    				Symbol temp=new Symbol("while",WHILE_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("for")){		//read "for"
    			if(checkExist("for")==false){
    				temp_t.add("for");
    				Symbol temp=new Symbol("for",FOR_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("+")){			//read "+"
    			if(checkExist("+")==false){
    				temp_t.add("+");
    				Symbol temp=new Symbol("+",PLUS_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("-")){			//read "-"
    			if(checkExist("-")==false){
    				temp_t.add("-");
    				Symbol temp=new Symbol("-",MINUS_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("*")){			//read "*"
    			if(checkExist("*")==false){
    				temp_t.add("*");
    				Symbol temp=new Symbol("*",MULT_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("/")){			//read "/"
    			if(checkExist("/")==false){
    				temp_t.add("/");
    				Symbol temp=new Symbol("/",DIV_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("and")){		//read "and"
    			if(checkExist("and")==false){
    				temp_t.add("and");
    				Symbol temp=new Symbol("and",AND_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("or")){		//read "or"
    			if(checkExist("or")==false){
    				temp_t.add("or");
    				Symbol temp=new Symbol("or",OR_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("=")){			//read "="
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
    		}else if(lex.get(i).toString().equals(">")){			//read ">"
    			if(str[1]==null){
    				j=1;
    				//continue;
    			}
    		}else if(lex.get(i).toString().equals("<")){			//read "<"
    			if(str[1]==null){
    				j=1;
    				//continue;
    			}
    		}else if(lex.get(i).toString().equals("begin")){		//read "begin"
    			if(checkExist("begin")==false){
    				temp_t.add("begin");
    				Symbol temp=new Symbol("begin",BEGIN_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}else if(lex.get(i).toString().equals("#")){			//read "#"
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
    			if(checkExist(lex.get(i).toString())==false){
    				
    				temp_t.add(lex.get(i).toString());
    				Symbol temp=new Symbol(lex.get(i).toString(),IDENTIFIER_TOKEN);
    				SymbolTable.add(temp);
    			}
    		}
		}
    	
		return SymbolTable;
	}
    
    public LinkedList<varValue> value(String str){					//產生有個varValue class去存 變數名稱及值
    	LinkedList<String> lex=new LinkedList();
    	LinkedList<String> temp_loop=new LinkedList();
    	//LinkedList<varValue> varValue=new LinkedList();
    	String temp="";
    	if(str.contains("int")){
    		StringTokenizer st=new StringTokenizer(str,"()><,; ",false);
    		while(st.hasMoreElements()){
   			 lex.add(st.nextToken());
    		}
    		for(int i=0;i<lex.size()-1;i++){
    			temp=lex.get(i+1).toString();
    			if(temp.contains("=")){
    				String s=temp;
    				String [] substring=s.split("=");
    				if(substring[1].contains(".")){
    					System.out.println("Not Integer");			//如果不是整數是小數就會將error_Var=false
    					error_Var=false;
    					//break;
    				}
    				else{
    					varValue.add(addVar(substring[0],Integer.parseInt(substring[1])));
    				}
    			}
    			else{
    				varValue.add(addVar(temp,0));
    			}
    		}
    		
    	}
		return varValue;
    	
    }
    public boolean lexError(String str, int row){							//判斷結尾是否是";"
    	LinkedList<String> sl=new LinkedList();
    	if(str.contains("#")){
    		String[] substr=str.split("#");
    		str=substr[0];
    		sl=genLexeme(str);
    		if(sl.get(sl.size()-1).equals(";")){
    			;
    		}
    		else{
    			String error="Mistaken on "+row+" column ,Because ;";
    			ErrorMessage.add(error);
    			error_Flag=false;
    		}
    	}
    	else{
    		sl=genLexeme(str);
    		if(sl.get(sl.size()-1).equals(";")){
    			;
    		}
    		else{
    			String error="Mistaken on "+row+" column ,Because ;";
    			ErrorMessage.add(error);
    			error_Flag=false;
    		}
    	}
    	
    	
    	/*if(error_Flag==false){
    		System.out.println("Error");
    	}*/
    	return error_Flag;
    }
    public void computeLoop(String str){							//計算for/if/while/end個數
    	if(str.contains("for")){
    		for_Num++;
    	}
    	if(str.contains("while")){
    		while_Num++;
    	}
    	if(str.contains("if")){
    		if_Num++;
    	}
    	if(str.contains("end")){
    		end_Num++;
    	}
    	if(str.contains("begin")){
    		begin_Num++;
    	}
    }
    public boolean loopError(){										//判斷for+if+while-end=0
    	int loop_Num=for_Num+if_Num+while_Num+begin_Num;
    	//System.out.println(loop_Num);
    	//System.out.println(end_Num);
    	if(loop_Num!=end_Num){
    		String error="Loop Error, Check Programe's for,if,while,end";
			ErrorMessage.add(error);
    		error_Loop=false;
    		//System.out.println("Please check your if/while/for/end");
    	}
    	return error_Loop;
    }
    public boolean beginError(){
    	if(begin_Num!=1){
    		String error="Begin Error, Check Programe's Begin";
			ErrorMessage.add(error);
    		error_Begin=false;
    	}
    	return error_Begin;
    }
    public boolean typeError(){										//判斷是否有無法辨別的型態如:float...
    	String str="";
    	int i;
    	for(i=0;i<SymbolTable.size();i++){
    		if(SymbolTable.get(i).getSymToken().equals("IDENTIFIER_TOKEN")){
    			if(checkVar(SymbolTable.get(i).getSymType())==false){
    				if(Numornot(SymbolTable.get(i).getSymType())==true){
    					continue;
    				}
    				else{
    					System.out.println(SymbolTable.get(i).getSymType());
    					String error="Type Error, Check Programe's Type";
    					ErrorMessage.add(error);
    					error_Type=false;
    				}
    			}
    			else{
    				continue;
    			}
    		}
    	}
    	return error_Type;
    }
    public boolean getError_Var(){
    	return error_Var;
    }
    public boolean getError_Flag(){
    	return error_Flag;
    }
	public static void main(String[] args) {
		LinkedList<String> lex=new LinkedList();					//以下是LexicalAnalyzer的寫法
		LinkedList<Symbol> s=new LinkedList();
		LinkedList<varValue> v=new LinkedList();
		LexicalAnalyzer LexicalAnalyzer=new LexicalAnalyzer();
		String line;
		int row=1;
		try{
			FileReader fr = new FileReader("test_p.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				if(LexicalAnalyzer.lexError(line,row)==false){
					break;
				}else{
					LexicalAnalyzer.computeLoop(line);				//計算for/if/while/end number
					lex=LexicalAnalyzer.genLexeme(line);			//產生Lexeme
					s=LexicalAnalyzer.genSymbolTable(lex);			//產生SymbolTable
					v=LexicalAnalyzer.value(line);					//產生 var name and value
					row++;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0;i<s.size();i++){
    		System.out.println(s.get(i).toString());
    	}
		for(int i=0;i<v.size();i++){
    		System.out.println(v.get(i).toString());
    	}
		/**這個為True才可以做接下來的動作**/
		if(LexicalAnalyzer.getError_Var()==true&&LexicalAnalyzer.loopError()==true
				&&LexicalAnalyzer.typeError()==true&&LexicalAnalyzer.getError_Flag()&&LexicalAnalyzer.beginError()==true){						
			System.out.println("Success");
		}
		else{
			for(int i=0;i<ErrorMessage.size();i++){
				System.err.println(ErrorMessage.get(i).toString());
			}
			//System.out.println("false");
		}
		//a.value("int x=1,yy=5,zzz=6,i;");
	}
}