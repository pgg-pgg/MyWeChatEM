package com.pgg.mywechatem.Activity.Profile_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/19.
 */

public class MoreInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private RelativeLayout rl_sex,rl_location,rl_mark;
    private TextView tv_sex_content,tv_location_content,tv_mark_content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_more_info);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        rl_sex=findViewById(R.id.rl_sex);
        rl_location=findViewById(R.id.rl_location);
        rl_mark=findViewById(R.id.rl_mark);
        tv_sex_content=findViewById(R.id.tv_sex_content);
        tv_location_content=findViewById(R.id.tv_location_content);
        tv_mark_content=findViewById(R.id.tv_mark_content);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("更多信息");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_mark.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(MoreInfoActivity.this);
                break;
            case R.id.rl_sex:
                //修改性别
                break;
            case R.id.rl_location:
                //修改地区
                break;
            case R.id.rl_mark:
                //修改个性签名
                break;
        }
    }
}
