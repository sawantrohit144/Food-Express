package com.yummy.foodonmytrain;

import android.provider.BaseColumns;

import androidx.fragment.app.Fragment;


public class TableEntries extends Fragment
{

    static class MemberEntry implements BaseColumns {
        static final String TABLE_NAME                   = "app_registered_members";
        //static final String MEMBER_ID                    = "member_id";
        static final String MEMBER_FIRST_NAME            = "member_first_name";
        static final String MEMBER_MIDDLE_NAME           = "member_middle_name";
        static final String MEMBER_LAST_NAME             = "member_last_name";
        static final String MEMBER_USER_TYPE             = "member_user_type";
        static final String MEMBER_PHONE_NUMBER          = "member_phone_number";
        static final String MEMBER_DOB                   = "member_dob";
        static final String MEMBER_TRAIN_NO              = "train_no";
    }

}
