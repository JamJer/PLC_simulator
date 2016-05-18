
public interface ErrorMsg_ITF {
	// Attribute
	public final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
	
	//LEXICAL ERROR
	public final String LEXICAL_PARSING_ERROR = "LEXICAL_PARSING_ERROR";
	public final String UNKNOWN_IDENTIFIER = "UNKNOWN_IDENTIFIER";
	public final String VALUE_OUT_OF_RANGE = "VALUE_OUT_OF_RANGE";
	public final String UNINITIALIZED_VARIABLE = "UNINITIALIZED_VARIABLE";
	public final String DUPLICATED_VARIABLE = "DUPLICATED_VARIABLE";
	//TO be added..
	
	//SYNTAX ERROR
	public final String SYNTAX_PARSING_ERROR = "SYNTAX_PARSING_ERROR";
	public final String STACK_OUT_OF_RANGE = "STACK_OUT_OF_RANGE";
	public final String CONVERT_TYPE_ERROR = "CONVERT_TYPE_ERROR";
	public final String MISSING_TOKEN = "MISSING_TOKEN";
	//TO be added..
	
	public String errorMsg = null;
	public int errorLine = -1;
	
	// function
	public void SetErrorMsg(int errCode);
	public String getErrorMsg();
	public String toString();
}
