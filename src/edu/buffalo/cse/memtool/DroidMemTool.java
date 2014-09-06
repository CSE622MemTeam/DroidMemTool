package edu.buffalo.cse.memtool;

import java.util.*;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class DroidMemTool extends Activity {
  TextView memoryUsage;
  List<byte[]> bytes = new LinkedList<byte[]>();
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    memoryUsage = (TextView) findViewById(R.id.memoryUsage);

    Button oneButton = (Button) findViewById(R.id.allocateOne);
    oneButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        allocateMemory(1);
        updateInformation();
      }
    });

    Button tenButton = (Button) findViewById(R.id.allocateTen);
    tenButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        allocateMemory(10);
        updateInformation();
      }
    });

    updateInformation();
  }

  private void allocateMemory(int mb) {
    Log.i("DroidMemTool", "Allocating "+mb+"MB");
    bytes.add(new byte[mb * 1024 * 1024]);
  }

  private void updateInformation() {
    Runtime info = Runtime.getRuntime();
    long max = info.maxMemory() >> 20;
    long usage = (info.totalMemory() - info.freeMemory()) >> 20;

    memoryUsage.setText("Memory usage: "+usage+"MB / "+max+"MB");
  }
}
