package com.weektodo.util;

public class Log {
	private static boolean enabled = true;

	public static int i(String tag, String msg) {
		if (enabled) {
			return android.util.Log.i(tag, msg);
		}
		return 0;
	}

	public static int w(String tag, String msg) {
		if (enabled) {
			return android.util.Log.w(tag, msg);
		}
		return 0;
	}

	public static int d(String tag, String msg) {
		if (enabled) {
			return android.util.Log.d(tag, msg);
		}
		return 0;
	}
}
