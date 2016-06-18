import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
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
	public static int tab_num=1;
	public boolean lexemeSuccess=true;
	public static LinkedList<String>ErrorMessage=new LinkedList();
	public LinkedList<Symbol> SymbolTable=new LinkedList();			//Symbol Table
	public LinkedList<String> temp_t=new LinkedList();				//temp data
	public LinkedList<varValue> varValue=new LinkedList();			//var Value
	public boolean error_Var=true;									/**this 4 boolean respective 4 error**/
	public boolean error_Type=true;
	public boolean error_Flag=true;
	public boolean error_Loop=true;
	public boolean error_Begin=true;
	static final int BEGIN_TOKEN = 0;								//begin token
	static final int COLON_TOKEN = 1;		 						//: token
	static final int SEMICOLON_TOKEN = 2;							//; token
	static final int LEFTPAREN_TOKEN = 3;							//( token
	static final int RIGHTPAREN_TOKEN = 4;							//) token
	static final int LEFTBRACE_TOKEN = 5;							//{ token
	static final int RIGHTBRACE_TOKEN = 6;							//} token
	static final int MOD_TOKEN = 7;									//% token
	static final int EOF_TOKEN = 8;									//end of stream token
	static final int TYPE_TOKEN =  9;								//int token
	static final int IF_TOKEN = 10;									//if token
	static final int ELSE_TOKEN = 11;								//else token
	static final int WHILE_TOKEN = 12;								//while token
	static final int FOR_TOKEN = 13;								//for token
	static final int IDENTIFIER_TOKEN = 14;							// 英文字母 及數字token
	static final int PLUS_TOKEN = 15;								//+ token
	static final int MINUS_TOKEN = 16;								//- token
	static final int MULT_TOKEN = 17;								//* token
	static final int DIV_TOKEN = 18;								//"/" token 
	static final int EQUAL_TOKEN = 19;								//==token
	static final int ASSIGN_TOKEN = 20;								//= token
	static final int GREATER_TOKEN = 21;							//> token
	static final int GREATEREQUAL_TOKEN = 22;						//>=token
	static final int LESS_TOKEN = 23;								//< token
	static final int LESSEQUAL_TOKEN = 24;							//<=token
	static final int AND_TOKEN = 25;								//and token
	static final int OR_TOKEN = 26;									//or token
	static final int HASHTAG_TOKEN = 27;							//# token
	static final int COMMA_TOKEN = 28;								//, token
	
	LinkedList<varValue> var=new LinkedList<varValue>();
	
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
    		}else if(lex.get(i).toString().equals(",")){			//read ","
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
    	str=str.replace(" ", "");
    	
    	if(str.contains("int")){
    		StringTokenizer st=new StringTokenizer(str,"()><;, ",false);
    		while(st.hasMoreElements()){
   			 lex.add(st.nextToken());
    		}
    		for(int i=0;i<lex.size();i++){
    			temp=lex.get(i).toString();
    			if(temp.contains("=")){
    				String s=temp;
    				if(s.contains("int")){
    					s=s.replace("int", "");
    				}
    				else{
    					;
    				}
    				String [] substring=s.split("=");
    				substring[1].replace(" ", "");
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
    				if(temp.contains("int")){
						temp=temp.replace("int", "");
					}
    				varValue.add(addVar(temp,0));
    			}
    		}
    		
    	}
		return varValue;
    	
    }
    public boolean lexError(String str, int row){							//判斷結尾是否是";"
    	LinkedList<String> sl=new LinkedList();
    	str=str.replace(" ", "");
    	if(str.contains("#")){
    		if(str.startsWith("#")){
    			;
        	}
    		else{
    			String[] substr=str.split("#");
    			str=substr[0];
    			sl=genLexeme(str);
    			if(sl.get(sl.size()-1).equals("")||sl.get(sl.size()-1).equals(";")){
    				;
    			}
    			else{
    				String error="Mistaken on "+row+" column ,Because ;";
    				ErrorMessage.add(error);
    				error_Flag=false;
    			}
    		}
    		
    	}
    	if(str.equals("")||str.startsWith("#")){
    		;
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
    	return error_Flag;
    }
    public void computeLoop(String str){							//計算for/if/while/end個數
    	if(str.startsWith("#")){
    		;
    	}
    	else{
    		if(str.contains("for")){
    			//System.out.println("for");
    			for_Num++;
    		}
    		if(str.contains("while")){
    			//System.out.println("while");
    			while_Num++;
    		}
    		if(str.contains("if")){
    			//System.out.println("if");
    			if_Num++;
    		}
    		if(str.contains("end")){
    			//System.out.println("end");
    			end_Num++;
    		}
    		if(str.contains("begin")){
    			//System.out.println("begin");
    			begin_Num++;
    		}
    	}
    	
    }
    public boolean loopError(){										//判斷for+if+while-end=0
    	boolean errors=true;
    	int loop_Num=for_Num+if_Num+while_Num+begin_Num;
    	//System.out.println(loop_Num);
    	//System.out.println(end_Num);
    	if(loop_Num!=end_Num){
    		String error="Loop Error, Check Programe's for,if,while,end and begin";
			ErrorMessage.add(error);
			errors=false;
    		//System.out.println("Please check your if/while/for/end");
    	}
    	return errors;
    }
    public boolean beginError(){
    	boolean errors=true;
    	if(begin_Num!=1){
    		String error="Begin Error, Check Programe's Begin";
			ErrorMessage.add(error);
			errors=false;
    	}
    	return errors;
    }
    public boolean typeError(){										//判斷是否有無法辨別的型態如:float...
    	boolean type=true;
    	String str="";
    	int i;
    	for(i=0;i<SymbolTable.size();i++){
    		if(SymbolTable.get(i).getSymToken()==14){
    			if(checkVar(SymbolTable.get(i).getSymType())==false){
    				if(Numornot(SymbolTable.get(i).getSymType())==true){
    					continue;
    				}
    				else{
    					System.out.println(SymbolTable.get(i).getSymType());
    					String error="Type Error, Check Programe's Type";
    					ErrorMessage.add(error);
    					type=false;
    				}
    			}
    			else{
    				continue;
    			}
    		}
    	}
    	return type;
    }
    public boolean getVarError(){									//得到數字非整數的錯誤
    	return error_Var;
    }
    public boolean getFlagError(){									//得到結尾非;的錯誤
    	return error_Flag;
    }
    public boolean getBeginError(){									//得到缺少begin的錯誤
    	return error_Begin;
    }
    public boolean getTypeError(){									//得到不支援保留字的錯誤
    	return error_Type;
    }
    public boolean getLoopError(){									//得到if,while,for,end的錯誤
    	return error_Loop;
    }
    public boolean isLexSuccess(){									//true的話 代表詞法分析成功
    	return lexemeSuccess;										//false 則須利用getErrorMessage函式印出錯誤訊息
    }
    public static String preprocess(String line){
    	String s=line;
    	s=s.replace(" ", "");
    	if(s.startsWith("\t")||s.startsWith(" ")){
    		s=s.replace("\t", "");
    		s=s.replace(" ", "");
    		if(s.contains("#")){
    			if(s.startsWith("#")){
    				;
    			}
    			else{
    				String[] sub=s.split("#");
    				s=sub[0];
    			}
    		}
    	}
    	else{
    		if(s.contains("#")){
    			if(s.startsWith("#")){
    				//System.out.println(line+" 559");
    				;
    			}
    			else{
    				String[] sub=s.split("#");
    				s=sub[0];
    			}
    		}
    		else{
    			if(s.contains("int")){
    				s=s.replace("int", "int ");
    			}
    		}
    	}
    	return s;
    }
    public void doLex(LinkedList<String> codeStrlls ){				//詞法分析器本體，一定要執行
    	String line="";
    	int row=1;
    	LinkedList<String> lex=new LinkedList();
    	LinkedList<Symbol> s=new LinkedList();
    	LinkedList<varValue> v=new LinkedList();
    	for(int i=0;i<codeStrlls.size();i++){
    		line=preprocess(codeStrlls.get(i).toString());
    		if(line.startsWith("#")){
    			line=line.replace(line, "");
    		}
    		//System.out.println(line+" 587");
    		if(lexError(line,row)==false){
    			lexemeSuccess=false;
    			break;
    		}else{
    			computeLoop(line);
    			lex=genLexeme(line);
    			s=genSymbolTable(lex);
    			v=value(line);
    			row++;
    		}
    	}
    	var=v;
    	SymbolTable=s;
    	error_Begin=beginError();
    	error_Type=typeError();
    	error_Loop=loopError();
    	if(error_Begin==true&&error_Type==true&&error_Loop==true&&getVarError()==true&&getFlagError()==true){
    		lexemeSuccess=true;
    	}
    	else{
    		lexemeSuccess=false;
    	}
    }
    public LinkedList<Symbol> getSymbolTable(){						//得到SymbolTable
    	return SymbolTable;
    }
    public LinkedList<varValue> getVarValue(){						//得到變數值
    	return var;
    }
    public LinkedList<String> getErrorMessage(){					//得到錯誤訊息
    	return ErrorMessage;
    }
   
    public static void main(String[] args){
    	LexicalAnalyzer LexicalAnalyzer=new LexicalAnalyzer();
    	LinkedList<varValue> temp = null;
    	String line="";
    	LinkedList<String> pro=new LinkedList();	
    	try{
			FileReader fr = new FileReader("abc.txt");
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				pro.add(line);
			}
			LexicalAnalyzer.doLex(pro);
			if(LexicalAnalyzer.isLexSuccess()){
				System.out.println("Success");
				temp = LexicalAnalyzer.getVarValue();
				for(int i=0;i<temp.size();i++){
					System.out.println(temp.get(i).getVar()+" "+temp.get(i).getValue());
				}
			}
			else{
				LinkedList<String> err = LexicalAnalyzer.getErrorMessage();
				for(int i=0;i<err.size();i++){
					System.out.println(err.get(i));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
    }
}