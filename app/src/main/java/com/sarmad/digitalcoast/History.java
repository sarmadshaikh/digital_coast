package com.sarmad.digitalcoast;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    DatabaseFunctions dbf;
    ListView lstHistory;
    ArrayAdapter adp;
    TextView listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lstHistory = (ListView) findViewById(R.id.lstHistory);
        listItem = (TextView) findViewById(R.id.txtListItem);

        dbf = new DatabaseFunctions(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Cursor cursor = dbf.getRecords();
        if(cursor.getCount() > 0)
        {
            ArrayList data = new ArrayList();
            int i = 0;
            final String[] itemId = new String[cursor.getCount()];
            while(cursor.moveToNext())
            {
                data.add(cursor.getString(10)+" - " + cursor.getString(2));
                itemId[i++] = cursor.getString(0);
            }
            adp = new ArrayAdapter(this, R.layout.my_list_item, data);
            lstHistory.setAdapter(adp);
            lstHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ViewAndEditData.class);
                    intent.putExtra("id", itemId[position]);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Quit App?")
                .setMessage("Are you sure you want to quit this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true)
                .create();
        alert.show();
    }
}
