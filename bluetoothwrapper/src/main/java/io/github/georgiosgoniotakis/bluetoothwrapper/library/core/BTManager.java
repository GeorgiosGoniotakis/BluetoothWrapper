package io.github.georgiosgoniotakis.bluetoothwrapper.library.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.BTSocketNotInitializedException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.MessageCodes;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.properties.Mode;

/**
 * This is the heart of the Bluetooth library. In here, all the methods
 * that control the state and the transmission of data are included. The
 * reason why it is a rather large class is to avoid bypass classes that
 * would transfer common information between constructors and methods.
 *
 * @author Georgios Goniotakis
 */
final public class BTManager {

    /**
     * Unique tag for this class
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Standard UUID code
     */
    private final String UUID_CODE = "00001101-0000-1000-8000-00805F9B34FB";

    /**
     * Variable to control the thread's state
     */
    private volatile boolean threadIsRunning;

    /**
     * The device's current adapter
     */
    private final BluetoothAdapter btAdapter;

    /**
     * The handler which exchanges information between this
     * class and an activity or logic class.
     */
    private Handler btHandler;

    /**
     * {@link InputStream} used for incoming message
     */
    private final InputStream btInStream;

    /**
     * {@link OutputStream} used for outgoing messages
     */
    private final OutputStream btOutStream;

    /**
     * The communication socket
     */
    private final BluetoothSocket btSocket;

    /**
     * Used to temporary store any incoming message
     */
    private byte[] inBuffer;

    /**
     * Used to track the reading position inside the buffer
     */
    private int pos;

    /**
     * The constructor here initializes a new socket and defines
     * the input and output streams.
     *
     * @param btDevice  The bluetooth device to connect to
     * @param btAdapter The device's current BT adapter
     * @param btHandler The handler which establishes the exchange of the incoming messages
     * @param mode      The mode of the connection (either {@link Mode#SECURE} SECURE
     *                  or {@link Mode#INSECURE})
     */
    BTManager(BluetoothDevice btDevice, BluetoothAdapter btAdapter, Handler btHandler,
              Mode mode) {

        this.btAdapter = btAdapter;
        this.btHandler = btHandler;

        BluetoothSocket tmpSocket = null;

        /* Has to be done inside constructor because final variables cannot be defined outside here
        *  (Ensure socket != null before continue)
        * */
        try {

            UUID uuid = UUID.fromString(UUID_CODE);

            switch (mode) {
                case SECURE:
                    tmpSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                    break;
                case INSECURE:
                    tmpSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    break;
                default:
                    tmpSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                    break;
            }


        } catch (IOException ex) {
            Log.e(TAG, "Problem initializing connection to socket");
        }

        this.btSocket = tmpSocket;

        if (btSocket == null) throw new BTSocketNotInitializedException();

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = btSocket.getInputStream();
        } catch (IOException | NullPointerException ex) {
            Log.e(TAG, "Error creating input stream");
        }

        try {
            tmpOut = btSocket.getOutputStream();
        } catch (IOException ex) {
            Log.e(TAG, "Error creating output stream");
        }

        btInStream = tmpIn;
        btOutStream = tmpOut;
    }


    /**
     * This method performs the initialization of the connection
     * with the BT socket.
     */
    private void initializeSocket() {

        btAdapter.cancelDiscovery();

        try {
            btSocket.connect();
        } catch (IOException connectionException) {
            Log.e(TAG, "Unable to connect to socket.");
            terminateTransmission();
        }

    }

    /**
     * Implements a regular buffer reader schema for the incoming messages.
     * When an incoming message is being traced, the handler sends it
     * to the appropriate class using a {@link Message}.
     */
    void beginTransmission() {

        final Handler handler = new Handler();
        final byte delimiter = 10; // ASCII new line character

        threadIsRunning = true;
        pos = 0;
        inBuffer = new byte[1024];


        Thread workerThread = new Thread(new Runnable() {

            public void run() {

                initializeSocket(); // Initialize the socket first

                while (!Thread.currentThread().isInterrupted() && threadIsRunning) {

                    try {

                        int numBytes = btInStream.available();

                        if (numBytes > 0) {

                            byte[] btReader = new byte[numBytes];
                            btInStream.read(btReader);

                            for (int i = 0; i < numBytes; i++) {

                                byte b = btReader[i];

                                if (b == delimiter) {

                                    byte[] parsedBytes = new byte[pos];
                                    System.arraycopy(inBuffer, 0, parsedBytes, 0, parsedBytes.length);
                                    final String data = new String(parsedBytes, "UTF-8");
                                    pos = 0;

                                    handler.post(new Runnable() {
                                        public void run() {

                                            /* Enable this to output the messages into Logcat */
                                            //Log.v("Incoming Message: ", data);


                                            /* Send the message back to the class using the Handler */
                                            Message readMsg = btHandler.obtainMessage(MessageCodes.MESSAGE_READ);
                                            Bundle tmpBundle = new Bundle();
                                            tmpBundle.putString(MessageCodes.INCOMING_MESSAGE, data);
                                            readMsg.setData(tmpBundle);
                                            btHandler.sendMessage(readMsg);
                                        }
                                    });

                                } else {
                                    inBuffer[pos++] = b;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        threadIsRunning = false;
                    }
                }
            }
        });

        workerThread.start();
    }

    /**
     * This method converts a given string to a series of bytes
     * and transmits it to the socket.
     *
     * @param inputMessage A given message as a string
     */
    void sendMessage(String inputMessage) {

        if (inputMessage == null) {
            return;
        }

        byte[] messageToBytes;

        try {
            messageToBytes = inputMessage.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            messageToBytes = inputMessage.getBytes();
        }

        try {

            btOutStream.write(messageToBytes);

        } catch (IOException ex) {
            Log.e(TAG, "Unable to write to buffer");
        }
    }

    /**
     * Closes the socket.
     */
    private void closeSocket() {
        try {
            btSocket.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to close socket");
        }
    }

    /**
     * This method clears the input and output streams.
     */
    private void clearStreams() {

        try {
            btInStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close input stream.");
        }

        try {
            btOutStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close output stream.");
        }

    }

    /**
     * Makes all the necessary calls to stop the data
     * transmission and close the socket.
     * <p>
     * CAUTION!! If you try to close an open socket with a particular
     * device and open it again straight afterwards you will notice that
     * the socket does not close instantly. Add a delay after the call
     * to {@link #closeSocket()} to ensure that the socket has been
     * closed.
     */
    void terminateTransmission() {
        threadIsRunning = false;
        clearStreams();
        closeSocket();
        BTExplorer.getInstance(null).disconnect();
    }

    /**
     * Makes available the socket state to outer classes.
     *
     * @return The state of the BT socket (true if active)
     */
    boolean isSocketConnected() {
        return btSocket.isConnected();
    }

}
