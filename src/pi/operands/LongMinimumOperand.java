package pi.operands;

public class LongMinimumOperand extends Operand<Long> {

	LongMinimumOperand(Long value) {
		super(value);
	}

	@Override
	protected Long operate(Operand<Long> longOperand) {
		return Math.min(operandValue, longOperand.getValue());
	}

}
