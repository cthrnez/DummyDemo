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
import java.util.Arrays;
import java.util.List;

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
import android.provider.Telephony.Mms.Part;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button button;
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
    			//String s = intent.getExtras().getString("ticket");
    			//System.out.println(s);
    			// Get ticket as array of bytes.
    			byte [] binary = intent.getExtras().getByteArray("bytes");
    			//System.out.println(binary);
    			//String ticket=Base64.encodeToString(binary,Base64.DEFAULT);
    			String ticket=null;
                try {
                    ticket = new String(binary, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    			
    			
    			TextView myText = (TextView) findViewById(R.id.textView1);
    			myText.setText(ticket);
    			
//    			
    			//one method that worked
//    			HttpClient client = new DefaultHttpClient();
//    			HttpPost post = new HttpPost("http://128.31.34.152:8080/");
//    			
//    			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//    			pairs.add(new BasicNameValuePair("ticket", "value1"));
//    			try {
//                    post.setEntity(new UrlEncodedFormEntity(pairs));
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//    			try {
//                    HttpResponse response = client.execute(post);
//                    System.out.println("Done!" + response);
//                } catch (ClientProtocolException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
    			
    			 System.out.println("at the end!");

    			postData(binary);
    			 
    			// Save ticket in local storage?  
//    			String filename="blah";
//    			try {
//    				System.out.println("Starting to write byte array.");
//    		        FileOutputStream fos= openFileOutput(filename, Context.MODE_PRIVATE);
//    		        fos.write(binary);
//    		        fos.flush();
//    		        fos.close();
//    		        System.out.println("Done writing byte array.");
//    		      }
//    		      catch (java.io.IOException e) {
//    		        Log.e("DemoError", "Exception in saving byte array to local", e);
//    		      }
    		}
    	};
    	registerReceiver(myReceiver, filter, "com.example.dummyKerb.KERB_LISTENER_PERM", null);
        addListenerOnButton();
    	
    	// Create intent to send to Kerberos app's BroadcastReceiver.  
//        Intent intent = new Intent();
//    	intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        intent.setAction("com.example.dummyKerb.TESTING");
//        //intent.putExtra("package", "com.example.dummydemo");
//        Context c = this.getApplicationContext();
//        String pname = c.getPackageName();
//        //System.out.println("Packname " + pname);
//        intent.putExtra("package", pname);
//        sendBroadcast(intent, "com.example.dummyKerb.KERB_LISTENER_PERM");
    }
    
    
    protected void postData(byte[] content) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
       // HttpPost httppost = new HttpPost("http://18.189.120.18:8080/");
        HttpPost httppost = new HttpPost("http://panda.xvm.mit.edu:8080/");
        
        // Save ticket in local storage?  
      String filename="blah";
      try {
          System.out.println("Starting to write byte array.");
          FileOutputStream fos= openFileOutput(filename, Context.MODE_PRIVATE);
          fos.write(content);
          fos.flush();
          fos.close();
          System.out.println("Done writing byte array.");
        }
        catch (java.io.IOException e) {
          Log.e("DemoError", "Exception in saving byte array to local", e);
        }
        
      

        try {
            // Add your data
            
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            String ticket=Base64.encodeToString(content,Base64.DEFAULT);
            while (ticket.length()%4!=0){
                ticket=ticket+"=";
                System.out.println("padddddding stuff");
            }
            
            File file = new File("/data/data/com.example.dummydemo/files/blah");
            System.out.println("before sending stuff"+ file);
            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
           // HttpResponse response = httpclient.execute(httppost);

//            nameValuePairs.add(new BasicNameValuePair("ticket", ticket));
//            nameValuePairs.add(new BasicNameValuePair("principal", "lsyang"));
          //  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

           // httppost.setEntity(new ByteArrayEntity(content));  
            
            HttpResponse response = httpclient.execute(httppost);
            System.out.println("after sending stuff");
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("response is "+result);

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

    public void addListenerOnButton() {
    	button = (Button) findViewById(R.id.button1);
    	button.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
	    		Intent intent = new Intent();
	        	intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	            intent.setAction("com.example.dummyKerb.TESTING");
	            //intent.putExtra("package", "com.example.dummydemo");
	            Context c = MainActivity.this.getApplicationContext();
	            String pname = c.getPackageName();
	            //System.out.println("Packname " + pname);
	            intent.putExtra("package", pname);
	            sendBroadcast(intent, "com.example.dummyKerb.LISTEN_PERM");
	            System.out.println("Sending stuff");
    		}
    	});
    	
    }
    
}
