package io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.receivers.*;

/**
 * This interface notifies the class which implements it about
 * changes in the bluetooth adapter's state and about the
 * bluetooth activity of the current device. It is used by
 * the two {@link android.content.BroadcastReceiver}.
 *
 * @author Georgios Goniotakis
 * @see BTAdapterReceiver
 * @see BTDeviceReceiver
 */
public interface BTNotifiable {

    /**
     * Returns a status code whenever a change in the adapter's
     * state is occurred.
     *
     * @param adapterState The status code of the BT adapter
     * @see BTAdapterReceiver
     */
    void onAdapterStateChange(int adapterState);

    /**
     * Returns a status code, as a string, whenever a change in a
     * connection with a device occurs.
     *
     * @param deviceState The status code of the connection
     */
    void onDeviceStateChange(String deviceState);
}
