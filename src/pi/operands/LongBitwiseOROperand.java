package pi.operands;

public class LongBitwiseOROperand extends Operand<Long> {

	LongBitwiseOROperand(Long value) {
		super(value);
	}

	@Override
	protected Long opearte(Operand<Long> longOperand) {
		return operandValue | longOperand.getValue();
	}

}
