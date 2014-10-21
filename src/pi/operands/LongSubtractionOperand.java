package pi.operands;

public class LongSubtractionOperand extends Operand<Long> {

	LongSubtractionOperand(Long value) {
		super(value);
	}

	@Override
	protected Long opearte(Operand<Long> longOperand) {
		return operandValue - longOperand.getValue();
	}

}
