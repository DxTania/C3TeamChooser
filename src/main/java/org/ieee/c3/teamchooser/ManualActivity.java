package org.ieee.c3.teamchooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ManualActivity extends Activity {

    private EditText uid;
    private Button submit;

    private static final int MANUAL = 3;

    public ManualActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_uid_entry);
        uid = (EditText) findViewById(R.id.manualId);
        submit = (Button) findViewById(R.id.submitManualId);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid.getText() == null) {
                    MainActivity.toast("Please enter an id", ManualActivity.this);
                    return;
                }
                String id = uid.getText().toString();
                String name = MainActivity.findEntry(id, ManualActivity.this);
                // Return id to main activity to add
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ID", id);
                setResult(RESULT_OK, returnIntent);
                if (!name.equals("")) {
                    MainActivity.toast("Thanks for signing in, " + name + "!", ManualActivity.this);
                    finish();
                } else {
                    Intent newPerson = new Intent(ManualActivity.this, NewPersonActivity.class);
                    newPerson.putExtra("ID", id);
                    startActivityForResult(newPerson, MANUAL);
                }
            }
        });
    }

    public void onActivityResult (int requestCode, int resultCode, Intent intent) {
        if (requestCode == MANUAL) {
            finish();
        }
    }
}
