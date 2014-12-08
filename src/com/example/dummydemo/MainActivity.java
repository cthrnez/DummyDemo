package com.example.dummydemo;

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
    		}
    	};
    	registerReceiver(myReceiver, filter);
    }

    // method to send intent to getTicket
    public void getTickets(){
    	
        Intent intent = new Intent();
    	intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.example.dummyKerb.TESTING");
        //intent.putExtra("package", "com.example.dummydemo");
        Context c = this.getApplicationContext();
        String pname = c.getPackageName();
        System.out.println("Packname " + pname);
        intent.putExtra("package", pname);
        sendBroadcast(intent);
    }
   
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(myReceiver);
    }
}
