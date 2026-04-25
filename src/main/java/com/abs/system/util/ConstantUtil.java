package com.abs.system.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author wdg
 * @date 2020-12-15
 * @version 1.01
 * @category java 8中静态插件常量的默认值
 *
 */
public class ConstantUtil {

	public static final Map<String, Object> basicmap=new HashMap<String, Object>();
	
	public static final Map<String, Object> parsemap=new HashMap<String, Object>();

	private static byte a;
	private static short b;
	private static int c;
	private static long d;
	private static float e;
	private static double f;
	private static char g;
	private static boolean h;

	private static byte[] aa;
	private static short[] bb;
	private static int[] cc;
	private static long[] dd;
	private static float[] ee;
	private static double[] ff;
	private static char[] gg;
	
	static {
		basicmap.put("byte", a);
		basicmap.put("short", b);
		basicmap.put("int", c);
		basicmap.put("long", d);
		basicmap.put("float", e);
		basicmap.put("double", f);
		basicmap.put("char", g);
		basicmap.put("boolean", h);
		basicmap.put("byte[]", aa);
		basicmap.put("short[]", bb);
		basicmap.put("int[]", cc);
		basicmap.put("long[]", dd);
		basicmap.put("float[]", ee);
		basicmap.put("double[]", ff);
		basicmap.put("char[]", gg);
		
		
		parsemap.put("short", Short.class);
		parsemap.put("int", Integer.class);
		parsemap.put("long", Long.class);
		parsemap.put("float", Float.class);
		parsemap.put("double", Double.class);
	
		
	}

}
