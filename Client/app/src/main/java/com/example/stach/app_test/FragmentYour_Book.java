package com.example.stach.app_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYour_Book extends Fragment {


    public FragmentYour_Book() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your__book, container, false);
    }

    /**
     * This method allows to get user books
     * @return
     *          list of current and recent books
     */
    public List<String> showBooks() {
        return new ArrayList<String>();
    }

    /**
     * This method allow user to remove a past or current book
     * @return
     *          true if book was removed
     */
    public boolean removeBooks(){
        return true;
    }

}
