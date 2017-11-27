package com.pgg.mywechatem.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.pgg.mywechatem.Activity.Chat_Activity.ChatActivity;
import com.pgg.mywechatem.Adapter.MainViewPagerAdapter;
import com.pgg.mywechatem.Domian.ActionItem;
import com.pgg.mywechatem.Domian.InviteMessage;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Fragment.FragmentFactory;
import com.pgg.mywechatem.Fragment.MessageFragment;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.TitlePopup;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView img_search, img_add;
    private RelativeLayout re_wechat;
    private View re_contact_list;
    private View re_find;
    private View re_profile;
    private ViewPager vp_content;
    private MainViewPagerAdapter adapter;
    private ImageView[] imageViews;
    private TitlePopup titlePopup;
    private TextView unread_msg_number, unread_address_number, unread_find_number;
    private NewMessageBroadcastReceiver msgReceiver;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String count = String.valueOf(msg.obj);
                if ((long)msg.obj>0){
                    unread_msg_number.setText(String.valueOf(count));
                    unread_msg_number.setVisibility(View.VISIBLE);
                }else {
                    unread_address_number.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        initPopWindow();
        initReceiver();
    }

    private void initView() {
        img_search = findViewById(R.id.img_search);
        img_add = findViewById(R.id.img_add);
        re_wechat = findViewById(R.id.re_wechat);
        re_contact_list = findViewById(R.id.re_contact_list);
        re_find = findViewById(R.id.re_find);
        re_profile = findViewById(R.id.re_profile);
        vp_content = findViewById(R.id.vp_content);
        unread_msg_number = findViewById(R.id.unread_msg_number);
        unread_address_number = findViewById(R.id.unread_address_number);
        unread_find_number = findViewById(R.id.unread_find_number);
        imageViews = new ImageView[4];
        imageViews[0] = findViewById(R.id.ib_weixin);
        imageViews[1] = findViewById(R.id.ib_contact_list);
        imageViews[2] = findViewById(R.id.ib_find);
        imageViews[3] = findViewById(R.id.ib_profile);
        imageViews[0].setSelected(true);
        vp_content.setCurrentItem(0);
        updateUnreadLabel();
    }
    private void initData() {
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);
    }

    private void initListener() {
        img_add.setOnClickListener(this);
        img_search.setOnClickListener(this);
        re_wechat.setOnClickListener(this);
        re_contact_list.setOnClickListener(this);
        re_find.setOnClickListener(this);
        re_profile.setOnClickListener(this);
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 4; i++) {
                    if (i == position) {
                        imageViews[i].setSelected(true);
                    } else {
                        imageViews[i].setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        //注册群聊相关的 listener
        EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
    }

    private void initPopWindow() {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
                R.drawable.icon_menu_group));
        titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
                R.drawable.icon_menu_addfriend));
        titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
                R.drawable.icon_menu_sao));
        titlePopup.addAction(new ActionItem(this, R.string.menu_money,
                R.drawable.abv));
        titlePopup.addAction(new ActionItem(this, R.string.menu_help, R.drawable.icon_menu_help));
    }

    private int keyBackClickCount = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    EMChatManager.getInstance().logout();// 退出环信聊天
                    BaseApplication.getBaseApplication().removeActivity();
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            // mLoadingDialog.show();
            switch (position) {
                case 0:// 发起群聊
                    Utils.start_Activity(MainActivity.this, AddGroupChatActivity.class);
                    break;
                case 1:// 添加朋友
                    Utils.start_Activity(MainActivity.this, AddFriendActivity.class,
                            new BasicNameValuePair(Constants.NAME, "添加朋友"));
                    break;
                case 2:// 扫一扫
                    Utils.start_Activity(MainActivity.this, CaptureActivity.class);
                    break;
                case 3:// 收钱
                    Utils.start_Activity(MainActivity.this, GetMoneyActivity.class);
                    break;
                case 4://帮助与反馈
                    Utils.start_Activity(MainActivity.this, HelpAndFeedbackActivity.class, new BasicNameValuePair(Constants.Title, "帮助与反馈"),
                            new BasicNameValuePair(Constants.URL,
                                    "http://weixin.qq.com/"));
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                titlePopup.show(findViewById(R.id.layout_bar));
                break;
            case R.id.img_search:
                Utils.start_Activity(MainActivity.this, SearchActivity.class);
                break;
            case R.id.re_wechat:
                //底部微信被点击
                vp_content.setCurrentItem(0);
                break;
            case R.id.re_contact_list:
                vp_content.setCurrentItem(1);
                break;
            case R.id.re_find:
                vp_content.setCurrentItem(2);
                break;
            case R.id.re_profile:
                vp_content.setCurrentItem(3);
                break;

        }
    }

    private void initReceiver() {
        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        EMChatManager.getInstance().getChatOptions().setRequireAck(true);
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        EMChat.getInstance().setAppInited();
    }


    /**
     * 新消息广播接收者
     *
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
            String from = intent.getStringExtra("from");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            if (ChatActivity.activityInstance != null) {
                if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                    if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                } else {
                    if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                }
            }
            // 注销广播接收者，否则在ChatActivity中会收到这个广播
            abortBroadcast();
            // 刷新bottom bar消息未读数
            updateUnreadLabel();
            if (vp_content.getCurrentItem()==0){
                // 当前页面如果为聊天历史页面，刷新此页面
                //TODO 如何让MessageFragment知晓有新消息来
            }
        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            // 刷新bottom bar消息未读数
            updateUnreadLabel();
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);

                if (msg != null) {
                    if (ChatActivity.activityInstance != null) {
                        if (msg.getChatType() == EMMessage.ChatType.Chat) {
                            if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                                return;
                        }
                    }
                    msg.isAcked = true;
                }
            }
        }
    };


    /**
     * 获取未读消息数
     */
    public void updateUnreadLabel() {
        int count = 0;
        count = EMChatManager.getInstance().getUnreadMsgsCount();
        if (count > 0) {
            unread_msg_number.setText(String.valueOf(count));
            unread_msg_number.setVisibility(View.VISIBLE);
        } else {
            unread_msg_number.setVisibility(View.INVISIBLE);
        }
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                    }else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)){
                            //连接不到聊天服务器
                        }else{
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }

    private class MyGroupChangeListener implements GroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,String inviter, String reason) {

            //收到加入群聊的邀请

            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter,
                                        String reason) {
            //群聊邀请被接受
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee,
                                         String reason) {
            //群聊邀请被拒绝
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //当前用户被管理员移除出群聊

        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            //群聊被创建者解散
            // 提示用户群被解散

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName,String applyer, String reason) {
            // 用户申请加入群聊，收到加群申请
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName,String accepter) {
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName,String decliner, String reason) {
            // 加群申请被拒绝
        }

    }

}
