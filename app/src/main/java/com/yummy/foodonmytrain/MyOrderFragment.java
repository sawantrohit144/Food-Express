package com.yummy.foodonmytrain;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;


public class MyOrderFragment extends Fragment {

    private DatabaseReference mFirebaseDatabaseRef;
    ArrayList<Order> mOrder=new ArrayList<>();
    AdptOrders mAdptOrders;
    RecyclerView mCustomerOrderRecyc;
    ProgressBar progressongoingorder;
    TextView txtnoorderorder;
    String trainNo, userType;
    private static final String ARG_PARAM="trainno" ;
    private static final String ARG_PARAM_1="userType" ;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    public static MyOrderFragment newInstance(String typeOfRequest, String userType) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle args = new Bundle();
        if(typeOfRequest!=null) {
            args.putString(ARG_PARAM, typeOfRequest);
        }
        if(userType!=null) {
            args.putString(ARG_PARAM_1, userType);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainNo = getArguments().getString(ARG_PARAM);
            userType = getArguments().getString(ARG_PARAM_1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(userType.equals("1")) {
            ((DrawerLocker)getActivity()).setDrawerEnabled(true,"My Order");
        }else if(userType.equals("2")){
            ((DrawerLocker)getActivity()).setDrawerEnabled(true,"Past Order");
        }

        mAdptOrders= new AdptOrders(getContext(),mOrder);
        mCustomerOrderRecyc=view.findViewById(R.id.last_order_recycler);
        progressongoingorder=view.findViewById(R.id.last_progressongoingorder);
        progressongoingorder.setVisibility(View.VISIBLE);
        txtnoorderorder=view.findViewById(R.id.last_txtnoorderorder);

        mCustomerOrderRecyc.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mCustomerOrderRecyc.setAdapter(mAdptOrders);
        if(getArguments() != null) {
            if(userType.equals("2")){
                if(mFirebaseDatabaseRef==null)
                    mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

                mFirebaseDatabaseRef.child("Orders").child(trainNo).orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        mOrder.clear();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            Order order = postSnapshot.getValue(Order.class);
                            if(order.isOrderStatus())
                                mOrder.add(order);
                        }
                        if(mOrder.size()<=0)
                            txtnoorderorder.setVisibility(View.VISIBLE);
                        else
                            txtnoorderorder.setVisibility(View.GONE);
                        progressongoingorder.setVisibility(View.GONE);
                        mAdptOrders.notifyDataChanged(mOrder);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressongoingorder.setVisibility(View.GONE);
                        System.out.println("The read failed: " + databaseError.getMessage());
                    }
                });
            }else {
                if(mFirebaseDatabaseRef==null)
                    mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
                //.orderByChild("customer_phone").equalTo(SharedPreferencesManager.get(SharedPreferencesManager.USER_NUMBER,"123"))
                mFirebaseDatabaseRef.child("Orders").orderByChild("timestamp")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                mOrder.clear();
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    for (DataSnapshot postSnapshot1: postSnapshot.getChildren()) {
                                        Order order = postSnapshot1.getValue(Order.class);
                                        if(order.isOrderStatus())
                                            if (order.getCustomer_phone().equalsIgnoreCase(SharedPreferencesManager.get(SharedPreferencesManager.USER_NUMBER, "123")))
                                                mOrder.add(order);
                                    }
                                }
                                if(mOrder.size()<=0)
                                    txtnoorderorder.setVisibility(View.VISIBLE);
                                else
                                    txtnoorderorder.setVisibility(View.GONE);
                                progressongoingorder.setVisibility(View.GONE);
                                mAdptOrders.notifyDataChanged(mOrder);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressongoingorder.setVisibility(View.GONE);
                                System.out.println("The read failed: " + databaseError.getMessage());
                            }
                        });
            }
        }

    }
}
