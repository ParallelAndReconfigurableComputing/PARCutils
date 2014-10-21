package pi.operands;

public class BooleanBitwiseANDOperand extends Operand<Boolean> {

	BooleanBitwiseANDOperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean opearte(Operand<Boolean> booleanOperand) {
		return operandValue & booleanOperand.getValue();
	}

}
