package com.yummy.foodonmytrain;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;


public class AdptOrders extends RecyclerView.Adapter<AdptOrders.CustHolder> {

    private final Context context;
    private ArrayList<Order> orderArrayListArrayList;

    public AdptOrders(Context context, ArrayList<Order> orderArrayListArrayList) {
        this.context = context;
        this.orderArrayListArrayList = orderArrayListArrayList;
    }

    @Override
    public CustHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CustHolder(LayoutInflater.from(context).inflate(R.layout.adapter_orderlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustHolder holder, int position) {

        if(SharedPreferencesManager.get(SharedPreferencesManager.IS_USER_CUSTOMER,false)){
            holder.txtlayAdptOrderListNameTitle.setVisibility(View.GONE);
            holder.txtlayAdptOrderListNumberTitle.setVisibility(View.GONE);
            holder.btnAdptOrderListStatus.setVisibility(View.INVISIBLE);
        }

        Order order= orderArrayListArrayList.get(position);
        holder.edtAdptOrderListName.setText(order.customername);
        holder.edtAdptOrderListNumber.setText(order.customer_phone);
        holder.edtAdptOrderListSeatNumber.setText(order.customer_seatno);
        holder.edtAdptOrderListTrainNumber.setText(order.getCustomer_trainno());

        holder.txtAdptOrderListTeaCoffeeCount.setText(" "+String.valueOf(order.tea_coffee));
        holder.txtAdptOrderListBreakfastCount.setText(" "+String.valueOf(order.breakfast_veg));
        holder.txtAdptOrderListBreakfastNonCount.setText(" "+String.valueOf(order.breakfast_nonveg));
        holder.txtAdptOrderListMealCount.setText(" "+String.valueOf(order.meal_veg));
        holder.txtAdptOrderListMealEggCount.setText(" "+String.valueOf(order.meal_egg));
        holder.txtAdptOrderListMealChickenCount.setText(" "+String.valueOf(order.meal_chicken));
        holder.txtAdptOrderListBiryaniCount.setText(" "+String.valueOf(order.biryani_veg));
        holder.txtAdptOrderListBiryaniEggCount.setText(" "+String.valueOf(order.biryani_egg));
        holder.txtAdptOrderListBiryaniChickenCount.setText(" "+String.valueOf(order.biryani_chicken));

        holder.txtAdptOrderListTeaCoffeePrice.setText(" = "+String.valueOf(15*order.tea_coffee));
        holder.txtAdptOrderListBreakfastPrice.setText(" = "+String.valueOf(40*order.breakfast_veg));
        holder.txtAdptOrderListBreakfastNonPrice.setText(" = "+String.valueOf(50*order.breakfast_nonveg));
        holder.txtAdptOrderListMealPrice.setText(" = "+String.valueOf(70*order.meal_veg));
        holder.txtAdptOrderListMealEggPrice.setText(" = "+String.valueOf(80*order.meal_egg));
        holder.txtAdptOrderListMealChickenPrice.setText(" = "+String.valueOf(120*order.meal_chicken));
        holder.txtAdptOrderListBiryaniPrice.setText(" = "+String.valueOf(70*order.biryani_veg));
        holder.txtAdptOrderListBiryaniEggPrice.setText(" = "+String.valueOf(80*order.biryani_egg));
        holder.txtAdptOrderListBiryaniChickenPrice.setText(" = "+String.valueOf(100*order.biryani_chicken));

        holder.txtAdptOrderListTotal.setText(""+(order.tea_coffee *(15)+ order.breakfast_veg *(40)+ order.breakfast_nonveg *(50)+ order.meal_veg *(70)+order.meal_egg*(80)+ order.meal_chicken *(120)+ order.biryani_veg *(70)+ order.biryani_egg *(80)+ order.biryani_chicken *(100)));

        if(order.tea_coffee<=0)
            holder.layAdptOrderListItem1.setVisibility(View.GONE);
        if(order.breakfast_veg<=0)
            holder.layAdptOrderListItem2.setVisibility(View.GONE);
        if(order.breakfast_nonveg<=0)
            holder.layAdptOrderListItem3.setVisibility(View.GONE);
        if(order.meal_veg<=0)
            holder.layAdptOrderListItem4.setVisibility(View.GONE);
        if(order.meal_egg<=0)
            holder.layAdptOrderListItem5.setVisibility(View.GONE);
        if(order.meal_chicken<=0)
            holder.layAdptOrderListItem6.setVisibility(View.GONE);
        if(order.biryani_veg<=0)
            holder.layAdptOrderListItem7.setVisibility(View.GONE);
        if(order.biryani_egg<=0)
            holder.layAdptOrderListItem8.setVisibility(View.GONE);
        if(order.biryani_chicken<=0)
            holder.layAdptOrderListItem9.setVisibility(View.GONE);

        if(order.isOrderStatus()) {
            holder.txtAdptOrderListStatus.setText("Delivered");
            holder.btnAdptOrderListStatus.setVisibility(View.INVISIBLE);
        }else
            holder.txtAdptOrderListStatus.setText("Pending");

        holder.btnAdptOrderListStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mFirebaseDatabaseRef=FirebaseDatabase.getInstance().getReference();
                order.setOrderStatus(true);
                order.setTimestamp(String.valueOf(System.currentTimeMillis()));
                mFirebaseDatabaseRef.child("Orders")
                        .child(order.getCustomer_trainno())
                        .child(order.getOrderId())
                        .setValue(order);
            }
        });

        holder.txtAdptOrderListRatingAndFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        if (orderArrayListArrayList != null) {
            return orderArrayListArrayList.size();
        }
        return 0;
    }

    public void notifyDataChanged(ArrayList<Order> emg) {
        orderArrayListArrayList=emg;
        notifyDataSetChanged();
    }

    class CustHolder extends RecyclerView.ViewHolder {

        EditText edtAdptOrderListName,edtAdptOrderListNumber,edtAdptOrderListTrainNumber,edtAdptOrderListSeatNumber;
        TextView txtAdptOrderListTeaCoffeeCount,txtAdptOrderListTeaCoffeePrice,
                txtAdptOrderListBreakfastCount,txtAdptOrderListBreakfastPrice,
                txtAdptOrderListBreakfastNonCount,txtAdptOrderListBreakfastNonPrice,
                txtAdptOrderListMealCount,txtAdptOrderListMealPrice,
                txtAdptOrderListMealEggCount,txtAdptOrderListMealEggPrice,
                txtAdptOrderListMealChickenCount,txtAdptOrderListMealChickenPrice,
                txtAdptOrderListBiryaniCount,txtAdptOrderListBiryaniPrice,
                txtAdptOrderListBiryaniEggCount,txtAdptOrderListBiryaniEggPrice,
                txtAdptOrderListBiryaniChickenCount,txtAdptOrderListBiryaniChickenPrice,
                txtAdptOrderListTotal,txtAdptOrderListStatus,txtAdptOrderListRatingAndFeedback;
        Button btnAdptOrderListStatus;
        TextInputLayout txtlayAdptOrderListNameTitle,txtlayAdptOrderListNumberTitle,
                txtlayAdptOrderListTrainIdTitle,txtlayAdptOrderListSeatNoTitle;
        LinearLayout layAdptOrderListItem1,layAdptOrderListItem2,layAdptOrderListItem3,
                layAdptOrderListItem4,layAdptOrderListItem5,layAdptOrderListItem6,
                layAdptOrderListItem7,layAdptOrderListItem8,layAdptOrderListItem9;

        CustHolder(View itemView) {
            super(itemView);
            edtAdptOrderListName=itemView.findViewById(R.id.edtAdptOrderListName);
            edtAdptOrderListNumber=itemView.findViewById(R.id.edtAdptOrderListNumber);
            edtAdptOrderListTrainNumber=itemView.findViewById(R.id.edtAdptOrderListTrainNumber);
            edtAdptOrderListSeatNumber=itemView.findViewById(R.id.edtAdptOrderListSeatNumber);

            txtAdptOrderListTeaCoffeeCount=itemView.findViewById(R.id.txtAdptOrderListTeaCoffeeCount);
            txtAdptOrderListTeaCoffeePrice=itemView.findViewById(R.id.txtAdptOrderListTeaCoffeePrice);
            txtAdptOrderListBreakfastCount=itemView.findViewById(R.id.txtAdptOrderListBreakfastCount);
            txtAdptOrderListBreakfastPrice=itemView.findViewById(R.id.txtAdptOrderListBreakfastPrice);
            txtAdptOrderListBreakfastNonCount=itemView.findViewById(R.id.txtAdptOrderListBreakfastNonCount);
            txtAdptOrderListBreakfastNonPrice=itemView.findViewById(R.id.txtAdptOrderListBreakfastNonPrice);
            txtAdptOrderListMealCount=itemView.findViewById(R.id.txtAdptOrderListMealCount);
            txtAdptOrderListMealPrice=itemView.findViewById(R.id.txtAdptOrderListMealPrice);
            txtAdptOrderListMealEggCount=itemView.findViewById(R.id.txtAdptOrderListMealEggCount);
            txtAdptOrderListMealEggPrice=itemView.findViewById(R.id.txtAdptOrderListMealEggPrice);
            txtAdptOrderListMealChickenCount=itemView.findViewById(R.id.txtAdptOrderListMealChickenCount);
            txtAdptOrderListMealChickenPrice=itemView.findViewById(R.id.txtAdptOrderListMealChickenPrice);
            txtAdptOrderListBiryaniCount=itemView.findViewById(R.id.txtAdptOrderListBiryaniCount);
            txtAdptOrderListBiryaniPrice=itemView.findViewById(R.id.txtAdptOrderListBiryaniPrice);
            txtAdptOrderListBiryaniEggCount=itemView.findViewById(R.id.txtAdptOrderListBiryaniEggCount);
            txtAdptOrderListBiryaniEggPrice=itemView.findViewById(R.id.txtAdptOrderListBiryaniEggPrice);
            txtAdptOrderListBiryaniChickenCount=itemView.findViewById(R.id.txtAdptOrderListBiryaniChickenCount);
            txtAdptOrderListBiryaniChickenPrice=itemView.findViewById(R.id.txtAdptOrderListBiryaniChickenPrice);
            txtAdptOrderListTotal=itemView.findViewById(R.id.txtAdptOrderListTotal);
            txtAdptOrderListStatus=itemView.findViewById(R.id.txtAdptOrderListStatus);
            txtAdptOrderListRatingAndFeedback=itemView.findViewById(R.id.txtAdptOrderListRatingAndFeedback);
            btnAdptOrderListStatus=itemView.findViewById(R.id.btnAdptOrderListStatus);

            txtlayAdptOrderListNameTitle=itemView.findViewById(R.id.txtlayAdptOrderListNameTitle);
            txtlayAdptOrderListNumberTitle=itemView.findViewById(R.id.txtlayAdptOrderListNumberTitle);
            txtlayAdptOrderListTrainIdTitle=itemView.findViewById(R.id.txtlayAdptOrderListTrainIdTitle);
            txtlayAdptOrderListSeatNoTitle=itemView.findViewById(R.id.txtlayAdptOrderListSeatNoTitle);

            layAdptOrderListItem1=itemView.findViewById(R.id.layAdptOrderListItem1);
            layAdptOrderListItem2=itemView.findViewById(R.id.layAdptOrderListItem2);
            layAdptOrderListItem3=itemView.findViewById(R.id.layAdptOrderListItem3);
            layAdptOrderListItem4=itemView.findViewById(R.id.layAdptOrderListItem4);
            layAdptOrderListItem5=itemView.findViewById(R.id.layAdptOrderListItem5);
            layAdptOrderListItem6=itemView.findViewById(R.id.layAdptOrderListItem6);
            layAdptOrderListItem7=itemView.findViewById(R.id.layAdptOrderListItem7);
            layAdptOrderListItem8=itemView.findViewById(R.id.layAdptOrderListItem8);
            layAdptOrderListItem9=itemView.findViewById(R.id.layAdptOrderListItem9);
        }
    }
}
