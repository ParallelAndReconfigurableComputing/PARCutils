package pi.operands;

public class BooleanOrOperand extends Operand<Boolean> {

	BooleanOrOperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean opearte(Operand<Boolean> booleanOperand) {
		return operandValue || booleanOperand.getValue() ;
	}

}
