package com.imooc.weixin6_0.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.imooc.weixin6_0.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by apple on 15/7/27.
 */
public class JokeAdapter extends BaseAdapter {
    private List<JokeListData> lists;

    private Context mContext;
    private RelativeLayout layout;


    public JokeAdapter(List<JokeListData> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = (RelativeLayout) inflater.inflate(R.layout.joke_item, null);
        TextView tv = (TextView) layout.findViewById(R.id.joke_item_tv);
        SimpleDraweeView iv = (SimpleDraweeView) layout.findViewById(R.id.joke_item_iv);
        tv.setText(lists.get(i).getContent());

//        imageView.setId();setId

//        getImage(lists.get(i).getUrl(), iv);
//        bitmap.bitmap
//            iv.setGifImage(new In);

//        iv.setGifImage(imageView.getId());
//        iv.setGifImage(Bitmap2Bytes(bitmap));

        Uri uri = Uri.parse(lists.get(i).getUrl());
//        iv.setImageURI(uri);
        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request)
                .setAutoPlayAnimations(true).build();
        iv.setController(controller);

        return layout;

    }




    public void getImage(String imgUrl, ImageView imageView) {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        Log.d("TAG_M", String.valueOf(R.drawable.actionbar_add_icon));
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.actionbar_add_icon, R.drawable.ic_menu_allfriends);

        imageLoader.get(imgUrl, listener);

    }






}
