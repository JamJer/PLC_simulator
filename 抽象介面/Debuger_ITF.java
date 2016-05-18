import java.util.LinkedList;

public interface Debuger_ITF {
	// Attributes
	public boolean start = false;

	// functions
	public void lexicalParser(LinkedList<String> codeData, LinkedList<Symbol_ITF> symbolTable,
			LinkedList<ErrorMsg_ITF> errorMsg);

	public void syntaxParser(LinkedList<String> codeData, LinkedList<Symbol_ITF> symbolTable,
			LinkedList<ErrorMsg_ITF> errorMsg);
}
