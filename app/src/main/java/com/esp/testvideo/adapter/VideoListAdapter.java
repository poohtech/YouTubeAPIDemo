package com.esp.testvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esp.testvideo.R;
import com.esp.testvideo.bean.ChannelBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 3/2/16.
 */
public class VideoListAdapter extends ArrayAdapter<ChannelBean> {
    private final Context context;
    ArrayList<ChannelBean> list = null;
    int resource;
    Uri uri;

    public VideoListAdapter(Context context, int resource, ArrayList<ChannelBean> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    public View getView(int position, View view, ViewGroup parent) {
        holder hobj;
        if (view == null) {
            hobj = new holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, parent, false);
            hobj.swamiImage = (ImageView) view.findViewById(R.id.img_Swami);
            hobj.txtTitle = (TextView) view.findViewById(R.id.txt_title);
            hobj.txtDate = (TextView) view.findViewById(R.id.txt_pubDate);

            view.setTag(hobj);
        } else {
            hobj = (holder) view.getTag();
        }
        if (this.list.get(position).pictureUrl.toString() != null) {
            try {
                //uri = Uri.parse(this.list.get(position).pictureUrl.toString());
                // bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                System.out.println("Urlllllll in adapter" + this.list.get(position).pictureUrl.toString());
                Picasso.with((Activity) context).load(this.list.get(position).pictureUrl.toString()).fit()
                        .centerCrop().into(hobj.swamiImage);

            } catch (Exception e) {
                // log error
                System.out.println("Exception of Url" + e.toString());
            }
        }


        hobj.txtTitle.setText(this.list.get(position).titleofVideo.toString());
        hobj.txtDate.setText(this.list.get(position).publishedDate.toString());

        return view;
    }

    class holder {
        TextView txtTitle, txtDate;
        ImageView swamiImage;
    }
}
