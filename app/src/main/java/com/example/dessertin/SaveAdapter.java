package com.example.dessertin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SaveAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> saveList;

    public SaveAdapter(Context context, int layout, ArrayList<Model> saveList) {
        this.context = context;
        this.layout = layout;
        this.saveList = saveList;
    }

    @Override
    public int getCount() {
        return saveList.size();
    }

    @Override
    public Object getItem(int i) {
        return saveList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView tName, tResep;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.tName = row.findViewById(R.id.tName);
            holder.tResep = row.findViewById(R.id.tResep);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Model model = saveList.get(i);

        holder.tName.setText(model.getName());
        holder.tResep.setText(model.getResep());

        byte[] recordImage = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}