package com.gyk2017.kutuphanetextdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class SearchActivity extends Activity implements AdapterView.OnItemClickListener {


    private ArrayList<String> resultBooks;
    private ListView mListView;
    private EditText et;
    private DBAdapter db;
    Resources resources;

    Cursor c = null;
    Bitmap mBitmap = null;
    AlertDialog.Builder myDialogBox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        setContentView(R.layout.activity_search);

        mListView = (ListView) findViewById(R.id.lvSearchResult);
        mListView.setOnItemClickListener(this);

        et = (EditText) findViewById(R.id.key);
        db = new DBAdapter(this);

    }

    public void onClick(View view) {
        String key = et.getText().toString();
        resultBooks = new ArrayList();
        db.open();
        Cursor c = db.getBooks(key);
        if (c.moveToFirst()) {
            do {
                resultBooks.add(c.getString(1));
            } while (c.moveToNext());
        }
        db.close();
        ArrayAdapter<String> mListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultBooks);
        mListView.setAdapter(mListAdapter);
    }

    public void onItemClick(AdapterView<?> adapter, View view, int position,
                            long id) {
        String rname = ((TextView) view).getText().toString();

        db = new DBAdapter(this);

        db.open();
        c = db.getBooks(rname);
        c.moveToFirst();
        db.close();

        myDialogBox = new AlertDialog.Builder(SearchActivity.this);

        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(c.getString(4),
                "drawable", this.getPackageName());

        myDialogBox.setIcon(resourceId);

        // set message, title, and icon
        myDialogBox.setTitle(c.getString(1));
        myDialogBox.setMessage("Author :" + c.getString(2)
                + "\n\nPublishYear: " + c.getString(3));

        // Set three option buttons
        myDialogBox.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // whatever should be done when answering "YES" goes
                        // here

                    }
                });

        myDialogBox.create();
        myDialogBox.show();

    }

}
