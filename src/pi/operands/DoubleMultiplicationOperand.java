package pi.operands;

public class DoubleMultiplicationOperand extends Operand<Double> {

	DoubleMultiplicationOperand(Double value) {
		super(value);
	}

	@Override
	protected Double opearte(Operand<Double> doubleOperand) {
		return operandValue * doubleOperand.getValue();
	}

}
