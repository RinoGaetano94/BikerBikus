package ferranti.bikerbikus.utils;

public final class Utils {

	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	public static String uppercase(String string) {
		if (string == null || string.length() < 1)
			return "";
		return string.substring(0, 1).toUpperCase().concat(string.substring(1));
	}

	public static String formatTime(int hour, int minutes) {
		return String.format("%02d:%02d", hour, minutes);
	}
}
