package asia.airobotics.womensecurity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import asia.airobotics.womensecurity.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Button sendingPhoneEt;
    String phone;
    private SharedPreferences userDetails;
    private Button setMessage;
    private  AlertDialog.Builder builder;
    private EditText nMsg;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootVie=inflater.inflate(R.layout.fragment_settings, container, false);

        init(rootVie);

        sendingPhoneEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phone=sendingPhoneEt.getText().toString();
                openSetPhoneDialog(rootVie,phone);

            }
        });
        setMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                openDialog(rootVie);
            }
        });

        return rootVie;
    }

    private void openSetPhoneDialog(View rootVie, final String phone) {
        nMsg.setHint("Enter New Phone Number");


        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(phone))
                {
                    SharedPreferences.Editor editor = userDetails.edit();
                    editor.putString("userPhone", phone);
                    editor.apply();
                    Toast.makeText(getContext(), "Emergency Phone Number Updated", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

    }

    private void openDialog(final View rootview) {

        nMsg.setHint("Enter New Message");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                nMsg=rootview.findViewById(R.id.msg_et);

                SharedPreferences.Editor editor = userDetails.edit();
                editor.putString("userMessage", nMsg.getText().toString());
                editor.apply();
                Toast.makeText(getContext(), "Emergency Message Updated", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });


    }

    private void init(View rootVie) {

        sendingPhoneEt=rootVie.findViewById(R.id.sending_phone_et);
        setMessage= rootVie.findViewById(R.id.setmsg_button_id);

        userDetails=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);

        builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.layout_dialog, null));

    }

}
