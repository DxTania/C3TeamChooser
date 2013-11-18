package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import org.ieee.c3.teamchooser.components.Person;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewPersonActivity extends Activity {

    private EditText nameField;
    private EditText emailField;
    private EditText uidField;
    private Button submitButton;
    private RadioGroup exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_person_form);
        nameField = (EditText) findViewById(R.id.nameField);
        emailField = (EditText) findViewById(R.id.emailField);
        uidField = (EditText) findViewById(R.id.uidField);
        submitButton = (Button) findViewById(R.id.submitPerson);
        exp = (RadioGroup) findViewById(R.id.experience);

        // Set UID to what was scanned / manually input
        uidField.setText(getIntent().getStringExtra("ID"));

        // Clicking submit writes the entry to a file
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get form values
                String name, email, uid, pref1, pref2;
                String person[] = new String[4];
                try {
                    person[0] = uidField.getText().toString();
                    person[1] = nameField.getText().toString();
                    person[2] = emailField.getText().toString();

                    pref1 = getIntent().getStringExtra("pref1");
                    pref2 = getIntent().getStringExtra("pref2");
                } catch (NullPointerException e) {
                    MainActivity.toast("Please enter name, email, UID, " +
                            "and try again.", NewPersonActivity.this);
                    return;
                }
                person[3] = getExpValue(exp.getCheckedRadioButtonId());
                if (person[3].equals("0")) {
                    MainActivity.toast("Please enter your experience", NewPersonActivity.this);
                    return;
                }

                if (writeEntryToFile(person)) {
                    MainActivity.toast("Thanks for registering, you've now been signed in!",
                            NewPersonActivity.this);
                }

                // Send person back with preferences
                Person p = new Person(person);
                if (pref1 != null && !pref1.equals("")) {
                    p.addPreference(pref1);
                }
                if (pref2 != null && !pref2.equals("")) {
                    p.addPreference(pref2);
                }

                MainActivity.resultOK(p.toString(), NewPersonActivity.this);
                finish();
            }
        });
    }

    /**
     * Gets the value 1-5 of the selected radio button.
     * @param id The R.id.x value of the radio button
     * @return int The value of the radio button
     */
    private String getExpValue(int id) {
        switch(id) {
            case R.id.eOne:
                return "1";
            case R.id.eTwo:
                return "2";
            case R.id.eThree:
                return "3";
            case R.id.eFour:
                return "4";
            case R.id.eFive:
                return "5";
            default:
                MainActivity.toast(null, this);
                return "0";
        }
    }

    /**
     * Writes a csv entry to the end of people.csv
     * @param person The string array representing the person
     * @return boolean Successful or not
     */
    private boolean writeEntryToFile(String person[]) {
        // Write values to csv file
        try {
            FileOutputStream fos = openFileOutput("people.csv", Context.MODE_APPEND);
            String csvEntry = person[0] + "," + person[1] + ","
                    + person[2] + "," + person[3] + "\n";
            fos.write(csvEntry.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.d("DBG New Person Activity", "File not found");
            MainActivity.toast(null, NewPersonActivity.this);
            return false;
        } catch (IOException e) {
            Log.d("DBG New Person Activity", "IO Exception!");
            MainActivity.toast(null, NewPersonActivity.this);
            return false;
        }
    }
}
