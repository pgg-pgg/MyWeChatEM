package com.pgg.mywechatem.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.SideBar;

import java.util.List;

/**
 * Created by PDD on 2017/11/16.
 */

public class ContactFragment extends BaseFragment {
    private ListView lvContact;
    private SideBar sideBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;
    private Activity ctx;
    private View layout_head;

    String [] a=new String[]{"sss","sss","sss"};

    @Override
    public View initView() {
        ctx = this.getActivity();
        mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        View view= Utils.getView(R.layout.fragment_contact_list);
        lvContact=view.findViewById(R.id.lvContact);
        sideBar=view.findViewById(R.id.sideBar);
        mDialogText = (TextView)Utils.getView(R.layout.list_position);
        mDialogText.setVisibility(View.INVISIBLE);
        sideBar.setListView(lvContact);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        sideBar.setTextView(mDialogText);
        layout_head =Utils.getView(R.layout.layout_head_friend);
        lvContact.addHeaderView(layout_head);
        lvContact.setAdapter(new ArrayAdapter<String>(ctx,R.layout.list_position,a));
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        EMContactManager.getInstance().setContactListener(new MyContactListener());
    }

    @Override
    public void onDestroy() {
        mWindowManager.removeView(mDialogText);
        super.onDestroy();
    }



    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除

        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒

        }

        @Override
        public void onContactAgreed(String username) {
            //同意好友请求
        }

        @Override
        public void onContactRefused(String username) {
            // 拒绝好友请求

        }

    }
}
