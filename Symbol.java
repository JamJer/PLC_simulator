public class Symbol {
	public String symType;
	public String symToken;
	public Symbol(){
		symType=null;
		symToken=null;
	}
	public Symbol(String s,String i){
		this.symType=s;
		this.symToken=i;
	}
	public void setSymType(String Type){
		this.symType=Type;
	}
	public void setSymToken(String Value){
		this.symToken=Value;
	}
	public String getSymType(){
		return this.symType;
	}
	public String getSymToken(){
		return this.symToken;
	}
	public String toString(){
		return "Type "+this.symType+" Value "+this.symToken;
	}
}
