import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

public class LoopCaseTool {
	private LinkedList<varValue> varlist = null;
	private LinkedList<String> codeStrlls = null;
	private MathCalcuTool mathCalcuTool = null;
	private IfCaseTool ifCaseHander = null;
	private boolean isForCaseFirst = true;
	
	public LoopCaseTool(LinkedList<varValue> varlist,LinkedList<String> codeStrlls,MathCalcuTool mathCalcuTool){
		this.mathCalcuTool = mathCalcuTool;
		ifCaseHander = new IfCaseTool(varlist, codeStrlls);
		this.varlist = varlist;
		this.codeStrlls = codeStrlls;
	}
	
	public boolean checkLoopCase(String line){
		if(line.contains("for")){
			line = line.replace("for ", "");
			String[] temp = line.split(" ");
			if(isForCaseFirst){
				mathCalcuTool.compute(temp[0]);
				if(ifCaseHander.checkIfIfCondition(temp[1])){
					isForCaseFirst = false;
					return true;
				}else {
					return false;
				}
			}else {
				String addedVar = temp[0].split("=")[0];
				String addvar = temp[2];
				mathCalcuTool.compute(addedVar+"="+addedVar+"+"+addvar);
				if(ifCaseHander.checkIfIfCondition(temp[1])){
					return true;
				}else {
					isForCaseFirst = true;
					return false;
				}
			}
		}
		if(line.contains("while")){
			line = line.replace("while ", "");
			System.out.println(ifCaseHander.checkIfIfCondition(line));
			return ifCaseHander.checkIfIfCondition(line);
		}
	    System.out.println("System compare loop expression faild!");
	    return false;
	}
	
	public static void main(String[] args){
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
		LoopCaseTool user = new LoopCaseTool(temp,null,null);
		System.out.println(user.checkLoopCase("for i=3 i<6 j"));
		System.out.println(user.checkLoopCase("for i=3 i<6 j"));
	}
}
