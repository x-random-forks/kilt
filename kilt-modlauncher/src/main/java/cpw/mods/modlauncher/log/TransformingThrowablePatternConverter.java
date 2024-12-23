package cpw.mods.modlauncher.log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TransformingThrowablePatternConverter {
    public static String generateEnhancedStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
