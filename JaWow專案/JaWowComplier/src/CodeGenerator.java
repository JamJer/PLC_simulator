import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class CodeGenerator {
	private LinkedList<String> codeStrlls = null;   //原始檔案輸入的字串串鍊
	private LinkedList<varValue> codeVarlls = null; //從詞法分析得到的初始變數(包括值)串鍊
	private MathCalcuTool varCalcuHandler = null;   //掌控變數計算的解析運算器
	private IfCaseTool ifCaseHandler = null;        //掌控條件計算的解析運算器
	private LoopCaseTool loopCaseHandler = null;    //掌控迴圈計算的解析運算器
	private int[] ifCaseJumpIndex = null;           //IF失敗的跳躍索引陣列
	private int[] forCaseJumpIndex = null;          //FOR失敗的跳躍索引陣列 
	private int[] whileCaseJumpIndex = null;        //WHILE失敗的跳躍索引陣列
	private int ifCaseLastIndex = 0;                //IF失敗的目前跳躍索引
	private int forCaseLastIndex = 0;               //FOR失敗的目前跳躍索引
	private int whileCaseLastIndex = 0;             //WHILE失敗的目前跳躍索引

	public CodeGenerator(LinkedList<String> codeStrlls, LinkedList<varValue> codeVarlls) {
		this.codeStrlls = codeStrlls;
		this.codeVarlls = codeVarlls;
		replaceDuplicateDeclartion(codeStrlls);
		reSetCodeFormat(codeStrlls);
		deleteNote(codeStrlls);
		initCaseJumpIndexArrays();
		varCalcuHandler = new MathCalcuTool(codeVarlls);
		ifCaseHandler = new IfCaseTool(codeVarlls, codeStrlls);
		loopCaseHandler = new LoopCaseTool(codeVarlls, codeStrlls, varCalcuHandler);
	}

	private void replaceDuplicateDeclartion(LinkedList<String> codells) { // 將程式碼合法的重複變數名稱加上序號
		LinkedList<String> temp = new LinkedList<String>();
		for (int i = 0; i < codells.size(); i++) {
			String str = codells.get(i);
			String[] tempStrArr;
			if (str.contains("int") || str.contains("float")) {
				tempStrArr = getLineVarName(str);
				for (int j = 0; j < tempStrArr.length; j++) {
					int times = checkIsDuplicate(temp, tempStrArr[j]);
					// System.out.println(times+" "+temp+" "+tempStrArr[j]);
					if (times > 0) {
						String temp2 = str.replaceAll(tempStrArr[j], tempStrArr[j] + "_" + times);
						codells.set(i, temp2);
					}
					if (times == 0) {
						if (!tempStrArr[j].equals("")) {
							temp.add(tempStrArr[j]);
						}
					}
				}
			}
			// System.out.println(temp.toString());
		}
	}

	private int checkIsDuplicate(LinkedList<String> lls, String str) { //檢查重複宣告的變數
		int times = 0;
		for (int i = 0; i < lls.size(); i++) {
			if (times == 0) {
				if (str.matches(lls.get(i)))
					times++;
			} else {
				if ((str + "_" + times).matches(lls.get(i)))
					times++;
			}
		}
		return times;
	}

	private void deleteNote(LinkedList<String> codells) { //刪除註解
		for (int i = 0; i < codells.size(); i++) {
			if (codells.get(i).contains("#")) {
				int firstCChap = findFirstCChap(codells.get(i));
				if (firstCChap == 0) {
					codells.remove(i);
					i--;
				} else {
					codells.set(i, codells.get(i).substring(0, firstCChap - 1));
				}
			}
		}
	}

	private int findFirstCChap(String str) {  //找出第一個#
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '#')
				return i;
		}
		return -1;
	}

	private String[] getLineVarName(String str) { //找出每行變數
		str = str.replace(";", "");
		str = str.replace(",", " ");
		str = str.replace("int", "");
		str = str.replace("float", "");
		str = str.replace("=", " ");
		str = str.replaceAll("[0-9]", "");
		return str.split("\\s+");
	}

	private void reSetCodeFormat(LinkedList<String> codells) { // 重製過濾程式碼為較好看的格式
		for (int i = 0; i < codells.size(); i++) {
			String temp = codells.get(i);
			temp = temp.replaceAll(";", "");
			temp = temp.replaceAll(",", " ");
			temp = temp.replace("(", " ");
			temp = temp.replace(")", " ");
			temp = temp.replaceAll("\\s+", " ");
			temp = deleteForwardBlank(temp);
			if (temp.equals("")) {
				codells.remove(i);
				i--;
			} else {
				codells.set(i, temp);
			}
		}
	}

	private String deleteForwardBlank(String str) { // 刪除程式碼前面的空格
		int subindex = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ')
				break;
			if (str.charAt(i) == ' ')
				subindex++;
		}
		return str.substring(subindex);
	}

	private void initCaseJumpIndexArrays() { //初始化跳躍陣列
		int ifindex = 0;
		int forindex = 0;
		int whielindex = 0;
		int ifCaseNum = 0;
		int forCaseNum = 0;
		int whileCaseNum = 0;
		for (int i = 0; i < codeStrlls.size(); i++) {
			if (codeStrlls.get(i).contains("if"))
				ifCaseNum++;
			if (codeStrlls.get(i).contains("for"))
				forCaseNum++;
			if (codeStrlls.get(i).contains("while"))
				whileCaseNum++;
		}
		//System.out.println("IF num: " + ifCaseNum + " FOr num: " + forCaseNum + " while num: " + whileCaseNum);
		ifCaseJumpIndex = new int[ifCaseNum];
		forCaseJumpIndex = new int[forCaseNum];
		whileCaseJumpIndex = new int[whileCaseNum];
		for (int i = 0; i < codeStrlls.size(); i++) {
			switch (findSyntaxType(codeStrlls.get(i))) {
			case 5:
				ifCaseJumpIndex[ifindex] = findFalseJumpindex(i);
				ifindex++;
				break;
			case 3:
				forCaseJumpIndex[forindex] = findFalseJumpindex(i);
				forindex++;
				break;
			case 4:
				whileCaseJumpIndex[whielindex] = findFalseJumpindex(i);
				whielindex++;
				break;
			default:
				break;
			}
		}
		//System.out.println("IF: " + Arrays.toString(ifCaseJumpIndex));
		//System.out.println("FOR: " + Arrays.toString(forCaseJumpIndex));
		//System.out.println("WHILE: " + Arrays.toString(whileCaseJumpIndex));
	}

	private int findFalseJumpindex(int curIndex) { //找出跳躍索引
		int stackTrace = 0;
		boolean checkLock = false;

		if (codeStrlls.get(curIndex + 1).contains("end")) {
			return curIndex + 2;
		}
		for (int i = curIndex + 1; i < codeStrlls.size(); i++) {
			if ((findSyntaxType(codeStrlls.get(i)) == 5) || (findSyntaxType(codeStrlls.get(i)) == 3)
					|| (findSyntaxType(codeStrlls.get(i)) == 4)) {
				stackTrace++;
				checkLock = true;
			}
			if (codeStrlls.get(i).contains("end") && checkLock) {
				stackTrace--;
				checkLock = false;
			}

			if (stackTrace == 0) {
				if (findSyntaxType(codeStrlls.get(i)) == 0) {
					if (codeStrlls.get(i + 1).contains("end")) {
						return i + 2;
					}
				}
				if (codeStrlls.get(i).contains("end") && codeStrlls.get(i + 1).contains("end")) {
					return i + 2;
				}
			}
		}
		return -999;
	}

	private void showingDebug(int cutline) { //偵錯用
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < codeVarlls.size(); j++) {
			System.out.println(codeVarlls.get(j).getVar() + " " + codeVarlls.get(j).getValue());
		}
		System.out.println();
	}

	public void RunCode() { //主執行程序
		int syntaxType = -1;
		int loopBackPointIndex = -1;
		boolean isInForLoop = false;
		boolean isInWhileLoop = false;

		for (int i = 0; i < codeStrlls.size(); i++) { // Line loop
			syntaxType = findSyntaxType(codeStrlls.get(i));
			//System.out.println("Current line: " + (i));
			switch (syntaxType) {
			case 0:
				varCalcuHandler.compute(codeStrlls.get(i));
				if ((i + 1) < codeStrlls.size()) {
					if (codeStrlls.get(i + 1).contains("end")) {
						if (isInForLoop || isInWhileLoop) {
							i = loopBackPointIndex - 1;
						}
					}
				}
				break;
			case 1:
				// DO nothing.
				break;
			case 2:
				if ((i + 1) < codeStrlls.size()) {
					if (codeStrlls.get(i + 1).contains("end")) {
						if (isInForLoop || isInWhileLoop) {
							i = loopBackPointIndex - 1;
						}
					}
				}
				break;
			case 3:
				if (loopCaseHandler.checkLoopCase(codeStrlls.get(i))) {
					isInForLoop = true;
					loopBackPointIndex = i;
				} else {
					i = forCaseJumpIndex[forCaseLastIndex] - 1;
					System.out.println("For case false index: "+i);
					forCaseLastIndex++;
					isInForLoop = false;
				}
				break;
			case 4:
				if (loopCaseHandler.checkLoopCase(codeStrlls.get(i))) {
					isInWhileLoop = true;
					loopBackPointIndex = i;
				} else {
					i = whileCaseJumpIndex[whileCaseLastIndex] - 1;
					whileCaseLastIndex++;
					isInWhileLoop = false;
				}
				break;
			case 5:
				System.out.println("AAAAAAAAAAAAAA");
				if (!ifCaseHandler.checkIfIfCondition(codeStrlls.get(i))) {
					i = ifCaseJumpIndex[ifCaseLastIndex] - 1;
					ifCaseLastIndex++;
				}
				break;
			case 6:
				// DO nothing.
				break;
			default:
				break;
			}
			//showingDebug(i);
		}
	}

	private int findSyntaxType(String str) { //找出每行的語法
		if (str.contains("begin"))
			return 1; // 開始
		if (str.contains("end"))
			return 2; // 結束
		if (str.contains("for"))
			return 3; // 迴圈
		if (str.contains("while"))
			return 4; // 迴圈
		if (str.contains("if"))
			return 5; // 條件
		if (str.contains("int") || str.contains("float") || str.contains("double"))
			return 6; // 宣告
		return 0; // 計算
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader buf = new BufferedReader(new FileReader("abc.txt"));
		LinkedList<String> templls = new LinkedList<String>();
		while (buf.ready()) {
			templls.add(buf.readLine());
		}
		LinkedList<varValue> temp = new LinkedList<varValue>();
		varValue aValue = new varValue();
		aValue.setVar("x");
		aValue.setValue(0);
		varValue bValue = new varValue();
		bValue.setVar("yy");
		bValue.setValue(4);
		varValue cValue = new varValue();
		cValue.setVar("zzz");
		cValue.setValue(5);
		varValue dValue = new varValue();
		dValue.setVar("i");
		dValue.setValue(0);
		varValue eValue = new varValue();
		eValue.setVar("j");
		eValue.setValue(1);
		temp.add(aValue);
		temp.add(bValue);
		temp.add(cValue);
		temp.add(dValue);
		temp.add(eValue);
		CodeGenerator user = new CodeGenerator(templls, temp);

		//user.RunCode();
		//System.out.println("Final result:");
		for (int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getVar() + " " + temp.get(i).getValue());
		}
	}
}
