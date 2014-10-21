package pi.operands;

public class FloatMultiplicationOperand extends Operand<Float> {

	FloatMultiplicationOperand(Float value) {
		super(value);
	}

	@Override
	protected Float operate(Operand<Float> floatOperand) {
		return (Float) (operandValue * floatOperand.getValue());
	}

}
