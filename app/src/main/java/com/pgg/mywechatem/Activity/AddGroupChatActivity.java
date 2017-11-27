package com.pgg.mywechatem.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.SideBar;


/**
 * Created by PDD on 2017/11/17.
 */

public class AddGroupChatActivity extends BaseActivity {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button btn_sure;
    private LinearLayout menuLinerLayout;
    private EditText et_search;
    private ListView listView;
    private ImageView iv_search;
    private TextView mDialogText;
    private SideBar indexBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_chat);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        menuLinerLayout = findViewById(R.id.linearLayoutMenu);
        btn_sure=findViewById(R.id.btn_sure);
        et_search = findViewById(R.id.et_search);
        listView =findViewById(R.id.list);
        iv_search =findViewById(R.id.iv_search);
        mDialogText = (TextView) Utils.getView(R.layout.list_position);
        mDialogText.setVisibility(View.INVISIBLE);
        indexBar =findViewById(R.id.sideBar);
        indexBar.setListView(listView);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("发起群聊");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        btn_sure.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finish(AddGroupChatActivity.this);
            }
        });
    }
}
