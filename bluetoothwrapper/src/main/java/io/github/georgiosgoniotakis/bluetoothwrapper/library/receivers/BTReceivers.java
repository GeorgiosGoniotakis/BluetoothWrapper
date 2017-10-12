package io.github.georgiosgoniotakis.bluetoothwrapper.library.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTNotifiable;

/**
 * This class can be used to initialize the two BroadcastReceivers
 * {@link BTAdapterReceiver} and {@link BTDeviceReceiver} at once.
 * <p>
 * Please use it only if you are use that the {@link Context} object
 * stays the same and current during the instance of this class. An
 * alternative is to copy the code of this class and adapt it to any
 * {@link android.app.Activity} class that you want.
 *
 * @author Georgios Goniotakis
 */
public class BTReceivers {

    /**
     * Unique identifier for this class
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * The current context
     */
    private final Context context;

    /**
     * The current {@link BTNotifiable} instance
     */
    private BTNotifiable btNotifiable;

    /**
     * A receiver to get updates for the BT adapter's state
     */
    private BTAdapterReceiver btAdapterReceiver;

    /**
     * A receiver to get updates for the BT connection's state
     */
    private BTDeviceReceiver btDeviceReceiver;

    /**
     * Constructor of the class. Initializes both receivers.
     *
     * @param context The current context
     * @param logging True if output in Logcat is preferred
     */
    public BTReceivers(Context context, boolean logging) {

        this.context = context;

        if (!validateParameters()) {
            Log.e(TAG, "The input parameters are invalid.");
            return;
        }

        this.btNotifiable = (BTNotifiable) this.context;

        this.btAdapterReceiver = new BTAdapterReceiver(this.btNotifiable, logging);
        this.btDeviceReceiver = new BTDeviceReceiver(this.btNotifiable, logging);
    }

    /**
     * Validates that the {@link Context} object that is passed
     * to the constructor is also a valid {@link BTNotifiable}
     * instance.
     *
     * @return Result of this check (true if it is)
     */
    private boolean validateParameters() {
        return this.context != null && this.context instanceof BTNotifiable;
    }

    /**
     * Can be called outside this class to register
     * the two receivers.
     */
    public void registerReceivers() {
        registerBTAdapterReceiver();
        registerBTDeviceReceiver();
    }

    /**
     * Unregisters the two receivers. Currently there is no method
     * implemented in the official API to check if a class is registered
     * to a {@link android.content.BroadcastReceiver}. A workaround of that is
     * to use {@link IllegalArgumentException}. Use it separately for each receiver.
     */
    public void unregisterReceivers() {

        try {
            context.unregisterReceiver(btAdapterReceiver);

        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "No active registration in BTAdapterReceiver");
        }

        try {
            context.unregisterReceiver(btDeviceReceiver);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "No active registration in BTDeviceReceiver");
        }
    }

    /**
     * Call this method to subscribe the class to the
     * {@link BTDeviceReceiver}
     */
    private void registerBTDeviceReceiver() {

        IntentFilter btDevFilter = new IntentFilter();
        btDevFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        btDevFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        btDevFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        context.registerReceiver(btDeviceReceiver, btDevFilter);

    }

    /**
     * Call this method to subscribe the class to the
     * {@link BTAdapterReceiver}
     */
    private void registerBTAdapterReceiver() {

        IntentFilter btAdFilter = new IntentFilter();
        btAdFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(btAdapterReceiver, btAdFilter);

    }
}
