package com.sarmad.digitalcoast;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAndEditData extends AppCompatActivity {

    ImageView imgBuilding;
    EditText edLength, edWidth, edHeight, edCapacity, edDistance;
    Spinner spnType;
    TextView txtLatitude, txtLongitude;
    DatabaseFunctions dbf;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_edit_data);
        intent =  getIntent();

        imgBuilding = (ImageView) findViewById(R.id.imgUpdBuilding);
        edLength = (EditText) findViewById(R.id.edtUpdLength);
        edWidth = (EditText) findViewById(R.id.edtUpdWidth);
        edHeight = (EditText) findViewById(R.id.edtUpdHeight);
        edCapacity = (EditText) findViewById(R.id.edtUpdCapacity);
        edDistance = (EditText) findViewById(R.id.edtUpdDistance);
        spnType = (Spinner) findViewById(R.id.spnUpdTypeOfBuilding);
        txtLatitude = (TextView) findViewById(R.id.txtUpdLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtUpdLongitude);

        dbf = new DatabaseFunctions(this);
        Cursor cursor = dbf.getRecords(intent.getStringExtra("id"));
        cursor.moveToNext();
        String bulidingType = cursor.getString(0);
        int position = 0;
        switch (bulidingType)
        {
            case "Cyclone Shelter": position = 0; break;
            case "School": position = 1; break;
            case "Hospital": position = 2; break;
            case "Police Station": position = 3;
        }
        spnType.setSelection(position);
        imgBuilding.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(1)));
        Log.d("IMAGE PATH", cursor.getString(1));
        txtLatitude.setText(cursor.getString(2));
        txtLongitude.setText(cursor.getString(3));
        edLength.setText(cursor.getString(4));
        edWidth.setText(cursor.getString(5));
        edHeight.setText(cursor.getString(6));
        edCapacity.setText(cursor.getString(7));
        edDistance.setText(cursor.getString(8));
    }

    public void updateData(View view)
    {
        String[] data = new String[6];
        data[0] = spnType.getSelectedItem().toString();
        data[1] = edLength.getText().toString();
        data[2] = edWidth.getText().toString();
        data[3] = edHeight.getText().toString();
        data[4] = edCapacity.getText().toString();
        data[5] = edDistance.getText().toString();
        if(dbf.updateRecord(intent.getStringExtra("id"), data) > 0)
        {
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT);
            startActivity(new Intent(this, History.class));
            finishAffinity();
        }
        else
        {
            Toast.makeText(this, "Unable to update data", Toast.LENGTH_SHORT);
        }
    }
}
