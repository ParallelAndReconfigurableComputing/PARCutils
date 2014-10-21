package pi.operands;

public class LongBitwiseANDOperand extends Operand<Long> {

	LongBitwiseANDOperand(Long value) {
		super(value);
	}

	@Override
	protected Long operate(Operand<Long> longOperand) {
		return operandValue & longOperand.getValue();
	}
}
