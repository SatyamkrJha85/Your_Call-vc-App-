package com.example.yourcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
EditText roomcode;
TextView insta;
Button joincreate ,share;
ImageView logoutbtn;
LottieAnimationView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roomcode=findViewById(R.id.roomcode);
        joincreate=findViewById(R.id.joincreate);
        share=findViewById(R.id.share);
        logoutbtn=findViewById(R.id.logoutbtn);
        insta=findViewById(R.id.insta);
        imageview=findViewById(R.id.imageview);
        URL serverURL;
        try {
            serverURL=new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL).build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        joincreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder().setRoom(roomcode.getText().toString())
                        .build();

                JitsiMeetActivity.launch(MainActivity.this,options);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=roomcode.getText().toString();
                Intent intent= new Intent();
                intent.setAction(intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"join meeting in Your Call \n Meeting ID : "+code);
                startActivity(Intent.createChooser(intent,"share via"));
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://instagram.com/satyam_kr_jha_?igshid=OTk0YzhjMDVlZA==";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SharedPreferences myPrefs = getSharedPreferences("Activity",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                //AppState.getSingleInstance().setLoggingOut(true);
                //setLoginState(true);
                Intent intent = new Intent(MainActivity.this,
                        Login.class);
                Toast.makeText(MainActivity.this, "Log_Out Successfull", Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });




    }
}