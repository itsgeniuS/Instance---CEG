package com.inspiregeniussquad.handstogether.appActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appBroadcastReceivers.SignalReceiver;
import com.inspiregeniussquad.handstogether.appData.DataStorage;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appInterfaces.Action;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

public class SuperCompatActivity extends AppCompatActivity {

    protected Gson gson = new Gson();

    public FirebaseAuth firebaseAuth;
    public DatabaseReference parentDatabaseReference, childDatabaseReference, usersDatabaseReference;
    public ProgressDialog progressDialog;

    protected DataStorage dataStorage;
    public List<Users> newUser;

    protected Animation scaleUpAnim, scaleDownAnim;

    protected SignalReceiver connectionChangeReceiver;

    protected AlertDialog noInternetDialog;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        //initializing butter knife
        ButterKnife.bind(this);

        //shared preferences data
        dataStorage = new DataStorage(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init firebase instance
        FirebaseApp.initializeApp(this);

        //firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //firebase database
        parentDatabaseReference = FirebaseDatabase.getInstance().getReference();

        childDatabaseReference = parentDatabaseReference.child("newUser");

        newUser = new ArrayList<>();

        usersDatabaseReference = getReferenceFromDatabase("Users");

        //progress view
        progressDialog = new ProgressDialog(this);

        //no internet alert receiver
        connectionChangeReceiver = new SignalReceiver(this, Keys.NO_INTERNET_CONNECTION, new Action() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onConnectionChangeDetected(AppHelper.isNetworkAvailable(context));
            }
        });

        //view for internet connection failure
        View noInternetView = LayoutInflater.from(this).inflate(R.layout.no_internet_view, null);
        AppCompatButton retryBtn = noInternetView.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissInternetAlert();
                onConnectionChangeDetected(AppHelper.isNetworkAvailable(SuperCompatActivity.this));
            }
        });
        noInternetDialog = new AlertDialog.Builder(this).create();
        noInternetDialog.setView(noInternetView);
        noInternetDialog.setCancelable(false);
        Objects.requireNonNull(noInternetDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent));

        //pre-loading animations
        loadAllAnims();
    }

    private void loadAllAnims() {
        scaleDownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down_animation);
        scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up_animatin);
    }

    protected DatabaseReference getReferenceFromDatabase(String tableName) {
        return FirebaseDatabase.getInstance().getReference(tableName);
    }

    private void onConnectionChangeDetected(boolean isInternetAvailable) {

        AppHelper.print("Network result: "+isInternetAvailable);

        if (!isInternetAvailable) {
            showNoInternetAlert();
        } else {
            dismissInternetAlert();
            onRetryClicked();
        }
    }

    private void showNoInternetAlert() {
        if (!noInternetDialog.isShowing() && !isFinishing()) {
            noInternetDialog.show();
        }
    }

    protected void onRetryClicked() {

    }

    private void dismissInternetAlert() {
        if (noInternetDialog != null && noInternetDialog.isShowing()) {
            noInternetDialog.dismiss();
        }
    }


    //for starting activity with single intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null) {
                startActivity(new Intent(from, to).putExtra(key, data));
            }
            if (close) {
                finish();
            }
        }

    }

    //for starting activity with two intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data, String key1, String data1) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null && key1 != null && data1 != null) {
                startActivity(new Intent(from, to).putExtra(key, data).putExtra(key1, data1));
            }
            if (close) {
                finish();
            }
        }

    }

    //for starting activity without intent values
    protected void goTo(Context from, Class to, boolean close) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            startActivity(new Intent(from, to));
            if (close) {
                finish();
            }
        }
    }

    //to show progress view
    protected void showProgress(String msg) {
        if (progressDialog != null) {
            if (!progressDialog.isShowing() && !isFinishing()) {
                progressDialog.setMessage(msg);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
    }

    //to hide showing progress view
    protected void cancelProgress() {
        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showSnack(String msg) {
        final View view = getRootView();
        if (view != null && msg != null) {
            Snackbar snackbar = Snackbar.make(view, msg, /*isLong ? Snackbar.LENGTH_LONG :*/ Snackbar.LENGTH_LONG);

            View snackBarView = snackbar.getView();

            //snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));  /*setting up action bar color*/
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.white)); /*setting background color*/
            ((TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(this, R.color.black)); /*Setting text color*/

            if (!isFinishing()) {
                snackbar.show();
            }
        }
//        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }

    private View getRootView() {
        final ViewGroup contentViewGroup = findViewById(android.R.id.content);
        View rootView = null;

        if (contentViewGroup != null)
            rootView = contentViewGroup.getChildAt(0);

        if (rootView == null)
            rootView = getWindow().getDecorView().getRootView();

        return rootView;
    }

    private void applyEnterAnimation() {
        overridePendingTransition(R.anim.slide_in_anim, R.anim.slide_out_anim);
    }

    private void applyExitAnimation() {
        overridePendingTransition(R.anim.slide_out_anim, R.anim.slide_in_anim);
    }

    private void registerReceivers() {
        connectionChangeReceiver.register();
    }

    private void unRegisterReceivers() {
        connectionChangeReceiver.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //registering broadcast receivers
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //removing broadcast receivers
        unRegisterReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

//        applyEnterAnimation();
    }

    @Override
    public void finish() {
        super.finish();

//        applyExitAnimation();
    }

    protected void showToast(Context context, String msg) {
        if (context != null && context.getResources() != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

}