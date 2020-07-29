package com.aec_developers.khurshid.tutionsearch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.TeacherDashBoard;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.JSONParser;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;
import com.aec_developers.khurshid.tutionsearch.model.Login_Helper_Model;
import com.aec_developers.khurshid.tutionsearch.model.Teacher;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Khurshid on 2/5/2018.
 */

public class TeacherLogin_Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int INTERNET_REQUEST_CODE = 12;
    private EditText username, password;
    private Button login;
    private String stUsername, stPassword;
    private Context context = this;
    private String encodedParams;
    private TextView teacherForgetPassword, teacherCreateAccount;
    private ProgressBar pb;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //for fcm
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login_ui);

        //code for fcm token
        SharedPreferences tokenSharedPref = getApplicationContext().getSharedPreferences("fcm_token", Context.MODE_PRIVATE);
        token = tokenSharedPref.getString("token", FirebaseInstanceId.getInstance().getToken());
//        Log.v("FCM", token);
        //ends


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

        username = findViewById(R.id.etTeacherLoginUsername);
        password = findViewById(R.id.etTeacherLoginPassword);
        teacherCreateAccount = findViewById(R.id.tvTeacherLoginCreateAccount);
        teacherForgetPassword = findViewById(R.id.tvTeacherLoginForgetPassword);
        pb = findViewById(R.id.pbTeacherLogin);
        login = findViewById(R.id.btTeacherLogin);

        login.setOnClickListener(this);


        //auto login if credentials exists in shared preference

        if (preferences.getString("tusername", "").length() > 0 && preferences.getString("tpassword", "").length() > 0) {
            stUsername = preferences.getString("tusername", "");
            stPassword = preferences.getString("tpassword", "");
            Map<String, String> params = new HashMap<>();
            params.put("username", stUsername);
            params.put("password", stPassword);
            params.put("Token", token);

            //get the encoded params
            encodedParams = HttpManager.getEncodedParams(params);

            //start a background task for a network call

            //here ask permission to access the internet
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                //permission granted proceed to work
                WriteToServerTask writeToServerTask = new WriteToServerTask();
                writeToServerTask.execute(encodedParams, SiteManager.TEACHER_LOGIN_URL);

            } else {

                //show rationale
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    //inform user that internet permission is required for login
                    AlertDialogClass alertDialogClass;
                    Bundle b;
                    alertDialogClass = new AlertDialogClass();
                    b = new Bundle();
                    b.putString("message", "Internet permission is required for user login");
                    alertDialogClass.setArguments(b);
                    alertDialogClass.show(getFragmentManager(), "login_alert");
                }
                //ask for permisson
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
            }


        }
        //ends
    }


    @Override
    public void onClick(View view) {

        stUsername = username.getText().toString().trim();
        stPassword = password.getText().toString().trim();
        int count = 0;
        //username cant be blank
        if (stUsername.length() == 0) {
            //length cant be empty
            username.setBackgroundResource(R.drawable.edittext_invalid);
            username.setHint("Username cant be empty");
            count++;

        } else {
            username.setBackgroundResource(R.drawable.edittext_valid);
        }

        //password cant be blank
        if (stPassword.length() == 0) {
            password.setBackgroundResource(R.drawable.edittext_invalid);
            password.setHint("Password cant be empty");
            count++;
        } else {
            password.setBackgroundResource(R.drawable.edittext_valid);
        }

        if (count == 0) {
            //everything is ok proceed to login

            //pack parameters in a hashmap
            Map<String, String> params = new HashMap<>();
            params.put("username", stUsername);
            params.put("password", stPassword);
            params.put("Token", token);


            //get the encoded params
            encodedParams = HttpManager.getEncodedParams(params);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                //start a background task for a network call
                WriteToServerTask writeToServerTask = new WriteToServerTask();
                writeToServerTask.execute(encodedParams, SiteManager.TEACHER_LOGIN_URL);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    //inform user that internet permission is required for login
                    AlertDialogClass alertDialogClass;
                    Bundle b;
                    alertDialogClass = new AlertDialogClass();
                    b = new Bundle();
                    b.putString("message", "Internet permission is required for user login");
                    alertDialogClass.setArguments(b);
                    alertDialogClass.show(getFragmentManager(), "login_alert");
                }
                //ask for permisson
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
            }


        } else {
            //some fields are left blank
            count = 0;
        }

    }

    private class WriteToServerTask extends AsyncTask<String, Void, Login_Helper_Model> {


        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            teacherCreateAccount.setClickable(false);
            teacherForgetPassword.setClickable(false);

        }

        @Override
        protected Login_Helper_Model doInBackground(String... strings) {


            String encodedParams = strings[0];
            String url = strings[1];
            Login_Helper_Model helper_model = new Login_Helper_Model();
            List<Teacher> mList;
            if (HttpManager.checkActiveInternetConnection(context)) {
                //if internet connection is available
                String response = HttpManager.readWrite(encodedParams, url);
//                Log.v("FCM",response);

                //coping up with the null pointer exception
                if (response != null)
                    response = response.trim();
                else
                    response = "";


                if (response.equals("c")) {
                    helper_model.setResponseString(response);
                } else if (response.equals("")) {
                    helper_model.setResponseString("");
                } else {
                    mList = JSONParser.jsonParseTeacherOld(response);
                    helper_model.setList(mList);
                    helper_model.setResponseString("m");
                }

            } else {
                //internet connection is not available
                helper_model.setResponseString("n");
            }

            return helper_model;
        }


        @Override
        protected void onPostExecute(Login_Helper_Model _login_helper_model) {

            pb.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            teacherCreateAccount.setClickable(true);
            teacherForgetPassword.setClickable(true);

            if (_login_helper_model.getResponseString().equals("c")) {
                //user doesnt exists
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "User doesn't exists, Please register");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
//                Toast.makeText(context, "c", Toast.LENGTH_LONG).show();

            } else if (_login_helper_model.getResponseString().equals("n")) {
                //no internet connection
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "No Internet connection. Please try again later");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");

            } else if (_login_helper_model.getResponseString().equals("m")) {
                //everything is ok.
                //put credentials to shared preference

                preferences = getSharedPreferences("TeacherCredentials", Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("tusername", stUsername);
                editor.putString("tpassword", stPassword);
                editor.commit();

                List<Teacher> mList = _login_helper_model.getList();


                if (mList != null) {
                    Teacher teacher = mList.get(0);
                    Intent intent = new Intent(context, TeacherDashBoard.class);
                    intent.putExtra("teacher", teacher);
                    startActivity(intent);
                    finish();
                } else {
                    //restart
                    Toast.makeText(context, "Server error try again", Toast.LENGTH_LONG).show();
                }


            } else {
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Server Error. Please try again or contact the developer");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            }

        }
    }

    public void teacherForgetPassword(View view) {
        startActivity(new Intent(this, TeacherForgotPassword_Activity.class));
    }

    public void teacherCreateAccount(View view) {
        startActivity(new Intent(this, TeacherRegister_Activity.class));

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                WriteToServerTask writeToServerTask = new WriteToServerTask();
                writeToServerTask.execute(encodedParams, SiteManager.TEACHER_LOGIN_URL);

            } else {
                //show a dialog that permission not granted
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Internet permission is not granted");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

}
