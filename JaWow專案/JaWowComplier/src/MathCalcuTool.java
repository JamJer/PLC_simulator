import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MathCalcuTool {
	private LinkedList<varValue> varlist = null;
	private String[] varName = null;
	private int[] varValue = null;
	private Set<String> varNameSet = null;
	private HashMap<String, Double> expMap = null;
	
	public MathCalcuTool(LinkedList<varValue> varlist){
		this.varlist = varlist;
		varNameSet = new HashSet<String>();
		expMap = new HashMap<String, Double>();
		initData();
	}
	
	private void initData(){
		varName = new String[varlist.size()];
		varValue = new int[varlist.size()];
		for(int i=0;i<varlist.size();i++){
			varName[i] = varlist.get(i).getVar();
			varValue[i] = varlist.get(i).getValue();
			//System.out.println(varName[i]+" "+varValue[i]);
		}
	}
	
	private void reSetData(){
		for(int i=0;i<varName.length;i++){
			varNameSet.add(varName[i]);
			expMap.put(varName[i], (double)varValue[i]);
		}
	}
	
	public void compute(String expMath){
		reSetData();
		expMath = expMath.replace(" ", "");
		String[] temp = expMath.split("=");
		Expression e = new ExpressionBuilder(temp[1])
				.variables(varNameSet)
				.build()
				.setVariables(expMap);
		int result = (int)e.evaluate();
		int resultInedex = findIndex(temp[0]);
		varValue[resultInedex] = result;
		varlist.get(resultInedex).setValue(result);
	}
	
	private int findIndex(String str){
		for(int i=0;i<varName.length;i++){
			if(varName[i].equals(str)) return i;
		}
		return -1;
	}
	
	public static void main(String[] args){
		LinkedList<varValue> temp = new LinkedList<varValue>();
		varValue aValue = new varValue();
		aValue.setVar("x"); aValue.setValue(0);
		varValue bValue = new varValue();
		bValue.setVar("yy"); bValue.setValue(4);
		varValue cValue = new varValue();
		cValue.setVar("zzz"); cValue.setValue(5);
		varValue dValue = new varValue();
		dValue.setVar("i"); dValue.setValue(21);
		varValue eValue = new varValue();
		eValue.setVar("j"); eValue.setValue(1);
		temp.add(aValue);
		temp.add(bValue);
		temp.add(cValue);
		temp.add(dValue);
		temp.add(eValue);
		MathCalcuTool user = new MathCalcuTool(temp);
		user.compute("i = i - 1");
		
		for(int i=0;i<temp.size();i++){
			System.out.println(temp.get(i).getVar()+" "+temp.get(i).getValue());
		}
	}
}
