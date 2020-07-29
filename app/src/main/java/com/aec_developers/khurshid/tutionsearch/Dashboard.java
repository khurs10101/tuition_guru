package com.aec_developers.khurshid.tutionsearch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aec_developers.khurshid.tutionsearch.model.Student;
import com.aec_developers.khurshid.tutionsearch.ui.About_Fragment;
import com.aec_developers.khurshid.tutionsearch.ui.Ad_Fragment;
import com.aec_developers.khurshid.tutionsearch.ui.Student_To_Teacher_Request;
import com.aec_developers.khurshid.tutionsearch.ui.TeacherListFragment;

public class Dashboard extends AppCompatActivity {

    private Student student;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    int id;
    String name, age, gender, email, phone, city, state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        Window window = this.getWindow();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }

        //code not used
        student = (Student) getIntent().getSerializableExtra("student");
        //end
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.pager);


        //retrieving student data
//        id = student.getId();
//        name = student.getName();
//        age = student.getAge();
//        gender = student.getGender();
//        email = student.getEmail();
//        phone = student.getMobile();
//        city = student.getCity();
//        state = student.getState();
        //end

        //implement swipe tabs
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), student);
        mViewPager.setAdapter(mAdapter);
        //linking viewpager with tab layout
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //end


    }


}

class MyPagerAdapter extends FragmentPagerAdapter {

    private Student student;

    public MyPagerAdapter(FragmentManager fm, Student student) {
        super(fm);
        this.student = student;
    }

    //returns fragment for a particular position
    @Override
    public Fragment getItem(int position) {
        //for position zero return the list of teachers
        if (position == 0) {
            return new TeacherListFragment();

        } else if (position == 1) {
            Student_To_Teacher_Request student_to_teacher_request = new Student_To_Teacher_Request();
            return student_to_teacher_request;
        } else if (position == 2) {
            return new Ad_Fragment();
        } else if (position == 3) {
            return new About_Fragment();
        }

        //for position 1 return the profile details of the student
        return null;
    }

    //total no of fragment
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Teachers";
        } else if (position == 1) {
            return "Request";
        } else if (position == 2) {
            return "Offers";
        } else if (position == 3) {
            return "About";
        }

        return "";
    }
}
