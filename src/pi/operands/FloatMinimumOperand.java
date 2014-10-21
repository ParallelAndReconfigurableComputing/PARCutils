package pi.operands;

public class FloatMinimumOperand extends Operand<Float> {

	FloatMinimumOperand(Float value) {
		super(value);
	}

	@Override
	protected Float operate(Operand<Float> floatOperand) {
		return Math.min(operandValue, floatOperand.getValue());
	}

}
