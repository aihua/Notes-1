package com.pekall.plist;

/**
 * Debug utility class
 */
@SuppressWarnings("UnusedDeclaration")
public class PlistDebug {
    private static boolean verboseDebugLog = false;

    /**
     * Enable/disable debug log
     * @param verboseDebugLog whether to enable debug log
     */
    public static void setVerboseDebugLog(boolean verboseDebugLog) {
        PlistDebug.verboseDebugLog = verboseDebugLog;
    }

    /**
     * Output verbose debug log
     * @param s log string
     */
    public static void logVerbose(String s) {
        if(verboseDebugLog) {
            System.out.println(s);
        }
    }

    /**
     * Output debug log
     * @param s log string
     */
    public static void log(String s) {
        System.out.println(s);
    }

    public static void logError(String s) {
        try {
            throw new Exception(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logTest(String s) {
        System.out.println(s);
    }
}
