package com.gyk2017.kutuphanetextdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private DBAdapter db;
    String[] arrBook;
    private ListView lvBooks;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lvBooks = (ListView) findViewById(R.id.lvNewBooks);
        // If the database already exist no need to copy it from the assets
        // folder
        try {
            String destPath = "/data/data/" + getPackageName()
                    + "/databases/Kutuphane";
            File file = new File(destPath);
            File path = new File("/data/data/" + getPackageName()
                    + "/databases/");
            if (!file.exists()) {
                path.mkdirs();
                CopyDB(getBaseContext().getAssets().open("Kutuphane"),
                        new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = new DBAdapter(this);

        getBookNames();
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnSave:
                insertFileDataToDatabase();
                break;
            case R.id.btnContinue:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void getBookNames() {
        names = new ArrayList<String>();
        // Reading the file and displaying it in Toast
        InputStream is = this.getResources().openRawResource(R.raw.newbooks);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = null;
        try {
            while ((str = br.readLine()) != null) {
                arrBook = str.split("%");
                names.add(arrBook[0]);
            }
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> mListAdapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, names);

        lvBooks.setAdapter(mListAdapter);
    }

    public void insertFileDataToDatabase() {
        // Reading the file and displaying it in Toast
        InputStream is = this.getResources().openRawResource(R.raw.newbooks);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = null;
        db.open();
        try {
            while ((str = br.readLine()) != null) {
                arrBook = str.split("%");
                db.insertBook(arrBook[0], arrBook[1], arrBook[2], arrBook[3]);

                displayToast(arrBook[0] + "\n" + arrBook[1] + "\n" + arrBook[2]
                        + "\n" + arrBook[3]);
            }
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.close();
        displayToast("Saved");
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        // Copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    private void displayToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
