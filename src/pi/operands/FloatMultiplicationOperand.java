package pi.operands;

public class FloatMultiplicationOperand extends Operand<Float> {

	FloatMultiplicationOperand(Float value) {
		super(value);
	}

	@Override
	protected Float opearte(Operand<Float> floatOperand) {
		return operandValue * floatOperand.getValue();
	}

}
