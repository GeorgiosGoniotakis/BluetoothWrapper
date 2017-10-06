package io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.core.BTManager;

/**
 * Throws a {@link RuntimeException} in case that the BT socket
 * inside the {@link BTManager} class cannot be initialized.
 *
 * @author Georgios Goniotakis
 */
public class BTSocketNotInitializedException extends RuntimeException {

    public BTSocketNotInitializedException() {
        super("The BT socket could not be initialized.");
    }
}
