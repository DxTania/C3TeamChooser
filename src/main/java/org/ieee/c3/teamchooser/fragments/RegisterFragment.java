package org.ieee.c3.teamchooser.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.ieee.c3.teamchooser.MainActivity;
import org.ieee.c3.teamchooser.ManualActivity;
import org.ieee.c3.teamchooser.NewPersonActivity;
import org.ieee.c3.teamchooser.R;
import org.ieee.c3.teamchooser.components.Person;

public class RegisterFragment extends Fragment {
    private static final String TAG = "DBG Register Fragment";
    private static final int BARCODE = 0;
    private static final int MANUAL = 1;
    private static final int SCANNED = 2;

    public Button scanBarcode, enterUid;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        if (rootView == null) {
            MainActivity.toast("Something went wrong! :(", getActivity());
            Log.d(TAG, "Error, null view");
            return null;
        }

        scanBarcode = (Button) rootView.findViewById(R.id.scanUID);
        enterUid = (Button) rootView.findViewById(R.id.manualUID);

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start barcode activity
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, BARCODE);
            }
        });

        enterUid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start activity to manually enter UID
                Intent intent = new Intent(getActivity(), ManualActivity.class);
                startActivityForResult(intent, MANUAL);
            }
        });

        return rootView;
    }

    /**
     * This method handles intents that were started for a result, such
     * as the barcode or manual UID entry intents.
     *
     * @param requestCode The code which indicates which activity finished
     * @param resultCode  The code which indicates the success or failure of the activity
     * @param intent      The intent returned from the activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BARCODE) {
            if (resultCode == Activity.RESULT_OK) {
                MainActivity activity = (MainActivity) getActivity();
                String contents = intent.getStringExtra("SCAN_RESULT");
                if (contents == null) {
                    MainActivity.toast(null, getActivity());
                    return;
                }
                String id = contents.substring(0, contents.length() - 1);
                Person p = MainActivity.findEntry(id, activity);
                if (p != null) {
                    if (!activity.isSignedIn(id)) {
                        activity.signIn(p);
                    }
                    MainActivity.toast("Thanks for signing in, " + p.getName() + "! :D", getActivity());
                } else {
                    Intent newPerson = new Intent(getActivity(), NewPersonActivity.class);
                    newPerson.putExtra("ID", id);
                    startActivityForResult(newPerson, SCANNED);
                }
            }
        } else if (requestCode == MANUAL) {
            if (resultCode == Activity.RESULT_OK) {
                MainActivity activity = (MainActivity) getActivity();
                Person p = new Person(intent.getStringExtra("person"));
                if (!activity.isSignedIn(p.getUid())) {
                    activity.signIn(p);
                }
            }
        } else if (requestCode == SCANNED) {
            if (resultCode == Activity.RESULT_OK) {
                MainActivity activity = (MainActivity) getActivity();
                Person p = new Person(intent.getStringExtra("person"));
                if (!activity.isSignedIn(p.getUid())) {
                    activity.signIn(p);
                }
            }
        }
    }
}
