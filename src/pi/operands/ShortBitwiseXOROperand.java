package pi.operands;

public class ShortBitwiseXOROperand extends Operand<Short> {

	ShortBitwiseXOROperand(Short value) {
		super(value);
	}

	@Override
	protected Short operate(Operand<Short> shortOperand) {
		return (short) (operandValue ^ shortOperand.getValue());
	}

}
