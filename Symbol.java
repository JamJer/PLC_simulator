public class Symbol {
	public String symType;
	public int symToken;
	public Symbol(){
		symType=null;
		symToken=-1;
	}
	public Symbol(String s,int i){
		this.symType=s;
		this.symToken=i;
	}
	public void setSymType(String Type){
		this.symType=Type;
	}
	public void setSymToken(int Value){
		this.symToken=Value;
	}
	public String getSymType(){
		return this.symType;
	}
	public int getSymToken(){
		return this.symToken;
	}
	public String toString(){
		return "Type "+this.symType+" Value "+this.symToken;
	}
}
