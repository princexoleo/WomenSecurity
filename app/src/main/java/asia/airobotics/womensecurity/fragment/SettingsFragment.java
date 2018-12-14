package asia.airobotics.womensecurity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import asia.airobotics.womensecurity.Popup;
import asia.airobotics.womensecurity.R;

import static android.content.Context.MODE_PRIVATE;



public class SettingsFragment extends Fragment {

    Button sendingPhoneEt;
    String phone;
    private SharedPreferences userDetails;
    private Button setMessage;
    private  AlertDialog.Builder builder;
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

        sendingPhoneEt.setOnClickListener(new View.OnClickListener() {
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

        sendingPhoneEt=rootVie.findViewById(R.id.sending_phone_et);
        setMessage= rootVie.findViewById(R.id.setmsg_button_id);

        userDetails=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);

        builder = new AlertDialog.Builder(getContext());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflaterD.inflate(R.layout.layout_dialog, null));
        nMsg=rootVie.findViewById(R.id.msg_et);

    }

}
