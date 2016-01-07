package simulation.common.tools;

import java.io.Closeable;
import java.io.IOException;

public class ClosingTools {

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ex) {
			}
		}
	}
}
