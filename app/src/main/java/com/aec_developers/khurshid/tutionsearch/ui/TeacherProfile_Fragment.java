package com.aec_developers.khurshid.tutionsearch.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.model.Teacher;

/**
 * Created by Khurshid on 2/16/2018.
 */

public class TeacherProfile_Fragment extends Fragment {
    private Teacher teacher;
    private TextView name, qualification, fee, verified, age, gender, mobile, subject, teachUpto, city;
    private Button updateDetails, verifyAccount, logOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teacher_profile, container, false);
        teacher = (Teacher) getArguments().getSerializable("teacher");
        name = v.findViewById(R.id.tvTeacherNameInProfile);
        qualification = v.findViewById(R.id.teacherQualificationInProfile);
        fee = v.findViewById(R.id.teacherFeeInProfile);
        verified = v.findViewById(R.id.teacherVerifiedInProfile);
        age = v.findViewById(R.id.teacherAgeInProfile);
        gender = v.findViewById(R.id.teacherGenderInProfile);
        mobile = v.findViewById(R.id.teacherPhoneInProfile);
        subject = v.findViewById(R.id.teacherSubjectInProfile);
        teachUpto = v.findViewById(R.id.teacherTeachUptoInProfile);
        updateDetails = v.findViewById(R.id.btTeacherUpdateDetails);
        verifyAccount = v.findViewById(R.id.btTeacherVerify);
        city=v.findViewById(R.id.teacherCityInProfile);
        logOut = v.findViewById(R.id.btTeacherLogOut);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.setText(teacher.getName());
        qualification.setText(teacher.getQualification());
        fee.setText(teacher.getFee());
        age.setText("Age: " + teacher.getAge());
        gender.setText("Gender: " + teacher.getGender());
        mobile.setText("Mobile: " + teacher.getPhone());
        teachUpto.setText("Can Teach upto: " + teacher.getTclass());
        verified.setText(teacher.getVerified());
        city.setText("City: "+teacher.getCity()+" State: "+teacher.getState());
        if (teacher.getField().trim() == "Engineering (Theory Subjects)".trim()) {
            subject.setText("Teaches: " + teacher.getSpecific_subject() + " in the field of " + teacher.getSubject());
        } else {
            subject.setText("Teaches: " + teacher.getSpecific_subject() + " in the field of " + teacher.getField());
        }

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TeacherUpdateDetails_Activity.class);
                startActivity(intent);
            }
        });

        verifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogClass alertDialogClass;
                Bundle b;
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Mail us your name, email, phone, qualification and related documents proving them to info@oxomtech.in.");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getActivity().getFragmentManager(), "login_alert");
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("TeacherCredentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent i = getActivity().getPackageManager()
                        .getLaunchIntentForPackage(
                                getActivity().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }


//    public void teacherUpdateDetails(View v) {
//        //open update details activity
//
//    }

//    public void teacherVerifyTips(View v) {
//        //show alert box and instruct how to verify
//
//    }

//    public void teacherLogOut(View v) {
//        //log out
//
//    }
}

