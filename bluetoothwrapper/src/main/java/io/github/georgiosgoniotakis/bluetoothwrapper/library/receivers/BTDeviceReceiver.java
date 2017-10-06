package io.github.georgiosgoniotakis.bluetoothwrapper.library.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTNotifiable;

/**
 * An custom {@link BroadcastReceiver} which notifies a member of the
 * {@link BTNotifiable} interface about changes in the BT connection's states.
 * <p>
 * Not implemented using the Singleton pattern because multiple classes may
 * want to subscribe to notifications about the change of the adapter's states.
 *
 * @author Georgios Goniotakis
 */
public class BTDeviceReceiver extends BroadcastReceiver {

    /**
     * Unique tag for this class.
     */
    private final String TAG = "BTDeviceReceiver.java";

    /**
     * Temporary instance of {@link BTNotifiable}
     */
    private final BTNotifiable btNotifiable;

    /**
     * Holds the preference of the user (true if logcat logging is on)
     */
    private final boolean logging;

    /**
     * Constructor of the class
     *
     * @param btNotifiable An instance of {@link BTNotifiable}
     * @param logging      The logging preference
     */
    public BTDeviceReceiver(BTNotifiable btNotifiable, boolean logging) {
        this.btNotifiable = btNotifiable;
        this.logging = logging;
    }

    /**
     * Custom implementation of the {@link BroadcastReceiver#onReceive(Context, Intent)}
     * method.
     *
     * @param context Current context
     * @param intent Current intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (this.btNotifiable != null) {
            btNotifiable.onDeviceStateChange(action);
        }

        if (this.logging) {
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.e(TAG, "Bluetooth device connected");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.e(TAG, "Bluetooth device disconnected");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    Log.e(TAG, "Bluetooth device is going to disconnect shortly");
                    break;
            }
        }
    }
}
