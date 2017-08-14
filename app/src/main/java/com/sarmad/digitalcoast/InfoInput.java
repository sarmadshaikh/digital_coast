package com.sarmad.digitalcoast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InfoInput extends AppCompatActivity {

    ImageView imgBuilding;
    EditText edLength, edWidth, edHeight, edCapacity, edDistance;
    Spinner spnType;
    TextView txtLatitude, txtLongitude;
    DatabaseFunctions dbf;
    Intent i;
    String photoPath;
    GPS gps;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_input);

        imgBuilding = (ImageView) findViewById(R.id.imgBuilding);
        edLength = (EditText) findViewById(R.id.edtLength);
        edWidth = (EditText) findViewById(R.id.edtWidth);
        edHeight = (EditText) findViewById(R.id.edtHeight);
        edCapacity = (EditText) findViewById(R.id.edtCapacity);
        edDistance = (EditText) findViewById(R.id.edtDistance);
        spnType = (Spinner) findViewById(R.id.spnTypeOfBuilding);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);

        dbf = new DatabaseFunctions(this);
        gps = new GPS(this);
        i = getIntent();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
//        }
        else {
            displayImage();
            location = gps.getMyLocation();
            if (location != null) {
                txtLatitude.setText(String.valueOf(location.getLatitude()));
                txtLongitude.setText(String.valueOf(location.getLongitude()));
            } else {
                txtLongitude.setText("0.0");
                txtLatitude.setText("0.0");
            }
//            imgBuilding.setImageDrawable(Drawable.createFromPath(path));
//            imgBuilding.setImageBitmap();
        }
    }

    private void displayImage()
    {
        String path = i.getStringExtra("photo_path");
//            img = new File(path);
//            Log.d("CONENT RESOLVER", ""+getContentResolver().getType(Uri.fromFile(img)));
        Log.d("FILE DIR", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
        photoPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + path + ".jpg";
        try {
            File f = new File(photoPath);
//                imgBuilding.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(photoPath, opt);
            opt.inSampleSize = Math.min(opt.outHeight / imgBuilding.getMaxHeight(), opt.outWidth / imgBuilding.getMaxWidth());
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opt.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(photoPath, opt);
            imgBuilding.setImageBitmap(bmp);
        } catch (Exception e) {
            Log.e("EXCEPTION: ",e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED)
        {
            displayImage();
            location = gps.getMyLocation();
            if(location != null){
                txtLatitude.setText(String.valueOf(gps.getMyLatitude()));
                txtLongitude.setText(String.valueOf(gps.getMyLongitude()));
            }
            else
            {
                txtLongitude.setText("0.0");
                txtLatitude.setText("0.0");
            }
        }
//        if(requestCode == 2 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//        {
//            displayImage();
//            location = gps.getMyLocation();
//            if(location != null){
//                txtLatitude.setText(String.valueOf(gps.getMyLatitude()));
//                txtLongitude.setText(String.valueOf(gps.getMyLongitude()));
//            }
//        }
    }

    public void saveData(View v)
    {
        String[] data = new String[]{
                spnType.getSelectedItem().toString(),
                photoPath,
                txtLatitude.getText().toString(),
                txtLongitude.getText().toString(),
                edLength.getText().toString(),
                edWidth.getText().toString(),
                edHeight.getText().toString(),
                edCapacity.getText().toString(),
                edDistance.getText().toString()
        };
        //Toast.makeText(this, i.getStringExtra("photo_path"), Toast.LENGTH_SHORT).show();
        if(dbf.insertRecords(data) > 0)
        {
            Toast.makeText(this, "Your data has been saved successfully..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, History.class));
            finishAffinity();
        }
        else
        {
            Toast.makeText(this, "Data is not entered properly..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        startActivity(new Intent(this, History.class));
    }
}
