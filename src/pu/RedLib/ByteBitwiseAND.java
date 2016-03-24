package pu.RedLib;

public class ByteBitwiseAND implements Reduction<Byte> {

	@Override
	public Byte reduce(Byte first, Byte second) {
		return (byte) (first & second);
	}
}
