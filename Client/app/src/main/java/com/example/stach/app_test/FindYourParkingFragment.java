package com.example.stach.app_test;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindYourParkingFragment extends Fragment {


    public FindYourParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_your_parking, container, false);
    }

    /**
     * This method allow to get current position
     */
    private Double[] getCurrentPosition(){
        //calls google api
        Double[] doubleD = {0.1,0.1};
        return doubleD;
    }
    /**
     * This method will contact the server to find lists of park
     */
    public void searchPark(String city){
    //calls google api
    }
}
