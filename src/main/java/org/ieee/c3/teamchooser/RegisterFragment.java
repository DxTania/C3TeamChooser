package org.ieee.c3.teamchooser;

import android.content.Context;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.*;

public class RegisterFragment extends Fragment {
    public Button scanBarcode, enterUid;
    private static final int MANUAL = 1;

    // global static variables
    private static final int BARCODE = 0;

    public RegisterFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        scanBarcode = (Button) rootView.findViewById(R.id.scanUID);
        enterUid = (Button) rootView.findViewById(R.id.manualUID);

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start barcode activity
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, BARCODE);
            }
        });

        enterUid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pop up ability to enter UID
                Intent intent = new Intent(getActivity(), ManualActivity.class);
                startActivityForResult(intent, MANUAL);
            }
        });

        return rootView;
    }

    /**
     * onActivityResult handles the intent which was launched by
     * reqBarcode
     *
     * /@see reqBarcode()
     */
    public void onActivityResult (int requestCode, int resultCode, Intent intent) {
        if (requestCode == BARCODE) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String id = contents.substring(0, contents.length() - 1);
                MainActivity activity = (MainActivity) getActivity();
                activity.addId(id);
                String name = activity.findEntry(id, activity);
                if (!name.equals("")) {
                    MainActivity.toast("Thanks for signing in, " + name + "! :)", getActivity());
                } else {
                    Intent newPerson = new Intent(getActivity(), NewPersonActivity.class);
                    newPerson.putExtra("ID", id);
                    startActivity(newPerson);
                }
            }
            else {
                MainActivity.toast("Something went wrong! :(", getActivity());
            }
        }
        else if (requestCode == MANUAL) {
            if (resultCode == Activity.RESULT_OK) {
                MainActivity activity = (MainActivity) getActivity();
                activity.addId(intent.getStringExtra("ID"));
            }
        }
    }
}
