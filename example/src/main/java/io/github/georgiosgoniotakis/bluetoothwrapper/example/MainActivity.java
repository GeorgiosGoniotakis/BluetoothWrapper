package io.github.georgiosgoniotakis.bluetoothwrapper.example;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.core.BTExplorer;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.BTDeviceNotFoundException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTNotifiable;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.MessageCodes;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.properties.Mode;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.receivers.BTReceivers;

/**
 * This is a demo activity for the BluetoothWrapper Android library.
 * Here, you can find a comprehensive example with all of the library's utilities.
 * <p>
 * Also, do not forget to implement {@link BTNotifiable} to receive updates by
 * the connection's {@link android.content.BroadcastReceiver}
 *
 * @author Georgios Goniotakis
 */
public class MainActivity extends AppCompatActivity implements BTNotifiable, View.OnClickListener {


    /**
     * The connection's flag. Can be any number greater than zero.
     */
    private final static int REQUEST_ENABLE_BT = 1;

    /**
     * A {@link BTExplorer} instance to communicate with
     * the library
     */
    private BTExplorer btExplorer;

    /* UI components */
    private TextView currentMessage;

    private BTReceivers btReceivers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        Button connectButton = (Button) findViewById(R.id.connectButton);
        Button disconnectButton = (Button) findViewById(R.id.disconnectButton);

        currentMessage = (TextView) findViewById(R.id.currentMessage);

        sendButton.setOnClickListener(this);
        connectButton.setOnClickListener(this);
        disconnectButton.setOnClickListener(this);

        btReceivers = new BTReceivers(this, true);
        btReceivers.registerReceivers();

        btExplorer = BTExplorer.getInstance(btHandler); // Get Singleton Instance

        /*
            This line outputs a list with the available devices in the information logcat and stores
            it into a String array
         */
        String[][] info = btExplorer.deviceList(true);


        /*
            Here you can store all the paired devices if you want to use them
            in any other place inside your code.
         */

        Set<BluetoothDevice> pairedDevices = btExplorer.pairedDevices();

    }

    /**
     * Implements a handler to let the two classes communicate. In this way
     * the logic class can notify this class when an incoming message is
     * available and thus the UI is updated.
     */
    private final Handler btHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageCodes.MESSAGE_READ:
                    currentMessage.setText(msg.getData().getString(MessageCodes.INCOMING_MESSAGE));
                    break;
            }
        }
    };

    /**
     * The interaction with the Bluetooth connection popup is going to
     * trigger this method.
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == 0) {
            Toast.makeText(this, "Bluetooth has to be enabled in order to use this application.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User successfully enabled his/her Bluetooth connection.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Gives four basic examples of connecting, disconnecting, sending a message
     * and restarting a connection with a device.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.connectButton:

                /* Perform a new secure connection to a device giving the MAC address */
                try {

                    if (!btExplorer.isSupported()) {
                        Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    } else if (!btExplorer.isEnabled()) {
                        Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBT, REQUEST_ENABLE_BT);
                    } else {
                        btExplorer.connect("00:00:00:00:00:00", Mode.SECURE);
                    }

                } catch (BTDeviceNotFoundException e) {

                    /* Device not available any more, inform the user*/
                    Toast.makeText(this, "Please choose another device from the list.",
                            Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.disconnectButton:
                btExplorer.disconnect(); // Terminate the current connection
                break;
            case R.id.sendButton:
                btExplorer.send("OPEN"); // Send a message to the device
                break;
        }
    }

    /**
     * Always remember to unregister receivers on stop. Also, consider
     * controlling the receivers' state when {@link Activity#onPause()}
     * {@link Activity#onResume()} etc.
     */
    @Override
    protected void onStop() {
        super.onStop();
        btReceivers.unregisterReceivers();
    }

    /**
     * This is called whenever an update on the adapter's
     * state occurs.
     *
     * @param adapterState The status code of the BT adapter
     */
    @Override
    public void onAdapterStateChange(int adapterState) {
        if (adapterState == BluetoothAdapter.STATE_TURNING_OFF) {
            Toast.makeText(this, "Please re-enable bluetooth.", Toast.LENGTH_LONG).show();
            btExplorer.disconnect();
        } else if (adapterState == BluetoothAdapter.STATE_TURNING_ON) {
            Toast.makeText(this, "Thank you for enabling you bluetooth.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This is called whenever an update on the connection's
     * state occurs.
     *
     * @param deviceState The status code of the connection
     */
    @Override
    public void onDeviceStateChange(String deviceState) {
        if (deviceState.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            Toast.makeText(this, "BT Device connected", Toast.LENGTH_LONG).show();
        } else if (deviceState.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
            Toast.makeText(this, "BT Device disconnected", Toast.LENGTH_LONG).show();
            btExplorer.disconnect();
        }
    }
}

