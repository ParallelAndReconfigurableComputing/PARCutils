package pi.operands;

public class FloatMaximumOperand extends Operand<Float> {

	FloatMaximumOperand(Float value) {
		super(value);
	}

	@Override
	protected Float operate(Operand<Float> floatOperand) {
		return Math.max(operandValue, floatOperand.getValue());
	}

}
