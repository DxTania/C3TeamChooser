package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.ieee.c3.teamchooser.components.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SignedInActivity extends Activity {

    private List<String> peopleList;
    private List<Person> people;
    private ArrayList<String> uids;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_in);

        ListView listView = (ListView) findViewById(R.id.listview);

        // Get and sort signed in people by UID
        String peopleString;
        if ((peopleString = getIntent().getStringExtra("people")) == null) {
            MainActivity.toast(null, SignedInActivity.this);
            return;
        }
        String values[] = peopleString.split(";");
        peopleList = new ArrayList<String>();
        people = new ArrayList<Person>();
        for (String value : values) {
            Person p = new Person(value);
            people.add(p);
            peopleList.add(p.getUid() + ": " + p.getName());
        }
        Collections.sort(peopleList);
        Collections.sort(people, new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.getUid().compareTo(p2.getUid());
            }
        });

        /**
         * Clicking on a person prompts deletion of their sign in
         */
        uids = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, peopleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int pos, long l) {
                // Remove from lists
                final Person p = people.get(pos);
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                uids.add(p.getUid());
                                peopleList.remove(pos);
                                people.remove(pos);
                                adapter.notifyDataSetChanged();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SignedInActivity.this);
                builder.setMessage("Delete " + p.getName() + "?");
                builder.setPositiveButton("Yes", listener);
                builder.setNegativeButton("No", listener);
                builder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra("uids", uids);
        this.setResult(RESULT_OK, returnIntent);
        finish();
    }

} 