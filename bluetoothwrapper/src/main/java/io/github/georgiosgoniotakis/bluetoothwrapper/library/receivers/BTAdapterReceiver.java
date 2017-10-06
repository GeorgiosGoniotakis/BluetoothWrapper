package io.github.georgiosgoniotakis.bluetoothwrapper.library.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.github.georgiosgoniotakis.bluetoothwrapper.library.interfaces.BTNotifiable;


/**
 * An custom {@link BroadcastReceiver} which notifies a member of the
 * {@link BTNotifiable} interface about changes in the BT adapter's states.
 * <p>
 * Not implemented using the Singleton pattern because multiple classes may
 * want to subscribe to notifications about the change of the adapter's states.
 *
 * @author Georgios Goniotakis
 */
public class BTAdapterReceiver extends BroadcastReceiver {

    /**
     * Unique tag for this class.
     */
    private final String TAG = "BTAdapterReceiver.java";

    /**
     * Temporary instance of {@link BTNotifiable}
     */
    private final BTNotifiable btNotifiable;

    /**
     * Holds the preference of the user (true if logcat logging is on)
     */
    private final boolean logging;

    /**
     * Constructor of the class
     *
     * @param btNotifiable An instance of {@link BTNotifiable}
     * @param logging      The logging preference
     */
    public BTAdapterReceiver(BTNotifiable btNotifiable, boolean logging) {
        this.btNotifiable = btNotifiable;
        this.logging = logging;
    }

    /**
     * Custom implementation of the {@link BroadcastReceiver#onReceive(Context, Intent)}
     * method.
     *
     * @param context Current context
     * @param intent Current intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED) && this.btNotifiable != null) {

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            this.btNotifiable.onAdapterStateChange(state);

            if (this.logging) {
                switch (state) {

                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.e(TAG, "STATE_CONNECTED");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.e(TAG, "STATE_CONNECTING");
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTED:
                        Log.e(TAG, "STATE_DISCONNECTED");
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTING:
                        Log.e(TAG, "STATE_DISCONNECTING");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.e(TAG, "STATE_OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.e(TAG, "STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.e(TAG, "STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.e(TAG, "STATE_TURNING_ON");
                        break;
                }
            }
        }
    }

}
