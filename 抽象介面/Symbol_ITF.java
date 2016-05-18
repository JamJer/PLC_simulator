
public interface Symbol_ITF {         //For implemented to be a object.
	// attributes
	public String symbolType = null;  // types of variable,identifier or operator..
	public String symbolValue = null; // Any value. e.g. "{","12","if","begin"....
	public boolean isHidden = false;  // Hidden for different scope.
	
	// Functions
	public String toString();         // Print the Symbol info.
}
