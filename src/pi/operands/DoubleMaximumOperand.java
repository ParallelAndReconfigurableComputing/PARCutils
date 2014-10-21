package pi.operands;

public class DoubleMaximumOperand extends Operand<Double> {

	DoubleMaximumOperand(Double value) {
		super(value);
	}

	@Override
	protected Double operate(Operand<Double> doubleOperand) {
		return Math.max(operandValue, doubleOperand.getValue());
	}

}
