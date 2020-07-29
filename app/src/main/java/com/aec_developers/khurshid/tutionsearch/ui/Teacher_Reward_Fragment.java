package com.aec_developers.khurshid.tutionsearch.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Khurshid on 2/8/2018.
 */

public class Teacher_Reward_Fragment extends Fragment implements View.OnClickListener, RewardedVideoAdListener {

    private RecyclerView rv;
    private static List<String> messageList;
    //    private static TeacherStudentFragmentAdapter adapter;
    private TextView coinCount;
    private static int coinValue = 0, intEnableEmail = 1;
    private Button refreshCoins, seeAds, enableEmail, seeAdsRetry;
    private AdView mAdView1, mAdView2, mAdView3, mAdView4, mAdView5;
    private String stUsername, stPassword;
    private SharedPreferences preferences;
    private static ProgressBar coinRefreshPb, enableEmailPb, pbSeeAds;
    private RewardedVideoAd mRewardedVideoAd;
    private static int adRetry = 0;
    private static boolean coinUpdateFailedFlag = false;
    private CoinUpdateTask task = null;
    private CoinUpdateUpdateTask ttask = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teacher_rewards, container, false);

        //reading sharedpref value
        preferences = getActivity().getSharedPreferences("TeacherCredentials", Context.MODE_PRIVATE);
        if (preferences.getString("tusername", "").length() > 0 && preferences.getString("tpassword", "").length() > 0) {
            stUsername = preferences.getString("tusername", "");
            stPassword = preferences.getString("tpassword", "");
        } else {
            //sign in again
        }

        preferences = getActivity().getSharedPreferences("EmailEnable", Context.MODE_PRIVATE);
        intEnableEmail = preferences.getInt("enable", 1);

        //for coin update
        preferences = getActivity().getSharedPreferences("CoinUpdate", Context.MODE_PRIVATE);
        coinUpdateFailedFlag = preferences.getBoolean("coinUpdateFlag", coinUpdateFailedFlag);
        coinValue = preferences.getInt("coinValue", coinValue);

        if (coinUpdateFailedFlag) {
            updateTheCoinMethod(coinValue);
        }

        //end
//        Log.v("FCM", stUsername + " " + stPassword);

        //textViews
        coinCount = v.findViewById(R.id.etCoinCount);
        //ends
        //buttons
        refreshCoins = v.findViewById(R.id.btCoinRefresh);
        seeAds = v.findViewById(R.id.btSeeAds);
        seeAdsRetry = v.findViewById(R.id.btSeeAdsTryAgain);
        enableEmail = v.findViewById(R.id.btEnableEmail);
        //ends
        //Ads
        mAdView1 = v.findViewById(R.id.adViewCoin);
        mAdView2 = v.findViewById(R.id.adViewCoin1);
        mAdView3 = v.findViewById(R.id.adViewCoin2);
        mAdView4 = v.findViewById(R.id.adViewCoin3);
        mAdView5 = v.findViewById(R.id.adViewCoin4);
        //ends

        //progress bars
        coinRefreshPb = v.findViewById(R.id.pbCoinRefresh);
        enableEmailPb = v.findViewById(R.id.pbEnableEmail);
        pbSeeAds = v.findViewById(R.id.pbSeeAds);
        //ends

        //TODO: Rewarded video ad implementation
//        MobileAds.initialize(getActivity(),
//                "ca-app-pub-3940256099942544~3347511713");
        //TODO:Production ads
        MobileAds.initialize(getActivity(),
                "ca-app-pub-1082342513695725~1180692024");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadNormalAds();
        loadRewardedVideoAd();

        //TODO:END


//        rv = v.findViewById(R.id.rvTeacherStudentList);
//        //setting the adapter of recycler view
//        messageList = new ArrayList<>();
//        adapter = new TeacherStudentFragmentAdapter(getActivity(), messageList);
//        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//        rv.setItemAnimator(new DefaultItemAnimator());
//        rv.setAdapter(adapter);
        return v;
    }

    private void updateTheCoinMethod(int coinValue) {
        Map<String, String> params = new HashMap<>();
        params.put("username", stUsername);
        params.put("password", stPassword);
        params.put("coin", String.valueOf(coinValue));
        String encodedParams = HttpManager.getEncodedParams(params);
        ttask = new CoinUpdateUpdateTask();
        ttask.execute(SiteManager.COIN_UPDATE, encodedParams);
    }

    private void loadRewardedVideoAd() {
//        refreshCoinMethod();

//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());

        mRewardedVideoAd.loadAd("ca-app-pub-1082342513695725/9341320861",
                new AdRequest.Builder().build());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshCoins.setOnClickListener(this);
        seeAds.setOnClickListener(this);
        seeAdsRetry.setOnClickListener(this);
        enableEmail.setOnClickListener(this);
        //when view is created refresh coins
        if (mRewardedVideoAd.isLoaded()) {
            seeAds.setClickable(true);
            seeAds.setText("Earn coins");
            loadNormalAds();
        } else {
            seeAds.setClickable(false);
            seeAds.setText("Please wait...");
        }
        refreshCoinMethod();
        emailEnabledUi(intEnableEmail);

    }

    private void loadNormalAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
        mAdView1.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
//                Log.v("FCM", "Normal ads failed to load " + String.valueOf(i));
            }
        });
        mAdView2.loadAd(adRequest);
        mAdView3.loadAd(adRequest);
        mAdView4.loadAd(adRequest);
        mAdView5.loadAd(adRequest);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btCoinRefresh) {
            refreshCoinMethod();
        }

        if (view.getId() == R.id.btSeeAds) {
            seeAdsMethod();
        }

        if (view.getId() == R.id.btEnableEmail) {
            enableEmailMethod();
        }

        if (view.getId() == R.id.btSeeAdsTryAgain) ;
        {
            //load ads again
            pbSeeAds.setVisibility(View.VISIBLE);
            seeAdsRetry.setClickable(false);
            refreshCoins.setClickable(false);
            enableEmail.setClickable(false);
            loadRewardedVideoAd();
        }

    }

    private void enableEmailMethod() {
        //encode username and password
        Map<String, String> params = new HashMap<>();
        params.put("username", stUsername);
        params.put("password", stPassword);
        params.put("enable", String.valueOf(intEnableEmail));
        String encodedParams = HttpManager.getEncodedParams(params);

        EmailEnableTask task = new EmailEnableTask();
        task.execute(SiteManager.ENABLE_EMAIL, encodedParams);
    }

    private void seeAdsMethod() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    //method to refresh coins
    private void refreshCoinMethod() {

        //encode the username and password
        Map<String, String> params = new HashMap<>();
        params.put("username", stUsername);
        params.put("password", stPassword);
        String encodedParams = HttpManager.getEncodedParams(params);
        //only get the updated coins from the server
        task = new CoinUpdateTask();
        task.execute(SiteManager.COIN_REFRESH, encodedParams);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //video ad is loaded allow button click
        //for redundancy
        refreshCoins.setClickable(true);
        enableEmail.setClickable(true);
        pbSeeAds.setVisibility(View.GONE);
        seeAds.setVisibility(View.VISIBLE);
        seeAdsRetry.setVisibility(View.GONE);
//        Log.v("FCM", "Ad loaded");
        seeAds.setClickable(true);
        seeAds.setText("Earn coins");
        adRetry = 0;
        refreshCoinMethod();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
//        Log.v("FCM", "Ad closed");
        pbSeeAds.setVisibility(View.GONE);
        seeAds.setClickable(false);
        seeAds.setText("Please wait...");
//        refreshCoinMethod();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        SharedPreferences.Editor editor;
//        Log.v("FCM", "You got rewards");
        int rewardCoin = rewardItem.getAmount();
        coinValue = coinValue + rewardCoin;
        preferences = getActivity().getSharedPreferences("CoinUpdate", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("coinValue", coinValue);
        editor.putBoolean("coinUpdateFlag", coinUpdateFailedFlag);
        editor.commit();
//        Log.v("FCM", "Reward coin is: " + String.valueOf(coinValue));
        updateTheCoinMethod(coinValue);
        if (coinUpdateFailedFlag) {
            //coin update failed store the value in shared pref and retry
            preferences = getActivity().getSharedPreferences("CoinUpdate", Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.putInt("coinValue", coinValue);
            editor.putBoolean("coinUpdateFlag", coinUpdateFailedFlag);
            editor.commit();
//            updateTheCoinMethod(coinValue);
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
//        Log.v("FCM", "Ad failed to load");
        Toast.makeText(getActivity(), "Ads failed to load", Toast.LENGTH_LONG).show();
        seeAds.setText("Restart the app again");
        seeAds.setVisibility(View.GONE);
        seeAdsRetry.setVisibility(View.VISIBLE);
        pbSeeAds.setVisibility(View.GONE);
        seeAdsRetry.setClickable(true);
        //retry 3 time
//        adRetry = 2;
//        if (adRetry >= 0) {
//            seeAds.setText("Retrying..");
//            adRetry--;
//            loadRewardedVideoAd();
//
//        } else {
//            seeAds.setText("Restart the app again");
//        }
    }

    //task to refresh coins
    private class CoinUpdateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            coinRefreshPb.setVisibility(View.VISIBLE);
            seeAds.setClickable(false);
            enableEmail.setClickable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String encodedParams = strings[1];
            String response = HttpManager.readWrite(encodedParams, url);
            if (response != null)
                response = response.trim();
            else
                response = "";
//            Log.v("FCM", "Response " + response);
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            //housekeeping task
            coinRefreshPb.setVisibility(View.GONE);
            seeAds.setClickable(true);
            enableEmail.setClickable(true);
            //ends

            //parsing response
            if (s.equals("")) {
                //network problem
                Toast.makeText(getActivity(), "Network problem. Try again later", Toast.LENGTH_LONG).show();
            } else if (s.equals("x")) {
                //no data is recieved from server
                Toast.makeText(getActivity(), "Server problem. Try again or contact the developer", Toast.LENGTH_LONG).show();
            } else {
                //data is recieved
                coinValue = Integer.parseInt(s);
                if (getActivity() == null || isCancelled()) {
//                    Log.v("FCM", "activity is destroyed");
                } else {
                    SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("CoinUpdate", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("coinUpdateFlag", coinUpdateFailedFlag);
                    editor.putInt("coinValue", coinValue);
                    editor.commit();
                    //set the value of coin
                    updateCoinInUI();
//                    Toast.makeText(getActivity(), "Coin refreshed", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void updateCoinInUI() {
        coinCount.setText(String.valueOf(coinValue));
    }

    private void enableBackPressed(Boolean flag) {
        if (flag) {

        }
    }

    //task to enable email
    private class EmailEnableTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            enableEmail.setClickable(false);
            enableEmailPb.setVisibility(View.VISIBLE);
            seeAds.setClickable(false);
            refreshCoins.setClickable(false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0];
            String encodedParams = strings[1];
            String response = HttpManager.readWrite(encodedParams, url);
            if (response != null)
                response = response.trim();
            else
                response = "";

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            enableEmail.setClickable(true);
            enableEmailPb.setVisibility(View.GONE);
            seeAds.setClickable(true);
            refreshCoins.setClickable(true);

            if (s.equals("x")) {
                //enabled
                intEnableEmail = 0;
                SharedPreferences preferences = getActivity().getSharedPreferences("EmailEnable", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("enable", intEnableEmail);
                editor.commit();
                emailEnabledUi(intEnableEmail);
            } else if (s.equals("y")) {
                intEnableEmail = 1;
                SharedPreferences preferences = getActivity().getSharedPreferences("EmailEnable", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("enable", intEnableEmail);
                editor.commit();
                emailEnabledUi(intEnableEmail);
            } else if (s.equals("a")) {
                //already enabled
                intEnableEmail = 0;
                SharedPreferences preferences = getActivity().getSharedPreferences("EmailEnable", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("enable", intEnableEmail);
                editor.commit();
                emailEnabledUi(intEnableEmail);

            } else if (s.equals("b")) {
                intEnableEmail = 1;
                SharedPreferences preferences = getActivity().getSharedPreferences("EmailEnable", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("enable", intEnableEmail);
                editor.commit();
                emailEnabledUi(intEnableEmail);
            } else {
                //error
                Toast.makeText(getActivity(), "Server problem. Try again or contact the developer", Toast.LENGTH_LONG).show();
            }

        }
    }

    //task to update coins
    private class CoinUpdateUpdateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //make buttons unclickable until updated
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String encodedParams = strings[1];
            String response = HttpManager.readWrite(encodedParams, url);
            if (response != null) {
                response = response.trim();
            } else
                response = "";
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            if (getActivity().isDestroyed()) {

            } else if (s.equals("p")) {
                //coin updated successfully
                Toast.makeText(getActivity(), "Coins updated successfully", Toast.LENGTH_LONG).show();
                coinUpdateFailedFlag = false;
                preferences = getActivity().getSharedPreferences("CoinUpdate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("coinUpdateFlag", coinUpdateFailedFlag);
                editor.putInt("coinValue", coinValue);
                editor.commit();
                refreshCoinMethod();
            } else {
                coinUpdateFailedFlag = true;
                preferences = getActivity().getSharedPreferences("CoinUpdate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("coinUpdateFlag", coinUpdateFailedFlag);
                editor.putInt("coinValue", coinValue);
                editor.commit();
                refreshCoinMethod();
                Toast.makeText(getActivity(), "Unable to update. Restart the application to see changes or contact the developer", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void emailEnabledUi(int intEnableEmail) {
        if (intEnableEmail == 0) {
            enableEmail.setText("Click to disable");
        } else if (intEnableEmail == 1) {
            enableEmail.setText("Click to enable");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
//            Log.v("FCM", "Asynctask stop is called");
            task.cancel(true);
        }
    }

    //    public static void updateTheList(String message) {
//        messageList.add(message);
//        //call to draw recycler view again?
//        adapter.notifyDataSetChanged();
//
//    }
}

//class TeacherStudentFragmentAdapter extends RecyclerView.Adapter<TeacherStudentFragmentAdapter.ViewHolder> {
//
//    private Context context;
//    private List<String> messageList;
//
//    TeacherStudentFragmentAdapter(Context context, List<String> messageList) {
//        this.context = context;
//        this.messageList = messageList;
//    }
//
//    @Override
//    public TeacherStudentFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.teacher_student_single_row, parent, false);
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(TeacherStudentFragmentAdapter.ViewHolder holder, int position) {
//        holder.messageContent.setText(messageList.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return messageList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView name, messageContent;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.tvStudentRequestName);
//            messageContent = itemView.findViewById(R.id.tvStudentRequestSubject);
//        }
//    }
//}

