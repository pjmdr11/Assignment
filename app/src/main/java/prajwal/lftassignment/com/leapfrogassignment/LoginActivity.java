package prajwal.lftassignment.com.leapfrogassignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import prajwal.lftassignment.com.leapfrogassignment.Helpers.HttpHelper;
import prajwal.lftassignment.com.leapfrogassignment.Helpers.ProgressIdlingResource;
import prajwal.lftassignment.com.leapfrogassignment.Helpers.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthenticationListener;
    private FirebaseUser firebaseUser;

    private EditText etEmail, etPassword;
    private Button btnSignin, btnRegister;
    private LinearLayout layoutSignIn;


    private AnimationDrawable animationDrawable;
    private RelativeLayout layoutMain;
    private String TAG = "Debug";
    private ProgressDialog progressDialog;

    private ProgressListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        etEmail = (EditText) findViewById(R.id.et_email_login);
        etPassword = (EditText) findViewById(R.id.et_password_login);
        layoutSignIn = (LinearLayout) findViewById(R.id.layout_signin);
        btnSignin = (Button) findViewById(R.id.btn_signin_login);
        btnRegister = (Button) findViewById(R.id.btn_register_login);
        layoutMain = (RelativeLayout) findViewById(R.id.layout_main_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging-in..");
        progressDialog.setCancelable(false);

        animationDrawable = (AnimationDrawable) layoutMain.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);


        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(this);
        btnSignin.setOnClickListener(this);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utils.validateEmail(etEmail);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utils.validateInput(etPassword);
            }
        });
        mAuthenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                progressDialog.dismiss();
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //user is signed in
                    Log.d(TAG, "Firebaseuser: Logged In");

                    Intent j = new Intent(LoginActivity.this, DashBoardActivity.class);
                    startActivity(j);
                    finish();


                } else {
                    layoutSignIn.setVisibility(View.VISIBLE);
                    //user is signed out
                    Log.d(TAG, "Firebaseuser: Logged Out");
                }
            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthenticationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthenticationListener != null) {
            mAuth.removeAuthStateListener(mAuthenticationListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin_login:

                if (HttpHelper.getInternetStatus(getApplicationContext())
                        && Utils.validateEmail(etEmail)
                        && Utils.validateInput(etPassword)) {
                    progressDialog.show();
                    notifyListener(mListener, true);
                    mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    notifyListener(mListener, false);
                                    if (task.isSuccessful()) {
                                        Intent x = new Intent(LoginActivity.this, DashBoardActivity.class);
                                        startActivity(x);
                                        finish();
                                    } else {
                                        Log.e("LOGIN ERROR", task.getException().toString());
                                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }


                break;

            case R.id.btn_register_login:
                Intent registerUser = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerUser);
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }


    public void setProgressListener(ProgressListener progressListener) {
        mListener = progressListener;
    }

    private void notifyListener(ProgressListener listener, Boolean progressShowing) {
        if (listener == null) {
            return;
        }
        if (progressShowing) {
            listener.onProgressShown();
        } else {
            listener.onProgressDismissed();
        }
    }

    public interface ProgressListener {
        public void onProgressShown();

        public void onProgressDismissed();
    }

    public boolean isInProgress() {
        // return true if progress is visible
        if (progressDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }
}
