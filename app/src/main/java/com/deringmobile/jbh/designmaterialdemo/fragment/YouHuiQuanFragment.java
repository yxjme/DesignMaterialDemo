package com.deringmobile.jbh.designmaterialdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deringmobile.jbh.designmaterialdemo.R;

/**
 * Created by zbsdata on 2017/9/8.
 */

public class YouHuiQuanFragment extends Fragment {


    private String state;
    public YouHuiQuanFragment(String state){
        this.state=state;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yhq_layout,container,false);
        initView(view);
        return view;
    }

    /**
     * @param view
     */
    private void initView(View view) {
        TextView showText=(TextView)view.findViewById(R.id.showText);
        showText.setText(state+"\n"+getString(R.string.content));
    }
}
