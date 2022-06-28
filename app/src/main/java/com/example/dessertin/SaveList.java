package com.example.dessertin;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SaveList extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    SaveAdapter mAdapter = null;
    ImageView imageViewIcon, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);

        //to enable back button in actionbar sett pare
        btnBack = findViewById(R.id.btnBack);
        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new SaveAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //get all data from sqlite
        final Cursor cursor = SaveDialog.mSQLiteHelper.get("SELECT * FROM SAVE");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String resep = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            //add to list
            mList.add(new Model(id, name, resep, image));
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size()==0){
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No save  found...", Toast.LENGTH_SHORT).show();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveList.this, MainActivity.class));
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId){
                TextView tv1 = (TextView) view.findViewById(R.id.tName);
                TextView tv2 = (TextView) view.findViewById(R.id.tResep);
                ImageView iv1 = (ImageView) view.findViewById(R.id.imgIcon);

                String name = tv1.getText().toString();
                String resep = tv2.getText().toString();
                byte[] image = imageViewToByte(iv1);

                Intent modify_intent = new Intent(getApplicationContext(), Product.class);
                modify_intent.putExtra("name", name);
                modify_intent.putExtra("resep", resep);
                modify_intent.putExtra("image", image);
                startActivity(modify_intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(SaveList.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            //update
                            Cursor cursor = SaveDialog.mSQLiteHelper.get("SELECT id FROM SAVE");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (cursor.moveToNext()){
                                arrID.add(cursor.getInt(0));
                            }
                            //show update dialog
                            showDialogUpdate(SaveList.this, arrID.get(position));
                        }
                        if (i == 1){
                            //delete
                            Cursor cursor = SaveDialog.mSQLiteHelper.get("SELECT id FROM SAVE");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (cursor.moveToNext()){
                                arrID.add(cursor.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });


    }

    private void showDialogDelete(final int idSave) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(SaveList.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SaveDialog.mSQLiteHelper.deleteData(idSave);
                    Toast.makeText(SaveList.this, "Delete successfuly", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateSaveList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");

        imageViewIcon = dialog.findViewById(R.id.uploadImage1);
        final EditText edtName = dialog.findViewById(R.id.edtName1);
        final EditText edtResep = dialog.findViewById(R.id.edtResep1);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        //get data of row clicked from sqlite
        Cursor cursor = SaveDialog.mSQLiteHelper.get("SELECT * FROM SAVE");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtName.setText(name); //set name to update dialog
            String resep = cursor.getString(2);
            edtResep.setText(resep); //set deskripsi
            byte[] image = cursor.getBlob(3);
            //set image got from sqlite
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));
            //add to list
            mList.add(new Model(id, name, resep, image));
        }

        //set width of dialog
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //set height of dialog
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();
        


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SaveDialog.mSQLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtResep.getText().toString().trim(),
                            imageViewToByte(imageViewIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update error", error.getMessage());
                }
                updateSaveList();
            }
        });
    }

    private void updateSaveList() {
        //get all data from qslite
        Cursor cursor = SaveDialog.mSQLiteHelper.get("SELECT * FROM SAVE");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String resep = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            mList.add(new Model(id,name, resep,image));
        }
        mAdapter.notifyDataSetChanged();
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //galerry intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
            } else {
                Toast.makeText(this, "Don't have permision to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == 888 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1, 1) //image will be rectangle
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
               imageViewIcon.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
