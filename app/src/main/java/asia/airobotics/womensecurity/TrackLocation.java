package asia.airobotics.womensecurity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.view.*;
import android.widget.Toast;

import com.dmgdesignuk.locationutils.easyaddressutility.EasyAddressUtility;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.subsub.library.BeautyButton;

import asia.airobotics.womensecurity.model.UserPersonalDetails;
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions;

public class TrackLocation extends AppCompatActivity implements Listener {

    private static final String TAG = "TrackLocation";

    private static final  int REQUEST_CODE_PERMISSION=1;
    private static final String[] PERMISSION={Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_SETTING_REQUEST_CODE = 11;
    private boolean permissionResult;

    TextView latitudetv,longitudetv,roadtv,citytv;
    BeautyButton stopTrackingbtn,startBtn;


    EasyWayLocation easyWayLocation;
    private Double lat=-23.0,lon=73.0;
    Context context = this;
    boolean requireFineGranularity = false;
    boolean passiveMode = false;
    long updateIntervalInMilliseconds = 6*1000;
    boolean requireNewLocation = false;

    EasyAddressUtility addressUtility;
    String roadAdd,cityAdd;
    private Location currentLocation=null;
    private String sendingMapsUrl;

    SharedPreferences locationsaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_location);

        initialize();


            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    trackMyLocation();
                    startBtn.setVisibility(View.INVISIBLE);
                    stopTrackingbtn.setVisibility(View.VISIBLE);

                }
            });

        stopTrackingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTrackMyLocation();
                startBtn.setVisibility(View.VISIBLE);
                stopTrackingbtn.setVisibility(View.INVISIBLE);

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        permissionGantResult();
        getAddress();


    }

    private void stopTrackMyLocation() {

        easyWayLocation.endUpdates();
    }

    private void trackMyLocation() {
        //stratTracking my Location
        //easy way location track
        easyWayLocation = new EasyWayLocation(context,requireFineGranularity,passiveMode,updateIntervalInMilliseconds,
                requireNewLocation);
        easyWayLocation.setListener(this);


        getAddress();


    }

    private void getAddress() {

        roadAdd=addressUtility.getAddressElement(EasyAddressUtility.AddressCodes.STREET_NAME,currentLocation);
        cityAdd=addressUtility.getAddressElement(EasyAddressUtility.AddressCodes.CITY_NAME,currentLocation);

        updateAddressUi(roadAdd,cityAdd);
    }

    private void updateAddressUi(String roadAdd, String cityAdd) {

        if(!TextUtils.isEmpty(roadAdd) && !TextUtils.isEmpty(cityAdd))
        {
            roadtv.setText("Road No:"+roadAdd);
            citytv.setText("City: "+cityAdd);

            SharedPreferences.Editor editor = locationsaved.edit();
            editor.putString("userCity", cityAdd);
            editor.putString("userRoad", roadAdd);
            editor.putString("userMap", sendingMapsUrl);
            editor.apply();

        }
        else {
            roadtv.setText("Road No: ");
            citytv.setText("City: ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        unregisterReceiver(smsDeliveredReceiver);
//        unregisterReceiver(smsSentReceiver);
//    }


    private void initialize() {



        stopTrackingbtn =findViewById(R.id.stop_track_btn);
        startBtn =findViewById(R.id.start_track_btn);

        latitudetv=findViewById(R.id.latitude_tv);
        longitudetv=findViewById(R.id.longi_tv);
        citytv=findViewById(R.id.city_tv);
        roadtv=findViewById(R.id.road_tv);

        addressUtility= new EasyAddressUtility(this);




        currentLocation=new Location("null location");
        locationsaved=getSharedPreferences("userinfo",MODE_PRIVATE);

    }

    @Override
    public void locationOn() {
        Log.d(TAG, "locationOn: ");
        Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
        easyWayLocation.beginUpdates();
        lat=easyWayLocation.getLatitude();
        lon=easyWayLocation.getLongitude();


        updateLatLonUi(lat,lon);
    }

    private void updateLatLonUi(Double lat, Double lon) {

        if((lat!=null)&&(lon!=null))
        {
            Log.i(TAG, "updateLatLonUi: not null ");
            String lati=String.valueOf(lat);
            String longi=String.valueOf(lon);

            currentLocation.setLatitude(lat);
            currentLocation.setLongitude(lon);

            latitudetv.setText(lati);
            longitudetv.setText(longi);

            sendingMapsUrl="https://www.google.com/maps/search/?api=1&query="+lati+","+longi;
            UserPersonalDetails.setCurrentLocation(currentLocation);
            UserPersonalDetails.setMapsUrl(sendingMapsUrl);


        }
        else {
            //lat lon null
        }
    }

    @Override
    public void onPositionChanged() {

        Toast.makeText(this, "location Changed", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPositionChanged: lati: "+easyWayLocation.getLatitude()+" Longi: "+easyWayLocation.getLongitude());
        lat=easyWayLocation.getLatitude();
        lon=easyWayLocation.getLongitude();

        updateLatLonUi(lat,lon);

    }

    @Override
    public void locationCancelled() {

        Log.d(TAG, "locationCancelled: ");
        easyWayLocation.showAlertDialog("Location Cancel","location is cancel",null);

    }

    private void permissionGantResult()
    {
        Log.d(TAG, "permissionGantResult: called ");
        if (EffortlessPermissions.hasPermissions(this,PERMISSION))
        {
            //permission Granted now go on...
            Log.i(TAG, "permissionGantResult: granted !!");
            permissionResult=true;
        }
        else if (EffortlessPermissions.somePermissionPermanentlyDenied(this,PERMISSION)) {
            //some permission Deined
            Log.i(TAG, "permissionGantResult: Deined !!!!");

        }
        else {

            //request the permission
            Log.i(TAG, "permissionGantResult: requestFor permission");
            EffortlessPermissions.requestPermissions(this,
                    "Request for Permission",
                    REQUEST_CODE_PERMISSION,
                    PERMISSION);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EffortlessPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;

        }
    }

    @Override
    protected void onStop() {
        easyWayLocation.endUpdates();
        super.onStop();

    }
}