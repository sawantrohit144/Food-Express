package com.yummy.foodonmytrain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SellerMainMenuFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM="trainno" ;
    private DatabaseReference mFirebaseDatabaseRef;
    ArrayList<Order> mOrder=new ArrayList<>();
    AdptOrders mAdptOrders;
    RecyclerView mCustomerOrderRecyc;
    FloatingActionButton mNewOrderbtn;
    ViewPager slidingpager;
    ProgressBar progressongoingorder;
    TextView txtnoorderorder;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.

    public static SellerMainMenuFragment newInstance(String typeOfRequest) {
        SellerMainMenuFragment fragment = new SellerMainMenuFragment();
        if(typeOfRequest!=null) {
            Bundle args = new Bundle();
            args.putString(ARG_PARAM, typeOfRequest);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_seller_main_menu, container, false);
        ((DrawerLocker)getActivity()).setDrawerEnabled(true,"Home");
        mAdptOrders= new AdptOrders(getContext(),mOrder);
        mCustomerOrderRecyc=view.findViewById(R.id.cust_order_recycler);
        slidingpager=view.findViewById(R.id.slidingpager);
        progressongoingorder=view.findViewById(R.id.progressongoingorder);
        progressongoingorder.setVisibility(View.VISIBLE);
        txtnoorderorder=view.findViewById(R.id.txtnoorderorder);
        PagerAdapter adapter = new CustomAdapter(getActivity(),new Integer[]{R.drawable.slidingimg1,R.drawable.slidingimg2,R.drawable.slidingimg3,R.drawable.slidingimg4});
        slidingpager.setAdapter(adapter);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage >= 4) {
                    currentPage = 0;
                }
                slidingpager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        mCustomerOrderRecyc.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mCustomerOrderRecyc.setAdapter(mAdptOrders);
        if(getArguments() != null) {
            if(mFirebaseDatabaseRef==null)
                mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

            mFirebaseDatabaseRef.child("Orders").child(getArguments().getString(ARG_PARAM)).orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    mOrder.clear();
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Order order = postSnapshot.getValue(Order.class);
                        if(!order.isOrderStatus())
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
                            if(!order.isOrderStatus())
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
        mNewOrderbtn=view.findViewById(R.id.new_orderbtn);

        if(!SharedPreferencesManager.get(SharedPreferencesManager.IS_USER_CUSTOMER,false))
            mNewOrderbtn.hide();


        mNewOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.mainFragContent, new CustomerMainMenuFragment())
                        .addToBackStack("FromSellerFragment")
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
