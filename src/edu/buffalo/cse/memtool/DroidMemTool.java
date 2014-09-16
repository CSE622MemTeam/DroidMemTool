package edu.buffalo.cse.memtool;

import java.util.*;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class DroidMemTool extends Activity {
  TextView vmUsage, procUsage, sysUsage;
  List<byte[]> byteList= new LinkedList<byte[]>();
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    vmUsage = (TextView) findViewById(R.id.vmUsage);
    procUsage = (TextView) findViewById(R.id.procUsage);
    sysUsage = (TextView) findViewById(R.id.sysUsage);

    Button oneButton = (Button) findViewById(R.id.allocateOne);
    oneButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        allocateMemory(1<<20);
        updateInformation();
      }
    });

    Button tenButton = (Button) findViewById(R.id.allocateTen);
    tenButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        allocateMemory(10<<20);
        updateInformation();
      }
    });

    Button mallocButton = (Button) findViewById(R.id.mallocHundred);
    mallocButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        allocateOutsideVMMemory(100<<20);
        updateInformation();
      }
    });

    Button fillButton = (Button) findViewById(R.id.mallocFill);
    fillButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        long size = 100<<20;
        while (size > (1<<20))
          if (!allocateOutsideVMMemory(size)) size /= 2;
        updateInformation();
      }
    });

    updateInformation();
  }

  public void onRestoreInstanceState(Bundle savedInstanceState) {
    updateInformation();
  }

  private void allocateMemory(int bytes) {
    Log.i("DroidMemTool", "Allocating "+pretty(bytes));
    byteList.add(new byte[bytes]);
  }

  private boolean allocateOutsideVMMemory(long bytes) {
    boolean success = MemUtil.malloc(bytes);
    Log.i("DroidMemTool", success ?
      "Allocating "+pretty(bytes)+" using malloc" :
      "Failed to allocate "+pretty(bytes)+" using malloc"
    );
    return success;
  }

  // Render a number of bytes in a human-readable way.
  private static String pretty(long bytes) {
    if (bytes < 1024)
      return bytes+"B";
    if ((bytes>>10) < 1024)
      return (bytes>>10)+"KB";
    if ((bytes>>20) < 1024)
      return (bytes>>20)+"MB";
    return (bytes>>30)+"GB";
  }

  private void updateInformation() {
    updateVMUsage();
    updateProcUsage();
    updateSysUsage();
  }

  private void updateVMUsage() {
    Runtime info = Runtime.getRuntime();
    long max = info.maxMemory();
    long cur = (info.totalMemory() - info.freeMemory());
    vmUsage.setText("VM memory: "+pretty(cur)+" / "+pretty(max));
  }

  private void updateProcUsage() {
    long proc = MemUtil.getProcessUsage();
    procUsage.setText("Proc memory: "+pretty(proc));
  }

  private void updateSysUsage() {
    long free = MemUtil.getMeminfo("MemFree");
    long max = MemUtil.getMeminfo("MemTotal");
    sysUsage.setText("Sys free memory: "+pretty(free)+" / "+pretty(max));
  }
} 
