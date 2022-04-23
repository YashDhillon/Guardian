package com.example.yash.guardian;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomLayout extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] arr;
    private final String[] arr1;
    private final String[] arr2;

    public CustomLayout(Activity context, String[] arr,String[] arr1,String[] arr2)
    {
        super(context,R.layout.customadapterview,arr);

        this.context = context;
        this.arr = arr;
        this.arr1 = arr1;
        this.arr2 =arr2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View viewrow = inflater.inflate(R.layout.customadapterview,null,true);

        TextView nameview = (TextView)viewrow.findViewById(R.id.nameview);
        TextView phonenoview = (TextView)viewrow.findViewById(R.id.phonenoview);
        TextView idview = (TextView)viewrow.findViewById(R.id.idview);

        nameview.setText(arr[position]);
        phonenoview.setText(arr1[position]);
        idview.setText(arr2[position]);
        return viewrow;
    }
}
