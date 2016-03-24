package pi.RedLib;

public class ByteBitwiseXOR implements Reduction<Byte> {

	@Override
	public Byte reduce(Byte first, Byte second) {
		return (byte) (first ^ second);
	}

}
