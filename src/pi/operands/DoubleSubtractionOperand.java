package pi.operands;

public class DoubleSubtractionOperand extends Operand<Double> {

	DoubleSubtractionOperand(Double value) {
		super(value);
	}

	@Override
	protected Double operate(Operand<Double> doubleOperand) {
		return operandValue - doubleOperand.getValue();
	}

}
