package com.abhinandan.nimus.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Models.User;
import com.bumptech.glide.Glide;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ActivityQRcode extends AppCompatActivity {
    private ImageView back;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String email;
    private String name;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView qrname,qrtitle,qrdate,qrduration,qrtime,qrhighlights;
    private String title;
    private String startdate;
    private String enddate;
    private String date;
    private String highlights;
    private String dateTwo;
    private String time;
    private AppCompatImageView QRImage;
    private Bitmap bitmap;
    private TextView save;
    private CardView progressBar;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBackground));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        email = user.getEmail();
        title = getIntent().getStringExtra("title");
        startdate = getIntent().getStringExtra("startdate");
        enddate = getIntent().getStringExtra("enddate");
        highlights = getIntent().getStringExtra("hMain")+ ", " + getIntent().getStringExtra("hOne") + ", " + getIntent().getStringExtra("hTwo") + ", " + getIntent().getStringExtra("hThree");
        time = getIntent().getStringExtra("startTime");
        dateTwo = getIntent().getStringExtra("startdateTwo");
        enddate = enddate.substring(1);
        date = startdate+ " - " + enddate;

        back = findViewById(R.id.ImageBackEditProfile);
        qrname = findViewById(R.id.QrUsername);
        qrtitle = findViewById(R.id.QRTitle);
        qrduration = findViewById(R.id.QRDuration);
        QRImage = findViewById(R.id.Qr_imagePlaceholder);
        qrdate = findViewById(R.id.QrDate);
        qrtime = findViewById(R.id.QRTime);
        qrhighlights = findViewById(R.id.QRhighlights);
        save =  findViewById(R.id.SaveQR);
        progressBar = findViewById(R.id.progressCardViewQR);
        mainLayout = findViewById(R.id.mainViewQR);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               saveImage(bitmap,user.getDisplayName());
                new iOSDialogBuilder(ActivityQRcode.this)
                        .setTitle("Image Saved!")
                        .setSubtitle("Check for image named 'QR-"+user.getDisplayName()+".jpg'")
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener("OK", iOSDialog::dismiss)
                        .build().show();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.child("users").child(user.getUid()).getValue(User.class);

                if(mUser!= null){
                    name = mUser.getFull_name();
                    email = mUser.getEmail();


                    if(name.isEmpty()){
                        qrname.setText("No value found");
                        Toast.makeText(ActivityQRcode.this, "Empty value", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        qrname.setText(name);
                    }
                }

                else{
                    Toast.makeText(ActivityQRcode.this, "User Object is null", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActivityQRcode.this, "Error generating your Ticket!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }
        });

        qrtitle.setText(title);
        qrduration.setText(date);
        qrtime.setText(time);
        qrdate.setText(dateTwo);
        qrhighlights.setText(highlights);


        QRGEncoder qrgEncoder = new QRGEncoder(email, null, QRGContents.Type.TEXT, 4800);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            Glide.with(ActivityQRcode.this)
                    .load(bitmap)
                    .centerCrop()
                    .into(QRImage);
        } catch (WriterException e) {
            Log.v("Error Qr", e.toString());
        }
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "NiMus");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root+"/NiMus");
        myDir.mkdirs();
        String fname = "NiMus-QR-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
