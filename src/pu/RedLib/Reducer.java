package pu.RedLib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Reducer {
	public enum OPERATION{AND, OR, XOR, BIT_AND, BIT_OR, BIT_XOR, INTERSECTION, JOIN, UNION,
		MAX, MIN, MULT, SUM};
	
	
	public Boolean reduce(Boolean[] array, OPERATION op){
		boolean result = false;
		
		Reduction<Boolean> operation = null;
		if(op.equals(OPERATION.AND))
			operation = new BooleanAND();
		else if(op.equals(OPERATION.OR))
			operation = new BooleanOR();
		else if(op.equals(OPERATION.XOR))
			operation = new BooleanXOR();
		else if(op.equals(OPERATION.BIT_AND))
			operation = new BooleanBitwiseAND();
		else if(op.equals(OPERATION.BIT_OR))
			operation = new BooleanBitwiseOR();
		else if(op.equals(OPERATION.BIT_XOR))
			operation = new BooleanBitwiseXOR();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public Byte reduce(Byte[] array, OPERATION op){
		Byte result = 0;
		
		Reduction<Byte> operation = null;		
		if(op.equals(OPERATION.BIT_AND))
			operation = new ByteBitwiseAND();
		else if(op.equals(OPERATION.BIT_OR))
			operation = new ByteBitwiseOR();
		else if(op.equals(OPERATION.BIT_XOR))
			operation = new ByteBitwiseXOR();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public Integer reduce(Integer[] array, OPERATION op){
		int result = 0;
		
		Reduction<Integer> operation = null;
		if(op.equals(OPERATION.SUM))
			operation = new IntegerSum();
		else if(op.equals(OPERATION.MAX))
			operation = new IntegerMaximum();
		else if(op.equals(OPERATION.MIN))
			operation = new IntegerMinimum();
		else if(op.equals(OPERATION.MULT))
			operation = new IntegerMultiplication();
		else if(op.equals(OPERATION.BIT_AND))
			operation = new IntegerBitwiseAND();
		else if(op.equals(OPERATION.BIT_OR))
			operation = new IntegerBitwiseOR();
		else if(op.equals(OPERATION.BIT_XOR))
			operation = new IntegerBitwiseXOR();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR INTEGER IN REDLIB");
		
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public Long reduce(Long[] array, OPERATION op){
		long result = 0l;
		
		Reduction<Long> operation = null;
		if(op.equals(OPERATION.SUM))
			operation = new LongSum();
		else if(op.equals(OPERATION.MAX))
			operation = new LongMaximum();
		else if(op.equals(OPERATION.MIN))
			operation = new LongMinimum();
		else if(op.equals(OPERATION.MULT))
			operation = new LongMultiplication();
		else if(op.equals(OPERATION.BIT_AND))
			operation = new LongBitwiseAND();
		else if(op.equals(OPERATION.BIT_OR))
			operation = new LongBitwiseOR();
		else if(op.equals(OPERATION.BIT_XOR))
			operation = new LongBitwiseXOR();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR LONG IN REDLIB");
		
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public Short reduce(Short[] array, OPERATION op){
		short result = 0;
		
		Reduction<Short> operation = null;
		if(op.equals(OPERATION.SUM))
			operation = new ShortSum();
		else if(op.equals(OPERATION.MAX))
			operation = new ShortMaximum();
		else if(op.equals(OPERATION.MIN))
			operation = new ShortMinimum();
		else if(op.equals(OPERATION.MULT))
			operation = new ShortMultiplication();
		else if(op.equals(OPERATION.BIT_AND))
			operation = new ShortBitwiseAND();
		else if(op.equals(OPERATION.BIT_OR))
			operation = new ShortBitwiseOR();
		else if(op.equals(OPERATION.BIT_XOR))
			operation = new ShortBitwiseXOR();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR INTEGER IN REDLIB");
		
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public Double reduce(Double[] array, OPERATION op){
		double result = 0d;
		
		Reduction<Double> operation = null;
		if(op.equals(OPERATION.SUM))
			operation = new DoubleSum();
		else if(op.equals(OPERATION.MAX))
			operation = new DoubleMaximum();
		else if(op.equals(OPERATION.MIN))
			operation = new DoubleMinimum();
		else if(op.equals(OPERATION.MULT))
			operation = new DoubleMultiplication();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public Float reduce(Float[] array, OPERATION op){
		Float result = 0f;
		
		Reduction<Float> operation = null;
		if(op.equals(OPERATION.SUM))
			operation = new FloatSum();
		else if(op.equals(OPERATION.MAX))
			operation = new FloatMaximum();
		else if(op.equals(OPERATION.MIN))
			operation = new FloatMinimum();
		else if(op.equals(OPERATION.MULT))
			operation = new FloatMultiplication();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public <T> List<T> reduce(List<T>[] array, OPERATION op){
		List<T> result = new ArrayList<>();
		
		Reduction<List<T>> operation = null;
		if(op.equals(OPERATION.UNION))
			operation = new ListUnion<>();
		else if(op.equals(OPERATION.INTERSECTION))
			operation = new ListIntersection<>();
		else if(op.equals(OPERATION.JOIN))
			operation = new ListJoin<>();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		result = operation.reduce(array[0], array[1]);
		
		for(int i = 2; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}		
	
	public <T> Collection<T> reduce(Collection<T>[] array, OPERATION op){
		
		Reduction<Collection<T>> operation = null;
		if(op.equals(OPERATION.UNION))
			operation = new CollectionUnion<>();
		else if(op.equals(OPERATION.INTERSECTION))
			operation = new CollectionIntersection<>();
		else if(op.equals(OPERATION.JOIN))
			operation = new CollectionJoin<>();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		Collection<T> result = array[0];
		
		for(int i = 1; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	public <T> Set<T> reduce(Set<T>[] array, OPERATION op){
		
		Reduction<Set<T>> operation = null;
		if(op.equals(OPERATION.UNION))
			operation = new SetUnion<>();
		else if(op.equals(OPERATION.INTERSECTION))
			operation = new SetIntersection<>();
		else
			throw new IllegalArgumentException("THE SPECIFIED OPERATION " + op.toString() + " IS NOT SUPPORTED FOR DOUBLE IN REDLIB");
				
		if(array.length < 1)
			throw new IllegalArgumentException("THE PROVIDED ARRAY IS EMPTY!");
		
		if(array.length == 1)
			return array[0];
		
		Set<T> result = array[0];
		
		for(int i = 1; i < array.length; i++)
			result = operation.reduce(result, array[i]);
		
		return result;
	}
	
	//The reduction object must be passed to us in this case, because the depth of nested reductions is not
	//known. 
	public <K, V> Map<K, V> reduce(Map<K, V>[] array, Reduction<Map<K, V>> op){
		return null;
	}
}
