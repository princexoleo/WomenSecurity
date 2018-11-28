package asia.airobotics.womensecurity.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dmgdesignuk.locationutils.easyaddressutility.EasyAddressUtility;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.common.util.CrashUtils;
import com.subsub.library.BeautyButton;

import asia.airobotics.womensecurity.BluetoothConnection;
import asia.airobotics.womensecurity.R;
import asia.airobotics.womensecurity.TrackLocation;
import asia.airobotics.womensecurity.model.UserPersonalDetails;
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private static final int MY_PERMISSION_REQUEST_SEND_SMS = 11;
    private static final  int REQUEST_CODE_PERMISSION=1;
    private static final String[] PERMISSION={Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_SETTING_REQUEST_CODE = 11;
    private boolean permissionResult;

    TextView currentLocationTv;

    //imageView
    CircleImageView userProfileImageView;

    //
    String SENT="SMS_SENT";
    String DELIVERED="SMS_DELIVERED";
    private String sendingMapsUrl;
    private String sendingMessage;
    String   custom_message="Hello !! i'm in danger please help me quickly !!\nthis my location:  ";
    //
    PendingIntent sendPendingIntent,deliveredPendingIntent;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;

    EasyWayLocation easyWayLocation;
    private Double lat=-23.0,lon=73.0;
    Context context = getActivity();
    boolean requireFineGranularity = false;
    boolean passiveMode = false;
    long updateIntervalInMilliseconds = 6*1000;
    boolean requireNewLocation = false;

    EasyAddressUtility addressUtility;
    String roadAdd,cityAdd;
    private Location currentLocation=null;
    SharedPreferences messagePref;




    BeautyButton connectButton,sendButton,startTrackbutton,stopTrackButton;

    //UserInfo
    UserPersonalDetails userinfo;

    SharedPreferences userPreferences;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        
        init(rootView);
        Log.d(TAG, "onCreateView: current City location: "+UserPersonalDetails.currentCityName);
        Log.d(TAG, "onCreateView: current Road location: "+UserPersonalDetails.currentRoadName);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goConnection=new Intent(getContext(),BluetoothConnection.class);
                startActivity(goConnection);
            }
        });

        userPreferences=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
//        String id=loginPreferences.getString("userId","");

       String City=userPreferences.getString("userCity","");
       String Road=userPreferences.getString("userRoad","");

        currentLocationTv.setText("Road: "+Road+"\n City: "+City);






        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //permissionCheck

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSION_REQUEST_SEND_SMS);
                }
                else{

                    String sendingPhoneNumber=userinfo.getEmergency_phoneNumber();

                    //permission granted
                    SmsManager sms= SmsManager.getDefault();
                    sms.sendTextMessage(sendingPhoneNumber,null,custom_message,sendPendingIntent,deliveredPendingIntent);

                }
            }
        });


        startTrackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Intent gotrackIntent=new Intent(getContext(),TrackLocation.class);
                startActivity(gotrackIntent);

            }
        });

        
        return rootView;
    }

    private void init(View rootView) {

        connectButton=rootView.findViewById(R.id.connect_btn);
        sendButton=rootView.findViewById(R.id.send_btn);
        startTrackbutton=rootView.findViewById(R.id.start_track_location_btn);
       // stopTrackButton=rootView.findViewById(R.id.stop_track_btn);

        String c=UserPersonalDetails.getCurrentCityName();
        String r=UserPersonalDetails.getCurrentRoadName();

        currentLocationTv=rootView.findViewById(R.id.current_location_tv);


        userProfileImageView=rootView.findViewById(R.id.user_profile_imageView);

        sendPendingIntent=PendingIntent.getBroadcast(getContext(),0,new Intent(SENT),0);
        deliveredPendingIntent=PendingIntent.getBroadcast(getContext(),0,new Intent(DELIVERED),0);

        messagePref=getContext().getSharedPreferences("userinfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = messagePref.edit();
        editor.putString("userMessage", custom_message);
        editor.putString("userPhone", "01911687821");
        editor.apply();





        
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }


    @Override
    public void onResume() {
        super.onResume();

        smsSentReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //sent done and result check
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "NO Services!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "No network!", Toast.LENGTH_SHORT).show();
                        break;


                }

            }
        };


        smsDeliveredReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //devlivered done
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered!", Toast.LENGTH_SHORT).show();
                        break;

                    case  Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };



      // context.registerReceiver(smsSentReceiver,new IntentFilter(SENT));
      //  context.registerReceiver(smsDeliveredReceiver,new IntentFilter(DELIVERED));
    }


}
