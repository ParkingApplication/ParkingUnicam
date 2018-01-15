package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView mImageView;
    private TextView t_user;
    private TextView t_saldo;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_profile, container, false);
        mImageView = (ImageView) view.findViewById(R.id.image_profile);
        mImageView.setImageResource(R.mipmap.ic_profile);
        t_user = (TextView) view.findViewById(R.id.username);
        t_user.setText(t_user.getText()+"Lorenzo Stacchio");
        t_saldo = (TextView) view.findViewById(R.id.money);
        t_saldo.setText(t_saldo.getText()+"100000 $");
        // return inflate
        return view;
    }




}
