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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.JSONParser;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;
import com.aec_developers.khurshid.tutionsearch.model.Login_Helper_Model;
import com.aec_developers.khurshid.tutionsearch.model.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Khurshid on 2/6/2018.
 */

public class TeacherListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, SearchDialog.SearchInterface {

    private static final int INTERNET_REQUEST_CODE = 12;
    private RecyclerView rvTeacherList;
    private RecyclerView.Adapter rvTeacherListAdapter;
    private ArrayAdapter<String> actTeacherSearchArrayAdapter, filterSubjectArrayAdapter;
    private String actTeacherSearchAutoCompleteString[], filterSubjectList[];
    private Toolbar toolbar;
    private String stUsername, stPassword;
    private SharedPreferences preferences;
    private String encodedParams;
    private ProgressBar pb;
    private MyTask task = null;

    //navigation components
    private ImageView ivBack, ivNext, ivFilter, ivReload;
    private static int totalPage = 0;
    private static int count = 0;
    private String searchLocation, searchSubject, searchClass;

    //Filter parameters
    private AutoCompleteTextView actTeacherSearch;
    private Button btFilter, btSearch;
    private Spinner filterSubject;
    private EditText filterCity;
    private String stSubject;


    //returning ui
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacherlist_frag_ui, container, false);
        rvTeacherList = view.findViewById(R.id.rvTeacherList);
//        toolbar = getActivity().findViewById(R.id.filterToolbar);
        pb = view.findViewById(R.id.pbTeacherList);

        //navigation parameter
        ivBack = view.findViewById(R.id.ivBack);
        ivNext = view.findViewById(R.id.ivNext);
        ivReload = view.findViewById(R.id.ivReload);
        ivFilter = view.findViewById(R.id.ivFilter);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivReload.setOnClickListener(this);
        //ends

        //filter parameters
        //ends

        return view;

    }

    //access the view inside layout
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getting value from shared preferences
        preferences = getActivity().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
        stUsername = preferences.getString("susername", "khurs10101@gmail.com");
        stPassword = preferences.getString("spassword", "123456");
//        Log.e("TutionDebug", "u: " + stUsername + " p: " + stPassword);
        //end

        //filter parameters
        actTeacherSearchAutoCompleteString = getActivity().getResources().getStringArray(R.array.autocomplete_suggestion);
        actTeacherSearchArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, actTeacherSearchAutoCompleteString);
//        actTeacherSearch.setAdapter(actTeacherSearchArrayAdapter);
//        actTeacherSearch.setThreshold(0);

        filterSubjectList = getActivity().getResources().getStringArray(R.array.filterSubjectList);
        filterSubjectArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, filterSubjectList);
//        filterSubject.setAdapter(filterSubjectArrayAdapter);
//        filterSubject.setOnItemSelectedListener(this);
//        btFilter.setOnClickListener(this);
//        btSearch.setOnClickListener(this);

        //ends

        //make a network call
        //encode the params
        Map<String, String> params = new HashMap<>();
        params.put("username", stUsername);
        params.put("password", stPassword);
//        Log.e("TutionDebug", "username " + stUsername);
        encodedParams = HttpManager.getEncodedParams(params);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
                task.cancel(true);
//            Log.v("BENCH", "RawData started");
            loadTask(encodedParams);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.INTERNET)) {
                //inform user that internet permission is required for login
                AlertDialogClass alertDialogClass;
                Bundle b;
                //inform user that internet permission is required for login
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Internet permission is required to display the list of teachers");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getActivity().getFragmentManager(), "login_alert");
            }
            //ask for permisson
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
        }


    }

    private void loadTask(String encodedParams) {
        task = new MyTask();
        task.execute(SiteManager.TEACHER_LIST_TEST, encodedParams, String.valueOf(count));
    }

    @Override
    public void onClick(View view) {

        //button click perform the search
//        if (view.getId() == R.id.btFilterToolbar) {
//
//            String stCity = filterCity.getText().toString().trim();
//
//            if (stCity.length() == 0) {
//                filterCity.setHint("Enter city");
//                filterCity.setBackgroundResource(R.drawable.edittext_invalid);
//            } else {
//                filterCity.setBackgroundResource(R.drawable.edittext_invalid);
//                Map<String, String> params = new HashMap<>();
//                params.put("subject", stSubject);
//                params.put("city", stCity);
//                String QueryParams = HttpManager.getEncodedParams(params);
//                FilterTask task = new FilterTask();
//                task.execute(QueryParams, SiteManager.TOOLBAR_FILTER);
//            }
//        }

//        if (view.getId() == R.id.allSearchToolbar) {
//            String query = actTeacherSearch.getText().toString().trim();
//            Map<String, String> param = new HashMap<>();
//            param.put("search_query", query);
//            String QueryParams = HttpManager.getEncodedParams(param);
//
//            //internet call
//            SearchTask task = new SearchTask();
//            task.execute(QueryParams, SiteManager.TOOLBAR_SEARCH);
//        }

        //onclick listener for page navigation
        if (view.getId() == R.id.ivBack) {

            //if count is less than or equal to zero dont allow back operation
            if (count <= 0) {
                Log.v("COUNT", "Value down Nothing :" + String.valueOf(count));
                Log.v("COUNT", "Total Page :" + String.valueOf(totalPage));
                Toast.makeText(getActivity(), "This is the first page", Toast.LENGTH_SHORT).show();
            } else {
                count--;
                Toast.makeText(getActivity(), "Page: " + String.valueOf(count + 1), Toast.LENGTH_SHORT).show();
                Log.v("COUNT", "Value down :" + String.valueOf(count));
                if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
                    task.cancel(true);
                loadTask(encodedParams);
            }

        }
        if (view.getId() == R.id.ivNext) {

            //dont increase the count if the count is greater than totalpage

            if (count >= totalPage) {
                Log.v("COUNT", "Value up Nothing :" + String.valueOf(count));
                Toast.makeText(getActivity(), "No more records", Toast.LENGTH_SHORT).show();
            } else {
                count++;
                Toast.makeText(getActivity(), "Page: " + String.valueOf(count + 1), Toast.LENGTH_SHORT).show();
                Log.v("COUNT", "Value down :" + String.valueOf(count));
                if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
                    task.cancel(true);
                loadTask(encodedParams);
            }

        }
        if (view.getId() == R.id.ivFilter) {
            //design a dialog
            SearchDialog dialog = new SearchDialog();
            dialog.setTargetFragment(this, 0);
            dialog.show(getActivity().getSupportFragmentManager(), "search_dialog");
        }

        if (view.getId() == R.id.ivReload) {
            loadTask(encodedParams);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//        if (adapterView.getId() == R.id.subjectToolbar) {
//            stSubject = filterSubjectArrayAdapter.getItem(i);
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //TODO: Communication with this fragment and DialogFragment (SearchDialog)
    @Override
    public void dataFromSearchDialog(String subject, String location, String searchClass) {
        searchLocation = location;
        searchSubject = subject;
        this.searchClass = searchClass;

        Map<String, String> map = new HashMap<>();
        map.put("search_subject", subject);
        map.put("search_location", location);
        map.put("search_field", this.searchClass);
        String encodedParams = HttpManager.getEncodedParams(map);
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
            task.cancel(true);
        loadTask(encodedParams);
    }

    //TODO: Main Asynctask which will run as soon as the fragment launches
    //remember to request for permissions
    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            ivNext.setClickable(false);
            ivBack.setClickable(false);
            ivFilter.setClickable(false);
            ivReload.setClickable(false);
//            btFilter.setClickable(false);
//            btSearch.setClickable(false);
        }

        @Override
        protected String doInBackground(String... as) {
            String url = as[0];
            String encodedParams = as[1];
            String page = as[2];
            url = url + "?page=" + count;
            //remember to add permission access mechanism
//            if (HttpManager.checkActiveInternetConnection(getActivity())) {
            //internet connection is available do the network call
            String rawData = HttpManager.readWrite(encodedParams, url);
            //taking care of null pointer
            if (rawData != null)
                rawData = rawData.trim();
            else
                rawData = "".trim();

//            Log.v("BENCH", "RawData Recieved");
            return rawData;

//            }

        }

        protected void onPostExecute(String c) {
            pb.setVisibility(View.GONE);
//            btFilter.setClickable(true);
//            btSearch.setClickable(true);
            if (c.equals("".trim())) {
                Toast.makeText(getActivity(), "Result not found or Problem with the Server. Try Searching Again.", Toast.LENGTH_SHORT).show();
//                if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
//                    task.cancel(true);
//                loadTask(encodedParams);
                ivNext.setClickable(true);
                ivBack.setClickable(true);
                ivFilter.setClickable(true);
                ivReload.setClickable(true);
            } else {
                //TODO: Start Asynctask for json parsing
//                Log.v("BENCH", "Parsing started");
                JSONParsingTask jsonParsingTask = new JSONParsingTask();
                jsonParsingTask.execute(c);
            }

        }
    }

    private class JSONParsingTask extends AsyncTask<String, Void, List<Teacher>> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Teacher> doInBackground(String... strings) {
            String rawData = strings[0];
            List<Teacher> mList;
            mList = JSONParser.jsonParseTeacher(rawData);
            return mList;
        }

        @Override
        protected void onPostExecute(List<Teacher> login_helper_model) {
            Teacher teacher = login_helper_model.get(0);
            totalPage = Integer.parseInt(teacher.getTotalCount());
            pb.setVisibility(View.GONE);
            ivNext.setClickable(true);
            ivBack.setClickable(true);
            ivFilter.setClickable(true);
            ivReload.setClickable(true);
//            Log.v("BENCH", "Parsing over");
            startRecyclerView(login_helper_model);

        }
    }


    //asynctask for search work
    //TODO:Unused class
    private class SearchTask extends AsyncTask<String, Void, Login_Helper_Model> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
//            btFilter.setClickable(false);
//            btSearch.setClickable(false);
        }

        @Override
        protected Login_Helper_Model doInBackground(String... as) {
            String encodedParams = as[0];
            String site = as[1];
            Login_Helper_Model model = new Login_Helper_Model();
            List<Teacher> mList;
            //remember to add permission access mechanism
            if (HttpManager.checkActiveInternetConnection(getActivity())) {
                //internet connection is available do the network call
                String rawData = HttpManager.readWrite(encodedParams, site).trim();
                if (rawData.equals("c")) {
                    model.setResponseString(rawData);
                } else {
                    //parse json
                    mList = JSONParser.jsonParseTeacher(rawData);
                    model.setList(mList);
                    model.setResponseString("success");
                }
                return model;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Login_Helper_Model c) {
            pb.setVisibility(View.GONE);
//            btFilter.setClickable(true);
//            btSearch.setClickable(true);

            if (c.getResponseString().equals("c")) {
                //error message
                AlertDialogClass alertDialogClass;
                Bundle b;
                //inform user that internet permission is required for login
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "No results found. Try again with different keyword");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getActivity().getFragmentManager(), "login_alert");
            } else if (c.getResponseString().equals("success")) {
                //initialize the adapter and show the content
//                Log.e("TutionDebug", "list Size" + c.getList().size());
                startRecyclerView(c.getList());
            }
        }
    }

    //asynctask for filter task
    //TODO:Unused class
    private class FilterTask extends AsyncTask<String, Void, Login_Helper_Model> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
//            btFilter.setClickable(false);
//            btSearch.setClickable(false);
        }


        @Override
        protected Login_Helper_Model doInBackground(String... as) {
            String encodedParams = as[0];
            String site = as[1];
            Login_Helper_Model model = new Login_Helper_Model();
            List<Teacher> mList;
            //remember to add permission access mechanism
            if (HttpManager.checkActiveInternetConnection(getActivity())) {
                //internet connection is available do the network call
                String rawData = HttpManager.readWrite(encodedParams, site).trim();
                if (rawData.equals("c")) {
                    model.setResponseString(rawData);
                } else {
                    //parse json
                    mList = JSONParser.jsonParseTeacher(rawData);
                    model.setList(mList);
                    model.setResponseString("success");
                }
                return model;
            }
            model.setResponseString("");
            return model;
        }

        @Override
        protected void onPostExecute(Login_Helper_Model c) {
            pb.setVisibility(View.GONE);
//            btFilter.setClickable(true);
//            btSearch.setClickable(true);

            if (c.getResponseString().equals("authentication failed")) {
                //error message
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "No results found. Try again with different keyword");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getActivity().getFragmentManager(), "login_alert");
            } else if (c.getResponseString().equals("success")) {
                //initialize the adapter and show the content
//                Log.e("TutionDebug", "list Size" + c.getList().size());
                startRecyclerView(c.getList());
            } else {
                Toast.makeText(getActivity(), "Check your internet. Try to reconnect or contact the Developer", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void startRecyclerView(List<Teacher> list) {
        rvTeacherListAdapter = new TeacherListViewAdapter(getActivity(), list);
        rvTeacherList.setAdapter(rvTeacherListAdapter);

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
                    task.cancel(true);
                loadTask(encodedParams);

            } else {
                //show a dialog that permission not granted

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.i("Fragment Lifecycle", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //if asynctask is not null and it is running then stop.
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
    }
}
//end of fragment


//start of adapter
class TeacherListViewAdapter extends RecyclerView.Adapter<TeacherListViewAdapter.ViewHolder> {

    private Context context;
    private List<Teacher> mList;

    public TeacherListViewAdapter(Context context, List<Teacher> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public TeacherListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.teacher_single_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TeacherListViewAdapter.ViewHolder holder, int position) {

        final Teacher teacher = mList.get(position);
        //binding happens here
        holder.teacherName.setText(teacher.getName());
        holder.teacherCity.setText("City: " + teacher.getCity());
        holder.teacherState.setText("State: " + teacher.getState());
//        holder.teacherQualification.setText(teacher.getQualification());
//        holder.teacherClass.setText(teacher.getTclass());
        holder.teacherSubject.setText("Teaches in the field of " + teacher.getField() + " with subjects " + teacher.getSpecific_subject());


        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TeacherDetails_Activity.class);
                intent.putExtra("teacher", teacher);
                context.startActivity(intent);
            }
        });


//        if(teacher.getTeacherImage()!=null){
//
//        }
//        else {
//
//        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView teacherImage;
        TextView teacherName, teacherCity, teacherState, teacherQualification, teacherSubject, teacherClass;
        Button detailsButton, verifiedButton;


        public ViewHolder(View itemView) {
            super(itemView);

//            teacherImage = itemView.findViewById(R.id.ivTeacher80by80image);
            teacherName = itemView.findViewById(R.id.tvTeacherNameInList);
            teacherCity = itemView.findViewById(R.id.tvTeacherCityInList);
            teacherState = itemView.findViewById(R.id.tvTeacherStateInList);
//            teacherQualification = itemView.findViewById(R.id.tvTeacherQualificationInList);
            teacherSubject = itemView.findViewById(R.id.tvTeacherAllSubjectInList);
//            teacherClass = itemView.findViewById(R.id.tvTeacherClassInList);
            detailsButton = itemView.findViewById(R.id.btTeacherDetailsInList);
//            verifiedButton = itemView.findViewById(R.id.btTeacherVerifiedButtonInList);

        }
    }
}