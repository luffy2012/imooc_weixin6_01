package com.imooc.weixin6_0;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.imooc.weixin6_0.net.HttpData;
import com.imooc.weixin6_0.net.HttpGetDataListener;
import com.imooc.weixin6_0.net.JokeAdapter;
import com.imooc.weixin6_0.net.JokeListData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/7/27.
 */
public class JokeFragment extends Fragment implements HttpGetDataListener {

    private HttpData httpData;
//    private   TextView tv;
    private ListView listView;
    private JokeAdapter jokeAdapter;

    private List<JokeListData> lists;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


       return inflater.inflate(R.layout.fragment_joke,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ActivityCreated","AaaaaaA" +
                "aaaaaaaaaaaaaaaaaaaaa");
        intiView();
        initData();
    }

    private void intiView() {
        View view = getView();
        listView = (ListView) view.findViewById(R.id.id_listView_joke);
        lists = new ArrayList<JokeListData>();
        jokeAdapter = new JokeAdapter(lists,getActivity());
        listView.setAdapter(jokeAdapter);
        JokeListData data = new JokeListData();
        jokeAdapter.notifyDataSetChanged();

    }

    private void initData() {
        httpData = (HttpData) new HttpData(
                "http://japi.juhe.cn/joke/img/text.from?key=10ebfee59e514e531a4e38f97613bdeb&page=1&pagesize=10", this).execute();
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataUrl(String data) {



//       tv.setText(data);
        try {
            JSONObject jokes = new JSONObject(data);
            JSONObject result = jokes.getJSONObject("result");
            JSONArray array = result.getJSONArray("data");
            for(int i=0;i <array.length();i++){
                JSONObject obj = (JSONObject) array.get(i);
                String content = obj.getString("content");
                String url =  obj.getString("url");
                String hashId = obj.getString("hashId");
                String unixtime = obj.getString("unixtime");
                String updatetime = obj.getString("updatetime");
                Log.d("URL", url);
                JokeListData joke = new JokeListData();
                joke.setContent(content);
//                getImage(url,joke);
                joke.setUrl(url);
                joke.setHashId(hashId);
                joke.setUnixtime(unixtime);
                joke.setUpdatetime(updatetime);
                lists.add(joke);

            }
            jokeAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
