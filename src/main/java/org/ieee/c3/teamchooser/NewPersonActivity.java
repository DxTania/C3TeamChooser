package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

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
                String name, email, uid;
                try {
                    name = nameField.getText().toString();
                    email = emailField.getText().toString();
                    uid = uidField.getText().toString();
                    // Send id back if needed
                    MainActivity.resultOK(uid, NewPersonActivity.this);
                } catch (NullPointerException e) {
                    MainActivity.toast("Please enter name, email, UID, " +
                            "and try again.", NewPersonActivity.this);
                    return;
                }

                int expValue = getExpValue(exp.getCheckedRadioButtonId());
                if (expValue == 0) return;
                if(writeEntryToFile(uid, name, email, expValue)) {
                    MainActivity.toast("Thanks for registering, you've now been signed in!",
                            NewPersonActivity.this);
                }
                finish();
            }
        });
    }

    /**
     * Gets the value 1-5 of the selected radio button.
     * @param id The R.id.x value of the radio button
     * @return int The value of the radio button
     */
    private int getExpValue(int id) {
        switch(id) {
            case R.id.eOne:
                return 1;
            case R.id.eTwo:
                return 2;
            case R.id.eThree:
                return 3;
            case R.id.eFour:
                return 4;
            case R.id.eFive:
                return 5;
            default:
                MainActivity.toast(null, this);
                return 0;
        }
    }

    /**
     * Writes a csv entry to the end of people.csv
     * @param uid The student's Bruincard UID
     * @param name The student's name
     * @param email The student's email
     * @param expValue The student's experience with coding
     * @return boolean Successful or not
     */
    private boolean writeEntryToFile(String uid, String name, String email, int expValue) {
        // Write values to csv file
        try {
            FileOutputStream fos = openFileOutput("people.csv", Context.MODE_APPEND);
            String csvEntry = uid + "," + name + ","
                    + email + "," + String.valueOf(expValue) + "\n";
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
