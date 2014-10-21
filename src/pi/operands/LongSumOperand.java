package pi.operands;

public class LongSumOperand extends Operand<Long> {

	LongSumOperand(Long value) {
		super(value);
	}

	@Override
	protected Long operate(Operand<Long> longOperand) {
		return operandValue + longOperand.getValue();
	}

}
