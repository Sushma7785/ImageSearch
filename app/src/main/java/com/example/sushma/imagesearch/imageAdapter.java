package com.example.sushma.imagesearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by sushma on 6/23/16.
 */
public class imageAdapter extends BaseAdapter {

    public List<String> imageList;
    Context context;
    public imageAdapter(Context paramContext, List<String> paramList) {
        this.imageList = paramList;
        this.context = paramContext;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final int itemPosition = position;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.image_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        new DownloadImageTask(holder.viewImage).execute(imageList.get(itemPosition));

        return view;
    }

    private static class ViewHolder {
        public View view;
        public ImageView viewImage;

        public ViewHolder(View view) {
            this.view = view;
            viewImage = (ImageView) view.findViewById(R.id.listItem);
        }
    }

}
