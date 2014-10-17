package pi.operands;

public class FloatSumOperand extends Operand<Float> {

	FloatSumOperand(Float value) {
		super(value);
	}

	@Override
	protected Float opearte(Operand<Float> floatOperand) {
		return operandValue + floatOperand.getValue();
	}

}
