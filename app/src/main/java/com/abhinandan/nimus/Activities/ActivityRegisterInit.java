package com.abhinandan.nimus.Activities;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.abhinandan.nimus.Helper.BitmapFunctions;
import com.abhinandan.nimus.Models.Team;
import com.abhinandan.nimus.Models.User;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Utils.ImagePickerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

//import static com.NiMusOrg.NiMus.main.Activities.test.REQUEST_IMAGE;

public class ActivityRegisterInit extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 100;
    CheckBox CheckBox;
    ImageView Heart;
    EditText Fullname,Username, University;
    Button Save, Skip;
    String fullname;
    String username;
    String university;
    FirebaseAuth mAuth;
    FirebaseUser user;
    CircleImageView ProfileImage;
    CardView progresscardView;
    ProgressBar progressBar;
    LottieAnimationView LottieAnimation;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mref;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_init);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey, this.getTheme()));
        }


        Heart = findViewById(R.id.heartSignup);
        CheckBox = findViewById(R.id.checkbox);
        Fullname = findViewById(R.id.fullnameInit);
        Username= findViewById(R.id.usernameInit);
        Save = findViewById(R.id.save);
        Skip = findViewById(R.id.skip);
        ProfileImage = findViewById(R.id.profileimage);
        progressBar = findViewById(R.id.ProgressBarInit);
        progresscardView = findViewById(R.id.progressCardView);
        LottieAnimation = findViewById(R.id.lottieHeartSignup);
        //University = findViewById(R.id.universityInit);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mDatabase= FirebaseDatabase.getInstance();
        mref = mDatabase.getReference();


        if ((Objects.requireNonNull(user).getPhotoUrl() != null)) {
            Glide
                    .with(ActivityRegisterInit.this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(getResources().getDrawable(R.drawable.header, getApplication().getTheme()))
                    .into(ProfileImage);

        }

        if (Objects.requireNonNull(user).getDisplayName() != null) {
            Fullname.setText(user.getDisplayName());
        }


        ProfileImage.setOnClickListener(view -> Dexter.withActivity(ActivityRegisterInit.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        showImagePickerOptions();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        showSettingsDialog();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check());

        CheckBox.setOnClickListener(view -> {

            if (CheckBox.isChecked()) {
                Heart.setVisibility(View.VISIBLE);
                Heart.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp, getApplication().getTheme()));
                //LottieAnimation.setVisibility(View.VISIBLE);
            } else if (!CheckBox.isChecked()) {
                //LottieAnimation.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp,getApplication().getTheme()));
                Heart.setVisibility(View.VISIBLE);
                Heart.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, getApplication().getTheme()));
            }

        });


        Skip.setOnClickListener(view -> {
            if ((user != null) && (user.isEmailVerified())) {
                Intent intent = new Intent(ActivityRegisterInit.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Save.setOnClickListener(view -> {
            fullname = Fullname.getText().toString();
            username = Username.getText().toString();
            //university = University.getText().toString();
            if (CheckBox.isChecked()) {
                progresscardView.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(fullname)||(TextUtils.isEmpty(username))) {
                    Toast.makeText(ActivityRegisterInit.this, "A field is empty", Toast.LENGTH_SHORT).show();
                    CheckBox.setChecked(false);
                    Heart.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, getApplication().getTheme()));
                } else {

                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullname)
                            .build();
                    user.updateProfile(userProfileChangeRequest).addOnCompleteListener(task -> {
                        task.addOnSuccessListener(aVoid -> {

                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), user.getPhotoUrl());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            String image = BitmapFunctions.BitMapToString(bitmap);

                            User mUser = new User("",user.getEmail(),user.getDisplayName(),username,user.getUid(),image,"","", new Team());


                            mref.child("users").child(user.getEmail().replace(".com","dotcom")).setValue(mUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ActivityRegisterInit.this, "Data saved Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityRegisterInit.this, "Error in saving data "+ e, Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(ActivityRegisterInit.this, "Fullname updated!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityRegisterInit.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });

                        task.addOnFailureListener(e -> Toast.makeText(ActivityRegisterInit.this, "Failed to update fullname " + e, Toast.LENGTH_SHORT).show());
                    });
                }
            } else {
                Toast.makeText(ActivityRegisterInit.this, "Accept the terms and conditions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfile(Uri url) {
        Glide
                .with(ActivityRegisterInit.this)
                .load(url)
                .centerCrop()
                .placeholder(getResources().getDrawable(R.drawable.header, getApplication().getTheme()))
                .into(ProfileImage);


        progressBar.setVisibility(View.VISIBLE);
        if (url != null) {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(task -> {
                task.addOnSuccessListener(aVoid -> {
                    Toast.makeText(ActivityRegisterInit.this, "Profile photo updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Glide
                            .with(ActivityRegisterInit.this)
                            .load(url)
                            .centerCrop()
                            .placeholder(getResources().getDrawable(R.drawable.header, getApplication().getTheme()))
                            .into(ProfileImage);
                });

                task.addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivityRegisterInit.this, "Failed Upload to Profile image " + e, Toast.LENGTH_SHORT).show();
                });
            });

        }
        else{
            Toast.makeText(this, "url is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ActivityRegisterInit.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }




    private void launchGalleryIntent() {
        Intent intent = new Intent(ActivityRegisterInit.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = Objects.requireNonNull(data).getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    loadProfile(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRegisterInit.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}
