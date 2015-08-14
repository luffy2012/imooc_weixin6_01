package com.imooc.weixin6_0;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.imooc.weixin6_0.net.AppContext;
import com.imooc.weixin6_0.net.HttpData;
import com.imooc.weixin6_0.net.ListData;
import com.imooc.weixin6_0.net.NetDetector;
import com.imooc.weixin6_0.net.TextAdapter;
import com.imooc.weixin6_0.net.HttpGetDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TulingFragment extends Fragment implements HttpGetDataListener,View.OnClickListener{

    private HttpData httpData;
    private List<ListData> lists;
    private ListView listView;
    private TextAdapter textAdapter;

    private EditText editText;
    private Button btnSend;
    private String content_str;
    private double currentTime=0, oldTime = 0;
    private String[] welcome_array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tuling, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("TAG", "onActivityCreated");
        intiView();
//        httpData = (HttpData) new HttpData("http://www.tuling123.com/openapi/api?key=6aec3af90d6fe6e1e3cd56d35f62bcf7&info=北京", this).execute();
//        System.out.println("BBBBBBBBBBBBBBBBBB");
        super.onActivityCreated(savedInstanceState);
    }

    private void intiView() {
        View view = getView();
        editText = (EditText) view.findViewById(R.id.editText);
        btnSend = (Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
//        View view = inflater.inflate(R.layout.fragment_tuling,null);
        listView = (ListView) view.findViewById(R.id.id_listView_tuling);
        lists = new ArrayList<ListData>();
        textAdapter = new TextAdapter(lists,getActivity());
        listView.setAdapter(textAdapter);
        ListData listData;
        listData = new ListData(getRandomWelcomeTips(), ListData.RECEIVER,
                getTime());
        lists.add(listData);
        textAdapter.notifyDataSetChanged();

    }


    @Override
    public void getDataUrl(String data) {
        Log.i("TU", data);
        parseText(data);
    }

    public void parseText(String str){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
//            Log.i("JSON",jsonObject.getString("code"));
//            Log.i("JSON",jsonObject.getString("text"));
            ListData data = new ListData(jsonObject.getString("text"),ListData.RECEIVER,getTime());
            lists.add(data);
            textAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_send){
            if(!NetDetector.isConn(AppContext.getInstance())){
                NetDetector.setNetworkMethod(getActivity());
                return;
            }
            content_str = editText.getText().toString();
            editText.setText("");
            String sendText = content_str.replace(" ","").replace("\n","");
            if(content_str.length() == 0){
                return;
            }
            ListData listData = new ListData(content_str,ListData.SEND,getTime());
            lists.add(listData);
            textAdapter.notifyDataSetChanged();
            httpData = (HttpData) new HttpData(
                    "http://www.tuling123.com/openapi/api?key=6aec3af90d6fe6e1e3cd56d35f62bcf7&info="
                            + sendText, this).execute();
        }

    }

    private String getTime() {
        currentTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        if (currentTime - oldTime >= 5000) {
            oldTime = currentTime;
            return str;
        } else {
            return "";
        }

    }

    private String getRandomWelcomeTips() {
        String welcome_tip = null;
        welcome_array = this.getResources()
                .getStringArray(R.array.welcome_tips);
        int index = (int) (Math.random() * (welcome_array.length - 1));
        welcome_tip = welcome_array[index];
        return welcome_tip;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("TAG","onAttah");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TAG", "onDetach");
    }
}
