package com.pgg.mywechatem.Activity.Chat_Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Adapter.ChatAdapter.ExpressionAdapter;
import com.pgg.mywechatem.Adapter.ChatAdapter.SmileAdapter;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.CommonUtils;
import com.pgg.mywechatem.Uitils.SmileUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.ExpandGridView;
import com.pgg.mywechatem.View.PasteEditText;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDD on 2017/11/26.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener{

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static ChatActivity activityInstance = null;

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private ImageButton ib_right;
    private String type;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final String COPY_IMAGE = "EASEMOBIMG";


    private ProgressBar pb_load_more;
    private ListView list_chat;
    private LinearLayout view_talk;//录音动画
    private ImageView mic_image;//录音图片
    private TextView recording_hint;//上滑取消
    private LinearLayout bar_bottom;//底部输入栏总布局
    private LinearLayout rl_bottom;//输入栏内布局
    private Button btn_set_mode_voice;//左边发送语音，点击显示中间发送语音的按钮
    private Button btn_set_mode_keyboard;//用于从发送语音界面切换到键盘界面
    private LinearLayout btn_press_to_speak;//按住说话按钮
    private RelativeLayout edittext_layout;//edittext和表情按钮的容器
    private PasteEditText et_sendmessage;//用于接收从键盘输入放入文字，或者表情
    private ImageView iv_emoticons_normal;//用于显示表情展示框
    private ImageView iv_emoticons_checked;//用于隐藏表情展示框
    private Button btn_more;//用于打开工具栏，发送图片，文件，地理位置...
    private Button btn_send;//用于发送消息按钮
    private LinearLayout more;//表情以及工具栏的显示容器
    private LinearLayout ll_face_container;//表情显示容器
    private LinearLayout ll_btn_container;//工具栏显示容器
    private ViewPager vPager;//表情显示的viewpager，分页展示
    private AnimationDrawable animationDrawable;
    private List<String> reslist;

    private ClipboardManager clipboard;//用于剪贴板功能
    private InputMethodManager manager;//系统软件软键盘管理类
    private PowerManager.WakeLock wakeLock;//管理系统屏幕亮或暗，唤醒屏幕

    String id = null;
    private String toChatUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);
    }
    /**
     * 设置系统返回键
     *
     * @param keyCode
     * @param event
     * @return
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.finish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        ib_right = findViewById(R.id.ib_right);
        pb_load_more = findViewById(R.id.pb_load_more);
        list_chat = findViewById(R.id.list);
        view_talk = findViewById(R.id.view_talk);
        mic_image = findViewById(R.id.mic_image);
        recording_hint = findViewById(R.id.recording_hint);
        bar_bottom = findViewById(R.id.bar_bottom);
        rl_bottom = findViewById(R.id.rl_bottom);
        btn_set_mode_voice = findViewById(R.id.btn_set_mode_voice);
        btn_set_mode_keyboard = findViewById(R.id.btn_set_mode_keyboard);
        btn_press_to_speak = findViewById(R.id.btn_press_to_speak);
        edittext_layout = findViewById(R.id.edittext_layout);
        et_sendmessage = findViewById(R.id.et_sendmessage);
        iv_emoticons_normal = findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = findViewById(R.id.iv_emoticons_checked);
        btn_more = findViewById(R.id.btn_more);
        btn_send = findViewById(R.id.btn_send);
        more = findViewById(R.id.more);
        ll_face_container = findViewById(R.id.ll_face_container);
        ll_btn_container = findViewById(R.id.ll_btn_container);
        vPager = findViewById(R.id.vPager);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        animationDrawable = (AnimationDrawable) mic_image.getBackground();//初始化录音动画
        animationDrawable.setOneShot(false);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setBackgroundResource(R.drawable.chat_friend_info);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);

        // 表情list
        reslist = getExpressionRes(62);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        vPager.setAdapter(new SmileAdapter(views));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
        et_sendmessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //监听EditText的焦点变化，根据焦点变换背景图
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }
            }
        });


        et_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听EditText的点击事件，当点击之后，变换背景图，隐藏表情框和工具栏，只显示软键盘
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                ll_face_container.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.GONE);
            }
        });

        /**
         * 按住说话的触摸事件
         */
        btn_press_to_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animationDrawable.start();//动画开始
                        if (!CommonUtils.isExitsSdcard()) {
                            String st4 = getResources().getString(
                                    R.string.Send_voice_need_sdcard_support);
                            Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT)
                                    .show();
                            return false;
                        }
                        try {
                            v.setPressed(true);
                            wakeLock.acquire();
                            view_talk.setVisibility(View.VISIBLE);
                            recording_hint.setText(getString(R.string.move_up_to_cancel));
                            recording_hint.setBackgroundColor(Color.TRANSPARENT);
                        } catch (Exception e) {
                            e.printStackTrace();
                            v.setPressed(false);
                            if (wakeLock.isHeld())
                                wakeLock.release();
                            view_talk.setVisibility(View.INVISIBLE);
                            Toast.makeText(ChatActivity.this, R.string.recoding_fail,
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        return true;
                    case MotionEvent.ACTION_MOVE: {
                        if (event.getY() < 0) {
                            recording_hint.setText(getString(R.string.release_to_cancel));
                            recording_hint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                        } else {
                            recording_hint.setText(getString(R.string.move_up_to_cancel));
                            recording_hint.setBackgroundColor(Color.TRANSPARENT);
                            animationDrawable.start();
                        }
                        return true;
                    }
                    case MotionEvent.ACTION_UP:
                        if (animationDrawable.isRunning()) {
                            animationDrawable.stop();
                        }
                        v.setPressed(false);
                        view_talk.setVisibility(View.INVISIBLE);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (event.getY() < 0) {

                        } else {
                            // stop recording and send voice file
                            String st1 = getResources().getString(
                                    R.string.Recording_without_permission);
                            String st2 = getResources().getString(
                                    R.string.The_recording_time_is_too_short);
                            String st3 = getResources().getString(
                                    R.string.send_failure_please);
                        }
                }
                return false;
            }
        });


        // 监听文字框,当有文字时，隐藏加号，显示发送按钮
        et_sendmessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_more.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_more.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);

        //工具栏各item的点击事件注册
        findViewById(R.id.view_camera).setOnClickListener(this);
        findViewById(R.id.view_file).setOnClickListener(this);
        findViewById(R.id.view_video).setOnClickListener(this);
        findViewById(R.id.view_photo).setOnClickListener(this);
        findViewById(R.id.view_location).setOnClickListener(this);
        findViewById(R.id.view_audio).setOnClickListener(this);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);//获取系统剪贴服务
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统软键盘管理类
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");//初始化一个wakelock

    }

    /**
     * 显示语音图标按钮
     * 最左侧语音图标点击事件
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btn_set_mode_keyboard.setVisibility(View.VISIBLE);
        btn_send.setVisibility(View.GONE);
        btn_more.setVisibility(View.VISIBLE);
        btn_press_to_speak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        ll_btn_container.setVisibility(View.VISIBLE);
        ll_face_container.setVisibility(View.GONE);

    }


    /**
     * 得到表情图片的drawable的id  R.drawable.f_static_0
     * @param getSum
     * @return
     */
    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 0; x <= getSum; x++) {
            String filename = "f_static_0" + x;
            reslist.add(filename);
        }
        return reslist;
    }

    /**
     * 获取表情的gridview的子view
     * 并设置表情的点击事件在EditText中显示
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 21);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(21, reslist.size()));
        } else if (i == 3) {
            list.addAll(reslist.subList(42, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (btn_set_mode_keyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.pgg.mywechat.Uitils.SmileUtils");
                            Field field = clz.getField(filename);
                            et_sendmessage.append(SmileUtils.getSmiledText(ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(et_sendmessage.getText())) {
                                int selectionStart = et_sendmessage.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = et_sendmessage.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            et_sendmessage.getEditableText().delete(i, selectionStart);
                                        else
                                            et_sendmessage.getEditableText().delete(selectionStart - 1, selectionStart);
                                    } else {
                                        et_sendmessage.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    /**
     * listview滑动监听listener
     *
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            switch (scrollState) {
//                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                    if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
//                        loadmorePB.setVisibility(View.VISIBLE);
//                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
//                        List<EMMessage> messages;
//                        try {
//                            // 获取更多messges，调用此方法的时候从db获取的messages
//                            // sdk会自动存入到此conversation中
//                            if (chatType == CHATTYPE_SINGLE)
//                                messages = conversation.loadMoreMsgFromDB(adapter
//                                        .getItem(0).getMsgId(), pagesize);
//                            else
//                                messages = conversation.loadMoreGroupMsgFromDB(
//                                        adapter.getItem(0).getMsgId(), pagesize);
//                        } catch (Exception e1) {
//                            loadmorePB.setVisibility(View.GONE);
//                            return;
//                        }
//                        try {
//                            Thread.sleep(300);
//                        } catch (InterruptedException e) {
//                        }
//                        if (messages.size() != 0) {
//                            // 刷新ui
//                            adapter.notifyDataSetChanged();
//                            listView.setSelection(messages.size() - 1);
//                            if (messages.size() != pagesize)
//                                haveMoreData = false;
//                        } else {
//                            haveMoreData = false;
//                        }
//                        loadmorePB.setVisibility(View.GONE);
//                        isloading = false;
//
//                    }
//                    break;
//            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    /**
     * 显示或隐藏图标按钮页
     * 最右侧加号点击事件
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            ll_btn_container.setVisibility(View.VISIBLE);
            ll_face_container.setVisibility(View.GONE);
        } else {
            if (ll_face_container.getVisibility() == View.VISIBLE) {
                ll_face_container.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 显示键盘图标
     * 最左侧键盘图标点击事件
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btn_set_mode_voice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        et_sendmessage.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        btn_press_to_speak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(et_sendmessage.getText())) {
            btn_more.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.GONE);
        } else {
            btn_more.setVisibility(View.GONE);
            btn_send.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public String getToChatUsername() {
        return toChatUsername;
    }

    private void initReceiver(){
        EMChatManager.getInstance().getChatOptions().setRequireDeliveryAck(true);
        //如果用到已发送的回执需要把这个flag设置成true
        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();//隐藏软键盘
        switch (v.getId()) {
            case R.id.ib_exit_01:
                Utils.finish(ChatActivity.this);
                break;
            case R.id.ib_right:
                //查看会话详细信息，是好友的话，点击查看好友详细信息，是群聊，点击查看群聊信息，跳转活动

                break;
            case R.id.view_camera:
                //工具栏拍照点击事件
                MoreLayoutCamera();
                break;
            case R.id.view_file:
                //工具栏文件点击事件
                MoreLayoutFile();
                break;
            case R.id.view_video:
                //工具栏视频通话点击事件
                MoreLayoutVideo();
                break;
            case R.id.view_photo:
                //工具栏照片点击事件
                MoreLayoutPhoto();
                break;
            case R.id.view_location:
                //工具栏发送位置点击事件
                MoreLayoutLocation();
                break;
            case R.id.view_audio:
                //工具栏语音点击事件
                MoreLayoutAudio();
                break;
            case R.id.iv_emoticons_normal:
                //打开表情框按钮
                more.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.INVISIBLE);
                iv_emoticons_checked.setVisibility(View.VISIBLE);
                ll_btn_container.setVisibility(View.GONE);
                ll_face_container.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.iv_emoticons_checked:
                //关闭表情框按钮
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                ll_btn_container.setVisibility(View.VISIBLE);
                ll_face_container.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                //发送消息
                String message = et_sendmessage.getText().toString();
                sendMessage(message);
                break;
            case R.id.btn_set_mode_voice:
                //发送语音按钮

                break;
        }
    }

    private void sendMessage(String content) {
        if (content.length() > 0) {

            setResult(RESULT_OK);
        }
    }

    private void MoreLayoutAudio() {
        //发送语音
    }

    private void MoreLayoutLocation() {
        //发送位置
    }

    private void MoreLayoutPhoto() {
        //发送图片

    }

    private void MoreLayoutVideo() {
        //视频通话
    }

    private void MoreLayoutFile() {
        //发送文件
    }

    private void MoreLayoutCamera() {
        //打开照相机

    }

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isDelivered = true;
                }
            }
        }
    };
}
