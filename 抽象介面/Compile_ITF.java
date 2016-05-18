import java.util.LinkedList;

public interface Compile_ITF {
	// attributes
	public LinkedList<String> codeData = null;         // For saving every line from
												       // code text.
	public LinkedList<Symbol_ITF> symbolTable = null;  // List table for saving
													   // symbols.
	public LinkedList<ErrorMsg_ITF> errorMsg = null;   // Error messages.
	public Debuger_ITF debuger = null;

	// functions
	public void initial();            // A function for initialing entries.
	public void initialSymbolTable(); // Initial table with reserved words and
									  // indentifiers.
	/*
	 * Here calls the below functions 
	 * debuger.lexicalParser
	 * debuger.syntaxParser
	 */
}
