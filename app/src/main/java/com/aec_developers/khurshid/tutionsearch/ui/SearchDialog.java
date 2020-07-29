package com.aec_developers.khurshid.tutionsearch.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.aec_developers.khurshid.tutionsearch.R;

/**
 * Created by Khurshid on 3/11/2018.
 */

public class SearchDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText subject, location;
    private Spinner classSpinner;
    private SearchInterface searchInterface;
    private ArrayAdapter<String> classAdapter;
    private String stClass;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0) {
            stClass = "All Subjects";
        } else if (i == 1) {
            stClass = "Multiple Subjects";
        } else {
            stClass = classAdapter.getItem(i);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface SearchInterface {
        public void dataFromSearchDialog(String subject, String location, String searchClass);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            searchInterface = (SearchInterface) getTargetFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_dialog_ui, null);

        subject = view.findViewById(R.id.etSearchSubject);
        location = view.findViewById(R.id.etSearchLocation);
        classSpinner = view.findViewById(R.id.spSearchClass);
        String[] classArray = getActivity().getResources().getStringArray(R.array.teacher_field_search);
        classAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, classArray);
        classSpinner.setAdapter(classAdapter);
        classSpinner.setOnItemSelectedListener(this);

        builder.setView(view)
                .setTitle("Search")
                .setMessage("If you don't find what you are looking for. Request a teacher from request tab")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sub = subject.getText().toString().trim();
                        String loc = location.getText().toString().trim();
                        searchInterface.dataFromSearchDialog(sub, loc, stClass);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

