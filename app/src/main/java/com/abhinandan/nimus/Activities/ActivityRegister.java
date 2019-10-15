package com.abhinandan.nimus.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.LandingPages.LandingPage;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class ActivityRegister extends AppCompatActivity {

    private String email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.defaultActivityColor, this.getTheme()));
        }

        final Button Login, SignUp;
        final CheckBox Checkbox;
        final EditText Email, Password;
        final ImageView Heart;

        Login = findViewById(R.id.LoginLink);
        Email = findViewById(R.id.EmailRegister);
        Password = findViewById(R.id.PasswordRegister);
        SignUp = findViewById(R.id.SignUp);
        Checkbox = findViewById(R.id.CheckBoxRegister);
        Heart = findViewById(R.id.heartSignup);

        mAuth = FirebaseAuth.getInstance();


        Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Checkbox.isChecked()) {
                    Heart.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp, getApplication().getTheme()));
                } else {
                    Heart.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, getApplication().getTheme()));
                }
            }
        });


        final ProgressDialog progressdialog = new ProgressDialog(ActivityRegister.this, R.style.CustomDialog);
        progressdialog.setMessage("Loading Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Checkbox.isChecked()) {
                    progressdialog.show();
                    email = Email.getText().toString();
                    password = Password.getText().toString();

                    if ((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(password))) {

                        progressdialog.dismiss();
                        new iOSDialogBuilder(ActivityRegister.this)
                                .setTitle("Invalid Action!")
                                .setSubtitle("One of the field is empty, please try Again")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener("Ok", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        dialog.dismiss();

                                    }
                                })
                                .build().show();
                        Toast.makeText(getApplicationContext(), "A field is empty!", Toast.LENGTH_SHORT).show();
                    } else {

                        if (password.length() < 6) {

                            progressdialog.dismiss();
                            new iOSDialogBuilder(ActivityRegister.this)
                                    .setTitle("Invalid Action!")
                                    .setSubtitle("Password should at least be 6 characters long!!")
                                    .setBoldPositiveLabel(true)
                                    .setCancelable(false)
                                    .setPositiveListener("Ok", new iOSDialogClickListener() {
                                        @Override
                                        public void onClick(iOSDialog dialog) {
                                            dialog.dismiss();
                                            Password.setText(null);

                                        }
                                    })
                                    .build().show();
                            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                            progressdialog.dismiss();
                            new iOSDialogBuilder(ActivityRegister.this)
                                    .setTitle("Invalid Email!")
                                    .setSubtitle("The Email you've entered doesn't exist")
                                    .setBoldPositiveLabel(true)
                                    .setCancelable(false)
                                    .setPositiveListener("Ok", new iOSDialogClickListener() {
                                        @Override
                                        public void onClick(iOSDialog dialog) {
                                            dialog.dismiss();
                                            Email.setText(null);
                                            Password.setText(null);
                                            Checkbox.setChecked(false);

                                        }
                                    })
                                    .build().show();
                            Toast.makeText(getApplicationContext(), "Invalid Email!", Toast.LENGTH_SHORT).show();
                        } else {

                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                        }
                                        Toast.makeText(ActivityRegister.this, "Success!", Toast.LENGTH_SHORT).show();
                                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                                            final FirebaseAuth auth = FirebaseAuth.getInstance();
                                            auth.getCurrentUser()
                                                    .reload()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            FirebaseUser user = auth.getCurrentUser();

                                                            if ((user != null) && (user.isEmailVerified())) {
                                                                progressdialog.dismiss();
                                                                Intent intent = new Intent(ActivityRegister.this, ActivityRegisterInit.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                progressdialog.dismiss();
                                                                Intent intent = new Intent(ActivityRegister.this, LandingPage.class);
                                                                intent.putExtra("value", "1");
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(ActivityRegister.this, "User not Logged In", Toast.LENGTH_SHORT).show();
                                        }

                                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                        progressdialog.dismiss();
                                        Password.setText(null);
                                        Email.setText(null);
                                        Checkbox.setChecked(false);
                                        new iOSDialogBuilder(ActivityRegister.this)
                                                .setTitle("Account Creation Failed")
                                                .setSubtitle("A user is Already registered with " + email)
                                                .setBoldPositiveLabel(true)
                                                .setCancelable(false)
                                                .setPositiveListener("Login instead", new iOSDialogClickListener() {
                                                    @Override
                                                    public void onClick(iOSDialog dialog) {
                                                        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                                        startActivity(intent);

                                                    }
                                                })
                                                .setNegativeListener("OK", new iOSDialogClickListener() {
                                                    @Override
                                                    public void onClick(iOSDialog dialog) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .build().show();
                                    } else {

                                        progressdialog.dismiss();
                                        new iOSDialogBuilder(ActivityRegister.this)
                                                .setTitle("Account Creation Failed!")
                                                .setSubtitle("Something went wrong, Please Try Again")
                                                .setBoldPositiveLabel(true)
                                                .setCancelable(false)
                                                .setPositiveListener("Ok", new iOSDialogClickListener() {
                                                    @Override
                                                    public void onClick(iOSDialog dialog) {
                                                        dialog.dismiss();

                                                    }
                                                })
                                                .build().show();
                                    }

                                }
                            });
                        }

                    }
                } else {
                    progressdialog.dismiss();
                    new iOSDialogBuilder(ActivityRegister.this)
                            .setTitle("Alert!")
                            .setSubtitle("You must agree to all our terms and conditions")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("Ok", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    dialog.dismiss();

                                }
                            })
                            .build().show();
                }


            }
        });
    }
}
