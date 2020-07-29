package com.aec_developers.khurshid.tutionsearch.helper_class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Khurshid on 1/26/2018.
 */

public class HttpManager {


    public static Bitmap getBitmap(String url) {

        InputStream in = null;
        try {
            in = (InputStream) new URL(url).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    public static String getRawData(String url) {

        HttpURLConnection con = null;
        BufferedReader br = null;
        try {
            URL site = new URL(url);
            con = (HttpURLConnection) site.openConnection();
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line + "\n");
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            try {
                if (br != null)
                    br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (con != null)
                con.disconnect();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (br != null)
                    br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (con != null)
                con.disconnect();
            return null;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (con != null)
                con.disconnect();
        }

    }


    public static String getEncodedParams(Map<String, String> params) {

        //The responsibilty of this method is to generate a string like this
        //name=khurshid&email=khurs10101@gmail.com&city=guwahati

        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //append & if there is somthing in the string
            if (sb.length() > 0)
                sb.append("&");

            sb.append(key + "=" + value);
        }

        return sb.toString();
    }

    public static void writeToServer(String encodedParams, String url) {

        HttpURLConnection con = null;
        BufferedWriter bw = null;

        try {

            URL site = new URL(url);
            con = (HttpURLConnection) site.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            bw.write(encodedParams);
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            if (con != null)
                con.disconnect();
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            if (con != null)
                con.disconnect();
        }

    }


    public static String readWrite(String encodedParams, String url) {

        HttpURLConnection con = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            URL site = new URL(url);
            con = (HttpURLConnection) site.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            //writing data to server
            bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            bw.write(encodedParams);
            bw.flush();

            if (con.getResponseCode() == 200) {
                //reading response from server
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line + "\n");

                return sb.toString();
            } else {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            //closing reader
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //closing writer
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //closing connection
            if (con != null)
                con.disconnect();
        }

    }


    //method to check whether network is available or not
    public static boolean isNetworkAvailable(Context context) {

        //runtime permission check

        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;

    }

    //method to check active internet connection
    public static boolean checkActiveInternetConnection(Context context) {
        Log.v("BENCH", "Internet check started");
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) (new URL("https://www.google.co.in").openConnection());
//            urlc.setRequestProperty("User-Agent", "Test");
//            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(2000);
            urlc.connect();
            Log.v("BENCH", "Internet check over");
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("INTERNET_CHECK", "Error: ", e);
        } finally {
            if (urlc != null)
                urlc.disconnect();
        }

        return false;
    }
}
