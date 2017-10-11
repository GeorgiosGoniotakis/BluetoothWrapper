package io.github.georgiosgoniotakis.bluetoothwrapper.library.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTNotifiable;

/**
 * File Name: FILE_NAME
 *
 * @author Georgios Goniotakis
 *         Date: 11-Oct-17 11:55 PM
 *         Description:
 */
public class BTReceivers {

    private final String TAG = getClass().getSimpleName();

    private final Context context;
    private BTNotifiable btNotifiable;

    /**
     * A receiver to get updates for the BT adapter's state
     */
    private BTAdapterReceiver btAdapterReceiver;

    /**
     * A receiver to get updates for the BT connection's state
     */
    private BTDeviceReceiver btDeviceReceiver;

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

    private boolean validateParameters() {
        return this.context != null && this.context instanceof BTNotifiable;
    }

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
