package io.github.georgiosgoniotakis.bluetoothwrapper.library.core;

import org.junit.Test;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.exceptions.NotValidHandlerException;

/**
 * Unit tests for the {@link BTExplorer} class.
 * <p>
 * More tests need to be carried out in case that a method
 * to mock the connection's elements can be found.
 *
 * @author Georgios Goniotakis
 */
public class BTExplorerTest {

    /**
     * Tests if the constructor of the {@link BTExplorer} class accepts
     * as argument a null Handler.
     * <p>
     * Outcome: It will throw a {@link NotValidHandlerException}
     */
    @Test(expected = NotValidHandlerException.class)
    public void testNullHandler() {
        BTExplorer.getInstance(null);
    }

}
