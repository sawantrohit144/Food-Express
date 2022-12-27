package com.yummy.foodonmytrain;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class FeedbackFragment extends Fragment {

    TextInputEditText textInputEditText;
    Button btnSubmitFeedback;



    Feedback feedback;
    Personal_details personal_details=new Personal_details();
    DatabaseHelper databaseHelper;
    private DatabaseReference mFirebaseDatabaseRef;

    private FeedbackFragment() {
        // Required empty public constructor
    }

    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Feedback");

        databaseHelper=new DatabaseHelper(getContext());
        personal_details=databaseHelper.getMemberDetails();

        textInputEditText = view.findViewById(R.id.edt_enter_feedback);
        btnSubmitFeedback = view.findViewById(R.id.btn_submit_feedback);

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = textInputEditText.getText().toString();

                if(!str.equals("")) {

                    String name = personal_details.getFirstname() + personal_details.getLastname();
                    String phone = personal_details.getPersonalno();

                    feedback = new Feedback(name, phone, str);

                    if (mFirebaseDatabaseRef == null)
                        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

                    mFirebaseDatabaseRef.child("Feedback")
                            .child(UUID.randomUUID().toString())
                            .setValue(feedback);

                    Toast.makeText(getActivity(), "Feedback uploaded Successfully ", Toast.LENGTH_LONG).show();
                    textInputEditText.getText().clear();

                }
            }
        });

    }
}
