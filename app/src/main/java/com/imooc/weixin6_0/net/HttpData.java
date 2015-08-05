package com.imooc.weixin6_0.net;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by apple on 15/7/23.
 */
public class HttpData extends AsyncTask<String,Void,String> {
    private HttpClient mHttpClient; //Http 客户端
    private HttpGet mHttpGet;  //Http请求方式
    private String url; //请求的url
    private HttpResponse mHttpResponse;
    private HttpEntity mHttpEntity; //Http返回的实体
    private InputStream in;
    private HttpGetDataListener listener;

    public HttpData(String url, HttpGetDataListener listener){
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        mHttpClient = new DefaultHttpClient();//构造一个默认的Http客户端
        try {
            mHttpGet = new HttpGet(url);
            mHttpResponse = mHttpClient.execute(mHttpGet);
            mHttpEntity  = mHttpResponse.getEntity();
            in = mHttpEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.getDataUrl(s);
        super.onPostExecute(s);
    }
}
