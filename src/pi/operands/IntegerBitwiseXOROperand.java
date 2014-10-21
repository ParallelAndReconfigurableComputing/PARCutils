package pi.operands;

public class IntegerBitwiseXOROperand extends Operand<Integer> {

	IntegerBitwiseXOROperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer operate(Operand<Integer> integerOperand) {
		return operandValue ^ integerOperand.getValue();
	}

}
