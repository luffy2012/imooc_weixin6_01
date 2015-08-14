package com.imooc.weixin6_0;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imooc.weixin6_0.net.AppContext;
import com.imooc.weixin6_0.net.HttpData;
import com.imooc.weixin6_0.net.HttpGetDataListener;
import com.imooc.weixin6_0.net.JokeAdapter;
import com.imooc.weixin6_0.net.JokeListData;
import com.imooc.weixin6_0.net.NetDetector;
import com.imooc.weixin6_0.widget.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/7/27.
 */
public class JokeFragment extends Fragment implements HttpGetDataListener,RefreshListView.IReflashListener {

    private HttpData httpData;
//    private   TextView tv;
    private RefreshListView listView;
    private JokeAdapter jokeAdapter;

    private List<JokeListData> lists;

    private int page = 10;
    private int pageSize = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


       return inflater.inflate(R.layout.fragment_joke,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intiView();
        loadMoreData(1, 10);
    }

    private void intiView() {
        View view = getView();
        listView = (RefreshListView) view.findViewById(R.id.id_listView_joke);
        lists = new ArrayList<JokeListData>();
        jokeAdapter = new JokeAdapter(lists,getActivity());
        listView.setAdapter(jokeAdapter);
        listView.setInterface(this);
        JokeListData data = new JokeListData();
        jokeAdapter.notifyDataSetChanged();

    }

//    private void initData() {
//        httpData = (HttpData) new HttpData(
//                "http://japi.juhe.cn/joke/img/text.from?key=10ebfee59e514e531a4e38f97613bdeb&page=1&pagesize=10", this).execute();
//        jokeAdapter.notifyDataSetChanged();
//    }

    private void loadMoreData(int page,int pageSize) {
        if(!NetDetector.isConn(AppContext.getInstance())){
            NetDetector.setNetworkMethod(getActivity());
            return;
        }
        httpData = (HttpData) new HttpData(
                "http://japi.juhe.cn/joke/img/text.from?key=10ebfee59e514e531a4e38f97613bdeb&page="+page+"&pagesize="+pageSize, this).execute();
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataUrl(String data) {
//       tv.setText(data);
//        if(data == null){
//            return;
//        }
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
           NetDetector.setNetworkMethod(getActivity());
        }
    }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub\
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                loadMoreData(1, 10);
                //通知listview 刷新数据完毕；
                listView.reflashComplete();
                jokeAdapter.notifyDataSetChanged();

            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                page = page +5;
                loadMoreData(page,pageSize);
                listView.onLoadMoreComplete();
            }
        }, 2000);
    }
}
