package pi.operands;

public class LongMaximumOperand extends Operand<Long> {

	LongMaximumOperand(Long value) {
		super(value);
	}

	@Override
	protected Long opearte(Operand<Long> longOperand) {
		return Math.max(operandValue, longOperand.getValue());
	}

}
