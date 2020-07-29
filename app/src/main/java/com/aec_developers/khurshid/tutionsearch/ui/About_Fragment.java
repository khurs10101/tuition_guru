package com.aec_developers.khurshid.tutionsearch.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;

/**
 * Created by Khurshid on 2/17/2018.
 */

public class About_Fragment extends Fragment implements View.OnClickListener {

    private TextView appVersion;
    private String versionDetails;
    private static int version, newVersion;
    private Button appUpdateButton;
    private static ProgressBar pbApp;
    private static String appPackageName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about, container, false);
        appPackageName = getActivity().getPackageName();
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(appPackageName, 0);
            versionDetails = pInfo.versionName;
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appVersion = v.findViewById(R.id.appVersion);
        appUpdateButton = v.findViewById(R.id.btCheckAppUpdate);
        pbApp = v.findViewById(R.id.pbAppUpdate);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appUpdateButton.setOnClickListener(this);
        appVersion.setText("Version " + versionDetails);
        checkUpdateMethod();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btCheckAppUpdate) {
            checkUpdateMethod();
        }
    }

    private void checkUpdateMethod() {
        VersionCheckTask task = new VersionCheckTask();
        task.execute(SiteManager.VERSION_CHECK);
    }

    private class VersionCheckTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            appUpdateButton.setVisibility(View.GONE);
            pbApp.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String response = HttpManager.readWrite("", url);
            if (response != null)
                response = response.trim();
            else
                response = "";
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            appUpdateButton.setVisibility(View.VISIBLE);
            pbApp.setVisibility(View.GONE);
            if (s.equals("")) {
                Toast.makeText(getActivity(), "Try again later", Toast.LENGTH_SHORT).show();
            } else {
                newVersion = Integer.parseInt(s);
                compareVersionMethod();
            }
        }
    }

    private void compareVersionMethod() {
        if (newVersion > version) {
            //new version available
            appUpdateButton.setText("Download the latest version");
            //open intent
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else {
            //already updated
            appUpdateButton.setText("Check for Updates");
            Toast.makeText(getActivity(), "Already running on latest version", Toast.LENGTH_LONG).show();
        }
    }
}
