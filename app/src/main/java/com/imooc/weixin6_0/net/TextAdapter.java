package com.imooc.weixin6_0.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.imooc.weixin6_0.R;

import java.util.List;

/**
 * Created by apple on 15/7/25.
 */
public class TextAdapter extends BaseAdapter {
    private List<ListData> lists;
    private Context mContext;
    private RelativeLayout layout;

    public TextAdapter(List<ListData> lists,Context mContext) {

        this.lists = lists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(lists.get(position).getFlag() == ListData.RECEIVER){
            layout = (RelativeLayout) inflater.inflate(R.layout.tuling_item_left, null);
        }
        if (lists.get(position).getFlag() == ListData.SEND) {
            layout = (RelativeLayout) inflater.inflate(R.layout.tuling_item_right, null);
        }
        TextView tv = (TextView) layout.findViewById(R.id.item_tv);
        TextView time = (TextView) layout.findViewById(R.id.time);
        tv.setText(lists.get(position).getContent());
        time.setText(lists.get(position).getTime());
        return layout;
    }

}
