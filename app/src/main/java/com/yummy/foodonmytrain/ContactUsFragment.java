package com.yummy.foodonmytrain;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ContactUsFragment extends Fragment {

    TextView txtContactUs;
    public ContactUsFragment() {
        // Required empty public constructor
    }

    public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DrawerLocker)getActivity()).setDrawerEnabled(true,"Contact Us");
        txtContactUs = view.findViewById(R.id.txt_contact_us);

        String htmlText = "<html>\n" +
                "<body>\n" +
                "<div class=\"contwrapper\">\n" +
                "<div class=\"routehead\">You may contact us</div>\n" +
                "<div class=\"content-ele\">\n" +
                "\n" +
                "<p><b>Customer Care No.</b>0755-6610661, 0755-4090600 (Language: Hindi and English)</p> \n" +
                "\n" +
                "<p><b>Fax no.</b>011-23345400</p>\n" +
                "<p><b>Chennai Customer Care No.</b> 044-25300000 (Language : English and Tamil) </p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><b>For Railway tickets booked through IRCTC</b></p>\n" +
                "<p><b>General Information</b></p>\n" +
                "<p><b>I-tickets/e-tickets:</b> <a href=\"mailto:care@irctc.co.in\">care@irctc.co.in</a></p>\n" +
                "<p><b>For Cancellation E-tickets:</b> <a href=\"mailto:etickets@irctc.co.in\">etickets@irctc.co.in</a></p>\n" +
                "<p><b>For Mumbai Suburban Season tickets:</b> <a href=\"mailto:seasontickets@irctc.co.in\">seasontickets@irctc.co.in</a></p>\n" +
                "\n" +
                "<p>&nbsp;</p>\n" +
                "<tr>\n" +
                "<td><b>For IRCTC SBI Card users who do not receive the card within 01 month \n" +
                "from the date of application kindly call on the Railway SBI Card \n" +
                "Helpline nos. at 0124-39021212 or 18001801295 (if calling from BSNL/MTNL \n" +
                "line) or send email to  <a href=\"mailto:feedback.gesbi@ge.com\">feedback.gesbi@ge.com</a> For other queries on your IRCTC SBI Card account, kindly email at <a href=\"mailto:shubhyatra@irctc.co.in\">shubhyatra@irctc.co.in</a></td></tr>\n" +
                "<br><br>\n" +
                "<p><b>Registered Office / Corporate Office :</b></p>\n" +
                "<p><b>Indian Railway Catering and Tourism Corporation Ltd.</b><br/>\n" +
                "              11th FLOOR ,B-148,STATESMAN HOUSE,<br>\n" +
                "              BARAKHAMBHA ROAD,NEW DELHI 110001.<br>\n" +
                "              <b>Tel:</b> 011-23311263/23311264<br>\n" +
                "              <b>Fax:</b> 011-23311259</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtContactUs.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtContactUs.setText(Html.fromHtml(htmlText));
        }
    }
}
