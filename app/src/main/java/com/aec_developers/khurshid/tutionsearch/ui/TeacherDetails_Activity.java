package com.aec_developers.khurshid.tutionsearch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.model.Teacher;

/**
 * Created by Khurshid on 2/12/2018.
 */

public class TeacherDetails_Activity extends AppCompatActivity {

    private static final int CALL_REQUEST_CODE = 101;
    private ImageView teacherImage;
    private Teacher teacher;
    private TextView name, qualification, gender, fee, age, field, subject;
    private Button callButton;
    private Intent callIntent;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_details);
//

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

//        }

        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        //declaration
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        name = findViewById(R.id.tvTeacherNameInDetails);
        subject = findViewById(R.id.tvTeacherSubjectInDetails);
        qualification = findViewById(R.id.tvTeacherQualificationInDetails);
        age = findViewById(R.id.tvTeacherAgeInDetails);
        gender = findViewById(R.id.tvTeacherGenderInDetails);
        fee = findViewById(R.id.tvTeacherFeeInDetails);
        callButton = findViewById(R.id.btTeacherCallInDetails);
        field = findViewById(R.id.tvTeacherFieldInDetails);
        //ends

        //mapping
        name.setText(teacher.getName());
        qualification.setText(teacher.getQualification());
        fee.setText(teacher.getFee());
        age.setText("Age: " + teacher.getAge());
        gender.setText("Gender: " + teacher.getGender());
        phoneNumber = teacher.getPhone().trim();
        field.setText(teacher.getField());
        subject.setText("Can teach " + teacher.getSpecific_subject());
        //ends
//

        // add PhoneStateListener
//        final PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
//        Toast.makeText(this, teacher.getName(), Toast.LENGTH_LONG).show();

        //click button to call
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //ask for permission (Runtime)
                if (ContextCompat.checkSelfPermission(TeacherDetails_Activity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(callIntent);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TeacherDetails_Activity.this, Manifest.permission.INTERNET)) {
                        //inform user that internet permission is required for login
                    }
                    //ask for permisson
                    ActivityCompat.requestPermissions(TeacherDetails_Activity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
                }

            }
        });

    }


//    private class PhoneCallListener extends PhoneStateListener {
//
//        private boolean isPhoneCalling = false;
//
//        String LOG_TAG = "LOGGING 123";
//
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            if (TelephonyManager.CALL_STATE_RINGING == state) {
//                // phone ringing
//                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
//            }
//
//            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
//                // active
//                Log.i(LOG_TAG, "OFFHOOK");
//
//                isPhoneCalling = true;
//            }
//
//            if (TelephonyManager.CALL_STATE_IDLE == state) {
//                // run when class initial and phone call ended,
//                // need detect flag from CALL_STATE_OFFHOOK
//                Log.i(LOG_TAG, "IDLE");
//
//                if (isPhoneCalling) {
//
//                    Log.i(LOG_TAG, "restart app");
//
//                    // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//
//                    isPhoneCalling = false;
//                }
//
//            }
//        }
//    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);

            } else {
                //show a dialog that permission not granted

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
}
