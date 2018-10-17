package com.inspiregeniussquad.handstogether.appActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class SignupActivity extends SuperCompatActivity {

    @BindView(R.id.email)
    TextInputEditText emailEd;

    @BindView(R.id.name)
    TextInputEditText nameEd;

    @BindView(R.id.radio_grp)
    RadioGroup radioGrp;

    @BindView(R.id.male_rb)
    RadioButton maleRdBtn;

    @BindView(R.id.female_rb)
    RadioButton femaleRdBtn;

    @BindView(R.id.register_me)
    AppCompatButton registerMeBtn;

    @BindView(R.id.login_me)
    TextView loginMeBtn;

    private static final int GMAIL_SIGNIN = 333;
    private GoogleApiClient googleApiClient;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //disabling auto focus of edittext
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //getting mobile number
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getStringExtra("mobile");
        } else {
            mobileNumber = "6380995858";
        }

        //setting up clients for email auto-retrival process
        setUpGoogle();
    }

    @OnClick({R.id.login_me, R.id.register_me, R.id.female_rb, R.id.male_rb, R.id.email})
    public void onclicked(View view) {
        switch (view.getId()) {
            case R.id.login_me:
                goTo(this, MobileNumberActivity.class, true);
                break;
            case R.id.register_me:
                //getting all necessary data from UI

                String email = emailEd.getText().toString().trim();
                String name = nameEd.getText().toString().trim();
                String gender = maleRdBtn.isChecked() ? "Male" : femaleRdBtn.isChecked() ? "Female" : "";

                showProgress(getString(R.string.registering_you));
                //function to validate and register user
                checkAndRegister(email, name, gender);
                break;
            case R.id.female_rb:
                maleRdBtn.setChecked(false);
                femaleRdBtn.setChecked(true);
                break;
            case R.id.male_rb:
                femaleRdBtn.setChecked(false);
                maleRdBtn.setChecked(true);
                break;
            case R.id.email:
                //clear views
                emailEd.setText("");
                nameEd.setText("");

                //function to show email list
                getUserEmail();
                break;
        }
    }

    private void checkAndRegister(String email, String name, String gender) {

        if (TextUtils.isEmpty(email)) {
            cancelProgress();
            showToast(SignupActivity.this, getString(R.string.enter_email));
            return;
        }

        if (TextUtils.isEmpty(name)) {
            cancelProgress();
            showToast(SignupActivity.this, getString(R.string.enter_name));
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            cancelProgress();
            showToast(SignupActivity.this, getString(R.string.choose_gender));
            return;
        }

        writeUserDataDb(email, name, mobileNumber, gender);
    }

    private int nextUserId;

    private void writeUserDataDb(final String email, final String name, final String mobileNumber, final String gender) {

        final DatabaseReference usersDatabaseReference = getReferenceFromDatabase(Keys.TABLE_USER_ID).child(Keys.USER_ID);

        usersDatabaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue(int.class) == null) {
                    usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                usersDatabaseReference.setValue(0);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            cancelProgress();
                            AppHelper.showToast(SignupActivity.this, "OnCancelled : doTransaction");
                        }
                    });
                    return Transaction.abort();
                }
                nextUserId = mutableData.getValue(int.class);
                mutableData.setValue(nextUserId + 1);

                AppHelper.print("User id: " + nextUserId);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean state, @Nullable DataSnapshot dataSnapshot) {
//                checkIsExistingUser(mobileNumber, nextUserId, name, email, gender);
                insertIntoDb(name, email, gender, nextUserId, mobileNumber);
                showToast(SignupActivity.this, "Logging you in!");
            }
        });
    }

    private void checkIsExistingUser(final String mobileNumber, final int nextUserId, final String name, final String email, final String gender) {

        DatabaseReference userReference = getReferenceFromDatabase(Keys.TABLE_USER);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot1.getValue(Users.class);

                    if (users != null && !users.getMobile().equals(mobileNumber)) {
                        insertIntoDb(name, email, gender, nextUserId, mobileNumber);
                        showToast(SignupActivity.this, "Logging you in!");
                    } else {
//                        goHome();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void insertIntoDb(final String name, final String email, final String gender, int nextUserId, final String mobileNumber) {

        usersDatabaseReference/*.child(Keys.TABLE_USER)*/
                .child(String.valueOf(nextUserId))
                .setValue(new Users(name, email, mobileNumber, gender))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dataStorage.saveString(Keys.USER_NAME, name);
                            dataStorage.saveString(Keys.MOBILE, mobileNumber);
                            dataStorage.saveString(Keys.USER_EMAIL, email);
                            dataStorage.saveString(Keys.USER_DATA, gson.toJson(new Users(name, email, mobileNumber, gender)));

                            cancelProgress();

                            dataStorage.saveBoolean(Keys.IS_ONLINE, true);
                            goTo(SignupActivity.this, HomeActivity.class, true);
                        } else {
                            cancelProgress();
                            showToast(SignupActivity.this, getString(R.string.some_error_occurred));
                        }
                    }
                });
    }


    private void setUpGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleApiClient.OnConnectionFailedListener googleConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                AppHelper.showToast(SignupActivity.this, getString(R.string.google_conn_failed));

            }
        };

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, googleConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    //getting user all email accounts in phone
    private void getUserEmail() {
        if (googleApiClient.isConnected()) {
            AppHelper.print("Trying to get user email");
            Intent signInIntent =
                    Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, GMAIL_SIGNIN);
        } else {
            AppHelper.showToast(SignupActivity.this, "Google Api client not connected");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AppHelper.print("Result code: " + resultCode);

        if (resultCode == RESULT_OK) {
            if (requestCode == GMAIL_SIGNIN) {
                AppHelper.print("OnActivityResult : gmail sign in");
                //getting email data
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                checkEmails(googleSignInResult);
            }
        }
    }

    //Getting email values and setting it in views
    private void checkEmails(GoogleSignInResult googleSignInResult) {
        AppHelper.print("Checking emails");

        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
            if (googleSignInAccount != null) {
                String name = googleSignInAccount.getDisplayName();
                String email = googleSignInAccount.getEmail();
                String imgUrl = String.valueOf(googleSignInAccount.getPhotoUrl());

                emailEd.setText(email);
                nameEd.setText(name);
                AppHelper.print("Image Url: " + imgUrl);
            } else {
                AppHelper.print("Can't get email id");
            }

            //signing out chosen email
            signOutEmail();
        } else {
            showSnack(getString(R.string.choose_email));
        }
    }

    private void signOutEmail() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    AppHelper.print("Email Signed Out!");
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //connecting to google api client
        if (googleApiClient != null) {
            googleApiClient.connect();
            AppHelper.print("GoogleApi client connected");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //disconnecting google api client
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
            AppHelper.print("GoogleApi client dis connected");
        }
    }
}
