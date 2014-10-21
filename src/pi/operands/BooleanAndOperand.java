package pi.operands;

public class BooleanANDOperand extends Operand<Boolean> {

	BooleanANDOperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean opearte(Operand<Boolean> booleanOperand) {
		return operandValue && booleanOperand.getValue();
	}

}
