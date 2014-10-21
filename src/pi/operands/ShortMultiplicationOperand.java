package pi.operands;

public class ShortMultiplicationOperand extends Operand<Short> {

	ShortMultiplicationOperand(Short value) {
		super(value);
	}

	@Override
	protected Short opearte(Operand<Short> shortOperand) {
		return (short) (operandValue * shortOperand.getValue());
	}

}
