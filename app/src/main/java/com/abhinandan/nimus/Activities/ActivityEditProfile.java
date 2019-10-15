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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Models.User;
import com.abhinandan.nimus.Utils.ImagePickerActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityEditProfile extends AppCompatActivity {

    private String email, fullname,uid,bio,username;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String imageUri;
    private EditText name,Email,Bio,Username;
    private CircleImageView image;
    private ProgressBar progress;
    private static  final String TAG = ActivityEditProfile.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Toast.makeText(ActivityEditProfile.this, "Fetching data.....", Toast.LENGTH_SHORT).show();
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        final ImageView Back;
        final TextView Save;
        final CardView loader, loader2;





        Back = findViewById(R.id.ImageBackEditProfile);
        name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        image = findViewById(R.id.ProfileImgViewEditProfile);
        Save = findViewById(R.id.Save);
        loader = findViewById(R.id.progressCardViewEP);
        loader2 = findViewById(R.id.progressCardViewEP2);
        progress = findViewById(R.id.ProgressBarEdit);
        Bio = findViewById(R.id.TextBio);
        Username = findViewById(R.id.Textusername);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();






        fullname = user.getDisplayName();


        //Init(user);

        if(fullname.isEmpty()){
            fullname = fullname + "Please add a fullname";
            Toast.makeText(ActivityEditProfile.this, fullname, Toast.LENGTH_SHORT).show();
        }
        //SetProfile(image,name, fullname,imageUri,Email,email);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User mUser = dataSnapshot.child("users").child(user.getEmail().replace(".com","dotcom")).getValue(User.class);


                if(mUser != null){
                    bio = mUser.getBio();
                    username = mUser.getUser_name();
                    fullname = mUser.getFull_name();
                    email = mUser.getEmail();
                    imageUri = mUser.getUrl();


                    if(bio.isEmpty()){
                        Bio.setText("No Bio Added Yet");
                    }
                    if(!bio.isEmpty()){
                        Bio.setText(bio);
                    }
                    if(username.isEmpty()){
                        Username.setText("Please Add a Username for yourself");
                    }
                    if(!username.isEmpty()){
                        Username.setText(username);
                    }

                    if(!fullname.isEmpty()){
                        name.setText(mUser.getFull_name());
                    }
                    else {
                        name.setText("Please add a fullname");
                    }

                    if(!email.isEmpty()){
                        Email.setText(email);
                    }
                    else{
                        Email.setText("Your mail will be displayed here");
                    }


                    if(!imageUri.isEmpty()){
                        Glide.with(ActivityEditProfile.this)
                                .load(StringToBitMap(imageUri))
                                .placeholder(R.drawable.header)
                                .into(image);
                    }
                    else{
                        Toast.makeText(ActivityEditProfile.this, "Image Url Empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ActivityEditProfile.this, "user object is Empty", Toast.LENGTH_SHORT).show();
                }

                loader2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ActivityEditProfile.this, "data fetching Error", Toast.LENGTH_SHORT).show();
            }
        });



        Save.setOnClickListener(view -> {

            View view1 = this.getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(ActivityEditProfile.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


            name.setFocusable(true);
            name.setFocusableInTouchMode(true);
            Bio.setFocusable(true);
            Bio.setFocusableInTouchMode(true);
            Username.setFocusable(true);
            Username.setFocusableInTouchMode(true);
            fullname = name.getText().toString();
            bio = Bio.getText().toString();
            username = Username.getText().toString();

                loader.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(fullname)) {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(ActivityEditProfile.this, "Full name is empty", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(bio)){
                    loader.setVisibility(View.GONE);
                    Toast.makeText(ActivityEditProfile.this, "Bio is empty", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(username)){
                    loader.setVisibility(View.GONE);
                    Toast.makeText(ActivityEditProfile.this, "username is empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    mRef.child("users").child(user.getEmail().replace(".com","dotcom")).child("bio").setValue(bio).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(ActivityEditProfile.this, "Bio Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityEditProfile.this, "Bio can't be updated, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

                    mRef.child("users").child(user.getEmail().replace(".com","dotcom")).child("user_name").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(ActivityEditProfile.this, "user name Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityEditProfile.this, "user name can't be updated, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mRef.child("users").child(user.getEmail().replace(".com","dotcom")).child("full_name").setValue(fullname).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(ActivityEditProfile.this, "fullname Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityEditProfile.this, "fullname can't be updated, please try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullname)
                            .build();
                    user.updateProfile(userProfileChangeRequest).addOnCompleteListener(task -> {
                        task.addOnSuccessListener(aVoid -> {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(ActivityEditProfile.this, "Updated!", Toast.LENGTH_SHORT).show();
                        });

                        task.addOnFailureListener(e ->
                                Toast.makeText(ActivityEditProfile.this, "Failed to update fullname " + e, Toast.LENGTH_SHORT).show());
                                loader.setVisibility(View.GONE);
                    });
                }
        });


        Back.setOnClickListener(view->{

            Intent intent = new Intent(ActivityEditProfile.this,MainActivity.class);
            intent.putExtra("val",1);
            startActivity(intent);
        });

        image.setOnClickListener(view -> Dexter.withActivity(ActivityEditProfile.this)
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
    }

    private void Init(FirebaseUser User){
        if((Objects.requireNonNull(User).getDisplayName()!= null)){
            fullname = User.getDisplayName();
        }
        else{
            Log.d(TAG, "Init: fullname or user empty");
            name.setText("Display Name");
        }
    }

    private void SetProfile(CircleImageView image, EditText name, String dname, Uri link, EditText email, String Email){

        name.setText(dname);
        email.setText(Email);
        Glide
                .with(ActivityEditProfile.this)
                .load(link)
                .centerCrop()
                .placeholder(R.drawable.header)
                .into(image);
    }

    private void showImagePickerOptions() {
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
        Intent intent = new Intent(ActivityEditProfile.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(ActivityEditProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditProfile.this);
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

    private void loadProfile(Uri url) throws IOException {

        Glide
                .with(ActivityEditProfile.this)
                .load(url)
               // .centerCrop()
                .placeholder(getResources().getDrawable(R.drawable.header, getApplication().getTheme()))
                .into(image);


        progress.setVisibility(View.VISIBLE);
        if (url != null) {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), url);

            String s = BitMapToString(bitmap);

            mRef.child("users").child(user.getEmail().replace(".com","dotcom")).child("url").setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ActivityEditProfile.this, "Stored Bitmap", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityEditProfile.this, "Bitmap cant be Stored", Toast.LENGTH_SHORT).show();
                }
            });


            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(task -> {
                task.addOnSuccessListener(aVoid -> {
                    Toast.makeText(ActivityEditProfile.this, "Profile photo updated", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    Glide
                            .with(ActivityEditProfile.this)
                            .load(url)
                           // .centerCrop()
                            .placeholder(getResources().getDrawable(R.drawable.header, getApplication().getTheme()))
                            .into(image);
                });

                task.addOnFailureListener(e -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(ActivityEditProfile.this, "Failed Upload to Profile image " + e, Toast.LENGTH_SHORT).show();
                });
            });

        }
        else{
            Toast.makeText(this, "url is null", Toast.LENGTH_SHORT).show();
        }
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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
