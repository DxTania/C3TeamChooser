package org.ieee.c3.teamchooser.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.ieee.c3.teamchooser.MainActivity;
import org.ieee.c3.teamchooser.R;
import org.ieee.c3.teamchooser.components.Person;
import org.ieee.c3.teamchooser.components.Team;

import java.util.*;

public class MakeTeamsFragment extends Fragment {
    private static String TAG = "DBG Make Teams Fragment";

    private Button makeTeams;
    private ScrollView teamView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teams, container, false);
        if (rootView == null) {
            Log.d(TAG, "View was null");
            MainActivity.toast(null, getActivity());
            return null;
        }
        makeTeams = (Button) rootView.findViewById(R.id.makeTeams);
        teamView = (ScrollView) rootView.findViewById(R.id.teamView);

        makeTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTeams();
            }
        });

        return rootView;
    }

    /**
     * Uses the people that are currently signed in in order to create teams
     * <p/>
     * This algorithm first creates teams for people with preferences. Enough
     * teams are created to accomodate those preferences. We then loop through
     * the teams that were just created in order to finish of those teams. We
     * do this by introducing an exp tolerance difference delta, and if we do
     * not find a person with exp within range, we increase delta until we
     * can finish the teams (or no people are left to put on teams) People
     * are then sorted by exp, and teams of 3 are made from this sorted list.
     * There will be at most 2 extra people, so a max of 2 four-people teams.
     * These extra people will appear at the end of the people list, and therefore
     * have the highest experience. Because of this, we place the highest
     * experience person on the highest experienced team, and the lower
     * experience person the second to highest experienced team.
     * <p/>
     * TODO: Perhaps change this so bigger teams have more freshman?
     */
    private void createTeams() {
        MainActivity activity = (MainActivity) getActivity();
        List<Person> people = new ArrayList<Person>(activity.getTodaysPeople());
        List<Team> teams = new ArrayList<Team>();

        // Extras will create teams of 4
        int numTeams = people.size() / 3;

        // Take care of people with preferences
        Iterator<Person> i = people.iterator();
        List<String> toRemove = new ArrayList<String>();
        while (i.hasNext()) {
            // Get next person and their preferences
            Person p = i.next();
            List<String> prefs = p.getPreferences();

            if (prefs.size() > 0 && !toRemove.contains(p.getName())) {
                Team t = new Team();
                numTeams--;
                t.addPerson(p);
                for (Person pr : people) {
                    if (pr.equals(p)) {
                        continue;
                    }
                    if (prefs.get(0).equalsIgnoreCase(pr.getName()) && !toRemove.contains(pr.getName())) {
                        t.addPerson(pr);
                        toRemove.add(pr.getName());
                    }
                    if (prefs.size() > 1 && prefs.get(1).equalsIgnoreCase(pr.getName()) && !toRemove.contains(pr.getName())) {
                        t.addPerson(pr);
                        toRemove.add(pr.getName());
                    }
                }
                teams.add(t);
                i.remove();
            }
        }

        // Delete those who were just added to teams from people list
        for (String s : toRemove) {
            i = people.iterator();
            while (i.hasNext()) {
                Person p = i.next();
                if (p.getName().equalsIgnoreCase(s)) {
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

        // Add people to current preferenced teams based on exp
        for (Team t : teams) {
            // Exp difference tolerance
            double delta = 0;
            if (t.getPeople().size() < 3) {
                while (t.getPeople().size() < 3 && people.size() > 0) {
                    // Keep increasing delta until we can finish teams
                    i = people.iterator();
                    while (i.hasNext() && t.getPeople().size() < 3) {
                        Person p = i.next();
                        if (p.getExp() >= t.getAvgExp() - delta && p.getExp() <= t.getAvgExp() + delta) {
                            t.addPerson(p);
                            i.remove();
                        }
                    }
                    delta += 0.5;
                }
            }
        }

        // Create teams of 3 from the sorted list, 4 if there are extras
        while (numTeams > 0 || people.size() > 0) {
            Team t;
            if (numTeams > 0) {
                t = new Team();
                teams.add(t);
                numTeams--;
                while (t.getPeople().size() < 3 && people.size() > 0) {
                    t.addPerson(people.get(0));
                    people.remove(0);
                }
            } else {
                // Sort the teams by experience
                Collections.sort(teams, new Comparator<Team>() {
                    public int compare(Team t1, Team t2) {
                        return (int) (t1.getAvgExp() - t2.getAvgExp());
                    }
                });
                // There will be at most 2 extra people to put on teams
                if (people.size() == 1) {
                    teams.get(teams.size() - 1).addPerson(people.get(0));
                    people.remove(0);
                } else {
                    // Earliest person has smaller exp, earlier team has smaller exp
                    teams.get(teams.size() - 2).addPerson(people.get(0));
                    teams.get(teams.size() - 1).addPerson(people.get(1));
                    people.remove(0);
                    people.remove(1);
                }
            }
        }

        // Display team results in a table
        TableLayout table = new TableLayout(getActivity());
        TableRow.LayoutParams llp = new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 6, 0);
        List<TableRow> rows = new ArrayList<TableRow>();
        for (int j = 0; j < teams.size(); j++) {
            int row = j / 2; // 0 indexed row, 2 teams per row
            TableRow tr;
            if (row < rows.size()) {
                tr = rows.get(row);
            } else {
                tr = new TableRow(getActivity());
                tr.setPadding(0, 0, 0, 2);
                rows.add(tr);
                table.addView(tr);
            }

            LinearLayout cell = new LinearLayout(getActivity());
            cell.setLayoutParams(llp);

            TextView peopleList = new TextView(getActivity());
            String peopleString = "", expString = "";
            List<Person> teamMembers = teams.get(j).getPeople();
            for (Person teamMember : teamMembers) {
                peopleString += teamMember.getAbbrName() + "\n";
                expString += String.format("%.0f", teamMember.getExp()) + ",";
            }
            expString = expString.substring(0, expString.length() - 1);
            peopleList.setText(peopleString);

            TextView teamNum = new TextView(getActivity());
            teamNum.setPadding(0, 0, 15, 0);
            teamNum.setTypeface(null, Typeface.BOLD);
            teamNum.setText("Team #" + (j + 1) + "\n~" +
                    String.format("%.2f", teams.get(j).getAvgExp()) +
                    "\n" + expString);

            cell.addView(teamNum);
            cell.addView(peopleList);
            tr.addView(cell);
        }
        teamView.addView(table);

    }

}
