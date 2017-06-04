package com.hemantithide.borisendesjaak.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.hemantithide.borisendesjaak.Engine.UsernameGenerator;
import com.hemantithide.borisendesjaak.R;

import java.util.ArrayList;

/**
 * Created by Daniel on 04/06/2017.
 */

public class NounAdapter extends ArrayAdapter<UsernameGenerator.Noun> {

    private Button button;

    public NounAdapter(@NonNull Context context, ArrayList<UsernameGenerator.Noun> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Object item = getItem(position);
        button = (Button)convertView.findViewById(R.id.spinner_textview);

        button.setText(item.toString());

        return convertView;
    }
}