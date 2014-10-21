package pi.operands;

public class FloatSubtractionOperand extends Operand<Float> {

	FloatSubtractionOperand(Float value) {
		super(value);
	}

	@Override
	protected Float operate(Operand<Float> floatOperand) {
		return operandValue - floatOperand.getValue();
	}

}
