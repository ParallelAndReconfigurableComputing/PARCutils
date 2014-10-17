package pi.operands;

public class BooleanAndOperand extends Operand<Boolean> {

	BooleanAndOperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean opearte(Operand<Boolean> booleanOperand) {
		return operandValue && booleanOperand.getValue();
	}

}
