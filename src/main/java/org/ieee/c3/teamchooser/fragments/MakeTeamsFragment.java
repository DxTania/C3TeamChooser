package org.ieee.c3.teamchooser.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.ieee.c3.teamchooser.MainActivity;
import org.ieee.c3.teamchooser.R;
import org.ieee.c3.teamchooser.components.Person;
import org.ieee.c3.teamchooser.components.Team;

import java.util.ArrayList;
import java.util.List;

public class MakeTeamsFragment extends Fragment {

    private Button makeTeams;

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

        makeTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTeams();
            }
        });

        return rootView;
    }

    private void createTeams() {
        MainActivity activity = (MainActivity) getActivity();
        List<Person> people = activity.getTodaysPeople();
        List<Team> teams = new ArrayList<Team>();
        int numTeams = people.size() / 3;
        int extras = people.size() % 3;

        // Take care of
        for (Person person : people) {

        }


    }

}
