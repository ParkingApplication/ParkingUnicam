package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {


    public ChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change__password_, container, false);
    }
    /**
     * This method allow contact server to change password
     * @param password
     *          new user password
     * @return
     *         true if password changes
     */
    public boolean changePassword(String password){
        return true;
    }
}
