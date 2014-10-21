package pi.operands;

public class LongMultiplicationOperand extends Operand<Long> {

	LongMultiplicationOperand(Long value) {
		super(value);
	}

	@Override
	protected Long operate(Operand<Long> longOperand) {
		return (Long)(operandValue * longOperand.getValue());
	}

}
