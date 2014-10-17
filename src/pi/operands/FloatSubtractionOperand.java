package pi.operands;

public class FloatSubtractionOperand extends Operand<Float> {

	FloatSubtractionOperand(Float value) {
		super(value);
	}

	@Override
	protected Float opearte(Operand<Float> floatOperand) {
		return operandValue - floatOperand.getValue();
	}

}
