package io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.core.BTExplorer;

/**
 * This interface is used by {@link BTExplorer}
 * to notify any class which implements it about the status of the bluetooth adapter.
 * and
 *
 * @author Georgios Goniotakis
 */
public interface BTReachable {

    /**
     * This method is being triggered if the user has disabled
     * the BT connection in their phone.
     */
    void onBluetoothDisabled();

    /**
     * This method is being triggered if the current device
     * does not support bluetooth functionality.
     */
    void onBluetoothNotSupported();
}
