package pi.operands;

public class BooleanOROperand extends Operand<Boolean> {

	BooleanOROperand(Boolean value) {
		super(value);
	}

	@Override
	protected Boolean operate(Operand<Boolean> booleanOperand) {
		return operandValue || booleanOperand.getValue() ;
	}

}
