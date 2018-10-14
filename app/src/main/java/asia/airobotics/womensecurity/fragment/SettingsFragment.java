package asia.airobotics.womensecurity.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import asia.airobotics.womensecurity.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    EditText sendingPhoneEt;
    String phone;
    private SharedPreferences userDetails;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootVie=inflater.inflate(R.layout.fragment_settings, container, false);

        init(rootVie);

        sendingPhoneEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phone=sendingPhoneEt.getText().toString();

                if(!TextUtils.isEmpty(phone))
                {
                    SharedPreferences.Editor editor = userDetails.edit();
                    editor.putString("userPhone", phone);
                    editor.apply();

                }
            }
        });



        return rootVie;
    }

    private void init(View rootVie) {

        sendingPhoneEt=rootVie.findViewById(R.id.sending_phone_et);

        userDetails=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);

    }

}
