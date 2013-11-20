package org.ieee.c3.teamchooser.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.ieee.c3.teamchooser.MainActivity;
import org.ieee.c3.teamchooser.R;
import org.ieee.c3.teamchooser.components.Person;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SettingsFragment extends Fragment {
    private static String TAG = "DBG Settings Fragment";

    private LinearLayout queryResults;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        if (rootView == null) {
            MainActivity.toast(null, getActivity());
            Log.d(TAG, "Error, null view");
            return null;
        }
        activity = (MainActivity) getActivity();
        queryResults = (LinearLayout) rootView.findViewById(R.id.queryResults);

        Button clearPeople = (Button) rootView.findViewById(R.id.clearCsv);
        clearPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                FileOutputStream fos;
                                try {
                                    fos = activity.openFileOutput("people.csv", Context.MODE_PRIVATE);
                                    fos.flush();
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    Log.d(TAG, "File not found");
                                    MainActivity.toast(null, activity);
                                } catch (IOException e) {
                                    Log.d(TAG, "IO Exception");
                                    MainActivity.toast(null, activity);
                                }
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete people.csv?");
                builder.setPositiveButton("Yes", listener);
                builder.setNegativeButton("No", listener);
                builder.show();
            }
        });

        Button clearSignedIn = (Button) rootView.findViewById(R.id.clearSignedIn);
        clearSignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                activity.getTodaysPeople().clear();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to clear sign ins?");
                builder.setPositiveButton("Yes", listener);
                builder.setNegativeButton("No", listener);
                builder.show();
            }
        });

        Button viewSignedIn = (Button) rootView.findViewById(R.id.viewSignedIn);
        viewSignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryResults.removeAllViews();
                List<Person> people = activity.getTodaysPeople();
                for (Person p : people) {
                    queryResults.addView(createRow(p.toString()));
                }
            }
        });

        Button viewFileContents = (Button) rootView.findViewById(R.id.viewFileContents);
        viewFileContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryResults.removeAllViews();
                String[] contents = MainActivity.getContents(activity).split("\\n");
                for (String content : contents) {
                    queryResults.addView(createRow(content));
                }
            }
        });

        return rootView;
    }

    private TextView createRow(String text) {
        TextView row = new TextView(activity);
        row.setText(text);
        return row;
    }
}
