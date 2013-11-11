package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MakeTeamsFragment extends Fragment {

    private Button makeTeams;
    private TextView fileContents;

    public MakeTeamsFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teams, container, false);
        if (rootView == null) {
            Log.d("DBG Make Teams Fragment:", "View was null");
            MainActivity.toast(null, getActivity());
            return null;
        }
        makeTeams = (Button) rootView.findViewById(R.id.makeTeams);

        return rootView;
    }


}
