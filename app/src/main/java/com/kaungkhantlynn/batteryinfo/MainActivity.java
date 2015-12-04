package com.kaungkhantlynn.batteryinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtLevel,txtStatus,txtCapacity,txtRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLevel=(TextView)findViewById(R.id.battery_level);
        txtStatus=(TextView)findViewById(R.id.battery_status);
        txtCapacity=(TextView)findViewById(R.id.battery_capacity);
        txtRemain=(TextView)findViewById(R.id.battery_remain);

        //add battery level - with broadcast receiver
       this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        getBatteryCapacity();

        /** Setting an alarm, which invokes the operation at alart_time */





    }

    //calculating the battery level
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            txtLevel.setText(String.valueOf(level) + "%");

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ;

            boolean isFull=  status == BatteryManager.BATTERY_STATUS_FULL;

            Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r=RingtoneManager.getRingtone(getApplicationContext(),path);

            if (isCharging==true)
            {
                txtStatus.setText("Yes it is Charge;");

                r.play();
            }
            else if (isFull == true)
            {
                txtStatus.setText("Please remove Charger, This is charged!");
            }
            else
            {
             txtStatus.setText("Charge not connect.!.");
             r.stop();
            }


        }
    };
    public Double getBatteryCapacity() {

        Object mPowerProfile_ = null;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);

        } catch (Exception e) {

            // Class not found?
            e.printStackTrace();
        }

        try {

            // Invoke PowerProfile method "getAveragePower" with param
            // "battery.capacity"
            batteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

        } catch (Exception e) {

            // Something went wrong
            e.printStackTrace();
        }
        txtCapacity.setText(String.valueOf(batteryCapacity)+" "+"MAH");
        return batteryCapacity;

    }

}
