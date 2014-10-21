package pi.operands;

public class ShortSubtractionOperand extends Operand<Short> {

	ShortSubtractionOperand(Short value) {
		super(value);
	}

	@Override
	protected Short opearte(Operand<Short> shortOperand) {
		return (short) (operandValue - shortOperand.getValue());
	}

}
