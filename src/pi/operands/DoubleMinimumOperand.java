package pi.operands;

public class DoubleMinimumOperand extends Operand<Double> {

	DoubleMinimumOperand(Double value) {
		super(value);
	}

	@Override
	protected Double opearte(Operand<Double> doubleOperand) {
		return Math.min(operandValue, doubleOperand.getValue());
	}

}
