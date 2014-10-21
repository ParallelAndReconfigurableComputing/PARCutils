package pi.operands;

public class BooleanXOROperand extends Operand<Boolean> {

	BooleanXOROperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean operate(Operand<Boolean> booleanOperand) {
		return ( (!operandValue && booleanOperand.getValue()) || (operandValue && !booleanOperand.getValue()) );
	}

}
