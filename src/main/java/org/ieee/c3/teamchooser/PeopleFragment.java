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

import java.util.List;

public class PeopleFragment extends Fragment {

    public PeopleFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.people_list, container, false);
        TextView text;
        if (rootView != null) {
            text = (TextView) rootView.findViewById(R.id.people);
        } else {
            Log.d("People Fragment", "Error, null view");
            return null;
        }
        MainActivity activity = (MainActivity) getActivity();
        String ids = "";
        List<String> allIds = activity.getTodaysIDs();
        if (allIds != null) {
            for (String allId : allIds) {
                ids += allId + ",";
            }
        }
        text.setText(ids);

        return rootView;
    }
}
