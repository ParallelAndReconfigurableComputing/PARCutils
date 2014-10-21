package pi.operands;

public class BooleanBitwiseOROperand extends Operand<Boolean> {

	BooleanBitwiseOROperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean opearte(Operand<Boolean> booleanOperand) {
		return operandValue | booleanOperand.getValue();
	}

}
