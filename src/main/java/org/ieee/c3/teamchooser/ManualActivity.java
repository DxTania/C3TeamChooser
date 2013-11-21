package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.ieee.c3.teamchooser.components.Person;

public class ManualActivity extends Activity {
    private static final int MANUAL = 0;

    private EditText uid;
    private EditText pref1;
    private EditText pref2;

    public ManualActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_uid_entry);
        uid = (EditText) findViewById(R.id.manualId);
        pref1 = (EditText) findViewById(R.id.pref1);
        pref2 = (EditText) findViewById(R.id.pref2);
        Button submit = (Button) findViewById(R.id.submitManualId);

        /**
         * If an entry is found, signs the person in with the given preferences
         * Else, prompts registration and eventually signs the person in
         *  with their given preferences
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid.getText() == null) {
                    MainActivity.toast("Please enter an id", ManualActivity.this);
                    return;
                }
                String id = uid.getText().toString();
                Person p = MainActivity.findEntry(id, ManualActivity.this);
                String p1 = pref1.getText().toString();
                String p2 = pref2.getText().toString();
                if (p != null) {
                    MainActivity.toast("Thanks for signing in, " + p.getName() + "!", ManualActivity.this);
                    if (p1 != null && !p1.equals("")) {
                        p.addPreference(p1);
                    }
                    if (p2 != null && !p2.equals("")) {
                        p.addPreference(p2);
                    }
                    MainActivity.resultOK(p.toString(), ManualActivity.this);
                    finish();
                } else {
                    Intent newPerson = new Intent(ManualActivity.this, NewPersonActivity.class);
                    newPerson.putExtra("ID", id);
                    newPerson.putExtra("pref1", p1);
                    newPerson.putExtra("pref2", p2);
                    startActivityForResult(newPerson, MANUAL);
                }
            }
        });
    }

    /**
     * Handles the result of the new person activity
     *
     * @param requestCode Will be MANUAL
     * @param resultCode  The result of the new person activity
     * @param intent      The intent returned from the activity, a person string
     */
    public void onActivityResult (int requestCode, int resultCode, Intent intent) {
        if (requestCode == MANUAL && resultCode == RESULT_OK) {
            MainActivity.resultOK(intent.getStringExtra("person"), ManualActivity.this);
            finish();
        }
    }

}
