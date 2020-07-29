package com.aec_developers.khurshid.tutionsearch.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aec_developers.khurshid.tutionsearch.R;

/**
 * Created by Khurshid on 2/12/2018.
 */

public class FilterDialog_Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner field, forClass;
    private EditText subject = null, city = null;
    private Button filter;
    private ArrayAdapter<String> fieldAdapter, forClassAdapter;
    String stField, stForClass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_dialog_fragment);
        field = findViewById(R.id.spFilterField);
        forClass = findViewById(R.id.spFilterClass);
        subject = findViewById(R.id.etFilterSubject);
        city = findViewById(R.id.etFilterCity);
        filter = findViewById(R.id.btFilter);
        filter.setOnClickListener(this);
        field.setOnItemSelectedListener(this);
        forClass.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        String stSubject = subject.getText().toString().trim();
        String stCity = city.getText().toString().trim();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spFilterField) {
            stField = fieldAdapter.getItem(i);
        }

        if (adapterView.getId() == R.id.spFilterClass) {
            stForClass = forClassAdapter.getItem(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
