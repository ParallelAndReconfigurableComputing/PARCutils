package pi.operands;

public class DoubleMultiplicationOperand extends Operand<Double> {

	DoubleMultiplicationOperand(Double value) {
		super(value);
	}

	@Override
	protected Double operate(Operand<Double> doubleOperand) {
		return (Double)(operandValue * doubleOperand.getValue());
	}

}
