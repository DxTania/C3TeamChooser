package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        makeTeams = (Button) rootView.findViewById(R.id.makeTeams);
        fileContents = (TextView) rootView.findViewById(R.id.fileContents);
        fileContents.setText(getContents());

        makeTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileContents.setText(getContents());
            }
        });

        return rootView;
    }

    private String getContents() {
        String file = "";
        int content;
        FileInputStream fos;
        try {
            fos = getActivity().openFileInput("people.csv");
        } catch (FileNotFoundException e) {
            return "";
        }
        try {
            while((content = fos.read()) != -1) {
                file += (char) content;
            }
        } catch (IOException e) {

        }
        return file;
    }
}
