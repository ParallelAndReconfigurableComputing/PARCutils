package pi.operands;

public class FloatMinimumOperand extends Operand<Float> {

	FloatMinimumOperand(Float value) {
		super(value);
	}

	@Override
	protected Float opearte(Operand<Float> floatOperand) {
		return Math.min(operandValue, floatOperand.getValue());
	}

}
