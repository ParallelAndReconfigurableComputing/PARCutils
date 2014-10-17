package pi.operands;

public class DoubleSumOperand extends Operand<Double> {

	DoubleSumOperand(Double value) {
		super(value);
	}

	@Override
	protected Double opearte(Operand<Double> doubleOperand) {
		return operandValue + doubleOperand.getValue();
	}

}
