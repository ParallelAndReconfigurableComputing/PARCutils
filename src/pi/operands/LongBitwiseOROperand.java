package pi.operands;

public class LongBitwiseOROperand extends Operand<Long> {

	LongBitwiseOROperand(Long value) {
		super(value);
	}

	@Override
	protected Long operate(Operand<Long> longOperand) {
		return operandValue | longOperand.getValue();
	}

}
