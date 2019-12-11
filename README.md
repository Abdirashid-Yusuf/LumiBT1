
# Bluetooth

                                      	## Introduction

Bluetooth is a way to send and receive data between two different devices over short distance. Android platform includes support for the Bluetooth framework that allows devices to wirelessly exchange data with other Bluetooth devices. It can also be used for connecting to devices with Bluetooth capability for wireless audio transmission. We can enable Bluetooth using the Bluetooth adapter class
```
BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
adapter.enable();

```
For this to happen, you need to add extra permisions in the AndroidManifest.xml
```

<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permisson.BLUETOOTH"/>

``` 
					### History

Bluetooth was first invented in 1994 by a man named Dr. Jaap Hartsen while working at Erricson.  Bluetooth has been available in the android studio from Android 2.0 éclair(API 5) but Bluetooth low energy support only from Android 4.3 Jelly Bean (API 18). The native Bluetooth stack is qualified for Bluetooth 5 in Android 8.0 . all devices need to have Bluetooth 5 qualified chipset in order to use the available Bluetooth 5 features.</br>


					### Major methods & Attributes 
## i)	BroadcastReicvers.    </br>
I have used different methods throughout my project. Broadcast receivers have come in hand to catch the state change and log them. The first one is for ACTION_FOUND.
```

private final BroadcastReceiver mBroadcastReciever1  = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //when descovery finds device
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state){
                case BluetoothAdapter.STATE_OFF:
                    Log.d(TAG,"onReceive: STATE OFF");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                    break;

            }


        }
    }
};

``` 
This is followed by mBroadcastReciever2 which catches and logs statechange i.e. Discoverability.
```
private final BroadcastReceiver mBroadcastReciever2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch (mode) {
//Device is in descoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                    break;
                  //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;

                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");

                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }
            }
        }
    };
```

Thirdly, mBroadcast3 I have usedBroadcastReceiver method for listing devices that are not yet paired
 ```
private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "onReceive: ACTION FOUND.");
        if (action.equals(BluetoothDevice.ACTION_FOUND)){
            BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
            mBTDevices.add(device);
            Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
           mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
           // mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view);
            lvNewDevices.setAdapter(mDeviceListAdapter);
        }


    }
};


```
This method is executed by a call to btnDiscover() method


```
public void btnDiscover(View view) {
    Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
    if (mBlutoothadapter.isDiscovering()){
        mBlutoothadapter.cancelDiscovery();
        Log.d(TAG, "btnDiscover: Canceling discovery.");

        //check BT permissions in manifest
        checkBTPermissions();

        mBlutoothadapter.startDiscovery();
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
    }

    if(!mBlutoothadapter.isDiscovering()){

        //check BT permissions in manifest
        checkBTPermissions();

        mBlutoothadapter.startDiscovery();
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
    }

}


```

The final Broadcast Reciever that I have used is for detecting bond state changes ( pairing state changes)
```
private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //3 cases:
            //case1: bonded already
            if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
            }
            //case2: creating a bone
            if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
            }
            //case3: breaking a bond
            if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
            }
        }
    }
};

```
## ii)	Protected methods </br>
protected void onDestroy() //  is called when activity is finishing.  </br>
 
## iii)	Public methods.     </br>
public void onReceive(Context context, Intent intent)// is called when broadcast receiver is receiving an intent broadcast </br>

public void enabledisable() // I have used this method to enable bluetooth if it is dibaled and disable if it is enable. </br>
private void checkBTPermissions()// check the permissions for bluetooth.   </br>
	


## iv)	Constants </br>
I have used several constants throughout my project.  They include:     </br>
ACTION_STATE_CHANGED // This notifies that Bluetooth status has been changed.     </br>
ACTION_SCAN_MODE_CHANGED // This notifies whenever the scan mode changes.        </br>
ACTION_FOUND //This is used for receiving information about each found device.      </br>
ACTION_BOND_STATE_CHANHGE // this indicate change in the bond state of the device.  </br>


Note: Apart from the main class I have written a class DeviceListAdapter  to store available devices

			### Refferences

I have used the following websites for guidance:  </br>
i)	https://developer.android.com/guide/topics/connectivity/bluetooth.     </br>
ii)	https://www.tutorialspoint.com/android/android_bluetooth.htm.       </br>
    


























