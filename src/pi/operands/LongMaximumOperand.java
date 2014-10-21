package pi.operands;

public class LongMaximumOperand extends Operand<Long> {

	LongMaximumOperand(Long value) {
		super(value);
	}

	@Override
	protected Long operate(Operand<Long> longOperand) {
		return Math.max(operandValue, longOperand.getValue());
	}

}
