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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.FieldValidation;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khurshid on 1/28/2018.
 */

public class TeacherRegister_Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int INTERNET_REQUEST_CODE = 12;
    private Button register;
    private EditText name, email, phone, age, city, username, password, rePassword, subject, fee;
    private Spinner sQualification, sField, sSubject, sClass, sState, sGender;
    private String stName, stEmail, stPhone, stAge, stCity, stState, stUsername, stPassword, stRePassword,
            stSpecificSubject, stQualification, stField, stSubject, stClass, stFee, stGender;

    private String teacherSubjectArray[], teacherClassArray[], teacherGenderArray[];
    private Context context = this;
    private String encodedParams;
    private ProgressBar pb;

    //for fcm
    private String token;

    private ArrayAdapter<String> teacherQualificationArrayAdapter, teacherFieldArrayAdapter, teacherSubjectArrayAdapter = null, teacherClassArrayAdapter = null;
    private ArrayAdapter<String> stateArrayAdapter, genderArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_ui);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        Window window = this.getWindow();

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }


        //for fcm purpose
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("fcm_token", Context.MODE_PRIVATE);
        token = preferences.getString("token", FirebaseInstanceId.getInstance().getToken());
        //ends

        //EditText
        name = findViewById(R.id.etTeacherName);
        email = findViewById(R.id.etTeacherEmail);
        phone = findViewById(R.id.etTeacherPhone);
        age = findViewById(R.id.etTeacherAge);
        city = findViewById(R.id.etTeacherCity);
        username = findViewById(R.id.etTeacherUsername);
        password = findViewById(R.id.etTeacherPassword);
        rePassword = findViewById(R.id.etTeacherRePassword);
        subject = findViewById(R.id.etTeacherEnterSpecificSubject);
        fee = findViewById(R.id.etTeacherFee);
        //Spinners
        sQualification = findViewById(R.id.spTeacherQualification);
        sField = findViewById(R.id.spTeacherField);
        sSubject = findViewById(R.id.spTeacherSubject);
        sClass = findViewById(R.id.spTeacherClass);
        sState = findViewById(R.id.spTeacherState);
        sGender = findViewById(R.id.spTeacherGender);
        //Button
        register = findViewById(R.id.btTeacherRegister);
        pb = findViewById(R.id.pbTeacherRegister);

        //populationg the spinner
        String states[] = getResources().getStringArray(R.array.states);
        teacherGenderArray = getResources().getStringArray(R.array.gender);
        stateArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, states);
        genderArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teacherGenderArray);
        sGender.setAdapter(genderArrayAdapter);
        sState.setAdapter(stateArrayAdapter);
        sState.setOnItemSelectedListener(this);
        sGender.setOnItemSelectedListener(this);

        //set spinner content
        setSpinnerContent();
        register.setOnClickListener(this);

    }

    private void setSpinnerContent() {

        //for teacher qualification;
        String teacherQualification[] = getResources().getStringArray(R.array.teacher_qualification);
        teacherQualificationArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teacherQualification);
        sQualification.setAdapter(teacherQualificationArrayAdapter);
        sQualification.setOnItemSelectedListener(this);

        //for teacher subject field
        String teacherField[] = getResources().getStringArray(R.array.teacher_field);
        teacherFieldArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teacherField);
        sField.setAdapter(teacherFieldArrayAdapter);
        sField.setOnItemSelectedListener(this);


    }

    //handling button click
    @Override
    public void onClick(View view) {


        //validation task
        if (view.getId() == R.id.btTeacherRegister) {

            //checking if cellular network is available


            int count = 0;
            //get data from edit text field
            stName = name.getText().toString().trim();
            stEmail = email.getText().toString().trim();
            stPhone = phone.getText().toString().trim();
            stAge = age.getText().toString().trim();
            stCity = city.getText().toString().trim();
            stUsername = username.getText().toString().trim();
            stPassword = password.getText().toString().trim();
            stRePassword = rePassword.getText().toString().trim();
            stSpecificSubject = subject.getText().toString().trim();
            stFee = fee.getText().toString().trim();


            //validation for edittext fields
            //name cant be empty
            //name field cant be empty
            if (stName.length() == 0) {
                name.setBackgroundResource(R.drawable.edittext_invalid);
                Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                count++;

            } else {
                name.setBackgroundResource(R.drawable.edittext_valid);
            }

            //email must be valid
            if (!FieldValidation.validate(stEmail) && stEmail.length() == 0) {
                //email format is incorrect
                Toast.makeText(this, "Invalid Email id", Toast.LENGTH_SHORT).show();
                email.setBackgroundResource(R.drawable.edittext_invalid);
                count++;

            } else
                email.setBackgroundResource(R.drawable.edittext_valid);


            //phone number must be a valid number
            if (!FieldValidation.phoneValidate(stPhone) && stPhone.length() < 10) {
                //phone format is incorrect
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                phone.setBackgroundResource(R.drawable.edittext_invalid);
                count++;
            } else
                phone.setBackgroundResource(R.drawable.edittext_valid);


            //username cant be empty
            if (stUsername.length() == 0) {
                username.setBackgroundResource(R.drawable.edittext_invalid);
                Toast.makeText(this, "Username cant be empty", Toast.LENGTH_SHORT).show();
                count++;
            } else
                username.setBackgroundResource(R.drawable.edittext_look);


            //password must be atleast 6 character long
            if (stPassword.length() == 0 || stPassword.length() < 6 || stPassword.length() > 99) {
                password.setBackgroundResource(R.drawable.edittext_invalid);
                password.setHint("password must be atleast 6 characters");
                Toast.makeText(this, "Password must be atleast 6 characters long", Toast.LENGTH_SHORT).show();
                count++;
            } else
                password.setBackgroundResource(R.drawable.edittext_valid);

            //password and retype password must match
            if (!stRePassword.equals(stPassword) || stRePassword.length() == 0) {
                rePassword.setBackgroundResource(R.drawable.edittext_invalid);
                rePassword.setText("");
                rePassword.setHint("Password dont match");
                Toast.makeText(this, "Password don't match", Toast.LENGTH_SHORT).show();
                count++;
            } else
                rePassword.setBackgroundResource(R.drawable.edittext_valid);

            //city name cant be empty
            if (stCity.length() == 0) {
                city.setBackgroundResource(R.drawable.edittext_invalid);
                Toast.makeText(this, "Enter city", Toast.LENGTH_SHORT).show();
                count++;
            } else {
                city.setBackgroundResource(R.drawable.edittext_valid);
            }


            //age cant be empty
            if (stAge.length() == 0) {
                age.setBackgroundResource(R.drawable.edittext_invalid);
                Toast.makeText(this, "Enter Age", Toast.LENGTH_SHORT).show();
                count++;
            } else {
                age.setBackgroundResource(R.drawable.edittext_valid);
            }


            if (stFee.length() == 0) {
                fee.setBackgroundResource(R.drawable.edittext_invalid);
                Toast.makeText(this, "Enter fee", Toast.LENGTH_SHORT).show();
                count++;
            } else {
                fee.setBackgroundResource(R.drawable.edittext_valid);
            }

            if (count == 0) {
                //everything is fine proceed to regitration

                //packing value in hashmap
                Map<String, String> params = new HashMap<>();
                params.put("name", stName);
                params.put("email", stEmail);
                params.put("phone", stPhone);
                params.put("age", stAge);
                params.put("specificSubject", stSpecificSubject);
                params.put("city", stCity);
                params.put("state", stState);
                params.put("qualification", stQualification);
                params.put("field", stField);
                params.put("subject", stSubject);
                params.put("class", stClass);
                params.put("username", stUsername);
                params.put("password", stPassword);
                params.put("fee", stFee);
                params.put("gender", stGender);
                params.put("Token", token);

                //encoding params
                encodedParams = HttpManager.getEncodedParams(params);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                    WriteToServerTask writeToServerTask = new WriteToServerTask();
                    writeToServerTask.execute(encodedParams, SiteManager.TEACHER_REGISTER_URL, SiteManager.TEACHER_USERNAME_VALIDATION_URL, stUsername);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                        //inform user that internet permission is required for login
                        AlertDialogClass alertDialogClass;
                        Bundle b;
                        alertDialogClass = new AlertDialogClass();
                        b = new Bundle();
                        b.putString("message", "Internet permission is required for user registration");
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

    }


    //inner class for asynctask
    private class WriteToServerTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //began the progress bar
            pb.setVisibility(View.VISIBLE);
            register.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String encodedParams = strings[0];
            String url = strings[1];
            String username_valid_url = strings[2];
            String username = strings[3];

            //write to server only when active internet connection is present
            if (HttpManager.checkActiveInternetConnection(context)) {

                //check if the username exists in database or not
                //will return true if username doesnt exists
                if (FieldValidation.usernameValidate(username, username_valid_url)) {
                    String response = HttpManager.readWrite(encodedParams, url);
                    //null pointer checking
                    if (response != null)
                        response = response.trim();
                    else
                        response = "";
                    return response;
                } else
                    return "m";

            } else {
                //internet connection is not available
                return "n";
            }

        }


        @Override
        protected void onPostExecute(String s) {

            //end the progress bar
            pb.setVisibility(View.GONE);
            register.setVisibility(View.VISIBLE);

            Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
            //String s = k.trim();
            if (s.equals("m")) {
                username.setBackgroundResource(R.drawable.edittext_invalid);
                username.setText("");
                username.setHint("username already taken");
            } else if (s.equals("a")) {
                //email or phone already registered
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Email or phone number already registered. Please login");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            } else if (s.equals("b")) {
                //query successfull
                SharedPreferences preferences = getSharedPreferences("TeacherCredentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tusername", stUsername);
                editor.putString("tpassword", stPassword);
                editor.commit();
                //open dashboard
                startActivity(new Intent(getBaseContext(), TeacherLogin_Activity.class));
            } else if (s.equals("c")) {
                //query unsucessfull
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Fatal Error. Unsuccessfull query. Contact the app developer");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            } else if (s.equals("n")) {
                //show alert dialog that internet is not availabe
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "No Internet connection. Please try again later");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            } else {
                //something went wrong
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Server Error. Try again or Contact the app developer");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");
            }

        }
    }


    //handling spinner content
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if (adapterView.getId() == R.id.spTeacherState) {
            stState = stateArrayAdapter.getItem(i);
        }

        if (adapterView.getId() == R.id.spTeacherGender) {
            stGender = genderArrayAdapter.getItem(i);
        }

        if (adapterView.getId() == R.id.spTeacherQualification) {
            stQualification = teacherQualificationArrayAdapter.getItem(i);
        }

        //
        if (adapterView.getId() == R.id.spTeacherField) {

            //
            switch (i) {

                //for all subjects
                //hide or disable for subject spinner
                //hide or disable specific subject edittext
                //set class to upto class 10
                case 0:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g Science, Math, Social Studies, English etc.");
                    teacherClassArray = new String[]{"Upto class 10"};

                    //updating teacher class array
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);

                    break;

                //multiple subjects
                case 1:
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g Maths, Physics, Chemistry etc.");
                    teacherClassArray = new String[]{"class 11 and 12"};

                    //updating teacher class array
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);

                    break;

                //for engineering
                //set Class start from degree
                case 2:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.VISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g compiler design (CSE), electrical machines (EE) etc");
                    teacherSubjectArray = getResources().getStringArray(R.array.engineering_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_degree);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(teacherSubjectArray);
                    updateTeacherClassSpinner(teacherClassArray);

                    break;

                //Humanities
                //class start from 11,12
                case 3:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g history, political science etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.humanities_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_hs);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);

                    break;

                //Commerce
                //class start from 11,12
                case 4:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g Accountancy, business studies etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.commerce_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_hs);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Science
                //class start from all
                //set subject to science field
                case 5:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g physics, chemistry, biology etc");
                    //teacherSubjectArray = getResources().getStringArray(R.array.science_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_hs);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Maths
                //class start from all for general maths
                case 6:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g algebra, topology etc");
                    //teacherSubjectArray = getResources().getStringArray(R.array.science_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_hs);
                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Medicine
                case 7:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g anatomy, biochemistry etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.engineering_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_degree);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Music
                case 8:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("e.g vocal, guiter etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.engineering_field);
                    //teacherClassArray=getResources().getStringArray(R.array.teacher_class_from_hs);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(new String[]{""});
                    break;

                //Language
                case 9:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("like Hindi, English etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.language_field);
                    teacherClassArray = getResources().getStringArray(R.array.teacher_class_from_hs);

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //web and programming
                case 10:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("eg. c++, java, android etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Fine arts and painting
                case 11:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("eg. fine art, painting etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Electronics project
                case 12:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("eg. arduino, raspberry pie etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Dancing
                case 13:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("eg. contemporary, ballad etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //Drama
                case 14:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.INVISIBLE);
                    //subject.setHint("eg. c++, java, android etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                // cooking
                case 15:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("eg. indian ,chinese etc");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                //other
                case 16:
                    //get the value of Field spinner
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("specify what you want to teach");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;

                default:
                    stField = teacherFieldArrayAdapter.getItem(i);

                    sSubject.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    subject.setHint("specify what you want to teach");
                    //teacherSubjectArray= getResources().getStringArray(R.array.science_field);
                    teacherClassArray = new String[]{"All Levels"};

                    //updating subject and class spinner
                    updateTeacherSubjectSpinner(new String[]{""});
                    updateTeacherClassSpinner(teacherClassArray);
                    break;
            }
        }


        if (adapterView.getId() == R.id.spTeacherSubject) {

            stSubject = teacherSubjectArrayAdapter.getItem(i);

        }

        if (adapterView.getId() == R.id.spTeacherClass) {

            stClass = teacherClassArrayAdapter.getItem(i);
        }


    }

    //to display the teacher class spinner
    private void updateTeacherClassSpinner(String[] strArray) {

        teacherClassArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArray);
        sClass.setAdapter(teacherClassArrayAdapter);
        sClass.setOnItemSelectedListener(this);
    }

    //to display the subject spinner
    private void updateTeacherSubjectSpinner(String[] strArray) {

        teacherSubjectArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArray);
        sSubject.setAdapter(teacherSubjectArrayAdapter);
        sSubject.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                WriteToServerTask writeToServerTask = new WriteToServerTask();
                writeToServerTask.execute(encodedParams, SiteManager.TEACHER_REGISTER_URL, SiteManager.TEACHER_USERNAME_VALIDATION_URL, stUsername);

            } else {
                //show a dialog that permission not granted
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Internet permission not granted");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getFragmentManager(), "login_alert");

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
}
