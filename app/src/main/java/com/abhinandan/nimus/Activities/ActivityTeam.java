package com.abhinandan.nimus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.nimus.Models.Team;
import com.abhinandan.nimus.Models.User;
import com.abhinandan.nimus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityTeam extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private User AppUser;
    private String TAG = "ActivityTeam";

    private EditText Memberone, MemberTwo, MemberThree, MemberFour, MemberFive, TeamName;
    private String memberOne, memberTwo, memberThree, memberFour, memberFive, teamName;
    private TextView save;
    private CardView progress;
    private ImageView back;
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white, getTheme()));

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        Memberone = findViewById(R.id.memberOne);
        MemberTwo = findViewById(R.id.memberTwo);
        MemberThree = findViewById(R.id.memberThree);
        MemberFour = findViewById(R.id.memberFour);
        MemberFive = findViewById(R.id.memberFive);
        TeamName = findViewById(R.id.teamName);
        save = findViewById(R.id.saveTeam);
        progress = findViewById(R.id.progressCardViewTeamView);
        back = findViewById(R.id.ImageBackTeamPage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTeam.this, MainActivity.class);
                intent.putExtra("val", 1);
                startActivity(intent);
            }
        });


        Init();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                memberOne = firebaseUser.getEmail();
                memberTwo = MemberTwo.getText().toString();
                memberThree = MemberThree.getText().toString();
                memberFour = MemberFour.getText().toString();
                memberFive = MemberFive.getText().toString();
                teamName = TeamName.getText().toString();

                counter = 0;

                if (memberTwo.equals("Member two email")) {
                    memberTwo = "";
                } else {
                    if(!memberTwo.isEmpty()){
                        counter++;
                    }

                }

                if (memberThree.equals("Member three email")) {
                    memberThree = "";
                } else {
                    if(!memberTwo.isEmpty()){
                        counter++;
                    }
                }

                if (memberFour.equals("Member four email")) {
                    memberFour = "";
                } else {
                    if(!memberTwo.isEmpty()){
                        counter++;
                    }
                }


                if (memberFive.equals("Member five email")) {
                    memberFive = "";
                } else {
                    if(!memberTwo.isEmpty()){
                        counter++;
                    }
                }

                if (teamName.equals("your team name")) {
                    teamName = "";
                }


                reference.child("users").child(firebaseUser.getEmail().replace(".com", "dotcom")).child("team").setValue(new Team(memberOne, memberTwo, memberThree, memberFour, memberFive, teamName)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActivityTeam.this, "Team added Successfully", Toast.LENGTH_SHORT).show();


                        if (counter + 1 >= 2) {
                            reference.child("users").child(firebaseUser.getEmail().replace(".com", "dotcom")).child("isteamLeader").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ActivityTeam.this, "You were appointed as team leader", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityTeam.this, "Error in appointing you as team Leader", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            reference.child("users").child(firebaseUser.getEmail().replace(".com", "dotcom")).child("isteamLeader").setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ActivityTeam.this, "You must have atleast 2 members in your team to be a team leader", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Error cant set isteamLeader to false " + e);
                                }
                            });
                        }
                        progress.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityTeam.this, "Error Saving Team", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });


    }

    void Init() {
        final String memberOne = firebaseUser.getEmail();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("users").child(firebaseUser.getEmail().replace(".com", "dotcom")).getValue(User.class);
                if (user != null) {
                    Team team = user.getTeam();

                    if (team != null) {
                        memberTwo = team.getMemberTwo();
                        if (memberTwo.isEmpty()) {
                            memberTwo = "Member two email";
                            MemberTwo.setText(memberTwo);
                        } else {
                            MemberTwo.setText(memberTwo);
                        }
                        memberThree = team.getMemberThree();
                        if (memberThree.isEmpty()) {
                            memberThree = "Member three email";
                            MemberThree.setText(memberThree);
                        } else {
                            MemberThree.setText(memberThree);
                        }
                        memberFour = team.getMemberFour();
                        if (memberFour.isEmpty()) {
                            memberFour = "Member four email";
                            MemberFour.setText(memberFour);
                        } else {
                            MemberFour.setText(memberFour);
                        }
                        memberFive = team.getMemberFive();
                        if (memberFive.isEmpty()) {
                            memberFive = "Member five email";
                            MemberFive.setText(memberFive);

                        } else {
                            MemberFive.setText(memberFive);
                        }

                        teamName = team.getTeamName();
                        if (teamName.isEmpty()) {
                            teamName = "your team name";
                            TeamName.setText(teamName);
                        } else {
                            TeamName.setText(teamName);
                        }
                    } else {
                        Toast.makeText(ActivityTeam.this, "You've haven't created any team yet", Toast.LENGTH_SHORT).show();
                        TeamName.setText("your team name");
                        MemberTwo.setText("Member two email");
                        MemberThree.setText("Member three emai");
                        MemberFour.setText("Member four email");
                        MemberFive.setText("Member five email");
                    }
                }

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActivityTeam.this, "Data Reading failed!!", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });

        Memberone.setText(memberOne);
    }
}
