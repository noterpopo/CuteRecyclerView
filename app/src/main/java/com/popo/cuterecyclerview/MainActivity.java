package com.popo.cuterecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Data> dataList;
    private static MainActivity mainActivity;
    private ExplosionField explosionField;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        explosionField = ExplosionField.attach2Window(this);
        initData();
        initView();
    }
    private void initView(){
        recyclerView=(RecyclerView)findViewById(R.id.rcy);
        CuteAdapter adapter=new CuteAdapter(dataList, this);
        linearLayoutManager=new LinearLayoutManager(this);
        adapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onMenuClick(int position) {
                View view=linearLayoutManager.findViewByPosition(position);
                if(view==null){
                    return;
                }
                explosionField.explode(view);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
    private void initData(){
        dataList=new ArrayList <>();
        for(int i=0;i<20;++i){
            dataList.add(new Data(i+""));
        }

    }

    public static MainActivity getMainActivity(){
        if(mainActivity!=null){
            return mainActivity;
        }else {
            return null;
        }
    }
}
