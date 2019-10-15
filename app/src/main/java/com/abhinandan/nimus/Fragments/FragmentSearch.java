package com.abhinandan.nimus.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.abhinandan.nimus.R;
import com.abhinandan.nimus.Models.User;
import com.abhinandan.nimus.Utils.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {
    private EditText mSearchField;
    private ImageView mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;


    public FragmentSearch() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");



        View view = inflater.inflate(R.layout.fragment_search, container, false);


        mSearchField =  view.findViewById(R.id.search_field);
        mSearchBtn =  view.findViewById(R.id.search_btn);

        mResultList =  view.findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                if(!searchText.isEmpty()){
                    firebaseUserSearch(searchText);
                }
                else{
                    Toast.makeText(getContext(), "Oh, crap you forgot to type the name", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return  view;
    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(getContext(), searchText, Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("user_name").startAt(searchText).endAt(searchText + "\uf8ff");



        FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.list_layout,
                UserViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder userViewHolder, User user, int i) {


                userViewHolder.setDetails(getContext(),user.getFull_name(),user.getUser_name(),user.getUrl());
                Toast.makeText(getContext(), "User Found", Toast.LENGTH_SHORT).show();
            }
        };
        mResultList.setAdapter(firebaseRecyclerAdapter);

}
}
// View Holder Class
