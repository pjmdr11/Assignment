package prajwal.lftassignment.com.leapfrogassignment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DecimalFormat;

import prajwal.lftassignment.com.leapfrogassignment.Helpers.DateUtils;
import prajwal.lftassignment.com.leapfrogassignment.Helpers.HttpHelper;
import prajwal.lftassignment.com.leapfrogassignment.Helpers.Preferences;
import prajwal.lftassignment.com.leapfrogassignment.Helpers.Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername, etPassword, etEmail;
    private ImageView ivProfileImage;
    private Button btnSignUp, btnCancel;
    private ProgressDialog progressDialog;
    private RelativeLayout imageSelectionLayout;

    private AnimationDrawable animationDrawable;
    private RelativeLayout layoutMain;

    private String selectedImagePath = "";
    private Uri downloadImageUrl;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Preferences prefs;
    private UserProfileChangeRequest userProfileChangeRequest;
    private StorageReference mStorageReferences;


    private final int IMG_REQ_CODE = 120;
    private final int EXT_STORAGE_PERMISSION = 200;
    private final int EXT_STORAGE_PERMISSION_REQUEST = 201;

    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        etUsername = (EditText) findViewById(R.id.et_username_registration);
        etPassword = (EditText) findViewById(R.id.et_password_registration);
        etEmail = (EditText) findViewById(R.id.et_email_registration);
        btnSignUp = (Button) findViewById(R.id.btn_register);
        btnCancel = (Button) findViewById(R.id.btn_cancel_registration);
        ivProfileImage = (ImageView) findViewById(R.id.iv_profile_image);
        imageSelectionLayout = (RelativeLayout) findViewById(R.id.layout_addimage_registration);

        layoutMain = (RelativeLayout) findViewById(R.id.layout_main_register);

        animationDrawable = (AnimationDrawable) layoutMain.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);


        prefs = new Preferences(getApplicationContext());
        decimalFormat = new DecimalFormat("00.0");

        auth = FirebaseAuth.getInstance();

        mStorageReferences = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(RegisterActivity.this);

        btnSignUp.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ivProfileImage.setOnClickListener(this);
        validateImageView();

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utils.validateInput(etUsername);
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

    }

    private void validateImageView() {
        if (selectedImagePath.equals("")) {
            imageSelectionLayout.setVisibility(View.VISIBLE);
        } else {
            imageSelectionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (Utils.validateEmail(etEmail) && Utils.validateInput(etUsername)
                        && Utils.validateInput(etPassword) &&
                         HttpHelper.getInternetStatus(getApplicationContext())) {
                    registerUser();
                }
                break;

            case R.id.btn_cancel_registration:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.iv_profile_image:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    getImagePicker();
                } else {
                    if (prefs.getPermissionStatus()) {
                        new AlertDialog.Builder(RegisterActivity.this).setTitle("No Permissions Provided").
                                setMessage("Please provide permission to read External Storage.").
                                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, EXT_STORAGE_PERMISSION_REQUEST);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    } else {
                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXT_STORAGE_PERMISSION);
                    }

                }

                break;
        }
    }

    private void getImagePicker() {
        Toast.makeText(getApplicationContext(), "Please Select an Image to upload", Toast.LENGTH_LONG).show();
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(imagePickerIntent, IMG_REQ_CODE);

    }



    private void registerUser() {
        progressDialog.setMessage("Registering User.Please wait..");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).
                addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = task.getResult().getUser();
                            try {
                                Log.d("Registration", "Registration Succesfull");
                                uploadImage();
                            } catch (Exception e) {

                            }
                        } else {
                            Log.e("REGISTRATIONEXCEPTION", task.getException().getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void uploadImage() {
        if (!selectedImagePath.equals("")) {
            Uri file = Uri.fromFile(new File(selectedImagePath));
            Log.d("StorageReference", "images/" + firebaseUser.getUid() + DateUtils.getDate() + ".jpg");
            mStorageReferences = mStorageReferences.child("images/" + firebaseUser.getUid() + "_" + DateUtils.getDate() + ".jpg");
            mStorageReferences.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Image Upload Completed", Toast.LENGTH_LONG).show();
                    downloadImageUrl = taskSnapshot.getDownloadUrl();
                    updateUserInfo();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    downloadImageUrl = null;
                    updateUserInfo();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploading Image: " + decimalFormat.format(progress) + "%...");
                }
            });
        } else {
            downloadImageUrl = null;
            updateUserInfo();
        }


    }

    private void updateUserInfo() {
        progressDialog.setMessage("Finalizing.Please Wait...");
        if (downloadImageUrl != null) {
            userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(etUsername.getText().toString())
                    .setPhotoUri(downloadImageUrl).build();
        } else {
            userProfileChangeRequest = new UserProfileChangeRequest.Builder().
                    setDisplayName(etUsername.getText().toString()).build();
        }

        firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Profile update successfull", Toast.LENGTH_LONG).show();
                    Intent dashBoard = new Intent(RegisterActivity.this, DashBoardActivity.class);
                    startActivity(dashBoard);
                    progressDialog.dismiss();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong.Please Try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_REQ_CODE) {
                Uri imageUri = data.getData();
                selectedImagePath = Utils.getRealPathFromURI(RegisterActivity.this,imageUri);
                Log.e("IMAGEPATH", selectedImagePath);
                Glide.with(getApplicationContext()).load(selectedImagePath).into(ivProfileImage);
            }
        } else if (requestCode == EXT_STORAGE_PERMISSION_REQUEST) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getImagePicker();

            }
        }
        validateImageView();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
        progressDialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXT_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImagePicker();
            } else {
                Boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (!showRationale) {
                    prefs.setPERMISSION_STRICTLY_DENIED(true);
                    // user also CHECKED "never ask again"

                } else {
                    prefs.setPERMISSION_STRICTLY_DENIED(false);
                    // user did NOT check "never ask again"
                    // this is a good place to explain the user
                    // why you need the permission and ask if he wants
                    // to accept it (the rationale)
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
