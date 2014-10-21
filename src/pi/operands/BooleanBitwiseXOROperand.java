package pi.operands;

public class BooleanBitwiseXOROperand extends Operand<Boolean> {

	BooleanBitwiseXOROperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean operate(Operand<Boolean> booleanOperand) {
		return operandValue ^ booleanOperand.getValue();
	}

}
