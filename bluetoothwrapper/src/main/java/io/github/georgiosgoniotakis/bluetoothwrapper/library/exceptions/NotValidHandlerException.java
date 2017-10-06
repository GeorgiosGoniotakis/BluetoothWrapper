package io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions;

import android.os.Handler;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTReachable;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.core.BTExplorer;

/**
 * This is a runtime exception which occurs whenever the developer
 * tries to pass a null {@link Handler} to the
 * {@link BTExplorer#getInstance(BTReachable, Handler)}
 *
 * @author Georgios Goniotakis
 */
public class NotValidHandlerException extends RuntimeException {

    public NotValidHandlerException() {
        super("Message handler cannot be null");
    }
}
