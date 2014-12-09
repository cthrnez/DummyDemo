package com.example.dummydemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private BroadcastReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("HAHAHHAA");
<<<<<<< HEAD
        
        // Create a Broadcast Receiver for receiving the ticket.  
=======
    }

    /* method to send intent to getTicket */
    public void getTickets(View view){
    	
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
    
	/* Create a Broadcast Receiver for receiving the ticket. 
	 * This method also starts display ticket activity*/
   
    public void receiveTicket(){
>>>>>>> 0c6fc797502609d7780ee3af8ac7227f6cb0d1ca
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.dummydemo.TESTING");
        myReceiver = new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			// Update view
    			Log.d("Debug", "Final receiver");
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
