package com.example.dummydemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Base64;
//import org.apache.commons.codec.binary.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button button1;  // List button.
	private Button button2;  // Reboot button.
	private String command = "";
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
    			Log.d("Debug", "Final receiver");
    			// Get ticket as array of bytes.
    			byte [] binary = intent.getExtras().getByteArray("bytes");
    			String ticket = null;
                try {
                    ticket = new String(binary, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    			// TextView myText = (TextView) findViewById(R.id.textView1);
    			// myText.setText(ticket);
    			System.out.println("at the end!");
    			// Send ticket to server.  
    			postData(binary);
    		}
    	};
    	registerReceiver(myReceiver, filter, "com.example.dummyKerb.KERB_LISTENER_PERM", null);
        addListenerOnButton();
    	
    }
    
    /*
     * Send the ticket and XVM command details to proxy server, 
     * output response.  
     */
    protected void postData(byte[] content) {
    	// Create a new HttpClient and Post Header
    	HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://128.31.35.19:8080/");
        // HttpPost httppost = new HttpPost("http://panda.xvm.mit.edu:8080/");
        try {
        	// Populate and send the HTTP Post.  
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        	String ticket=new String(Base64.encode(content, Base64.NO_WRAP));
        	Log.i("decoded ticket is ", ticket);
       	 	nameValuePairs.add(new BasicNameValuePair("ticket", ticket));
       	 	nameValuePairs.add(new BasicNameValuePair("principal", "lsyang"));
       	 	nameValuePairs.add(new BasicNameValuePair("command", command));
       	 	EditText machine = (EditText) findViewById(R.id.machine);
       	 	nameValuePairs.add(new BasicNameValuePair("machine", machine.getText().toString()));
       	 	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
       	 	HttpResponse response = httpclient.execute(httppost);
       	 	System.out.println("response is "+ response);
       	 	// Output the server's response.  
       	 	HttpEntity entity  = response.getEntity();
       	 	String responseString = EntityUtils.toString(entity, "UTF-8");
       	 	TextView myText = (TextView) findViewById(R.id.textView1);
			myText.setText(responseString);
        } catch (ClientProtocolException e) {
        	// TODO Auto-generated catch block
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        }
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
    
    /*
     * Check if machine name contains only alphanumeric characters or dashes.
     * (SIPB only allows these machine names).
     */
    public boolean isGoodMachine(){
        EditText machine = (EditText) findViewById(R.id.machine);
        String fullname = machine.getText().toString();
        String name = fullname.replace("-", "");
        String pattern= "^[a-zA-Z0-9]*$";
        if(name.matches(pattern)) {
            return true;
        }
        return false;   
    }
    
    /*
     * Send ticket request to the Kerberos app if machine name is correctly done.  
     */
    private void sendTicketRequest() {
        if (!isGoodMachine()) {
            TextView myText = (TextView) findViewById(R.id.textView1);
            myText.setText("Improper machine name");
            return;
        }
    	Intent intent = new Intent();
    	intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.example.dummyKerb.TESTING");
        Context c = MainActivity.this.getApplicationContext();
        String pname = c.getPackageName();
        //System.out.println("Packname " + pname);
        intent.putExtra("package", pname);
        intent.putExtra("servicePrincipal", "HTTP@xvm.mit.edu");
        intent.putExtra("server", "18.181.0.62");
        intent.putExtra("port", 442);
        sendBroadcast(intent, "com.example.dummyKerb.KERB_LISTENER_PERM");
        System.out.println("Sending stuff");
    }

    /*
     * Add listeners to the two buttons.  
     */
    public void addListenerOnButton() {
    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			command = "list";
	    		sendTicketRequest();
    		}
    	});
    	button2 = (Button) findViewById(R.id.button2);
    	button2.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			command = "reboot";
	    		sendTicketRequest();
    		}
    	});
    }
    
}
