package com.userapp.alive.aliveapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    String fan_value = "FAN_OFF", light1_value = "LIGHT1_OFF", light2_value = "LIGHT2_OFF", light3_value = "LIGHT3_OFF", socket_value = "SOCKET_OFF";
  //  public  ArrayAdapter mArrayAdapter;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    byte[] data;
    TextView text_view,textView2,textView3;
    private final static int REQUEST_ENABLE_BT = 1;
    UUID MY_UUID;
    ListView lv;
    View idk;
    public static ArrayList<Devices> devicelist ;
    boolean bluetooth_connected, transferInitiated;
    BluetoothDevice toConnect ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_view = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);


        final Button fan_butt = (Button) findViewById(R.id.fan);
        final Button light1_butt = (Button) findViewById(R.id.light1);
        final Button light2_butt = (Button) findViewById(R.id.light2);
        final Button light3_butt = (Button) findViewById(R.id.light3);
        final Button socket_butt = (Button) findViewById(R.id.socket);
        final Button connect_butt = (Button) findViewById(R.id.connect);
        devicelist = new <Devices>ArrayList();
        bluetooth_connected = false;
        transferInitiated = false;

        //BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            text_view.setText("Bluetooth is not supported");
        }

        MY_UUID = UUID.fromString("a3b16182-a98b-4fe9-9cc9-5f089d79c417");

        //see how to display devices in a list view and then select a device and send it to thread constructor
        //a connection is established then, now when user will press a button then start connected thread with
        //connected socket and write the byte data to another device and close the connection.

        fan_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferInitiated = true;


                if (fan_value.equals("FAN_ON")) {
                    fan_value = "FAN_OFF";
                    data = fan_value.getBytes();
                    checkBluetooth();
                    fan_butt.setText("Fan off");
                } else {
                    fan_value = "FAN_ON";
                    data = fan_value.getBytes();
                    checkBluetooth();
                    fan_butt.setText("Fan on");
                }
            }
        });

        light1_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (light1_value.equals("LIGHT1_ON")) {
                    light1_value = "LIGHT1_OFF";
                    light1_butt.setText("Light off");
                } else {
                    light1_value = "LIGHT1_ON";
                    light1_butt.setText("Light on");
                }
            }
        });

        light2_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (light2_value.equals("LIGHT2_ON")) {
                    light2_value = "LIGHT2_OFF";
                    light2_butt.setText("Light off");
                } else {
                    light2_value = "LIGHT2_ON";
                    light2_butt.setText("Light on");
                }
            }
        });

        light3_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (light3_value.equals("LIGHT3_ON")) {
                    light3_value = "LIGHT3_OFF";
                    light3_butt.setText("Light off");
                } else {
                    light3_value = "LIGHT3_ON";
                    light3_butt.setText("Light on");
                }
            }
        });

        socket_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (socket_value.equals("SOCKET_ON")) {
                    socket_value = "SOCKET_OFF";
                    socket_butt.setText("Socket off");
                } else {
                    socket_value = "SOCKET_ON";
                    socket_butt.setText("Socket on");
                }
            }
        });

        connect_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transferInitiated = false;
                checkBluetooth();
            }
        });








    }
    private void startTransfer(){
        textView2.setText("Start "+toConnect.getName()+" data to sent is "+fan_value);

        if(bluetooth_connected && transferInitiated) {
            ConnectThread ct = new ConnectThread(toConnect);

            ct.start();
            textView3.setText("Thread has been started");
            //ct.interrupt();
        }
        else{
           text_view.setText("Device is already connected");                 //????
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void manageConnectedSocket(BluetoothSocket mmSocket){
        //see what to do!!
        ConnectedThread conT = new ConnectedThread(mmSocket);
        if(data != null) {
            conT.write(data);
            String tempString = new String(data);
            text_view.setText("Data Sent" + " " + tempString);
            conT.interrupt();
        }
        else
            text_view.setText("DAta is null");
    }

    private void checkBluetooth(){
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
           // bluetooth_connected = true;
         //   start_connection();

        }
        else{
            bluetooth_connected = true;
            text_view.setText("Bluetooth is  ON");
            if(toConnect == null)
                start_connection();
            else
                startTransfer();
        }
    }

    private void start_connection(){

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    //   mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    if(checkDuplicacy(device))
                        devicelist.add(new Devices(device.getName(), device.getAddress(),device));
                }
            }
        passToList();





    }

    private boolean checkDuplicacy(BluetoothDevice device){
        for(Devices dev:devicelist){
            if(dev.address == device.getAddress()){
                return false;
            }
        }
        return true;
    }
    private void passToList(){
        //start discovering devices
        mBluetoothAdapter.startDiscovery();

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        // lv.setAdapter(mArrayAdapter);
         text_view.setText("I got till here");
        // mArrayAdapter.setDropDownViewResource(R.layout.device_list);
        if(bluetooth_connected){
            Intent intent = new Intent(this, ShowList.class);
            startActivity(intent);
            text_view.setText("But not here");
           // resumeActivity();
        }


    }

    @Override
    protected void onNewIntent(Intent recievedIntent){
        super.onNewIntent(recievedIntent);

        //Intent recievedIntent = getIntent();
        if(recievedIntent != null) {
            String name = recievedIntent.getStringExtra(ShowList.DEVICE_NAME);
            String address = recievedIntent.getStringExtra(ShowList.DEVICE_ADDRESS);
            text_view.setText("Finally device to connect is " + name + " " + address+" transfer is "+transferInitiated);

            for (Devices device : devicelist) {
                if (device.address.equals(address)) {
                    toConnect = device.deviceFound;
                    break;
                }
            }
            if (transferInitiated) {
                startTransfer();
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode,Intent data){
        super.onActivityResult(requestCode ,resultCode,data);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                text_view.setText("Bluetooth is on");
                bluetooth_connected = true;
                start_connection();
            }
            else if(resultCode == RESULT_CANCELED){
                checkBluetooth();

            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
               // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                textView2.setText("Hum chall rhe h" + " "+ device.getName());
                if(checkDuplicacy(device))
                    devicelist.add(new Devices(device.getName(),device.getAddress(),device));

            }
        }
    };




    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }


        // * Will cancel an in-progress connection, and close the socket


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }


    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    //  mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                    //    .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }


    // Call this from the main activity to shutdown the connection

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }


    }





}
