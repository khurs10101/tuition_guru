package com.aec_developers.khurshid.tutionsearch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aec_developers.khurshid.tutionsearch.model.Teacher;
import com.aec_developers.khurshid.tutionsearch.ui.About_Fragment;
import com.aec_developers.khurshid.tutionsearch.ui.Ad_Fragment;
import com.aec_developers.khurshid.tutionsearch.ui.TeacherProfile_Fragment;
import com.aec_developers.khurshid.tutionsearch.ui.Teacher_Reward_Fragment;

/**
 * Created by Khurshid on 2/8/2018.
 */

public class TeacherDashBoard extends AppCompatActivity {

    private Teacher teacher;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TeacherPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_dashboard);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        Window window = this.getWindow();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }

        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        mTabLayout = findViewById(R.id.teacherTabLayout);
        mViewPager = findViewById(R.id.teacherPager);


        //implement swipe tabs
        mAdapter = new TeacherPagerAdapter(getSupportFragmentManager(), teacher);
        mViewPager.setAdapter(mAdapter);
        //linking viewpager with tab layout
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //end


    }


}

class TeacherPagerAdapter extends FragmentPagerAdapter {

    private Teacher teacher;

    public TeacherPagerAdapter(FragmentManager fm, Teacher teacher) {
        super(fm);
        this.teacher = teacher;
    }

    //returns fragment for a particular position
    @Override
    public Fragment getItem(int position) {
        //for position zero return the list of teachers
        if (position == 0) {
            //show teacher profile page
            TeacherProfile_Fragment teacherProfile_fragment = new TeacherProfile_Fragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("teacher", teacher);
            teacherProfile_fragment.setArguments(bundle);
            return teacherProfile_fragment;

        } else if (position == 1) {
            //show comming soon page
            Teacher_Reward_Fragment teacherStudentRequest_fragment = new Teacher_Reward_Fragment();
            return teacherStudentRequest_fragment;
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
            return "My Profile";
        } else if (position == 1) {
            return "Rewards";
        } else if (position == 2) {
            return "Offers";
        } else if (position == 3) {
            return "About";
        }

        return "";
    }
}
