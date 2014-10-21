package pi.operands;

public class ByteBitwiseOROperand extends Operand<Byte> {

	ByteBitwiseOROperand(Byte value) {
		super(value);
	}

	@Override
	protected Byte opearte(Operand<Byte> byteOperand) {
		return (byte) (operandValue | byteOperand.getValue());
	}

}
