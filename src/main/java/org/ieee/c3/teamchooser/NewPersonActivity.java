package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewPersonActivity extends Activity {

    private EditText nameField;
    private EditText emailField;
    private EditText uidField;
    private Button submitButton;
    private RadioGroup exp;

    public NewPersonActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_person_form);
        nameField = (EditText) findViewById(R.id.nameField);
        emailField = (EditText) findViewById(R.id.emailField);
        uidField = (EditText) findViewById(R.id.uidField);
        submitButton = (Button) findViewById(R.id.submitPerson);
        exp = (RadioGroup) findViewById(R.id.experience);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get form values
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String uid = uidField.getText().toString();
                int expValue = getExpValue(exp.getCheckedRadioButtonId());
                if (expValue == 0) return;
                // Write values to csv file
                FileOutputStream fos;
                try {
                   fos = openFileOutput("people.csv", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    MainActivity.toast("Something went wrong! :(", getParent());
                    return;
                }
                String csvEntry = name + ", " + email + ", "
                        + uid + ", " + String.valueOf(expValue) + "\n";
                try {
                    fos.flush();
                    fos.write(csvEntry.getBytes());
                    fos.close();
                } catch (IOException e) {
                    MainActivity.toast("Something went wrong! :(", getParent());
                    return;
                }
                MainActivity.toast("Thanks for registering!", getParent());
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
                MainActivity.toast("Something went wrong! :(", this);
                return 0;
        }
    }

    public void onRadioButtonClicked(View view) {
        int content;
        String file = "";
        try {
            FileInputStream fos = openFileInput("people.csv");
            while((content = fos.read()) != -1) {
                file += (char) content;
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        MainActivity.toast(file, this);
    }
}
