package com.aec_developers.khurshid.tutionsearch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.SplashPage;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khurshid on 2/15/2018.
 */

public class TeacherUpdateDetails_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final int INTERNET_REQUEST_CODE = 11;
    private EditText age, phone, city, subject, fee;
    private Spinner field, uptoClass, branch, state;
    private ProgressBar pb;
    private String stAge, stPhone, stCity, stSubject, stFee, stField, stBranch = "", stUptoClass, encodedParams, stUsername, stPassword, stState;
    private ArrayAdapter<String> fieldArrayAdapter, uptoClassArrayAdapter, branchArrayAdapter, stateArrayAdapter;
    private String fieldList[], uptoClassList[], branchList[], stateList[];
    private SharedPreferences preferences;
    private Button update;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_update_details);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        Window window = this.getWindow();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }

        age = findViewById(R.id.etAgeUpdate);
        phone = findViewById(R.id.etPhoneUpdate);
        city = findViewById(R.id.etCityUpdate);
        subject = findViewById(R.id.etSubjectUpdate);
        fee = findViewById(R.id.etFeeUpdate);
        field = findViewById(R.id.spUpdateField);
        uptoClass = findViewById(R.id.spClassUpdate);
        branch = findViewById(R.id.spUpdateBranch);
        branch.setVisibility(View.GONE);
        pb = findViewById(R.id.pbTeacherUpdate);
        state = findViewById(R.id.spUpdateState);

        update = findViewById(R.id.btUpdateTeacherDetails);

        //getting values from shared preference
        preferences = getSharedPreferences("TeacherCredentials", Context.MODE_PRIVATE);
        stUsername = preferences.getString("tusername", "");
        stPassword = preferences.getString("tpassword", "");
        //ends


        fieldList = getResources().getStringArray(R.array.teacher_field);
        uptoClassList = getResources().getStringArray(R.array.teacher_class);
        branchList = getResources().getStringArray(R.array.engineering_field);
        stateList = getResources().getStringArray(R.array.states);
        fieldArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fieldList);
        uptoClassArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, uptoClassList);
        branchArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, branchList);
        stateArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateList);
        field.setAdapter(fieldArrayAdapter);
        field.setOnItemSelectedListener(this);
        uptoClass.setAdapter(uptoClassArrayAdapter);
        uptoClass.setOnItemSelectedListener(this);
        branch.setOnItemSelectedListener(this);
        state.setAdapter(stateArrayAdapter);
        state.setOnItemSelectedListener(this);
        update.setOnClickListener(this);

    }


    public void teacherDetailsUpdate() {

        //all field must me made mandatory
        int count = 0;
        Map<String, String> params = new HashMap<>();
        //submit the details to the database
        //submit only those field which are not empty
        stAge = age.getText().toString().trim();
        stPhone = phone.getText().toString().trim();
        stCity = city.getText().toString().trim();
        stSubject = subject.getText().toString().trim();
        stFee = fee.getText().toString().trim();

        if (stAge.length() == 0) {
            age.setHint("Age cant be empty");
            count++;
            age.setBackgroundResource(R.drawable.edittext_invalid);
        } else {
            age.setBackgroundResource(R.drawable.edittext_valid);
        }

        if (stPhone.length() == 0) {
            phone.setHint("phone number cant be empty");
            count++;
            phone.setBackgroundResource(R.drawable.edittext_invalid);
        } else {
            phone.setBackgroundResource(R.drawable.edittext_valid);
        }

        if (stCity.length() == 0) {
            city.setHint("city name cant be empty");
            city.setBackgroundResource(R.drawable.edittext_invalid);
            count++;
        } else {
            city.setBackgroundResource(R.drawable.edittext_valid);
        }

        if (stSubject.length() == 0) {
            subject.setHint("subject cant be empty");
            count++;
            subject.setBackgroundResource(R.drawable.edittext_invalid);
        } else {
            subject.setBackgroundResource(R.drawable.edittext_valid);
        }

        if (stFee.length() == 0) {
            fee.setHint("fee cant be empty ");
            fee.setBackgroundResource(R.drawable.edittext_invalid);
            count++;
        } else {
            fee.setBackgroundResource(R.drawable.edittext_valid);
        }

        if (stUsername.length() == 0 || stPassword.length() == 0) {
            //fatal exception
            //take to the login page
            Toast.makeText(this, "Fatal Error. Please Login Again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SplashPage.class));
            finish();
        }

        if (stField == null) {
            count++;
            AlertDialogClass alertDialogClass;
            Bundle b;
            alertDialogClass = new AlertDialogClass();
            b = new Bundle();
            b.putString("message", "Field cant be empty");
            alertDialogClass.setArguments(b);
            alertDialogClass.show(getFragmentManager(), "login_alert");
        }

        if (stUptoClass == null) {
            count++;
            AlertDialogClass alertDialogClass;
            Bundle b;
            alertDialogClass = new AlertDialogClass();
            b = new Bundle();
            b.putString("message", "Upto Class cant be empty");
            alertDialogClass.setArguments(b);
            alertDialogClass.show(getFragmentManager(), "login_alert");
        }

        if (count == 0) {

//            Log.v("PARAM", "Values :" + stAge + " " + stPhone + " " + stCity + " " + stSubject + " " + stBranch + " " + stField + " " + stUptoClass + " " + stUsername + " " + stPassword + " " + stState);

            params.put("age", stAge);
            params.put("phone", stPhone);
            params.put("city", stCity);
            params.put("subject", stSubject);
            params.put("branch", stBranch);
            params.put("fee", stFee);
            params.put("field", stField);
            params.put("uptoClass", stUptoClass);
            params.put("username", stUsername);
            params.put("password", stPassword);
            params.put("state", stState);
            encodedParams = HttpManager.getEncodedParams(params);

            //runtime internet permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                //permission granted proceed to work
                MyTask task = new MyTask();
                task.execute(encodedParams, SiteManager.TEACHER_UPDATE_DETAILS);

            } else {

                //show rationale
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    AlertDialogClass alertDialogClass;
                    Bundle b;
                    //inform user that internet permission is required for login
                    alertDialogClass = new AlertDialogClass();
                    b = new Bundle();
                    b.putString("message", "Internet permission is required for the details to update");
                    alertDialogClass.setArguments(b);
                    alertDialogClass.show(getFragmentManager(), "login_alert");
                }
                //ask for permisson
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
            }

        } else {
            count = 0;
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spUpdateField) {
            stField = fieldArrayAdapter.getItem(i);
            if (i == 2) {
                //engineering
                stField = fieldArrayAdapter.getItem(i);
                branch.setVisibility(View.VISIBLE);
                branch.setAdapter(branchArrayAdapter);
            } else {
                branch.setVisibility(View.GONE);
                stBranch = "";
            }

        }

        if (adapterView.getId() == R.id.spClassUpdate) {
            stUptoClass = uptoClassArrayAdapter.getItem(i);
        }

        if (adapterView.getId() == R.id.spUpdateBranch) {
            stBranch = branchArrayAdapter.getItem(i);
        }

        if (adapterView.getId() == R.id.spUpdateState) {
            stState = stateArrayAdapter.getItem(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        if (adapterView.getId() == R.id.spUpdateField) {
//            stField = null;
//        }
//
//        if (adapterView.getId() == R.id.spClassUpdate) {
//            stUptoClass = null;
//        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btUpdateTeacherDetails) {
            teacherDetailsUpdate();
        }
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String encodedParams = strings[0];
            String site = strings[1];
            String response = HttpManager.readWrite(encodedParams, site);
            if (response != null)
                response = response.trim();
            else
                response = "";
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            pb.setVisibility(View.GONE);
            if (s.equals("a")) {
                //updated successfully
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Details Updated Sucessfully.");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
                startActivity(new Intent(TeacherUpdateDetails_Activity.this, TeacherLogin_Activity.class));
                finish();
            } else if (s.equals("c")) {
                //unable to update try later
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Something went wrong. Try again later or contact the app developer.");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            } else {
                //fatal error
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Server Error! Try again or contact the app developer.");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AlertDialogClass alertDialogClass;
        Bundle b;
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                MyTask task = new MyTask();
                task.execute(encodedParams, SiteManager.TEACHER_UPDATE_DETAILS);

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
