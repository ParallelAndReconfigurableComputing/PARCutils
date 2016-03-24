package pu.RedLib;

public class ByteBitwiseOR implements Reduction<Byte> {

	@Override
	public Byte reduce(Byte first, Byte second) {
		return (byte) (first | second);
	}

}
