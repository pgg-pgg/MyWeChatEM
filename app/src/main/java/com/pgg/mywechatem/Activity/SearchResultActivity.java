package com.pgg.mywechatem.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.pgg.mywechatem.R;


/**
 * Created by PDD on 2017/11/18.
 */

public class SearchResultActivity extends BaseActivity {
    private ImageButton ib_exit_01;
    private EditText et_search;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01=findViewById(R.id.ib_exit_01);
        et_search=findViewById(R.id.et_search);
    }

    @Override
    public void initView() {
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null&&(!bundle.getString("result").equals(""))){
            String result=bundle.getString("result");
            et_search.setHint(result);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity(SearchResultActivity.this);
            }
        });
    }

}
