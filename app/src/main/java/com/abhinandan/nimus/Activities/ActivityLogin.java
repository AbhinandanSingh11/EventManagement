package com.abhinandan.nimus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.abhinandan.nimus.R;
import com.abhinandan.nimus.LandingPages.LandingPage;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {
    private String email, password;
    private FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button Signup,Login;
        final EditText Email,Password;
        final FirebaseAuth mAuth;
        final TextView ForgotPassword;



        Signup = findViewById(R.id.signupLink);
        Login = findViewById(R.id.login);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        ForgotPassword = findViewById(R.id.forgotpasswordLink);


        ForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityLogin.this,ActivityForgotPassword.class);
            startActivity(intent);
        });


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(ActivityLogin.this, MainActivity.class));
            finish();
        }


        final ProgressDialog progressdialog = new ProgressDialog(ActivityLogin.this,R.style.CustomDialog);
        progressdialog.setMessage("Loading Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);


        Signup.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityLogin.this,ActivityRegister.class);
            startActivity(intent);
        });

        Login.setOnClickListener(view -> {

            progressdialog.show();
            email = Email.getText().toString();
            password = Password.getText().toString();

            if((TextUtils.isEmpty(email))||(TextUtils.isEmpty(password))){
                progressdialog.dismiss();

                new iOSDialogBuilder(ActivityLogin.this)
                        .setTitle("Invalid Action!")
                        .setSubtitle("One of the field is empty, please try Again")
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener("OK", iOSDialog::dismiss)
                        .build().show();
                Toast.makeText(getApplicationContext(), "A field is empty!", Toast.LENGTH_SHORT).show();
            }

            else{

                if((Patterns.EMAIL_ADDRESS.matcher(email).matches())){

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(ActivityLogin.this, task -> {

                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            String displayname =  "welcome ";
                            if((user!= null)&&(user.getDisplayName() != null)){
                                    if(user.getDisplayName().contains(" ")){
                                        displayname = displayname+ user.getDisplayName().substring(0, user.getDisplayName().lastIndexOf(" "));
                                    }
                                    else{
                                        displayname = displayname+user.getDisplayName();
                                    }

                            }
                            Toast.makeText(ActivityLogin.this,  displayname, Toast.LENGTH_SHORT).show();
                            if((user!=null)&&(user.isEmailVerified())){
                                Intent intent = new Intent(ActivityLogin.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent = new Intent(ActivityLogin.this, LandingPage.class);
                                startActivity(intent);

                            }


                        }

                        else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                            progressdialog.dismiss();


                            new iOSDialogBuilder(ActivityLogin.this)
                                    .setTitle("Invalid Login Credentials")
                                    .setSubtitle("Please check your password and E-mail before trying again!!! (ERR:fbEICE)")
                                    .setBoldPositiveLabel(true)
                                    .setCancelable(false)
                                    .setPositiveListener("Reset password", dialog -> {
                                        Intent intent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                                        startActivity(intent);

                                    })
                                    .setNegativeListener("OK", dialog -> {
                                        dialog.dismiss();
                                        Email.setText(null);
                                        Password.setText(null);
                                    })
                                    .build().show();
                        }

                        else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                            progressdialog.dismiss();
                            new iOSDialogBuilder(ActivityLogin.this)
                                    .setTitle("User not Found")
                                    .setSubtitle("Looks like you are not registered to NiMus yet, Please create  new account by clicking sign up (ERR:fbEIUE)")
                                    .setBoldPositiveLabel(true)
                                    .setCancelable(false)
                                    .setPositiveListener("Sign up", dialog -> {
                                        Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                                        startActivity(intent);

                                    })
                                    .setNegativeListener("OK", dialog -> {
                                        dialog.dismiss();
                                        Email.setText(null);
                                        Password.setText(null);
                                    })
                                    .build().show();
                        }


                        else{

                            progressdialog.dismiss();
                            //loader.setVisibility(View.GONE);
                            new iOSDialogBuilder(ActivityLogin.this)
                                    .setTitle("Oops!")
                                    .setSubtitle("Sorry, your request cannot be completed at this time, please try again!! (ERR:fbErrNK)")
                                    .setBoldPositiveLabel(true)
                                    .setCancelable(false)
                                    .setPositiveListener("OK", dialog -> {
                                        dialog.dismiss();
                                        Email.setText(null);
                                        Password.setText(null);

                                    })
                                    .build().show();
                        }

                    });
                }

                else{
                    progressdialog.dismiss();
                    new iOSDialogBuilder(ActivityLogin.this)
                            .setTitle("Invalid Email!")
                            .setSubtitle("The Email you've entered doesn't exist")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("OK", dialog -> {
                                dialog.dismiss();
                                Email.setText(null);
                                Password.setText(null);

                            })
                            .build().show();
                    Toast.makeText(getApplicationContext(), "Invalid Email!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
