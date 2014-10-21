package pi.operands;

public class IntegerMaximumOperand extends Operand<Integer> {

	IntegerMaximumOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer operate(Operand<Integer> integerOperand) {
		return Math.max(operandValue, integerOperand.getValue());
	}

}
