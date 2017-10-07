package io.github.georgiosgoniotakis.bluetoothwrapper.library.core;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.BTDeviceNotFoundException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.NotValidBTMemberException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.NotValidHandlerException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTReachable;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.properties.Mode;

/**
 * This it the main file of the BluetoothWrapper library. This class monitors
 * initializes a basic connection with the device's BT adapter and after that
 * it monitors any paired devices. It contains many utility methods which allow
 * the developer to obtain information about any paired devices.
 * <p>
 * Please refer to the library's example to get started.
 * This class is implemented following the Thread-Safe Singleton pattern.
 *
 * @author Georgios Goniotakis
 * @version 1.0
 * @since 5 October 2017
 */
public class BTExplorer {

    /**
     * Unique debug identifier for this class
     */
    private final String TAG = "BTExplorer.java";

    /**
     * An instance of {@link BTReachable}
     */
    private BTReachable btReachable;

    /**
     * Global instance of {@link BluetoothAdapter} for this class
     */
    private BluetoothAdapter bluetoothAdapter = null;

    /**
     * Handler which transports the incoming and outgoing
     * messages to another class.
     */
    private Handler btHandler;

    /**
     * {@link BTExplorer} instance used for Singleton pattern
     */
    private static BTExplorer instance;

    /**
     * {@link BTManager} instance used for managing the connection
     */
    private BTManager btManager;

    /**
     * BTExplorer's private constructor to disallow access.
     *
     * @param btReachable An instance of a {@link BTReachable} member
     * @param btHandler   A {@link Handler} for incoming and outgoing messages
     */
    private BTExplorer(BTReachable btReachable, Handler btHandler) {

        this.btReachable = btReachable;
        this.btHandler = btHandler;
        initializeConnection();
    }


    /**
     * Standard getInstace method of Singleton pattern. It is synchronized
     * to be Thread-Safe and uses a @RequiresPermission annotation to remind
     * to the user about the required permissions. Can be initialized with
     * {@link BTReachable} and {@link Handler} objects to enable
     * passing the BT notifications and incoming messages to a given class.
     * <p>
     *
     * @param btReachable An instance of a {@link BTReachable} member
     * @param btHandler   A {@link Handler} for incoming and outgoing messages
     * @return The instance of this class
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public static synchronized BTExplorer getInstance(BTReachable btReachable, Handler btHandler) {

        if (instance == null) {
            instance = new BTExplorer(btReachable, btHandler);
        }

        return instance;
    }


    /**
     * Initializes a BT connection after it checks that all the
     * criteria are met. Otherwise, it notifies the {@link BTReachable}
     * member.
     */
    private void initializeConnection() {

        validateObject();
        validateHandler();
        getBluetoothAdapter();

        if (isSupported()) {

            if (!isEnabled()) {
                btReachable.onBluetoothDisabled();
            }

        } else {
            btReachable.onBluetoothNotSupported();
        }
    }

    /**
     * Checks whether the class which uses the library is an
     * implementor of the {@link BTReachable} interface.
     */
    private void validateObject() {
        if (btReachable == null) throw new NotValidBTMemberException();
    }

    /**
     * Checks whether the Handler is null.
     */
    private void validateHandler() {
        if (btHandler == null) throw new NotValidHandlerException();
    }

    /**
     * Get the device's {@link BluetoothAdapter}
     */
    private void getBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Returns the device's Bluetooth compatibility
     *
     * @return Returns false if the device does not support BT
     */
    private boolean isSupported() {
        return bluetoothAdapter != null;
    }

    /**
     * Returns the device's Bluetooth current status
     *
     * @return Returns false if the device's BT is disabled
     */
    private boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    /**
     * Uses the {@link #isSupported()} and {@link #isEnabled()}
     * methods to check whether the connection criteria are met.
     *
     * @return boolean
     */
    private boolean meetsCriteria() {
        return isSupported() && isEnabled();
    }


    /**
     * This method produces a {@link Set} with all the available Bluetooth
     * devices as {@link BluetoothDevice} objects.
     *
     * @return Returns a {@link Set} with all the available
     * paired Bluetooth devices.
     */
    public Set<BluetoothDevice> pairedDevices() {

        if (!meetsCriteria()) {
            Log.e(TAG, "List cannot be obtained because connection criteria are not met.");
            return null;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            return pairedDevices;
        }

        return null;

    }

    /**
     * Converts the list of paired devices to a {@link Map}
     * to enable the user to connect to a device using a device name. In this way,
     * the {@link #connect(String, Mode)} method can use a name to connect to a
     * BT device. It uses a {@link TreeMap} to sort the list as fast as possible.
     *
     * @return A Map with the paired BT devices along with their names
     */
    private Map<String, BluetoothDevice> devicesToMap() {

        if (!meetsCriteria()) {
            Log.e(TAG, "List cannot be obtained because connection criteria are not met.");
            return null;
        }

        Set<BluetoothDevice> pairedDevices = pairedDevices();
        Map<String, BluetoothDevice> devices = new TreeMap<>();

        if (pairedDevices != null) {
            for (BluetoothDevice bluetoothDevice : pairedDevices) {
                devices.put(bluetoothDevice.getAddress(), bluetoothDevice);
            }
        }

        return devices;
    }

    /**
     * Produces a printed list with all the paired Bluetooth devices
     * along with their MAC addresses in the information logcat window.
     *
     * @param output The developer's preference for logcat logging
     */
    public String[][] deviceList(boolean output) {

        if (!meetsCriteria()) {
            Log.e(TAG, "List cannot be printed because connection criteria are not met.");
            return null;
        }


        Set<BluetoothDevice> pairedDevices = pairedDevices();
        String[][] info = new String[pairedDevices().size()][2];

        StringBuilder table = new StringBuilder();
        table.append("----------------------------------------------------\n");


        if (pairedDevices != null) {

            int cnt = 0;

            table.append(String.format("%-30s | %-20s\n", "DEVICE NAME", "MAC ADDRESS"));

            for (BluetoothDevice bluetoothDevice : pairedDevices) {

                info[cnt][0] = bluetoothDevice.getName();
                info[cnt][1] = bluetoothDevice.getAddress();

                table.append(String.format("%-30s | %-20s\n", bluetoothDevice.getName(),
                        bluetoothDevice.getAddress()));

                cnt++;
            }
        } else {
            table.append("No paired devices in list\n");
        }

        table.append("----------------------------------------------------\n");

        if (output) {
            Log.i(TAG, table.toString());
        }

        return info;

    }


    /**
     * Performs all necessary checks and establishes the communication with
     * the requested device.
     * <p>
     * The developer passes a MAC address either manually or obtained by
     * the {@link #deviceList(boolean)} method.
     *
     * @param deviceMAC The device's MAC address
     * @param mode       Preferred connection mode ({@link Mode#SECURE}
     *                   or {@link Mode#INSECURE})
     * @throws BTDeviceNotFoundException If the device is not available any more an
     *                                   exception is being thrown
     */
    public void connect(String deviceMAC, Mode mode)
            throws BTDeviceNotFoundException {

        if (!meetsCriteria()) {
            Log.e(TAG, "Connection cannot be performed because connection criteria are not met.");
            return;
        }


        if (isConnectionActive()) {
            Log.e(TAG, "BT device currently connected. Please disconnect first.");
            return;
        }


        Map<String, BluetoothDevice> tmp = devicesToMap();

        if (tmp != null && tmp.containsKey(deviceMAC)) {
            btManager = new BTManager(tmp.get(deviceMAC), bluetoothAdapter, btHandler, mode);
            btManager.beginTransmission();
        } else {
            throw new BTDeviceNotFoundException();
        }

    }

    /**
     * Performs all necessary checks and establishes the communication with
     * the requested device.
     * <p>
     * The developer passes the {@link BluetoothDevice} object
     * obtained by the {@link #pairedDevices()} method.
     *
     * @param btDevice The bluetooth device
     * @param mode     Preferred connection mode ({@link Mode#SECURE}
     *                 or {@link Mode#INSECURE})
     * @throws BTDeviceNotFoundException If the device is not available any more an
     *                                   exception is being thrown
     */
    public void connect(BluetoothDevice btDevice, Mode mode)
            throws BTDeviceNotFoundException {

        if (!meetsCriteria()) {
            Log.e(TAG, "Connection cannot be performed because connection criteria are not met.");
            return;
        }


        if (isConnectionActive()) {
            Log.e(TAG, "BT device currently connected. Please disconnect first.");
            return;
        }


        Set<BluetoothDevice> pairedDevices = pairedDevices();

        if (btDevice != null && pairedDevices != null && pairedDevices.contains(btDevice)) {
            btManager = new BTManager(btDevice, bluetoothAdapter, btHandler, mode);
        } else {
            throw new BTDeviceNotFoundException();
        }

    }

    /**
     * Use this to send a message to a BT device.
     *
     * @param message The message as a String
     */
    public void send(String message) {
        if (btManager != null) {
            btManager.sendMessage(message);
        } else {
            Log.e(TAG, "No active connection with device. Message cannot be sent.");
        }
    }

    /**
     * Returns the state of a connected device
     *
     * @return True if a connection with a BT device is active
     */
    private boolean isConnectionActive() {
        return btManager != null;
    }

    /**
     * Call this every time that you want to disconnect from
     * a connected device.
     */
    public void disconnect() {

        if (isConnectionActive()) {
            btManager.terminateTransmission();
            btManager = null;
            Log.v(TAG, "A connection to another device was active. It is now deactivated.");
        }
    }
}
