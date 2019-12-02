package com.example.lumibt;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    BluetoothAdapter mBlutoothadapter;
    ;




    //create broadcast reciever for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReciever1  = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when descovery finds device
            if(action.equals(mBlutoothadapter.ACTION_STATE_CHANGED)){
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBlutoothadapter.ERROR);
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


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");

        super.onDestroy();
        unregisterReceiver(mBroadcastReciever1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btONOFF = (Button) findViewById(R.id.btONOFF);

        mBlutoothadapter = BluetoothAdapter.getDefaultAdapter();

    btONOFF.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: enabling/disabling bluetooth.");
            enabledisable();

        }
    });

    }
    public void enabledisable(){
        if(mBlutoothadapter== null){
            Log.d(TAG,"enabledisableBT: Does not have bluetooth capability");
        }
        if (!mBlutoothadapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            registerReceiver(mBroadcastReciever1, BTIntent);

        }
        if(mBlutoothadapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");

            mBlutoothadapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            registerReceiver(mBroadcastReciever1, BTIntent);
        }
    }
}
