package com.example.dummydemo;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private BroadcastReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("HAHAHHAA");
        
        // Create a Broadcast Receiver for receiving the ticket.  
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.dummydemo.TESTING");
        myReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			// Update view
    			Log.d("Debug", "Final receiver");
    			String s = intent.getExtras().getString("ticket");
    			System.out.println(s);
    			// Get ticket as array of bytes.
    			byte [] binary = intent.getExtras().getByteArray("bytes");
    			// Save ticket in local storage?  
    			String filename="blah";
    			try {
    				System.out.println("Starting to write byte array.");
    		        FileOutputStream fos= openFileOutput(filename, Context.MODE_PRIVATE);
    		        fos.write(binary);
    		        fos.flush();
    		        fos.close();
    		        System.out.println("Done writing byte array.");
    		      }
    		      catch (java.io.IOException e) {
    		        Log.e("DemoError", "Exception in saving byte array to local", e);
    		      }
    		}
    	};
    	registerReceiver(myReceiver, filter, "com.example.dummyKerb.LISTEN_PERM", null);
    	
    	// Create intent to send to Kerberos app's BroadcastReceiver.  
        Intent intent = new Intent();
    	intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.example.dummyKerb.TESTING");
        //intent.putExtra("package", "com.example.dummydemo");
        Context c = this.getApplicationContext();
        String pname = c.getPackageName();
        //System.out.println("Packname " + pname);
        intent.putExtra("package", pname);
        sendBroadcast(intent, "com.example.dummyKerb.LISTEN_PERM");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(myReceiver);
    }
    
}
