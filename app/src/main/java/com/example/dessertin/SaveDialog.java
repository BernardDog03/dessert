package com.example.dessertin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SaveDialog extends AppCompatActivity {

    EditText mEdtName, mEdtResep;
    Button mBtnSave, mBtnView;
    ImageView mImageView, BackBtn;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_dialog);

        mEdtName = findViewById(R.id.edtName);
        mEdtResep = findViewById(R.id.edtResep);
        mBtnSave = findViewById(R.id.btnSave);
        mBtnView = findViewById(R.id.btnView);
        mImageView = findViewById(R.id.uploadImage);
        BackBtn = findViewById(R.id.btnBack);

        //create database
        mSQLiteHelper = new SQLiteHelper(this, "SAVEDB.sqlite", null, 1);

        //creating table in database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS SAVE(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, resep VARCHAR, image BLOB)");

        //select image by on imageview click
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for device android 6.0 and above
                ActivityCompat.requestPermissions(
                        SaveDialog.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        //add save to sqlite
        mBtnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    mSQLiteHelper.insertData(
                            mEdtName.getText().toString().trim(),
                            mEdtResep.getText().toString().trim(),
                            imageViewToByte(mImageView)
                    );
                    Toast.makeText(SaveDialog.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    //reset views
                    mEdtName.setText("");
                    mEdtResep.setText("");
                    mImageView.setImageResource(R.drawable.addphoto);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        //show save list
        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start view list activity
                startActivity(new Intent(SaveDialog.this, SaveList.class));
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveDialog.this, MainActivity.class));
            }
        });
    }

    public static  byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                mImageView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
