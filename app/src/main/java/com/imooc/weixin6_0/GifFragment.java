package com.imooc.weixin6_0;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by apple on 15/8/5.
 */
public class GifFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gif, container, false);


    }
}
