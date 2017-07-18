package prajwal.lftassignment.com.leapfrogassignment;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import prajwal.lftassignment.com.leapfrogassignment.Helpers.Preferences;
import prajwal.lftassignment.com.leapfrogassignment.Helpers.Utils;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivProfile, ivEditDisplayName;
    private TextView tvUserName, tvEmail, tvLogOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthenticationListener;
    private UserProfileChangeRequest userProfileChangeRequest;
    private StorageReference mStorageReferences;

    private String userName = "", emailAddress = "", displayName = "";
    private Uri imagePath;
    private ImageView ivBack;
    private Preferences prefs;
    private String selectedImagePath = "";
    private Uri downloadImageUrl;

    private ProgressDialog progressDialog;
    private DecimalFormat decimalFormat;

    private final int EXT_STORAGE_PERMISSION_REQUEST = 220;
    private final int IMG_REQ_CODE = 202;
    private final int EXT_STORAGE_PERMISSION = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        tvUserName = (TextView) findViewById(R.id.tv_username_profile);
        tvEmail = (TextView) findViewById(R.id.tv_email_profile);
        ivBack = (ImageView) findViewById(R.id.iv_back_profile);
        ivEditDisplayName = (ImageView) findViewById(R.id.iv_edit_displayname);
        tvLogOut = (TextView) findViewById(R.id.tv_logout_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        prefs = new Preferences(getApplicationContext());

        progressDialog = new ProgressDialog(ProfileActivity.this);
        decimalFormat = new DecimalFormat("00.0");

        ivBack.setOnClickListener(this);
        tvLogOut.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivEditDisplayName.setOnClickListener(this);

        mAuthenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                } else {
                    firebaseUser = firebaseAuth.getCurrentUser();

                    emailAddress = firebaseUser.getEmail().toString();
                    mStorageReferences = FirebaseStorage.getInstance().getReference();
                    if (firebaseUser.getDisplayName() != null) {
                        userName = firebaseUser.getDisplayName().toString();
                    } else {
                        userName = "-";
                    }
                    if (firebaseUser.getPhotoUrl() != null) {
                        imagePath = firebaseUser.getPhotoUrl();
                    }
                    setupProfile();
                }
            }
        };


    }

    private void setupProfile() {
        if (!userName.equals("")) {
            tvUserName.setText(userName);
        } else {
            tvUserName.setText("-");
        }

        if (emailAddress != null && !emailAddress.equals("")) {
            tvEmail.setText(emailAddress);
        } else {
            tvEmail.setText("-");
        }

        Glide.with(getApplicationContext()).load(imagePath).asBitmap().placeholder(R.drawable.ic_person_grey).diskCacheStrategy(DiskCacheStrategy.RESULT).
                placeholder(R.drawable.ic_person_grey).into(new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivProfile.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                ivProfile.setImageResource(R.drawable.ic_person_grey);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_profile:
                finish();
                break;

            case R.id.tv_logout_profile:
                firebaseAuth.getInstance().signOut();
                break;

            case R.id.iv_profile:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    getImagePicker();
                } else {
                    if (prefs.getPermissionStatus()) {
                        new AlertDialog.Builder(ProfileActivity.this).setTitle("No Permissions Provided").
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
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, EXT_STORAGE_PERMISSION);
                    }

                }
                break;

            case R.id.iv_edit_displayname:
                updateDisplayName();
                break;
        }
    }

    private void updateDisplayName() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Please set Display Name.");
        LinearLayout mLayout = new LinearLayout(ProfileActivity.this);
        final EditText et = new EditText(ProfileActivity.this);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et.setPadding(10, 15, 10, 5);

        if (firebaseUser.getDisplayName() != null)
            et.setText(firebaseUser.getDisplayName().toString());
        else
            et.setHint("Username");


        mLayout.addView(et);
        alertDialog.setView(mLayout);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utils.validateInput(et)) {
                    downloadImageUrl = null;
                    displayName = et.getText().toString();
                    updateUserInfo();
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void getImagePicker() {
        Toast.makeText(getApplicationContext(), "Please Select an Image to upload", Toast.LENGTH_LONG).show();
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(imagePickerIntent, IMG_REQ_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_REQ_CODE) {
                Uri imageUri = data.getData();
                selectedImagePath = Utils.getRealPathFromURI(ProfileActivity.this, imageUri);
                Log.e("IMAGEPATH", selectedImagePath);
                Glide.with(getApplicationContext()).load(selectedImagePath).into(ivProfile);
                uploadImage();
            }
        } else if (requestCode == EXT_STORAGE_PERMISSION_REQUEST) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getImagePicker();

            }
        }

    }

    private void uploadImage() {
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploading Image: " + decimalFormat.format(progress) + "%...");
                }
            });
        }

    }

    private void updateUserInfo() {
        if (downloadImageUrl != null) {
            userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadImageUrl).build();
            progressDialog.setMessage("Finalizing ... ");
        } else {
            userProfileChangeRequest = new UserProfileChangeRequest.Builder().
                    setDisplayName(displayName).build();
        }

        firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Profile update successfull", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    setupProfile();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthenticationListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthenticationListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthenticationListener);
    }


}
