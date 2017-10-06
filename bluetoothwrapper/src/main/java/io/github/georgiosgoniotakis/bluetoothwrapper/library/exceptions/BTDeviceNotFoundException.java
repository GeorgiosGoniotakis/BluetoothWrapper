package io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions;

/**
 * This exception is thrown whenever a connection to a
 * device that does not exist anymore, in the list of paired
 * devices, takes place. The developer is obliged to handle this,
 * defining in this way how his/her app should react.
 *
 * @author Georgios Goniotakis
 */
public class BTDeviceNotFoundException extends Exception {

    public BTDeviceNotFoundException() {
        super("The Bluetooth device with this name does not exist.");
    }
}
