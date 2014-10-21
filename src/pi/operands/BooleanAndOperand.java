package pi.operands;

public class BooleanANDOperand extends Operand<Boolean> {

	BooleanANDOperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean operate(Operand<Boolean> booleanOperand) {
		return operandValue && booleanOperand.getValue();
	}

}
