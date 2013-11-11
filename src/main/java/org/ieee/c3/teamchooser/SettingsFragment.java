package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.List;

public class SettingsFragment extends Fragment {

    private TextView textView;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.people_list, container, false);
        if (rootView == null) {
            MainActivity.toast(null, getActivity());
            Log.d("People Fragment", "Error, null view");
            return null;
        }
        activity = (MainActivity) getActivity();
        textView = (TextView) rootView.findViewById(R.id.people);

        Button clearPeople = (Button) rootView.findViewById(R.id.clearcsv);
        clearPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream fos;
                try {
                    fos = activity.openFileOutput("people.csv", Context.MODE_PRIVATE);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d("DBG People Fragment", "File not found");
                    MainActivity.toast(null, activity);
                } catch (IOException e) {
                    Log.d("DBG People Fragment:", "IO Exception");
                    MainActivity.toast(null, activity);
                }
            }
        });

        Button clearSignedIn = (Button) rootView.findViewById(R.id.clearsignedin);
        clearSignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getTodaysIDs().clear();
            }
        });

        Button viewSignedIn = (Button) rootView.findViewById(R.id.viewsignedin);
        viewSignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(activity.getTodaysIDs().toString());
            }
        });

        Button viewFileContents = (Button) rootView.findViewById(R.id.view_file_contents);
        viewFileContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(MainActivity.getContents(activity));
            }
        });

        return rootView;
    }
}
