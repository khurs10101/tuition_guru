package com.aec_developers.khurshid.tutionsearch.ui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.aec_developers.khurshid.tutionsearch.R;
import com.aec_developers.khurshid.tutionsearch.helper_class.HttpManager;
import com.aec_developers.khurshid.tutionsearch.model.Ad_Copy;

/**
 * Created by Khurshid on 2/17/2018.
 */

public class Ad_Details_Activity extends AppCompatActivity {

    private Ad_Copy ad;
    private ImageView image;
    private TextView title, address, description, contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_details);

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

        ad = (Ad_Copy) getIntent().getSerializableExtra("ad");
        image = findViewById(R.id.adImageInDetails);
        title = findViewById(R.id.adTitleInDetails);
        address = findViewById(R.id.adAddressInDetails);
        description = findViewById(R.id.adDescriptionInDetails);
        contact = findViewById(R.id.adContactInDetails);

        //Load Image from the web
        loadImage();

        //setting data
//        image.setImageBitmap(ad.getAdImage());
        title.setText(ad.getName());
        description.setText(ad.getDescription());
        StringBuilder con = new StringBuilder();
        con.append("Phone 1: " + ad.getPhone1() + "\n");
        if (!ad.getPhone2().trim().equals("null"))
            con.append("Phone 2: " + ad.getPhone2() + "\n");
        if (!ad.getPhone3().trim().equals("null"))
            con.append("Phone 3: " + ad.getPhone3() + "\n");
        if (!ad.getPhone4().trim().equals("null"))
            con.append("Phone 4: " + ad.getPhone4() + "\n");
        if (ad.getEmail().length() > 0)
            con.append("Email: " + ad.getEmail());
        String stContact = con.toString();
        contact.setText(stContact);

        StringBuilder sbAdd = new StringBuilder();
        sbAdd.append(ad.getAddress() + "\n");
        sbAdd.append("City: " + ad.getCity() + ", State: " + ad.getState());
        String stAddress = sbAdd.toString();
        address.setText(stAddress);


    }

    private void loadImage() {
        ImageLoadTask task = new ImageLoadTask();
        task.execute(ad.getAdImageUrl());
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String site = strings[0];
            Bitmap bitmap = HttpManager.getBitmap(site);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
}
