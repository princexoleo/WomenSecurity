package asia.airobotics.womensecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Popup extends AppCompatActivity {
    private static final String TAG = "Popup";

    private Button btnClose;
    private Button btnSubmit;
    private TextView txtView;
    private EditText SetEditText;
    private SharedPreferences userDetails;

    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        btnClose = findViewById(R.id.btn_close);
        btnSubmit=findViewById(R.id.btn_submit);
        SetEditText = findViewById(R.id.new_et);
        txtView = findViewById(R.id.textView);
        userDetails=getApplicationContext().getSharedPreferences("userinfo",Context.MODE_PRIVATE);



        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int  height= dm.heightPixels;

        getWindow().setLayout((int)(width *.8),(int)(height *.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x= 0;
        params.y =-20;

        getWindow().setAttributes(params);


        Intent intent = getIntent();
        msg = intent.getStringExtra("EXTRA");
        Log.i(TAG, "onCreate: "+msg);

        if (msg.equals("phone"))
        {
            txtView.setText("Enter New Phone Number");
           SetEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        else{
            txtView.setText("Enter New Message to set");
           SetEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = userDetails.edit();

                if (msg.equals("phone"))
                {
                    editor.putString("userPhone", SetEditText.getText().toString());

                }
                else
                {
                    editor.putString("userMessage", SetEditText.getText().toString());
                }

                editor.apply();
                Toast.makeText(getApplicationContext(), "Updated Successfully done !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
