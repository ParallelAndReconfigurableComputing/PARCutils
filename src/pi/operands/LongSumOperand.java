package pi.operands;

public class LongSumOperand extends Operand<Long> {

	LongSumOperand(Long value) {
		super(value);
	}

	@Override
	protected Long opearte(Operand<Long> longOperand) {
		return operandValue + longOperand.getValue();
	}

}
