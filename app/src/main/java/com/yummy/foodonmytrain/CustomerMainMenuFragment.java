package com.yummy.foodonmytrain;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CustomerMainMenuFragment extends Fragment implements View.OnClickListener {

    static int tea_coffee;
    static int breakfast_veg;
    static int breakfast_nonveg;
    static int meal_veg;
    static int meal_egg;
    static int meal_chicken;
    static int biryani_veg;
    static int biryani_egg;
    static int biryani_chicken;

    static int total_price;
    String mSeat_no,mTrain_no;
    boolean cartEmptyChecker;

    Button mTeaCoffeeinc,mTeaCoffeedec,mBreakfastVeginc,mBreakfastVegdec,
            mBreakfastNonveginc,mBreakfastNonvegdec,mMealVeginc,mMealVegdec,
            mMealEgginc,mMealEggdec,mMealChickeninc,mMealChickendec,mBiryaniVeginc,mBiryaniVegdec,
            mBiryaniEgginc,mBiryaniEgggdec,mBiryaniChickeninc,mBiryaniChickendec,mFinalizeOrder;

    View view;

    Personal_details personal_details=new Personal_details();
    Order order=new Order();
    DatabaseHelper databaseHelper;
    private DatabaseReference mFirebaseDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.lay_customer_order_menu, container, false);
        databaseHelper=new DatabaseHelper(getContext());
        personal_details=databaseHelper.getMemberDetails();
        ((DrawerLocker)getActivity()).setDrawerEnabled(true,"New Order");
        mTeaCoffeeinc=view.findViewById(R.id.tea_coffee_incid);
        mTeaCoffeedec=view.findViewById(R.id.tea_coffee_decid);
        mBreakfastVeginc=view.findViewById(R.id.breakfast_veg_incid);
        mBreakfastVegdec=view.findViewById(R.id.breakfast_veg_decid);
        mBreakfastNonveginc=view.findViewById(R.id.breakfast_nonveg_incid);
        mBreakfastNonvegdec=view.findViewById(R.id.breakfast_nonveg_decid);
        mMealVeginc=view.findViewById(R.id.meal_veg_incid);
        mMealVegdec=view.findViewById(R.id.meal_veg_dec);
        mMealEgginc=view.findViewById(R.id.meal_egg_incid);
        mMealEggdec=view.findViewById(R.id.meal_egg_decid);
        mMealChickeninc=view.findViewById(R.id.meal_chicken_incid);
        mMealChickendec=view.findViewById(R.id.meal_chicken_decid);
        mBiryaniVeginc=view.findViewById(R.id.biryani_veg_incid);
        mBiryaniVegdec=view.findViewById(R.id.biryani_veg_decid);;
        mBiryaniEgginc=view.findViewById(R.id.biryani_egg_incid);
        mBiryaniEgggdec=view.findViewById(R.id.biryani_egg_decid);
        mBiryaniChickeninc=view.findViewById(R.id.biryani_chicken_incid);
        mBiryaniChickendec=view.findViewById(R.id.biryani_chicken_decid);
        mFinalizeOrder=view.findViewById(R.id.order_btn);

        mTeaCoffeeinc.setOnClickListener(this);
        mTeaCoffeedec.setOnClickListener(this);
        mBreakfastVeginc.setOnClickListener(this);
        mBreakfastVegdec.setOnClickListener(this);
        mBreakfastNonveginc.setOnClickListener(this);
        mBreakfastNonvegdec.setOnClickListener(this);
        mMealVeginc.setOnClickListener(this);
        mMealVegdec.setOnClickListener(this);
        mMealEgginc.setOnClickListener(this);
        mMealEggdec.setOnClickListener(this);
        mMealChickeninc.setOnClickListener(this);
        mMealChickendec.setOnClickListener(this);
        mBiryaniVeginc.setOnClickListener(this);
        mBiryaniVegdec.setOnClickListener(this);
        mBiryaniEgginc.setOnClickListener(this);
        mBiryaniEgggdec.setOnClickListener(this);
        mBiryaniChickeninc.setOnClickListener(this);
        mBiryaniChickendec.setOnClickListener(this);
        mFinalizeOrder.setOnClickListener(this);

        total_cal(view);
        orders_list_init(view);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tea_coffee_incid:
                tea_coffee_inc(view);
                break;
            case R.id.tea_coffee_decid:
                tea_coffee_dec(view);
                break;
            case R.id.breakfast_veg_incid:
                breakfast_veg_inc(view);
                break;
            case R.id.breakfast_veg_decid:
                breakfast_veg_dec(view);
                break;
            case R.id.breakfast_nonveg_incid:
                breakfast_nonveg_inc(view);
                break;
            case R.id.breakfast_nonveg_decid:
                breakfast_nonveg_dec(view);
                break;
            case R.id.meal_veg_incid:
                meal_veg_inc(view);
                break;
            case R.id.meal_veg_dec:
                meal_veg_dec(view);
                break;
            case R.id.meal_egg_incid:
                meal_egg_inc(view);
                break;
            case R.id.meal_egg_decid:
                meal_egg_dec(view);
                break;
            case R.id.meal_chicken_incid:
                meal_chicken_inc(view);
                break;
            case R.id.meal_chicken_decid:
                meal_chicken_dec(view);
                break;
            case R.id.biryani_veg_incid:
                biryani_veg_inc(view);
                break;
            case R.id.biryani_veg_decid:
                biryani_veg_dec(view);
                break;
            case R.id.biryani_egg_incid:
                biryani_egg_inc(view);
                break;
            case R.id.biryani_egg_decid:
                biryani_egg_dec(view);
                break;
            case R.id.biryani_chicken_incid:
                biryani_chicken_inc(view);
                break;
            case R.id.biryani_chicken_decid:
                biryani_chicken_dec(view);
                break;
            case R.id.order_btn:
                finalize_order(view);
                break;
        }
    }


    public void tea_coffee_inc(View view){
        tea_coffee =inc(tea_coffee);
        TextView tv = (TextView) view.findViewById(R.id.tea_coffee_order);
        tv.setText("" + tea_coffee);
        total_cal(view);
    }
    public void tea_coffee_dec(View view) {
        if (tea_coffee >= 0) {
            tea_coffee = dec(tea_coffee);
            TextView tv = (TextView) view.findViewById(R.id.tea_coffee_order);
            if(tea_coffee >0) tv.setText("" + tea_coffee);
            else tv.setText("__");
            total_cal(view);
        }
    }

    public void breakfast_veg_inc(View view){
        breakfast_veg =inc(breakfast_veg);
        TextView tv = (TextView) view.findViewById(R.id.breakfast_veg_order);
        tv.setText("" + breakfast_veg);
        total_cal(view);
    }
    public void breakfast_veg_dec(View view) {
        if (breakfast_veg >= 0) {
            breakfast_veg = dec(breakfast_veg);
            TextView tv = (TextView) view.findViewById(R.id.breakfast_veg_order);
            if(breakfast_veg >0) tv.setText("" + breakfast_veg);
            else tv.setText("__");

            total_cal(view);
        }
    }


    public void breakfast_nonveg_inc(View view){
        breakfast_nonveg =inc(breakfast_nonveg);
        TextView tv = (TextView) view.findViewById(R.id.breakfast_nonveg_order);
        tv.setText("" + breakfast_nonveg);
        total_cal(view);
    }
    public void breakfast_nonveg_dec(View view) {
        if (breakfast_nonveg >= 0) {
            breakfast_nonveg = dec(breakfast_nonveg);
            TextView tv = (TextView) view.findViewById(R.id.breakfast_nonveg_order);
            if(breakfast_nonveg >0) tv.setText("" + breakfast_nonveg);
            else tv.setText("__");

            total_cal(view);
        }
    }

    public void meal_veg_inc(View view){
        meal_veg =inc(meal_veg);
        TextView tv = (TextView) view.findViewById(R.id.meal_veg_order);
        tv.setText("" + meal_veg);
        total_cal(view);
    }
    public void meal_veg_dec(View view) {
        if (meal_veg >= 0) {
            meal_veg = dec(meal_veg);
            TextView tv = (TextView) view.findViewById(R.id.meal_veg_order);
            if(meal_veg >0) tv.setText("" + meal_veg);
            else tv.setText("__");

            total_cal(view);
        }
    }

    public void meal_egg_inc(View view){
        meal_egg=inc(meal_egg);
        TextView tv = (TextView) view.findViewById(R.id.meal_egg_order);
        tv.setText("" + meal_egg);

        total_cal(view);
    }
    public void meal_egg_dec(View view) {
        if (meal_egg>= 0) {
            meal_egg= dec(meal_egg);
            TextView tv = (TextView) view.findViewById(R.id.meal_egg_order);
            if(meal_egg>0) tv.setText("" + meal_egg);
            else tv.setText("__");

            total_cal(view);
        }
    }

    public void meal_chicken_inc(View view){
        meal_chicken =inc(meal_chicken);
        TextView tv = (TextView) view.findViewById(R.id.meal_chicken_order);
        tv.setText("" + meal_chicken);

        total_cal(view);
    }
    public void meal_chicken_dec(View view) {
        if (meal_chicken >= 0) {
            meal_chicken = dec(meal_chicken);
            TextView tv = (TextView) view.findViewById(R.id.meal_chicken_order);
            if(meal_chicken >0) tv.setText("" + meal_chicken);
            else tv.setText("__");

            total_cal(view);
        }
    }


    public void biryani_veg_inc(View view){
        biryani_veg =inc(biryani_veg);
        TextView tv = (TextView) view.findViewById(R.id.biryani_veg_order);
        tv.setText("" + biryani_veg);
        total_cal(view);
    }
    public void biryani_veg_dec(View view) {
        if (biryani_veg >= 0) {
            biryani_veg = dec(biryani_veg);
            TextView tv = (TextView) view.findViewById(R.id.biryani_veg_order);
            if(biryani_veg >0) tv.setText("" + biryani_veg);
            else tv.setText("__");

            total_cal(view);
        }
    }


    public void biryani_egg_inc(View view){
        biryani_egg =inc(biryani_egg);
        TextView tv = (TextView) view.findViewById(R.id.biryani_egg_order);
        tv.setText("" + biryani_egg);

        total_cal(view);
    }
    public void biryani_egg_dec(View view) {
        if (biryani_egg >= 0) {
            biryani_egg = dec(biryani_egg);
            TextView tv = (TextView) view.findViewById(R.id.biryani_egg_order);
            if(biryani_egg >0) tv.setText("" + biryani_egg);
            else tv.setText("__");

            total_cal(view);
        }
    }


    public void biryani_chicken_inc(View view){
        biryani_chicken =inc(biryani_chicken);
        TextView tv = (TextView) view.findViewById(R.id.biryani_chicken_order);
        tv.setText("" + biryani_chicken);

        total_cal(view);
    }
    public void biryani_chicken_dec(View view) {
        if (biryani_chicken >= 0) {
            biryani_chicken = dec(biryani_chicken);
            TextView tv = (TextView) view.findViewById(R.id.biryani_chicken_order);
            if(biryani_chicken >0) tv.setText("" + biryani_chicken);
            else tv.setText("__");

            total_cal(view);
        }
    }


    public void total_cal(View view){
        total_price = tea_coffee *(15)+ breakfast_veg *(40)+ breakfast_nonveg *(50)+ meal_veg *(70)+meal_egg*(80)+ meal_chicken *(120)+ biryani_veg *(70)+ biryani_egg *(80)+ biryani_chicken *(100);

        if (total_price > 0) {
            TextView tv = (TextView) view.findViewById(R.id.price_total_id);
            tv.setText(""+"₹"+ (total_price));
            tv.setError(null);
            cartEmptyChecker=true;
        }
        else{
            cartEmptyChecker=false;
            TextView tv = (TextView) view.findViewById(R.id.price_total_id);
            tv.setError("Please order from menu first..");
            tv.setText("");

        }
    }

    public int dec(int x) {
        if (x > 0) {
            x--;
            return x;
        }
        else return 0;
    }


    public int inc(int x) {
        x++;
        return (x);
    }

    public void orders_list_init(View view){
        if(tea_coffee >0) {
            TextView tv = (TextView) view.findViewById(R.id.tea_coffee_order);
            tv.setText("" + tea_coffee);
        }
        if(breakfast_veg >0) {
            TextView tv = (TextView) view.findViewById(R.id.breakfast_veg_order);
            tv.setText("" + breakfast_veg);
        }

        if(breakfast_nonveg >0) {
            TextView tv = (TextView) view.findViewById(R.id.breakfast_nonveg_order);
            tv.setText("" + breakfast_nonveg);
        }
        if(meal_veg >0) {
            TextView tv = (TextView) view.findViewById(R.id.meal_veg_order);
            tv.setText("" + meal_veg);
        }
        if(meal_egg>0) {
            TextView tv = (TextView) view.findViewById(R.id.meal_egg_order);
            tv.setText("" + meal_egg);
        }
        if(meal_chicken >0) {
            TextView tv = (TextView) view.findViewById(R.id.meal_chicken_order);
            tv.setText("" + meal_chicken);
        }
        if(biryani_veg >0) {
            TextView tv = (TextView) view.findViewById(R.id.biryani_veg_order);
            tv.setText("" + biryani_veg);
        }
        if(biryani_egg >0) {
            TextView tv = (TextView) view.findViewById(R.id.biryani_egg_order);
            tv.setText("" + biryani_egg);
        }

        if(biryani_chicken >0) {
            TextView tv = (TextView) view.findViewById(R.id.biryani_chicken_order);
            tv.setText("" + biryani_chicken);
        }
    }

    public void finalize_order(View view)
    {
        if(cartEmptyChecker)
           //seatAlertBuilder();

            //Seat and Train alert for future use
            seatAlertTrainAlert();
        dataaggregator();

    }

    private void seatAlertTrainAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        //LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setTitle("Enter Train No. & Seat No.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.lay_alert_seattrainno, null);

        EditText mSeatNo= dialogView.findViewById(R.id.edtSeatno);
        EditText mTrainNo= dialogView.findViewById(R.id.edtTrainno);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mTrainNo.getText().toString().length()>0 && mSeatNo.getText().toString().length()>0) {

                            dataaggregator();
                            mTrain_no=mTrainNo.getText().toString();
                            mSeat_no = mSeatNo.getText().toString();

                            order.setCustomer_trainno(mTrain_no);
                            order.setCustomer_seatno(mSeat_no);
                            order.setOrderId(getUUID());
                            order.setOrderStatus(false);
                            if(mFirebaseDatabaseRef==null)
                                mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

                            mFirebaseDatabaseRef.child("Orders")
                                    .child(mTrain_no)
                                    .child(order.getOrderId())
                                    .setValue(order);

                           /* mFirebaseDatabaseRef.child("Users")
                                    .child(personal_details.getPersonalno())
                                    .child(getUUID())
                                    .setValue(order);*/

                            Toast.makeText(getActivity(), "Order Placed Successfully ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            getFragmentManager().popBackStack();
                        }else {
                            mTrainNo.setError("Please enter train no.");
                            mSeatNo.setError("Please enter seat no.");
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void dataaggregator() {
        String name,phone;
        name=personal_details.getFirstname()+personal_details.getLastname();
        phone=personal_details.getPersonalno();
        order.setCustomername(name);
        order.setCustomer_phone(phone);
        order.setTea_coffee(tea_coffee);
        order.setBreakfast_veg(breakfast_veg);
        order.setBreakfast_nonveg(breakfast_nonveg);
        order.setMeal_veg(meal_veg);
        order.setMeal_egg(meal_egg);
        order.setMeal_chicken(meal_chicken);
        order.setBiryani_veg(biryani_veg);
        order.setBiryani_egg(biryani_egg);
        order.setBiryani_chicken(biryani_chicken);
    }

    public void seatAlertBuilder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seat No.");

        // Set up the input
        final EditText input = new EditText(getContext());
        builder.setCancelable(false);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Submit",null);
/*        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });*/
        AlertDialog alertDialog=builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(input.getText().toString().length()>0) {
                            dataaggregator();
                            mSeat_no = input.getText().toString();
                            order.setCustomer_seatno(mSeat_no);
                            order.setTimestamp(String.valueOf(System.currentTimeMillis()));
                            if(mFirebaseDatabaseRef==null)
                                mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();

                            mFirebaseDatabaseRef.child("Orders")
                                    .child(personal_details.getTrainNo())
                                    .child(getUUID())
                                    .setValue(order);

                            /*mFirebaseDatabaseRef.child("Users")
                                    .child(personal_details.getPersonalno())
                                    .child(getUUID())
                                    .setValue(order);*/
                            Toast.makeText(getActivity(), "Order Placed ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else
                            input.setError("Please enter seat no.");
                    }
                });
            }
        });
        alertDialog.show();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
