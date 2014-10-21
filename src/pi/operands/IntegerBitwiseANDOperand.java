package pi.operands;

public class IntegerBitwiseANDOperand extends Operand<Integer> {

	IntegerBitwiseANDOperand(Integer value) {
		super(value);
	}

	@Override
	protected Integer opearte(Operand<Integer> integetOperand) {
		return operandValue & integetOperand.getValue();
	}

}
