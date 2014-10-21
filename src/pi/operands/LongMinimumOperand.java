package pi.operands;

public class LongMinimumOperand extends Operand<Long> {

	LongMinimumOperand(Long value) {
		super(value);
	}

	@Override
	protected Long opearte(Operand<Long> longOperand) {
		return Math.min(operandValue, longOperand.getValue());
	}

}
