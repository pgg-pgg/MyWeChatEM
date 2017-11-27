package com.pgg.mywechatem.Activity.Find_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/18.
 */

public class ShareMomentsActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ib_exit_01,ib_right;
    private View vertical_line;
    private Button btn_sure,btn_add_img;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private EditText et_share_message;
    private HorizontalScrollView hsv_banner;
    private LinearLayout ll_banner;
    private TextView txt_location;
    private RelativeLayout txt_who;
    private TextView txt_tip;
    private ImageView img_qzone;
    private TextView tv_method_of_see;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_share_moments);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.ib_right);
        btn_sure=findViewById(R.id.btn_sure);
        et_share_message=findViewById(R.id.et_share_message);
        hsv_banner=findViewById(R.id.hsv_banner);
        ll_banner=findViewById(R.id.ll_banner);
        btn_add_img=findViewById(R.id.btn_add_img);
        txt_location=findViewById(R.id.txt_location);
        txt_who=findViewById(R.id.txt_who);
        txt_tip=findViewById(R.id.txt_tip);
        img_qzone=findViewById(R.id.img_qzone);
        tv_method_of_see =findViewById(R.id.tv_method_of_see);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.GONE);
        title_tv_center.setVisibility(View.GONE);
        btn_sure.setVisibility(View.VISIBLE);
        btn_sure.setText("发送");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        btn_add_img.setOnClickListener(this);
        txt_location.setOnClickListener(this);
        txt_who.setOnClickListener(this);
        txt_tip.setOnClickListener(this);
        img_qzone.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(ShareMomentsActivity.this);
                break;
            case R.id.btn_add_img:
                //添加照片

                break;
            case R.id.txt_location:
                //获取所在位置
                break;
            case R.id.txt_who:
                //设置谁可以看
                break;
            case R.id.txt_tip:
                //提醒谁看
                break;
            case R.id.img_qzone:
                //同步到QQ空间
                break;
        }
    }
}
