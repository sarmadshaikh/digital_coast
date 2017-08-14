package com.sarmad.digitalcoast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView img;
    String currentPhotoPath, fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        img = (ImageView) findViewById(R.id.imgThumbnail);

        //img.setImageBitmap(BitmapFactory.decodeFile("file:Android/data/com.sarmad.digitalcoast/files/Pictures/MYIMG_12042017_0018.jpg"));
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null)
        {
            //Now we create the image and save it on the external space
            File photo = null;
            try
            {
                photo = createFile();
            }
            //Unable to create file
            catch (IOException e)
            {
                return;
            }
            //Check whether file created
            if(photo != null)
            {
                Uri photoUri = FileProvider.getUriForFile(this, "com.sarmad.digitalcoast.fileprovider", photo);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Bitmap mImageBitmap;
//        if (requestCode == 1 && resultCode == -1) {
//            try {
//                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(currentPhotoPath));
//                img.setImageBitmap(mImageBitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        if(resultCode != -1){
            startActivity(new Intent(this, History.class));
            finishAffinity();
        }
        else {
            Intent i = new Intent(this, InfoInput.class);
            i.putExtra("photo_path", fileName);
            startActivity(i);
            finishAffinity();
        }
//        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
//            Toast.makeText(this, "Image saved successfully..", Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), "Request Code: " + requestCode + "\t\tResult Code: " + resultCode, Toast.LENGTH_LONG).show();
//            Bundle thumb = data.getExtras();
//            Bitmap bmp = (Bitmap) thumb.get("data");
//            img.setImageBitmap(bmp);
//
//        }
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        opt.inJustDecodeBounds = true;
//        img.setImageBitmap(viewImage());
    }

    public Bitmap viewImage()
    {
        BitmapFactory.Options opt= new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        // Get the file currently captured
        BitmapFactory.decodeFile(currentPhotoPath, opt);

        //Get the dimensions of ImageView
        int opH = img.getHeight();
        int opW = img.getWidth();

        //Get the dimensions of the image
        opt.inJustDecodeBounds = false;
        int inH = opt.outHeight;
        int inW = opt.outWidth;

        //Scale the image according to the ImageView
        float scale = Math.min(inH/opH, inW/opW);
        opt.inSampleSize = (int) scale;

        //return the image with new dimensions
        return BitmapFactory.decodeFile(currentPhotoPath, opt);
    }

    public File createFile() throws IOException{
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String fileName = "MYIMG_" + timeStamp;

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + "/" + fileName + ".jpg");

        currentPhotoPath = "file:" +image.getAbsolutePath();
        this.fileName = fileName;
        return image;
    }
}
