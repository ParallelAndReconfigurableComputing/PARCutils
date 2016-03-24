package pi.util;

import sun.misc.Unsafe;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;

/**
 * Author: xiaoxing
 * Date: 13/05/13
 */
public class VMSpec {
	public final static int referenceSize;
	public final static int headerSize;
	public final static int objectAlignment = 8;


	private static Unsafe unsafe = null;

	public static Unsafe getUnsafe() {
		if (unsafe == null) {
			try{
				Field field =Unsafe.class.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				unsafe =(Unsafe)field.get(null);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return unsafe;
	}

	static class HeaderTester {
		public boolean field;
	}

	static class TestObject {
		public Object obj1;
		public Object obj2;
	}

	private static int log2p(int x) {
		int r = 0;
		while ((x >>= 1) != 0)
			r++;
		return r;
	}

	static {
//		referenceSize = getUnsafe().addressSize();
		int hs = 12;
		try {
			long offset = getUnsafe().objectFieldOffset(TestObject.class.getField("obj1"));
			hs = (int) offset;
		} catch (NoSuchFieldException e) {
			e.printStackTrace(); //TODO Resolve the catch block.
		}
		headerSize = hs;

		int oopSize = 4;
		try {
			long off1 = getUnsafe().objectFieldOffset(TestObject.class.getField("obj1"));
			long off2 = getUnsafe().objectFieldOffset(TestObject.class.getField("obj2"));
			oopSize = (int) Math.abs(off2 - off1);
		} catch (NoSuchFieldException e) {
			e.printStackTrace(); //TODO Resolve the catch block.
		}
		referenceSize = oopSize;

//		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
//		try {
//			ObjectName mbean = new ObjectName("com.sun.management:type=HotSpotDiagnostic");
//			CompositeDataSupport compressedOopsValue = (CompositeDataSupport) server.invoke(mbean, "getVMOption", new Object[]{"UseCompressedOops"}, new String[]{"java.lang.String"});
//			boolean compressedOops = Boolean.valueOf(compressedOopsValue.get("value").toString());
//			if (compressedOops) {
//				// if compressed oops are enabled, then this option is also accessible
//				CompositeDataSupport alignmentValue = (CompositeDataSupport) server.invoke(mbean, "getVMOption", new Object[]{"ObjectAlignmentInBytes"}, new String[]{"java.lang.String"});
//				int align = Integer.valueOf(alignmentValue.get("value").toString());
//				System.out.println(String.format("align: %d", align));
//			}
//
//		} catch (MalformedObjectNameException e) {
//			e.printStackTrace(); //TODO Resolve the catch block.
//		} catch (ReflectionException e) {
//			e.printStackTrace(); //TODO Resolve the catch block.
//		} catch (MBeanException e) {
//			e.printStackTrace(); //TODO Resolve the catch block.
//		} catch (InstanceNotFoundException e) {
//			e.printStackTrace(); //TODO Resolve the catch block.
//		}
	}


	private static Instrumentation instrumentation = null;

	public static Instrumentation getInstrumentation() throws Exception {
//		if (instrumentation == null) {
//			throw new Exception("VM agent is not enabled, use -javaagent: to add this JAR as Java agent.");
//		} else {
//			return instrumentation;
//		}
		return instrumentation;
	}

//	static Map<Object, Long> addressMap = new HashMap<Object, Long>();

	public static long addressOf(Object o) {
//		if (!addressMap.containsKey(o)) {
//			addressMap.put(o, addressOf(o, referenceSize));
//		}
//		return addressMap.get(o);
		return addressOf(o, referenceSize);
	}

	private static long addressOf(Object o, int oopSize) {
		Object[] array = new Object[]{o};

		long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
		long objectAddress;
		switch (oopSize) {
			case 4:
				objectAddress = getUnsafe().getInt(array, baseOffset);
				break;
			case 8:
				objectAddress = getUnsafe().getLong(array, baseOffset);
				break;
			default:
				throw new Error("unsupported address size: " + oopSize);
		}

		return (objectAddress);
	}

	public static void printAddress(Object o) {
		Object[] array = new Object[]{o};
		long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
		int intAddress = getUnsafe().getInt(array, baseOffset);
		long longAddress = getUnsafe().getLong(array, baseOffset);

		System.out.format("int:\t%X\nlong\t%X\n", intAddress, longAddress);
	}


	private static int gcd(int a, int b) {
		while (b > 0) {
			int temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}

	public static int align(int addr) {
		if ((addr % objectAlignment) == 0) {
			return addr;
		} else {
			return ((addr / objectAlignment) + 1) * objectAlignment;
		}
	}


	public static void premain(String args, Instrumentation inst) {
		instrumentation = inst;
	}

	public static int sizeOfType(Class<?> type) {
		if (type == byte.class)    { return 1; }
		if (type == boolean.class) { return 1; }
		if (type == short.class)   { return 2; }
		if (type == char.class)    { return 2; }
		if (type == int.class)     { return 4; }
		if (type == float.class)   { return 4; }
		if (type == long.class)    { return 8; }
		if (type == double.class)  { return 8; }
		return referenceSize;
	}

	public static String display() {
		String str = "";
		str += String.format("reference size: %d\n", referenceSize);
		str += String.format("header size: %d\n", headerSize);
		str += String.format("object alignment: %d\n", objectAlignment);
		return str;
	}
}