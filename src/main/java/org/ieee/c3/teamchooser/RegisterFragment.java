package org.ieee.c3.teamchooser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegisterFragment extends Fragment {
    public Button scanBarcode, enterUid, newPerson;

    public RegisterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        scanBarcode = (Button) rootView.findViewById(R.id.scanUID);
        enterUid = (Button) rootView.findViewById(R.id.manualUID);
        newPerson = (Button) rootView.findViewById(R.id.newPerson);

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start barcode activity
            }
        });

        enterUid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pop up ability to enter UID
            }
        });

        newPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // person intent
                Intent intent = new Intent(getActivity(), NewPersonActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
