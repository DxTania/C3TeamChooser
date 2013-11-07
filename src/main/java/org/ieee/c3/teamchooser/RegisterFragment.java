package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegisterFragment extends Fragment {
    public Button scanBarcode, enterUid, newPerson;
    /**
     * m_barcode is the variable
     * which stores the value read
     * by the barcode scanner intent
     */
    private int m_barcode;
    /**
     * m_isBarcodeSet indicates whether
     * or not the activity has been called yet
     * to read a barcode
     */
    private boolean m_isBarcodeSet;

    // global static variables
    private static final int BARCODE = 0;

    public RegisterFragment() {
        m_barcode = 0;
        m_isBarcodeSet = false;
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
                reqBarcode();
                if (isBarcodeSet()) {
                    MainActivity.toast(String.valueOf(getBarcode()), getActivity());
                }
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

    /**
     * reqBarcode will initialize a barcode reader Intent
     * and then launch it. The results are handled at
     * onActivityResult
     *
     * /@see onActivityResult()
     */
    protected void reqBarcode()
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "ONE_D_MODE");
        startActivityForResult(intent, BARCODE);
    }

    /**
     * onActivityResult handles the intent which was launched by
     * reqBarcode
     *
     * /@see reqBarcode()
     */
    public void onActivityResult (int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == BARCODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                m_barcode = Integer.parseInt(contents);
                m_isBarcodeSet = true;
            }
            else
            {
                // bad!
                m_isBarcodeSet = true;
                m_barcode = -1;
            }
        }
    }

    /**
     * a simple getter for m_isBarcodeSet
     */
    public boolean isBarcodeSet()
    {
        return m_isBarcodeSet;
    }

    /**
     * a simple getter for m_barcode
     */
    public int getBarcode()
    {
        return m_barcode;
    }
}
