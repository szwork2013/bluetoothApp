package edu.berkeley.sampleapps.bluetoothsampleapp;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.berkeley.monitoring.util.bluetooth.BluetoothExceptions;
import edu.berkeley.monitoring.util.bluetooth.BluetoothInterface;
import edu.berkeley.monitoring.util.bluetooth.BluetoothService;
import edu.berkeley.monitoring.util.bluetooth.PairedBTDevices;
import edu.berkeley.monitoring.util.bluetooth.UnpairedBTDevices;

public class MainActivityBluetoothSampleApp extends Activity implements BluetoothInterface{
    private static final boolean D = true;
    private static final String TAG = "BluetoothChat";
    public static BluetoothService bluetoothServiceHandler;
    private Button mTurnOnBluetooth;
    private Button mStartScan;
    private Button mMakeDiscoverable;
    private Button mEnlistPairedDevices;
    private Activity thisActivity = this;
    public static ArrayList<PairedBTDevices> listPairedDevices;
    public static ArrayList<UnpairedBTDevices> listUnpairedDevices;
    public static final String TOAST = "toast";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bluetooth_sample_app);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        // Set up the window layout
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main_bluetooth_sample_app);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        // Set up the custom title
        //mTitle = (TextView) findViewById(R.id.title_left_text);
        //mTitle.setText(R.string.app_name);
        //mTitle = (TextView) findViewById(R.id.title_right_text);
        //mTextFile = (TextView)findViewById(R.id.textfile);
        // Get local Bluetooth adapter
    
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        try{

        	bluetoothServiceHandler = new BluetoothService(this, msgHandler, this);
        }
        catch(BluetoothExceptions e){
        	//TODO
        }
        setupChat();
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        
        listPairedDevices = new ArrayList<PairedBTDevices>();
        listUnpairedDevices = new ArrayList<UnpairedBTDevices>();
        
        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        //mConversationView = (ListView) findViewById(R.id.in);
        //mConversationView.setAdapter(mConversationArrayAdapter);
        // Initialize the compose field with a listener for the return key
        //mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        //mOutEditText.setOnEditorActionListener(mWriteListener);
        // Initialize the send button with a listener that for click events
        mTurnOnBluetooth = (Button) findViewById(R.id.buttonTurnOnBT);
        mTurnOnBluetooth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	bluetoothServiceHandler.switchOnBluetooth();
            }
        });
        
        mStartScan = (Button) findViewById(R.id.buttonStartScan);
        mStartScan.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		bluetoothServiceHandler.startScan();
        	}
        	
        });
        
        mMakeDiscoverable = (Button) findViewById(R.id.buttonMakeDiscoverable);
        mMakeDiscoverable.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		bluetoothServiceHandler.ensureDiscoverable();
        	}
        	
        });
        
        
        mEnlistPairedDevices = (Button) findViewById(R.id.buttonEnlistPairedDevices);
        mEnlistPairedDevices.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		bluetoothServiceHandler.getPairedDevices(listPairedDevices);
        	}
        	
        });
        //mMakeDiscoverable = (Button) findViewById(R.id.button_makeDiscoverable);
        //mMakeDiscoverable.setOnClickListener(new OnClickListener() {
        //    public void onClick(View v) {        // Ensure this device is discoverable by others
        //        ensureDiscoverable();

        //    }
        //});
        /**
         * mConnect2Device = (Button) findViewById(R.id.button_connect2device);
         
        mConnect2Device.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	fConnect2Device();
            }
        });
        mPickAFile = (Button) findViewById(R.id.button_pickAFile);
        mPickAFile.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            	intent.setType("file/*");
            	startActivityForResult(intent,PICKFILE_RESULT_CODE);
            	    
            	   }});

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_bluetooth_sample_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onFinishedScanning() {
		if (D)
			Log.e(TAG,"Callback from library");
        Intent serverIntent = new Intent(thisActivity, ListUnpairedDevices.class);
        this.startActivity(serverIntent);     		

		
	}

	@Override
	public void onObtainedOneUnpairedDevices(UnpairedBTDevices pUnpairedBTDevice) {
		listUnpairedDevices.add(pUnpairedBTDevice);		
	}

	@Override
	public void onFinishedObtainingPairedDevices() {
        if (D)
        	Log.e(TAG,"onFinishedObtainingPairedDevices");
        Intent serverIntent = new Intent(thisActivity, ListPairedDevices.class);
        this.startActivity(serverIntent);      		
		
	}

	@Override
	public void onFinishObtainingUnpairedDevices() {
		// TODO Auto-generated method stub
		
	}
	
    // The Handler that gets information back from the BluetoothChatService
    private final Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
/**            case BluetoothService.MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;*/
            case BluetoothService.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	@Override
	public void onSwitchingonBluetooth() {
		// TODO Auto-generated method stub
		
	}
}

