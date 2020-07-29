package com.aec_developers.khurshid.tutionsearch.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Khurshid on 2/18/2018.
 */

public class Ad extends Ad_Copy implements Serializable {


    private Bitmap adImage;

    public Bitmap getAdImage() {
        return adImage;
    }

    public void setAdImage(Bitmap adImage) {
        this.adImage = adImage;
    }
}

