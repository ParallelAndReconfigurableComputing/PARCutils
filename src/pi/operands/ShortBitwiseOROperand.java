package pi.operands;

public class ShortBitwiseOROperand extends Operand<Short> {

	ShortBitwiseOROperand(Short value) {
		super(value);
	}

	@Override
	protected Short opearte(Operand<Short> shortOperand) {
		return (short) (operandValue | shortOperand.getValue());
	}

}
