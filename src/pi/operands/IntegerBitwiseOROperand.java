package pi.operands;

public class IntegerBitwiseOROperand extends Operand<Integer> {

	IntegerBitwiseOROperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer opearte(Operand<Integer> integerOperand) {
		return operandValue | integerOperand.getValue();
	}

}
