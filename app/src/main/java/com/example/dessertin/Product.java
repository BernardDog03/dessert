package com.example.dessertin;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Product extends AppCompatActivity {
    private TextView txt1, txt2;
    private ImageView image1;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        txt1 = (TextView) findViewById(R.id.txtName);
        txt2 = (TextView) findViewById(R.id.txtResep);
        image1 = (ImageView) findViewById(R.id.iconmenu);
        btnBack = (ImageView) findViewById(R.id.btnBack);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String resep = intent.getStringExtra("resep");
        byte [] image = intent.getByteArrayExtra("image");

        txt1.setText(name);
        txt2.setText(resep);
        image1.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Product.this, SaveList.class));
            }
        });
    }
}
