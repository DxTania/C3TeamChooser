package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        ListView listView = (ListView) findViewById(R.id.listView);
        /**
         * Clears the currently signed in people
         */
        Button clearSignedIn = (Button) findViewById(R.id.clearSignedIn);
        clearSignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("clear", true);
                                SignedInActivity.this.setResult(RESULT_OK, returnIntent);
                                SignedInActivity.this.finish();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SignedInActivity.this);
                builder.setMessage("Are you sure you want to clear sign ins?");
                builder.setPositiveButton("Yes", listener);
                builder.setNegativeButton("No", listener);
                builder.show();
            }
        });

        uids = new ArrayList<String>();
        peopleList = getIntent().getStringArrayListExtra("people");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, peopleList);

        // Get and sort signed in people by UID
        if (peopleList == null || peopleList.size() == 0) {
            if (peopleList == null) {
                peopleList = new ArrayList<String>();
            }
            peopleList.add("No people to display!");
        } else {
            people = new ArrayList<Person>();
            for (int i = 0; i < peopleList.size(); i++) {
                Person p = new Person(peopleList.get(i));
                people.add(p);
                peopleList.set(i, p.getUid() + ": " + p.getName());
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
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra("uids", uids);
        this.setResult(RESULT_OK, returnIntent);
        finish();
    }

} 