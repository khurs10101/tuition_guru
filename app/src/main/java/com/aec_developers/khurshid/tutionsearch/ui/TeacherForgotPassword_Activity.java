package com.aec_developers.khurshid.tutionsearch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khurshid on 2/15/2018.
 */

public class TeacherForgotPassword_Activity extends AppCompatActivity {

    private static final int INTERNET_REQUEST_CODE = 11;
    private EditText email;
    private Button retrieve;
    private String stEmail, encodedParams;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_forgot_password);

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

        email = findViewById(R.id.etTeacherEmailInForgotPassword);
        retrieve = findViewById(R.id.btTeacherForgotPassword);
        pb = findViewById(R.id.pbTeacherForgotPassword);
    }

    public void teacherRetrievePassword(View view) {
        stEmail = email.getText().toString().trim();
        //make a network call and send password via self email
        if (stEmail.length() == 0) {
            //field cant be empty
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("email", stEmail);
            encodedParams = HttpManager.getEncodedParams(params);

            //permisson settings
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                //permission granted proceed to work
                MyTask task = new MyTask();
                task.execute(encodedParams, SiteManager.TEACHER_FORGOT_PASSWORD);

            } else {

                //show rationale
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    AlertDialogClass alertDialogClass;
                    Bundle b;
                    //inform user that internet permission is required for login
                    alertDialogClass = new AlertDialogClass();
                    b = new Bundle();
                    b.putString("message", "Internet permission is required for retrieving password");
                    alertDialogClass.setArguments(b);
                    alertDialogClass.show(getFragmentManager(), "login_alert");
                }
                //ask for permisson
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
            }

        }
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            retrieve.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String encodedParams = strings[0];
            String site = strings[1];

            String response = HttpManager.readWrite(encodedParams, site);
            //null pointer checking
            if (response != null)
                response = response.trim();
            else
                response = "";
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.INVISIBLE);
            retrieve.setVisibility(View.VISIBLE);

            if (s.equals("a")) {
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Credentials is sent to your email address. Check your spam folder");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            } else if (s.equals("c")) {
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "No user exists. Please register yourself.");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            } else {
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Something went wrong. Try again or contact the app developer.");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            }


        }
    }

    public void teacherCreateAccountInForgotPassword(View view) {
        //open link to create account for teacher
        startActivity(new Intent(this, TeacherRegister_Activity.class));
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AlertDialogClass alertDialogClass;
        Bundle b;
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                MyTask task = new MyTask();
                task.execute(encodedParams, SiteManager.STUDENT_FORGOT_PASSWORD);


            } else {
                //show a dialog that permission not granted
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Internet Permission Not Granted");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
}
