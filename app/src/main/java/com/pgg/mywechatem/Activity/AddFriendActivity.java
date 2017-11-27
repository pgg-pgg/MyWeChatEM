package com.pgg.mywechatem.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/17.
 */

public class AddFriendActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button btn_sure;
    private EditText et_search;
    private TextView tv_my_wechat_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_friend);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        btn_sure=findViewById(R.id.btn_sure);
        et_search=findViewById(R.id.et_search);
        tv_my_wechat_id=findViewById(R.id.tv_my_wechat_id);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("添加朋友");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        btn_sure.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        et_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(AddFriendActivity.this);
                break;
            case R.id.rl_add_friend_leida:

                break;
            case R.id.et_search:
                //搜索框点击事件
                break;
        }
    }
}
