package com.pgg.mywechatem.Activity.Find_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/18.
 */

public class ChangeBackgroundActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView tv_from_phone;
    private TextView tv_play_photo;
    private TextView tv_from_net;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_background);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        tv_from_net=findViewById(R.id.tv_from_net);
        tv_from_phone=findViewById(R.id.tv_from_phone);
        tv_play_photo=findViewById(R.id.tv_play_photo);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("更换相册封面");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        tv_from_phone.setOnClickListener(this);
        tv_from_net.setOnClickListener(this);
        tv_play_photo.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(ChangeBackgroundActivity.this);
                break;
            case R.id.tv_from_phone:
                //从手机选一张

                break;
            case R.id.tv_from_net:
                //摄影师作品

                break;
            case R.id.tv_play_photo:
                //拍一张
                break;
        }
    }

}
