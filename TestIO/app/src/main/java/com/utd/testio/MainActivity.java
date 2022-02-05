/******************************************************************************
 * Simple File I/O demo program.
 *
 * Enter text into the EditText field, then press the Write button.  It should
 * be written to a file named testio.txt on your device.  If you press the Read
 * button, the data will be read back and put into the TextView below the
 * buttons.  I didn't have to make any changes to the manifest for this to run.
 *
 * Written by John Cole on March 3, 2019 at The University of Texas at Dallas
 * as an example of Android text file I/O.
 ******************************************************************************/
package com.utd.testio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    EditText editText;

    private File dir;
    File fScores;
    PrintWriter scores = null;
    String strFilename = "testio.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt = findViewById(R.id.textView);
        editText = findViewById(R.id.edittext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**************************************************************************
     * Write a string to a file.  Show an error if it doesn't work.
     * @param strLine -- The line of text to be written.
     * @return -- True if the line was written, false if error.
     **************************************************************************/
    private boolean writeLine(String strLine) {
        boolean bReturn = true;
        try {
            dir = new File(this.getFilesDir(), strFilename);
            scores = new PrintWriter(dir);
            scores.println(strLine);
            scores.close();
          } catch (Exception ex) {
            Snackbar.make(txt, ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            bReturn = false;
        }
        return bReturn;
    }

    /**************************************************************************
     * Read a line from a file.  Show an error if it fails.
     * @return -- The single line of text just read.
     **************************************************************************/
    private String readLine() {
        // Opening a file for input under Android.
        Scanner scores = null;
        String strData = "***";
        try {
            dir = new File(this.getFilesDir(), strFilename);
            scores = new Scanner(dir);
            strData = scores.nextLine();
        } catch (Exception ex) {
            Snackbar.make(txt, ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
        scores.close();
        return strData;
    }

    public void btnRead_click(View view) {
        String strText = readLine();
        txt.setText(strText);
    }

    public void btnWrite_click(View view) {
        String strText = editText.getText().toString();
        if (writeLine(strText))
        {
            txt.setText("");
            editText.setText("");
        }
    }
}
