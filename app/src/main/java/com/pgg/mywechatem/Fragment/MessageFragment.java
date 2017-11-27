package com.pgg.mywechatem.Fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.pgg.mywechatem.Activity.Chat_Activity.ChatActivity;
import com.pgg.mywechatem.Activity.Chat_Activity.PublishMsgListActivity;
import com.pgg.mywechatem.Activity.MainActivity;
import com.pgg.mywechatem.Adapter.MyMessageFragAdapter;
import com.pgg.mywechatem.Domian.GroupInfo;
import com.pgg.mywechatem.Domian.PublicMsgInfo;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.GloableParams;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.NetUtil;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;


/**
 * Created by PDD on 2017/11/16.
 */

public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private TextView txt_nochat;
    private ListView list_of_wechat_message;
    private MyMessageFragAdapter adapter;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private View view;
    private RelativeLayout errorItem;

    @Override
    public View initView() {
        view = Utils.getView(R.layout.fragment_wechat);
        txt_nochat = view.findViewById(R.id.txt_nochat);
        list_of_wechat_message = view.findViewById(R.id.list_of_wechat_message);
        errorItem = view.findViewById(R.id.rl_error_item);
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        conversationList.clear();
        initViews();
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        initViews();
    }

    private void initViews() {
        conversationList.addAll(loadConversationsWithRecentChat());
        if (conversationList != null && conversationList.size() > 0) {
            txt_nochat.setVisibility(View.GONE);
            adapter = new MyMessageFragAdapter(getActivity(),conversationList);
            list_of_wechat_message.setAdapter(adapter);
        } else {
            view.findViewById(R.id.txt_nochat).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取所有会话
     *
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMConversation> list = new ArrayList<EMConversation>();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0)
                list.add(conversation);
        }
        // 排序
        sortConversationByLastChatTime(list);
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     */
    private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(final EMConversation con1,
                               final EMConversation con2) {

                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        list_of_wechat_message.setOnItemClickListener(this);
        errorItem.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.PublicMsg != null && position == 0) {
            // 打开订阅号列表页面
            Utils.start_Activity(getActivity(), PublishMsgListActivity.class);
        } else {
            ((MainActivity) getActivity()).updateUnreadLabel();
            EMConversation conversation = conversationList.get(position);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            Hashtable<String, String> ChatRecord = adapter.getChatRecord();
            if (ChatRecord != null) {
                if (conversation.isGroup()) {
//                    GroupInfo info = GloableParams.GroupInfos.get(conversation.getUserName());
//                    if (info != null) {
//                        intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
//                        intent.putExtra(Constants.GROUP_ID, info.getGroup_id());
//                        intent.putExtra(Constants.NAME, info.getGroup_name());// 设置标题
//                        getActivity().startActivity(intent);
//                    } else {
//                        intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
//                        intent.putExtra(Constants.GROUP_ID, info.getGroup_id());
//                        intent.putExtra(Constants.NAME, R.string.group_chats);// 设置标题
//                        getActivity().startActivity(intent);
//                    }
                    intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
                    intent.putExtra(Constants.GROUP_ID, conversation.getUserName());
                    intent.putExtra(Constants.NAME,  R.string.group_chats);// 设置标题
                    getActivity().startActivity(intent);

                } else {
//                    User user = GloableParams.Users.get(conversation.getUserName());
//                    if (user != null) {
//                        intent.putExtra(Constants.NAME, user.getUserName());// 设置昵称
//                        intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
//                        intent.putExtra(Constants.User_ID, conversation.getUserName());
//                        getActivity().startActivity(intent);
//                    } else {
//                        intent.putExtra(Constants.NAME, "好友");
//                        intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
//                        intent.putExtra(Constants.User_ID, conversation.getUserName());
//                        getActivity().startActivity(intent);
//                    }
                    intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
                    intent.putExtra(Constants.GROUP_ID, conversation.getUserName());
                    intent.putExtra(Constants.NAME,  R.string.group_chats);// 设置标题
                    getActivity().startActivity(intent);
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_error_item:
                NetUtil.openSetNetWork(getActivity());
                break;
            default:
                break;
        }
    }
}
