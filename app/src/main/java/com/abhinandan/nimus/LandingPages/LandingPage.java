package com.abhinandan.nimus.LandingPages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Activities.ActivityRegisterInit;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPage extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private String INTENT_ID = "value";
    private int ACTIVITY_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        ACTIVITY_CODE = getIntent().getIntExtra(INTENT_ID,1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey));
        }

        final Button check;

        progressDialog = new ProgressDialog(LandingPage.this,R.style.CustomDialog);
        progressDialog.setMessage("Loading Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);


        check = findViewById(R.id.check_Btn);



        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            Toast.makeText(this,"yes", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }

        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.show();
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.getCurrentUser()
                        .reload()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseUser user = auth.getCurrentUser();
                                if((FirebaseAuth.getInstance().getCurrentUser()!= null)&&(user.isEmailVerified())){
                                    Intent intent = new Intent(LandingPage.this, ActivityRegisterInit.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    progressDialog.dismiss();
                                    new iOSDialogBuilder(LandingPage.this)
                                            .setTitle("Alert!!")
                                            .setSubtitle("Email not confirmed yet, please try Again")
                                            .setBoldPositiveLabel(true)
                                            .setCancelable(false)
                                            .setPositiveListener("OK",new iOSDialogClickListener() {
                                                @Override
                                                public void onClick(iOSDialog dialog) {
                                                    dialog.dismiss();

                                                }
                                            })
                                            .setNegativeListener("Resend", new iOSDialogClickListener() {
                                                @Override
                                                public void onClick(iOSDialog dialog) {
                                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                    Toast.makeText(LandingPage.this, "Verification email Sent", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .build().show();

                                }
                            }
                        });

            }
        });

    }
}
