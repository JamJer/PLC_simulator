import java.util.LinkedList;

public class IfCaseTool {
	private LinkedList<varValue> varlist = null;
	private LinkedList<String> codeStrlls = null;
	private int[] nextJumpIndex = null;
	private int curIfIndex = 0;

	public IfCaseTool(LinkedList<varValue> varlist,LinkedList<String> codeStrlls) {
		this.codeStrlls = codeStrlls;
		this.varlist = varlist;
		this.nextJumpIndex = initIfJumpLineIndex(codeStrlls);
	}

	public boolean checkIfIfCondition(String line) {
		line = line.replace("if", "");
		line = line.replace(" ", "");
		String conType = connectType(line);
		if (conType.equals("none")) {
			int Optype = getcompareOpType(line);
			return compareOp(line, Optype, varlist);
		}
		if (conType.equals("and")) {
			String[] andSplit = line.split("and");
			int OptypeL = getcompareOpType(andSplit[0]);
			int OptypeR = getcompareOpType(andSplit[1]);
			return (compareOp(andSplit[0], OptypeL, varlist) && compareOp(andSplit[1], OptypeR, varlist));
		}
		if (conType.equals("or")) {
			String[] andSplit = line.split("or");
			int OptypeL = getcompareOpType(andSplit[0]);
			int OptypeR = getcompareOpType(andSplit[1]);
			return (compareOp(andSplit[0], OptypeL, varlist) || compareOp(andSplit[1], OptypeR, varlist));
		}
		return false;
	}

	public int getcompareOpType(String str) {
		if (str.contains("!="))
			return -1;
		if (str.contains("=="))
			return 0;
		if (str.contains(">="))
			return 1;
		if (str.contains(">"))
			return 2;
		if (str.contains("<="))
			return 3;
		if (str.contains("<"))
			return 4;
		return -99;
	}

	public boolean compareOp(String str, int Optype, LinkedList<varValue> varlist) {
		String[] temp = null;
		double leftVar = -999;
		double rightVar = -999;

		switch (Optype) {
		case -1:
			temp = str.split("!=");
			leftVar = getVarValue(temp[0], varlist);
			rightVar = Double.parseDouble(temp[1]);
			if (leftVar != rightVar)
				return true;
			break;
		case 0:
			temp = str.split("==");
			leftVar = getVarValue(temp[0], varlist);
			rightVar = Double.parseDouble(temp[1]);
			if (leftVar == rightVar)
				return true;
			break;
		case 1:
			temp = str.split(">=");
			leftVar = getVarValue(temp[0], varlist);
			rightVar = Double.parseDouble(temp[1]);
			if (leftVar >= rightVar)
				return true;
			break;
		case 2:
			temp = str.split(">");
			leftVar = getVarValue(temp[0], varlist);
			rightVar = Double.parseDouble(temp[1]);
			if (leftVar > rightVar)
				return true;
			break;
		case 3:
			temp = str.split("<=");
			leftVar = getVarValue(temp[0], varlist);
			rightVar = Double.parseDouble(temp[1]);
			if (leftVar <= rightVar)
				return true;
			break;
		case 4:
			temp = str.split("<");
			leftVar = getVarValue(temp[0], varlist);
			rightVar = Double.parseDouble(temp[1]);
			if (leftVar < rightVar)
				return true;
			break;
		default:
			System.out.println("wrong compare op type");
			System.exit(1);
			break;
		}
		return false;
	}

	private double getVarValue(String str, LinkedList<varValue> varlist) {
		for (int i = 0; i < varlist.size(); i++) {
			if (varlist.get(i).getVar().equals(str)) {
				return (double)varlist.get(i).getValue();
			}
		}
		return -999;
	}

	private String connectType(String str) {
		if (str.contains("and"))
			return "and";
		if (str.contains("or"))
			return "or";
		return "none";
	}
	
	private int[] initIfJumpLineIndex(LinkedList<String> codells){
		int totalIf = 0;
		int stackIndex = 0;
		boolean checkIf = true;
		
		for(int i=0;i<codells.size();i++){
			if(codells.get(i).contains("if")) totalIf++;
		}
		
		int[] ifNextJumpIndex = new int[totalIf];
		int s = 0;
		for(int i=0;i<codells.size();i++){
			if(checkIf){
				if(codeStrlls.get(i).contains("if")){
					checkIf = false;
				}
			}
			if(!checkIf){
				if(codeStrlls.get(i).contains("end") && codeStrlls.get(i+1).contains("end")){
					checkIf = true;
					ifNextJumpIndex[s] = i+2;
					s++;
					i = i+1;
				}
			}
		}
		return ifNextJumpIndex;
	}

	public void increaseIfJumpIndex(){
		curIfIndex++;
	}
	
	public int[] getIfJumpIndexArr(){
		return nextJumpIndex;
	}
	
	public int getIfJumpIndex(){
		return nextJumpIndex[curIfIndex];
	}
	
	//測試用進入點
	public static void main(String[] args) {
		LinkedList<varValue> temp = new LinkedList<varValue>();
		varValue aValue = new varValue();
		aValue.setVar("x"); aValue.setValue(3);
		varValue bValue = new varValue();
		bValue.setVar("yy"); bValue.setValue(4);
		varValue cValue = new varValue();
		cValue.setVar("zzz"); cValue.setValue(5);
		varValue dValue = new varValue();
		dValue.setVar("i"); dValue.setValue(0);
		varValue eValue = new varValue();
		eValue.setVar("j"); eValue.setValue(2);
		temp.add(aValue);
		temp.add(bValue);
		temp.add(cValue);
		temp.add(dValue);
		temp.add(eValue);
		IfCaseTool user = new IfCaseTool(temp,null);
		System.out.println(user.checkIfIfCondition("if i==0 and zzz != 6"));
	}
}
