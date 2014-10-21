package pi.operands;

public class IntegerBitwiseOROperand extends Operand<Integer> {

	IntegerBitwiseOROperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer operate(Operand<Integer> integerOperand) {
		return operandValue | integerOperand.getValue();
	}

}
