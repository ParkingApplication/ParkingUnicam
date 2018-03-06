package com.example.stach.app_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        //get buttons from xml
        ImageButton changePassword = (ImageButton) view.findViewById(R.id.btnChangePassword);
        //ImageButton inputSearch = (ImageButton) view.findViewById(R.id.btnInputLocation);
        //setto le proprietà dei bottoni
        //AUTOMATIC SEARCH
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword changePasswordObj = new ChangePassword();
                //eseguo la transazione
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //eseguo la transazione
                fragmentTransaction.replace(R.id.fram, changePasswordObj);
                //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
                //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
                //io possa tornare indietro
                fragmentTransaction.addToBackStack("Fragment_Settings");
                fragmentTransaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * This method allow contact server to change email
     * @param mail
     *          new user mail
     * @return
     *         true if mail changes
     */
    public boolean changeMail(String mail){
        return true;
    }



}
