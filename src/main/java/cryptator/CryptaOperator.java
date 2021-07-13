package cryptator;

public enum CryptaOperator {

	ADD("+"), SUB("-"), MUL("*"), DIV("/"), POW("^"), ID(""),
	EQ("=="), NEQ("!="), LEQ("<="), GEQ(">=");
	
	public final String operator;

	private CryptaOperator(String operator) {
		this.operator = operator;
	}

	public final String getOperator() {
		return operator;
	}
	
	
	
}
