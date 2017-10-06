package io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions;

import android.os.Handler;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.core.BTExplorer;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTReachable;

/**
 * This is a runtime exception which occurs whenever the developer
 * tries to pass a class member which is not an instance of the
 * {@link BTReachable} class to the
 * {@link BTExplorer#BTExplorer(BTReachable, Handler)}.
 *
 * @author Georgios Goniotakis
 */
public class NotValidBTMemberException extends RuntimeException {

    public NotValidBTMemberException() {
        super("The object passed in the BTExplorer is not implementing the BTReachable interface.");
    }
}
