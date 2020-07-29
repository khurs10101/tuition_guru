package com.aec_developers.khurshid.tutionsearch.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.aec_developers.khurshid.tutionsearch.Custom_Adapters.PlaceAutocompleteAdapter;
import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khurshid on 3/17/2018.
 */

public class Student_To_Teacher_Request extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int INTERNET_REQUEST_CODE = 14;
    private EditText name, phone, email, subject;
    private Spinner field;
    private Button request;
    private String stName, stPhone, stEmail, stSubject, stField, stCity, stState;
    private String encodedParams;
    private ArrayAdapter<String> fieldArrayAdapter;
    private ProgressBar pb;
    private AutoCompleteTextView location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_to_teacher_frag_ui, container, false);
        name = v.findViewById(R.id.etRequestName);
        phone = v.findViewById(R.id.etRequestPhone);
        email = v.findViewById(R.id.etRequestEmail);
        field = v.findViewById(R.id.spRequestField);
        subject = v.findViewById(R.id.etRequestSubject);
        request = v.findViewById(R.id.btRequest);
        location = v.findViewById(R.id.etRequestCity);
        pb = v.findViewById(R.id.pbRequest);
        //initializing the spinner
        String[] fieldArray = getActivity().getResources().getStringArray(R.array.teacher_field_search);
        fieldArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_white_text, fieldArray);
        field.setAdapter(fieldArrayAdapter);

        //google places autocomplete
        GeoDataClient mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
        //filter by city
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        //Lat long bounds (for assam only)
        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(24.138499, 89.685638),
                new LatLng(27.968216, 96.013161));
        //autocomplete adapter
        PlaceAutocompleteAdapter adapter = new PlaceAutocompleteAdapter(getActivity(), mGeoDataClient, latLngBounds, typeFilter);
        location.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //handling button click
        request.setOnClickListener(this);
        //handling spinner click
        field.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btRequest) {


            stName = name.getText().toString();
            stPhone = phone.getText().toString();
            stEmail = email.getText().toString();
            stSubject = subject.getText().toString();
            String loc = location.getText().toString();

            int count = 0;
            //field validation code
            //name field cant be empty
            if (stName.length() == 0) {
                name.setBackgroundResource(R.drawable.edittext_invalid);
                count++;

            } else {
                name.setBackgroundResource(R.drawable.edittext_valid);
            }

            if (stPhone.length() == 0) {
                phone.setBackgroundResource(R.drawable.edittext_invalid);
                count++;

            } else {
                phone.setBackgroundResource(R.drawable.edittext_valid);
            }

            if (stEmail.length() == 0) {
                email.setBackgroundResource(R.drawable.edittext_invalid);
                count++;

            } else {
                email.setBackgroundResource(R.drawable.edittext_valid);
            }

            if (stSubject.length() == 0) {
                subject.setBackgroundResource(R.drawable.edittext_invalid);
                count++;

            } else {
                subject.setBackgroundResource(R.drawable.edittext_valid);
            }

            if (loc.length() == 0) {
                location.setBackgroundResource(R.drawable.edittext_invalid);
            } else {
                location.setBackgroundResource(R.drawable.edittext_valid);
            }

            if (count == 0) {
                //everything is fine proceed to submit the request

                //get the location value
                String[] fragloc = loc.split(",");
                if (fragloc[0] != null)
                    stCity = fragloc[0].trim();
                if (fragloc[1] != null)
                    stState = fragloc[1];

                Map<String, String> params = new HashMap<>();
                params.put("name", stName);
                params.put("phone", stPhone);
                params.put("email", stEmail);
                params.put("field", stField);
                params.put("subject", stSubject);
                params.put("city", stCity);
                params.put("state", stState);

                encodedParams = HttpManager.getEncodedParams(params);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                    runTask();

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.INTERNET)) {
                        //inform user that internet permission is required for login
//
                    }
                    //ask for permisson
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
                }


            } else {
                count = 0;
            }

        }
    }

    private void runTask() {
        WriteToServerTask writeToServerTask = new WriteToServerTask();
        writeToServerTask.execute(encodedParams, SiteManager.STUDENT_REQUEST);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        stField = fieldArrayAdapter.getItem(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class WriteToServerTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String encodedParams = strings[0];
            String url = strings[1];

            //make a http call here
            String response = HttpManager.readWrite(encodedParams, url);
            if (response != null) {
                response = response.trim();
            } else {
                response = "";
            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.GONE);
            if (s.equals("a")) {
                //request successfull
                Toast.makeText(getActivity(), "Request submitted successfully", Toast.LENGTH_LONG).show();

            } else if (s.equals("b")) {

                Toast.makeText(getActivity(), "Request Already Submitted.", Toast.LENGTH_LONG).show();

            } else {
                //request not successfull
                Toast.makeText(getActivity(), "Request not submitted", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                runTask();
            } else {
                //show a dialog that permission not granted

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
}
