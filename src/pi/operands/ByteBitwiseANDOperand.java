package pi.operands;

public class ByteBitwiseANDOperand extends Operand<Byte> {

	ByteBitwiseANDOperand(Byte value) {
		super(value);
	}

	@Override
	protected Byte operate(Operand<Byte> byteOperand) {
		return (byte) (operandValue & byteOperand.getValue());
	}
}
