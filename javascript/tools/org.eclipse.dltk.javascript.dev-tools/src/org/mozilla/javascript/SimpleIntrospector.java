package org.mozilla.javascript;

public class SimpleIntrospector {

	/**
	 * A hack to access protected methods of NativeFunction
	 * 
	 * @param func
	 * @return
	 */
	public static String[] getParameterNames(NativeFunction func) {
		int paramCount = func.getParamCount();
		String[] result = new String[paramCount];
		for (int a = 0; a < paramCount; a++)
			result[a] = func.getParamOrVarName(a);
		return result;
	}

}
