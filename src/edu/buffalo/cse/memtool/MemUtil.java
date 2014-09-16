package edu.buffalo.cse.memtool;

import java.io.*;
import java.util.*;

import android.util.*;

/** General memory-related utility methods. */
public final class MemUtil {
  private MemUtil() { /* utility class */ }

  static {
    System.loadLibrary("memutil");
  }

  /** Returns true on success. */
  public static native boolean malloc(long bytes);

  /** Get this process's memory usage in bytes. */
  public static long getProcessUsage() {
    int pid = android.os.Process.myPid();
    return scanProcForField("/proc/"+pid+"/status", "VmSize");
  }

  /** Get something from /proc/meminfo. */
  public static long getMeminfo(String field) {
    return scanProcForField("/proc/meminfo", field);
  }

  private static long scanProcForField(String path, String field) {
    File file = new File(path);
    Scanner scanner = null;
    field += ":";

    try {
      scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String token = scanner.next();
        if (token.equals(field))
          return scanner.nextLong() << 10;  // It's in KB...
        String s = scanner.nextLine();
      }
    } catch (Exception e) {
      // Should we propagate this? Fall through for now.
      Log.e("MemUtil", "Error scanning "+file+" for "+field, e);
    } finally {
      if (scanner != null) scanner.close();
    } return -1;
  }
}
