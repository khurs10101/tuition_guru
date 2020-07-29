package com.aec_developers.khurshid.tutionsearch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.aec_developers.khurshid.tutionsearch.ui.TeacherLogin_Activity;

/**
 * Created by Khurshid on 2/8/2018.
 */

public class SplashPage extends AppCompatActivity implements View.OnClickListener {

    private Button startForStudent, startForTeacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        startForStudent = findViewById(R.id.btStartForStudent);
        startForTeacher = findViewById(R.id.btStartForTeacher);
        startForTeacher.setOnClickListener(this);
        startForStudent.setOnClickListener(this);
//        Window window = this.getWindow();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }

    }

//    public void startForTeacher(View view) {
//        //start teacher login or registration page
//        startActivity(new Intent(this, TeacherLogin_Activity.class));
//
//    }
//
//    //possible point of failure
//    public void startForStudent(View view) {
//        //start student login page and finish this activity
////        startActivity(new Intent(this, LoginActivity.class));
//
//        //avoid opening student login page.
//        startActivity(new Intent(this, Dashboard.class));
//    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btStartForStudent) {
            startActivity(new Intent(this, Dashboard.class));
        }

        if (view.getId() == R.id.btStartForTeacher) {
            startActivity(new Intent(this, TeacherLogin_Activity.class));
        }

    }
}
