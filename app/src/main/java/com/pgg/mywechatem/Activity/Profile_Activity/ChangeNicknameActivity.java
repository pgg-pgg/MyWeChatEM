package com.pgg.mywechatem.Activity.Profile_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by PDD on 2017/11/19.
 */

public class ChangeNicknameActivity extends BaseActivity {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button ib_right;
    private EditText et_my_nickname;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_nickname);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.btn_sure);
        et_my_nickname=findViewById(R.id.et_my_nickname);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("更改名字");
        ib_right.setText("保存");
        ib_right.setVisibility(View.VISIBLE);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        Bundle bundle=getIntent().getExtras();
        name = bundle.getString(Constants.NAME);
        if (bundle!=null&&name!=null){
            et_my_nickname.setText(name);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        et_my_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!s.toString().equals(name))&&(!s.toString().equals(""))){
                    ib_right.setEnabled(true);
                }else {
                    ib_right.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.start_Activity(ChangeNicknameActivity.this,MyInfoActivity.class);
                Utils.finish(ChangeNicknameActivity.this);
            }
        });
        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.start_Activity(ChangeNicknameActivity.this,MyInfoActivity.class,
                        new BasicNameValuePair(Constants.NAME,et_my_nickname.getText().toString()));
                Utils.finish(ChangeNicknameActivity.this);
            }
        });
    }
}
