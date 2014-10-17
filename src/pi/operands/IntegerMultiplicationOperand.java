package pi.operands;

public class IntegerMultiplicationOperand extends Operand<Integer> {

	IntegerMultiplicationOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer opearte(Operand<Integer> integerOperand) {
		return operandValue * integerOperand.getValue();
	}

}
