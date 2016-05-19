
public class Symbol {
	public String symType;
	public String symValue;
	public Symbol(){
		symType=null;
		symValue=null;
	}
	public Symbol(String s,String i){
		this.symType=s;
		this.symValue=i;
	}
	public void setSymType(String Type){
		this.symType=Type;
	}
	public void setSymvalue(String Value){
		this.symValue=Value;
	}
	public String getSymType(){
		return this.symType;
	}
	public String getSymValue(){
		return this.symValue;
	}
	public String toString(){
		return "Type "+this.symType+" Value "+this.symValue;
	}
}
