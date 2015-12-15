package jp.co.lafla.bletest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;


@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothAdapter.LeScanCallback leScanCallback;
    public static BluetoothManager bluetoothManager;
    public final String TAG = "MainActivity";
    public final int START_LE_SCAN_MAX_RETRY = 3;
    public static boolean scanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void printState(){
        Log.i(TAG, String.format("getState: %s", bluetoothAdapter.getState()));
        Log.i(TAG, String.format("getScanMode: %s", bluetoothAdapter.getScanMode()));
        Log.i(TAG, String.format("isDiscovering: %s", bluetoothAdapter.isDiscovering()));
    }

    @SuppressWarnings("deprecation")
    public int start(Context context) {
        Log.i(TAG, "Start scanning");
        bluetoothManager = (BluetoothManager)context.getSystemService(
                Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter!=null) {
            return startBluetoothAdapter();
        }
        return 1;
    }

    @SuppressWarnings("deprecation")
    public int startBluetoothAdapter(){
        if (!scanning) {
            leScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    leScanHandler(device, rssi, scanRecord);
                }
            };
            int retry = START_LE_SCAN_MAX_RETRY;
            while (!bluetoothAdapter.startLeScan(leScanCallback)) {
                retry -= 1;
                if (retry < 0){
                    Log.e(TAG, "startLeScan failed!");
                    return 1;
                }
                Log.w(TAG, "startLeScan failed! retry...");
            }
            scanning = true;
            printState();
            return 0;
        }
        return 1;
    }

    private void leScanHandler(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.i(TAG, String.format("RSSI: %s", rssi));
    }
}
