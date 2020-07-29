package com.aec_developers.khurshid.tutionsearch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.helper_class.JSONParser;
import com.aec_developers.khurshid.tutionsearch.helper_class.SiteManager;
import com.aec_developers.khurshid.tutionsearch.model.Ad;
import com.aec_developers.khurshid.tutionsearch.model.Ad_Copy;
import com.aec_developers.khurshid.tutionsearch.model.Login_Helper_Model;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

/**
 * Created by Khurshid on 2/17/2018.
 */

public class Ad_Fragment extends Fragment {

    private static final int INTERNET_REQUEST_CODE = 11;
    private RecyclerView rv;
    private RecyclerView.Adapter rvAdapter;
    private Ad ad;
    private ProgressBar pb;
    private AdFetchTask task = null;
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ad_fragment_layout, container, false);

        //code for ads
        MobileAds.initialize(getActivity(),
                "ca-app-pub-1082342513695725~1180692024");
        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //ads end


        rv = v.findViewById(R.id.rvAdRecyclerView);
        pb = v.findViewById(R.id.adProgressBar);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ask for internet permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            addFetch();
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

    private void addFetch() {
        task = new AdFetchTask();
        task.execute(SiteManager.AD_FETCH);
    }


    private class AdFetchTask extends AsyncTask<String, Void, Login_Helper_Model> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected Login_Helper_Model doInBackground(String... strings) {
            String site = strings[0];
            String dummyParams = "";
            Login_Helper_Model model = new Login_Helper_Model();
            List<Ad> mList;

            if (HttpManager.checkActiveInternetConnection(getActivity())) {
                //internet connection is available do the network call
                String rawData = HttpManager.readWrite(dummyParams, site).trim();
                if (rawData.equals("c")) {
                    model.setResponseString(rawData);
                } else {
                    //parse json
                    mList = JSONParser.jsonParseAd(rawData);
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

            if (c.getResponseString().equals("c")) {
                //error message
                AlertDialogClass alertDialogClass;
                Bundle b;
                //inform user that internet permission is required for login
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "No Ads Found.");
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

    private void startRecyclerView(List<Ad> list) {
        rvAdapter = new AdRecyclerViewAdapter(getActivity(), list);
        rv.setAdapter(rvAdapter);

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do the task, permission granted
                task = new AdFetchTask();
                task.execute(SiteManager.AD_FETCH);

            } else {
                //show a dialog that permission not granted
                AlertDialogClass alertDialogClass;
                Bundle b;
                //inform user that internet permission is required for login
                alertDialogClass = new AlertDialogClass();
                b = new Bundle();
                b.putString("message", "Internet permission not granted");
                alertDialogClass.setArguments(b);
                alertDialogClass.show(getActivity().getFragmentManager(), "login_alert");

                //wait for 1 second
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            //restart activity
                            Intent i = getActivity().getPackageManager()
                                    .getLaunchIntentForPackage(
                                            getActivity().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }
                });

                //close the current activity and take to splash page
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
    }
}

class AdRecyclerViewAdapter extends RecyclerView.Adapter<AdRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Ad> mList;

    public AdRecyclerViewAdapter(Context context, List<Ad> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public AdRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.ad_single_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdRecyclerViewAdapter.ViewHolder holder, int position) {

        final Ad ad = mList.get(position);
        final Ad_Copy ad_copy = new Ad_Copy(ad);

        holder.title.setText(ad.getName());
        holder.desc.setText(ad.getDescription());
        holder.location.setText(ad.getCity() + ", " + ad.getState());
        holder.knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Ad_Details_Activity.class);
                intent.putExtra("ad", ad_copy);
                context.startActivity(intent);
            }
        });


        //lazyLoad
        if (ad.getAdImage() == null) {
            //download image from internet
            //here we need to pass Ad object and holder
            ImageDownloadHelper helper = new ImageDownloadHelper(ad, holder);
            ImageDownloadTask task = new ImageDownloadTask();
            task.execute(helper);
        } else {
            holder.iv.setImageBitmap(ad.getAdImage());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ImageDownloadHelper {
        Ad ad;
        AdRecyclerViewAdapter.ViewHolder holder;
        Bitmap bitmap;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public ImageDownloadHelper(Ad ad, ViewHolder holder) {
            this.ad = ad;
            this.holder = holder;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView title, desc, location, knowMore;


        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.adImage);
            title = itemView.findViewById(R.id.adTitle);
            desc = itemView.findViewById(R.id.adDescription);
            location = itemView.findViewById(R.id.adLocation);
            knowMore = itemView.findViewById(R.id.adKnowMore);

        }
    }

    private class ImageDownloadTask extends AsyncTask<ImageDownloadHelper, Void, ImageDownloadHelper> {

        @Override
        protected ImageDownloadHelper doInBackground(ImageDownloadHelper... imageDownloadHelpers) {
            ImageDownloadHelper helper = imageDownloadHelpers[0];
            Bitmap bitmap = HttpManager.getBitmap(helper.ad.getAdImageUrl());
            helper.setBitmap(bitmap);
            return helper;
        }

        @Override
        protected void onPostExecute(ImageDownloadHelper imageDownloadHelper) {

            Bitmap bitmap = imageDownloadHelper.getBitmap();
            if (bitmap == null) {
//                Log.e("bitmap", "empty");
            } else {
                imageDownloadHelper.holder.iv.setImageBitmap(bitmap);
                imageDownloadHelper.ad.setAdImage(bitmap);
            }

        }
    }
}
