package io.github.georgiosgoniotakis.bluetoothwrapper.library.core;

import android.os.Handler;

import org.junit.Test;


import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.NotValidBTMemberException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.NotValidHandlerException;
import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTReachable;

/**
 * Unit tests for the {@link BTExplorer} class.
 * <p>
 * More tests need to be carried out in case that a method
 * to mock the connection's elements can be found.
 *
 * @author Georgios Goniotakis
 */
public class BTExplorerTest implements BTReachable {

    /**
     * Tests if the constructor of the {@link BTExplorer} class accepts
     * as argument a null object.
     * <p>
     * Outcome: It will throw a {@link NotValidBTMemberException}
     */
    @Test(expected = NotValidBTMemberException.class)
    public void testNullObject() throws NotValidBTMemberException {
        BTExplorer.getInstance(null, new Handler());
    }

    /**
     * Tests if the constructor of the {@link BTExplorer} class accepts
     * as argument a null Handler.
     * <p>
     * Outcome: It will throw a {@link NotValidHandlerException}
     */
    @Test(expected = NotValidHandlerException.class)
    public void testNullHandler() {
        BTExplorer.getInstance(this, null);
    }


    /* Unused implementations */

    @Override
    public void onBluetoothDisabled() {

    }

    @Override
    public void onBluetoothNotSupported() {

    }
}
