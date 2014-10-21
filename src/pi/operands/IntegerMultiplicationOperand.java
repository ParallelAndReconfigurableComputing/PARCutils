package pi.operands;

public class IntegerMultiplicationOperand extends Operand<Integer> {

	IntegerMultiplicationOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer operate(Operand<Integer> integerOperand) {
		return operandValue * integerOperand.getValue();
	}

}
