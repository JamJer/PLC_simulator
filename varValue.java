
public class varValue {
	public String var;
	public int value;
	
	public varValue(){
		var="";
		value=0;
	}
	public varValue(String s,int i){
		this.var=s;
		this.value=i;
	}
	public void setVar(String Type){
		this.var=Type;
	}
	public void setValue(int Value){
		this.value=Value;
	}
	public String getVar(){
		return this.var;
	}
	public int getValue(){
		return this.value;
	}
	public String toString(){
		return "Var "+this.var+" Value "+this.value;
	}
}
