package com.deringmobile.jbh.designmaterialdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbsdata on 2017/9/2.
 */

public class MainFragment extends Fragment {


    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private List<String> titles;
    private MyPagerAdapter adapter;
    public MainFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView=inflater.inflate(R.layout.main_fragment_layout,container,false);
        initView(contentView);
        initData();
        return contentView;
    }

    private void initData() {
        fragments=new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("推荐");
        titles.add("首页");
        titles.add("电影");
        titles.add("综艺");
        titles.add("记录");
        titles.add("恐怖片");
        titles.add("演唱会");
        for (int i = 0 ; i < titles.size() ; i++ ){
            ItemFragment f=new ItemFragment(titles.get(i));
            fragments.add(f);
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        adapter=new MyPagerAdapter(getFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器
        adapter.notifyDataSetChanged();
    }


    /**
     * @param contentView
     */
    private void initView(View contentView) {
        mTabLayout=(TabLayout)contentView.findViewById(R.id.mTabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager=(ViewPager)contentView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
    }
}
