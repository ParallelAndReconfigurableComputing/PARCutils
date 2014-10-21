package pi.operands;

public class ShortSumOperand extends Operand<Short> {

	ShortSumOperand(Short value) {
		super(value);
	}

	@Override
	protected Short opearte(Operand<Short> shortOperand) {
		return (short) (operandValue + shortOperand.getValue());
	}

}
