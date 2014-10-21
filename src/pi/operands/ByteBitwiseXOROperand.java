package pi.operands;

public class ByteBitwiseXOROperand extends Operand<Byte> {

	ByteBitwiseXOROperand(Byte value) {
		super(value);
	}

	@Override
	protected Byte operate(Operand<Byte> byteOperand) {
		return (byte)(operandValue ^ byteOperand.getValue());
	}

}
