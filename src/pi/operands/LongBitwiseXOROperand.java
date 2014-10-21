package pi.operands;

public class LongBitwiseXOROperand extends Operand<Long> {

	LongBitwiseXOROperand(Long value) {
		super(value);
	}

	@Override
	protected Long opearte(Operand<Long> longOperand) {
		return operandValue ^ longOperand.getValue();
	}

}
