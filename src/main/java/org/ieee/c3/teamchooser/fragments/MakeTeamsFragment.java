package org.ieee.c3.teamchooser.fragments;

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
import org.ieee.c3.teamchooser.components.Team;

import java.util.*;

public class MakeTeamsFragment extends Fragment {

    private Button makeTeams;
    private LinearLayout teamText;

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
        teamText = (LinearLayout) rootView.findViewById(R.id.teamText);

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

        // Take care of people with preferences
        Iterator<Person> i = people.iterator();
        List<String> toRemove = new ArrayList<String>();
        while (i.hasNext()) {
            Person p = i.next(); // must be called before you can call i.remove()
            List<String> prefs = p.getPreferences();
            if (prefs.size() > 0) {
                Team t = new Team();
                numTeams--;
                t.addPerson(p);
                for (Person pr : people) {
                    if (prefs.get(0).equalsIgnoreCase(pr.getName())) {
                        t.addPerson(pr);
                        toRemove.add(pr.getName());
                    } else if (prefs.size() > 1 && prefs.get(1).equalsIgnoreCase(pr.getName())) {
                        t.addPerson(pr);
                        toRemove.add(pr.getName());
                        prefs.remove(1);
                    }
                }
                teams.add(t);
                i.remove();
            }
        }

        // Delete those who were just added to teams
        for (String s : toRemove) {
            i = people.iterator();
            while (i.hasNext()) {
                Person p = i.next();
                if (p.getName().equals(s)) {
                    i.remove();
                    break;
                }
            }
        }

        // Sort the people by experience
        Collections.sort(people, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return (int) (p1.getExp() - p2.getExp());
            }
        });

        // Add people to current preferenced teams until those teams are finished
        for (Team t : teams) {
            double delta = 0;
            if (t.getPeople().size() < 3) {
                while (t.getPeople().size() < 3) {
                    i = people.iterator();
                    while (i.hasNext() && t.getPeople().size() < 3) {
                        Person p = i.next();
                        if (p.getExp() > t.getAvgExp() - delta && p.getExp() < t.getAvgExp() + delta) {
                            t.addPerson(p);
                            i.remove();
                        }
                    }
                    delta += 0.5;
                }
            }
        }

        // Create teams of 3 from the sorted list
        int ovf = 0;
        while (numTeams > 0 || people.size() > 0) {
            Team t;
            if (numTeams > 0) {
                t = new Team();
                teams.add(t);
                numTeams--;
            } else {
                t = teams.get(ovf);
                ovf++;
                t.addPerson(people.get(0));
                people.remove(0);
                continue;
            }

            while (t.getPeople().size() < 3 && people.size() > 1) {
                t.addPerson(people.get(0));
                people.remove(0);
            }
        }

        teamText.removeAllViews();
        for (Team t : teams) {
            TextView txt = new TextView(activity);
            txt.setText(t.toString());
            teamText.addView(txt);
        }


    }

}
