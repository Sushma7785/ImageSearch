package com.example.sushma.imagesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by sushma on 6/23/16.
 */
public class imageFragment extends Fragment {

    private imageAdapter adapter = null;
    ArrayList<String> list = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        View rootView = inflater.inflate(R.layout.all_images_frag, null);
        if(data != null) {
            if(list != null) {
                list.clear();
            }
            list = data.getStringArrayList("urlList");
        }
            adapter = new imageAdapter(getContext(), list);
        GridView gView = (GridView) rootView.findViewById(R.id.grid_layout);
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), FullViewActivity.class);
                intent.putExtra("uri", list.get(position));
                startActivity(intent);
            }
        });
        gView.setAdapter(adapter);
        return rootView;
    }

    }
