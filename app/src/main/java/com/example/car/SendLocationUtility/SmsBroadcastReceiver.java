package com.example.car.SendLocationUtility;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.car.ConstantsUtility.Constants;

import static android.content.Context.MODE_PRIVATE;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private String PrivateCode ;
    private String[] request;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from , msgBody;
            if(bundle != null)
            {
                try
                {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i=0 ; i< msgs.length ; i++)
                    {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getDisplayOriginatingAddress().toString();
                        msgBody = msgs[i].getMessageBody().toString();
                        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILE_NAME, MODE_PRIVATE);
                        request = msgBody.split(":");
                        PrivateCode = sharedPreferences.getString(Constants.PRIVATE_CODE_KEY, "");
                        if(PrivateCode.equals(request[0]))    // checking if the received sms containing secret code
                        //if(PrivateCode.equals(msgBody))
                        {                                          // if yes send to this phone number current car location
                            if("SMS".equals(request[1])){
                                Toast.makeText(context, msg_from+" : "+ msgBody, Toast.LENGTH_LONG).show();
                                Intent sendLocationService = new Intent(context , SendLocationService.class); // by starting send location service
                                sendLocationService.putExtra("phone" , msg_from);
                                context.startService(sendLocationService);
                            } else if("Enable4G".equals(request[1])){
                                startLocationService(context);
                            }
                            else if("Disable4G".equals(request[1])){
                                stopLocationService(context);
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }



    private boolean isLocationServiceRunning(Context context){
        ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null)
        {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName()))
                {
                    if(service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(Context context){
        if(!isLocationServiceRunning(context)){
            Intent intent = new Intent(context , LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            context.startService(intent);
            Toast.makeText(context, "Location Service Started", Toast.LENGTH_LONG).show();
        }
    }

    private void stopLocationService(Context context){
        if(isLocationServiceRunning(context)){
            Intent intent = new Intent(context , LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            context.startService(intent);
            Toast.makeText(context, "Location Service Stopped", Toast.LENGTH_LONG).show();
        }
    }
}

