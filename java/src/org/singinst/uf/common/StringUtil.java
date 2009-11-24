package org.singinst.uf.common;

import java.util.Collection;

public class StringUtil {
	public static String join(String delimiter, Collection<String> collection) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String string : collection) {
			if (!first) {
				builder.append(delimiter);
			}
			first = false;
			builder.append(string);
		}
		return builder.toString();
	}
}
