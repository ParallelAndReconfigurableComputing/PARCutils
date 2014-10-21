package pi.operands;

public class IntegerMinimumOperand extends Operand<Integer> {

	IntegerMinimumOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer opearte(Operand<Integer> integerOperand) {
		return Math.min(operandValue, integerOperand.getValue());
	}

}
