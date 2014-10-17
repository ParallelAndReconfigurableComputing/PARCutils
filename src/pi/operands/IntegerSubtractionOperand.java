package pi.operands;

public class IntegerSubtractionOperand extends Operand<Integer> {

	IntegerSubtractionOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer opearte(Operand<Integer> integerOperand) {
		return operandValue - integerOperand.getValue();
	}

}
