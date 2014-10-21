package pi.operands;

public class ShortBitwiseANDOperand extends Operand<Short> {

	ShortBitwiseANDOperand(Short value) {
		super(value);
	}

	@Override
	protected Short opearte(Operand<Short> shortOperand) {
		return (short) (operandValue & shortOperand.getValue());
	}
}
