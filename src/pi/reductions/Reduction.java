/*
 *  Copyright (C) 2009 Nasser Giacaman, Oliver Sinnen
 *
 *  This file is part of Parallel Iterator.
 *
 *  Parallel Iterator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Iterator is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Iterator. If not, see <http://www.gnu.org/licenses/>.
 */

package pi.reductions;

import java.util.ArrayList;

/**
 * Defines a reduction and includes a range of built-in reductions (only a few common ones are implemented). Users may also define their own by implementing this interface. 
 *
 * @author Vikas Added reductions for minus, multi, bitOR, bitXOR, bitAND; added Float, Short, Byte reductions
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 */
public interface Reduction<E> {
	
	/**
	 * Specifies a reduction as defined by 2 elements into 1. 
	 * 
	 * The reduction must obey the following 2 constraints:
	 * <ul>
	 *  <li>	<i>Associative</i>: the order of evaluating the reduction makes no difference, and
	 *  <li>	<i>Commutative</i>:	the order of the thread-local values makes no difference.
	 * </ul>
	 * @param first		The first element in the reduction.
	 * @param second	The second element in the reduction.
	 * @return			The result of reducing <code>first</code> with <code>second</code>.
	 */
	public E reduce(E first, E second);
	
	/**
	 * Returns the maximum for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerMAX = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return Math.max(first, second);
		}
	};

	/**
	 * Returns the minimum for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerMIN = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return Math.min(first, second);
		}
	};
	
	/**
	 * Returns the sum for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerSUM = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return first +second;
		}
	};
	
	/**
	 * Returns the multiplication for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerMULTI = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return first * second;
		}
	};
	
	/**
	 * Returns the minus for <code>Integer</code>
	 * <b>Minus results are added</b>
	 */
	public static Reduction<Integer> IntegerMINUS = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return first + second;
		}
	};
	
	/**
	 * Returns the bitwise AND for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerBITAND = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return (first & second);
		}
	};
	
	/**
	 * Returns the bitwise OR for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerBITOR = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return first | second;
		}
	};
	
	/**
	 * Returns the bitwise XOR for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerBITXOR = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			return first ^ second;
		}
	};
	
	/**
	 * Returns the logical AND for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerLOGAND = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			throw new RuntimeException("Logical AND is undefined on Integer");
		}
	};
	
	/**
	 * Returns the logical OR for <code>Integer</code>
	 */
	public static Reduction<Integer> IntegerLOGOR = new Reduction<Integer>() {
		@Override
		public Integer reduce(Integer first, Integer second) {
			throw new RuntimeException("Logical OR is undefined on Integer");
		}
	};	
	
	/**
	 * Returns the maximum for <code>Double</code> 
	 */
	public static Reduction<Double> DoubleMAX = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			return Math.max(first, second);
		}
	};

	/**
	 * Returns the minimum for <code>Double</code>  
	 */
	public static Reduction<Double> DoubleMIN = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			return Math.min(first, second);
		}
	};

	/**
	 * Returns the sum for <code>Double</code>
	 */
	public static Reduction<Double> DoubleSUM = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			return first + second;
		}
	};

	/**
	 * Returns the multiplication for <code>Double</code>
	 */
	public static Reduction<Double> DoubleMULTI = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			return first * second;
		}
	};
	
	/**
	 * Returns the minus for <code>Double</code>
	 * <b>Minus results are added</b>
	 */
	public static Reduction<Double> DoubleMINUS = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			return first + second;
		}
	};
	
	/**
	 * Returns the bitwise AND for <code>Double</code>
	 */
	public static Reduction<Double> DoubleBITAND = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			throw new RuntimeException("Bitwise AND is undefined on Double");
		}
	};
	
	/**
	 * Returns the bitwise OR for <code>Double</code>
	 */
	public static Reduction<Double> DoubleBITOR = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			throw new RuntimeException("Bitwise OR is undefined on Double");
		}
	};
	
	/**
	 * Returns the bitwise XOR for <code>Double</code>
	 */
	public static Reduction<Double> DoubleBITXOR = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			throw new RuntimeException("Bitwise XOR is undefined on Double");
		}
	};
	
	/**
	 * Returns the logical AND for <code>Double</code>
	 */
	public static Reduction<Double> DoubleLOGAND = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			throw new RuntimeException("Logical AND is undefined on Double");
		}
	};
	
	/**
	 * Returns the logical OR for <code>Double</code>
	 */
	public static Reduction<Double> DoubleLOGOR = new Reduction<Double>() {
		@Override
		public Double reduce(Double first, Double second) {
			throw new RuntimeException("Logical OR is undefined on Double");
		}
	};	
	
	/**
	 * Returns the maximum for <code>Long</code> 
	 */
	public static Reduction<Long> LongMAX = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return Math.max(first, second);
		}
	};

	/**
	 * Returns the minimum for <code>Long</code>  
	 */
	public static Reduction<Long> LongMIN = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return Math.min(first, second);
		}
	};

	/**
	 * Returns the sum for <code>Long</code>
	 */
	public static Reduction<Long> LongSUM = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return first + second;
		}
	};

	/**
	 * Returns the multiplication for <code>Long</code>
	 */
	public static Reduction<Long> LongMULTI = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return first * second;
		}
	};
	
	/**
	 * Returns the minus for <code>Long</code>
	 * <b>Minus results are added</b>
	 */
	public static Reduction<Long> LongMINUS = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return first + second;
		}
	};
	
	/**
	 * Returns the bitwise AND for <code>Long</code>
	 */
	public static Reduction<Long> LongBITAND = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return first & second;
		}
	};
	
	/**
	 * Returns the bitwise OR for <code>Long</code>
	 */
	public static Reduction<Long> LongBITOR = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return first | second;
		}
	};
	
	/**
	 * Returns the bitwise XOR for <code>Long</code>
	 */
	public static Reduction<Long> LongBITXOR = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			return first ^ second;
		}
	};
	
	/**
	 * Returns the logical OR for <code>Long</code>
	 */
	public static Reduction<Long> LongLOGOR = new Reduction<Long>() {
		@Override
		public Long reduce(Long first, Long second) {
			throw new RuntimeException("Logical OR is undefined on Long");
		}
	};
	
	/**
	 * Returns the maximum for <code>Float</code> 
	 */
	public static Reduction<Float> FloatMAX = new Reduction<Float>() {
		@Override
		public Float reduce(Float first, Float second) {
			return Math.max(first, second);
		}
	};

	/**
	 * Returns the minimum for <code>Float</code>  
	 */
	public static Reduction<Float> FloatMIN = new Reduction<Float>() {
		@Override
		public Float reduce(Float first, Float second) {
			return Math.min(first, second);
		}
	};

	/**
	 * Returns the sum for <code>Float</code>
	 */
	public static Reduction<Float> FloatSUM = new Reduction<Float>() {
		@Override
		public Float reduce(Float first, Float second) {
			return first + second;
		}
	};
	
	/**
	 * Returns the multiplication for <code>Float</code>
	 */
	public static Reduction<Float> FloatMULTI = new Reduction<Float>() {
		@Override
		public Float reduce(Float first, Float second) {
			return first * second;
		}
	};
	
	/**
	 * Returns the minus for <code>Float</code>
	 * <b>Minus results are added</b>
	 */
	public static Reduction<Float> FloatMINUS = new Reduction<Float>() {
		@Override
		public Float reduce(Float first, Float second) {
			return first + second;
		}
	};
	
	/**
	 * Returns the maximum for <code>Short</code> 
	 */
	public static Reduction<Short> ShortMAX = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) Math.max(first, second);
		}
	};

	/**
	 * Returns the minimum for <code>Short</code>  
	 */
	public static Reduction<Short> ShortMIN = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) Math.min(first, second);
		}
	};

	/**
	 * Returns the sum for <code>Short</code>
	 */
	public static Reduction<Short> ShortSUM = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) (first + second);
		}
	};
	
	/**
	 * Returns the multiplication for <code>Short</code>
	 */
	public static Reduction<Short> ShortMULTI = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) (first * second);
		}
	};
	
	/**
	 * Returns the minus for <code>Short</code>
	 * <b>Minus results are added</b>
	 */
	public static Reduction<Short> ShortMINUS = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) (first + second);
		}
	};
	
	/**
	 * Returns the bitwise AND for <code>Short</code>
	 */
	public static Reduction<Short> ShortBITAND = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) (first & second);
		}
	};
	
	/**
	 * Returns the bitwise OR for <code>Short</code>
	 */
	public static Reduction<Short> ShortBITOR = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) (first | second);
		}
	};
	
	/**
	 * Returns the bitwise XOR for <code>Short</code>
	 */
	public static Reduction<Short> ShortBITXOR = new Reduction<Short>() {
		@Override
		public Short reduce(Short first, Short second) {
			return (short) (first ^ second);
		}
	};
	
	/**
	 * Returns the maximum for <code>Byte</code> 
	 */
	public static Reduction<Byte> ByteMAX = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) Math.max(first, second);
		}
	};

	/**
	 * Returns the minimum for <code>Byte</code>  
	 */
	public static Reduction<Byte> ByteMIN = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) Math.min(first, second);
		}
	};

	/**
	 * Returns the sum for <code>Byte</code>
	 */
	public static Reduction<Byte> ByteSUM = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) (first + second);
		}
	};
	
	/**
	 * Returns the multiplication for <code>Byte</code>
	 */
	public static Reduction<Byte> ByteMULTI = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) (first * second);
		}
	};
	
	/**
	 * Returns the minus for <code>Byte</code>
	 * <b>Minus results are added</b>
	 */
	public static Reduction<Byte> ByteMINUS = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) (first + second);
		}
	};
	
	/**
	 * Returns the bitwise AND for <code>Byte</code>
	 */
	public static Reduction<Byte> ByteBITAND = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) (first & second);
		}
	};
	
	/**
	 * Returns the bitwise OR for <code>Byte</code>
	 */
	public static Reduction<Byte> ByteBITOR = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) (first | second);
		}
	};
	
	/**
	 * Returns the bitwise XOR for <code>Byte</code>
	 */
	public static Reduction<Byte> ByteBITXOR = new Reduction<Byte>() {
		@Override
		public Byte reduce(Byte first, Byte second) {
			return (byte) (first ^ second);
		}
	};
	
	/**
	 * Returns the bitwise AND for <code>Boolean</code>
	 */
	public static Reduction<Boolean> BooleanBITAND = new Reduction<Boolean>() {
		@Override
		public Boolean reduce(Boolean first, Boolean second) {
			return first & second;
		}
	};
	
	/**
	 * Returns the bitwise OR for <code>Boolean</code>
	 */
	public static Reduction<Boolean> BooleanBITOR = new Reduction<Boolean>() {
		@Override
		public Boolean reduce(Boolean first, Boolean second) {
			return first | second;
		}
	};
	
	/**
	 * Returns the bitwise XOR for <code>Boolean</code>
	 */
	public static Reduction<Boolean> BooleanBITXOR = new Reduction<Boolean>() {
		@Override
		public Boolean reduce(Boolean first, Boolean second) {
			return first ^ second;
		}
	};
	
	/**
	 * Returns the logical AND for <code>Boolean</code>  
	 */
	public static Reduction<Boolean> BooleanLOGAND = new Reduction<Boolean>() {
		@Override
		public Boolean reduce(Boolean first, Boolean second) {
			return first && second;
		}
	};

	/**
	 * Returns the logical OR <code>Boolean</code>
	 */
	public static Reduction<Boolean> BooleanLOGOR = new Reduction<Boolean>() {
		@Override
		public Boolean reduce(Boolean first, Boolean second) {
			return first || second;
		}
	};
	
	/**
	 * Combines the elements of two <code>arrays</code> into one
	 * */
	public static Reduction<Object[]> ArrayCOMBINE = new Reduction<Object[]>(){
		@Override
		public Object[] reduce(Object[] firstArray, Object[] secondArray){
			ArrayList<Object> list = new ArrayList<Object>();
			for (Object obj : firstArray){
				list.add(obj);
			}
			for (Object obj : secondArray){
				list.add(obj);
			}
			return (list.toArray());
		}
	};
	
}
