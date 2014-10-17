package pi.operands;

public class IntegerSumOperand extends Operand<Integer> {

	IntegerSumOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer opearte(Operand<Integer> integerOperand) {
		return operandValue + integerOperand.getValue();
	}

}
