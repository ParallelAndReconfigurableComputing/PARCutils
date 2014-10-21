package pi.operands;

public class ByteBitwiseXOROperand extends Operand<Byte> {

	ByteBitwiseXOROperand(Byte value) {
		super(value);
	}

	@Override
	protected Byte opearte(Operand<Byte> byteOperand) {
		return (byte)(operandValue ^ byteOperand.getValue());
	}

}
