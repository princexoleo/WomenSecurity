package asia.airobotics.womensecurity.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import asia.airobotics.womensecurity.Popup;
import asia.airobotics.womensecurity.R;


public class SettingsFragment extends Fragment {

    Button setNewPhonNumberbtn;
    String phone;
    private SharedPreferences userDetails;
    private Button setMessage;
    private EditText nMsg;
    LayoutInflater inflaterD ;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         inflaterD = inflater;
         final View rootVie=inflater.inflate(R.layout.fragment_settings, container, false);

        init(rootVie);

        phone=userDetails.getString("userPhone","");
        setNewPhonNumberbtn.setText(phone);

        setNewPhonNumberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openSetPhoneDialog(rootVie,phone);
                String message ="phone";
                Intent popIntent = new Intent(getContext(),Popup.class);
                popIntent.putExtra("EXTRA",message);
                startActivity(popIntent);

            }
        });
        setMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message ="message";
                Intent popIntent = new Intent(getContext(),Popup.class);
                popIntent.putExtra("EXTRA",message);
                startActivity(popIntent);


            }
        });

        return rootVie;
    }

    private void init(View rootVie) {

        setNewPhonNumberbtn =rootVie.findViewById(R.id.set_new_phone_btn);
        setMessage= rootVie.findViewById(R.id.setmsg_button_id);

        userDetails=getContext().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        nMsg=rootVie.findViewById(R.id.msg_et);

    }

}
