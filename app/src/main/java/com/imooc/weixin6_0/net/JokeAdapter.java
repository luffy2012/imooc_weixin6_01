package com.imooc.weixin6_0.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ant.liao.GifView;
import com.imooc.weixin6_0.PowerImageView;
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
        PowerImageView iv = (PowerImageView) layout.findViewById(R.id.joke_item_iv);
        tv.setText(lists.get(i).getContent());

//        imageView.setId();setId

        getImage(lists.get(i).getUrl(), iv);
//        bitmap.bitmap
//            iv.setGifImage(new In);

//        iv.setGifImage(imageView.getId());
//        iv.setGifImage(Bitmap2Bytes(bitmap));
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


    public InputStream Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * return a bitmap from service
     *
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;

        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;

            conn = (HttpURLConnection) myFileUrl.openConnection();

            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }
}
