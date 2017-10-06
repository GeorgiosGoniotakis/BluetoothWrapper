package io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.core.BTManager;

/**
 * This includes some basic messages' codes that the developer
 * can use to track the purpose of each {@link android.os.Message}
 * delivered by the handler of the {@link BTManager}
 *
 * @author Georgios Goniotakis
 */
public interface MessageCodes {

    /**
     * Code to notify for incoming message
     */
    int MESSAGE_READ = 0;

    /**
     * Code to notify for upcoming message transmission
     */
    int MESSAGE_WRITE = 1;

    /**
     * Identifier for a particular type of incoming messages
     */
    String INCOMING_MESSAGE = "INCOMING_MESSAGE";

    /* Other codes or identifier can be defined in this file (i.e. error codes etc.) */
}
