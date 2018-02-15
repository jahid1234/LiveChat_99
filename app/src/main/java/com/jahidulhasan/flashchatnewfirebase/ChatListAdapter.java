package com.jahidulhasan.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Jahid on 1/11/2018.
 */

public class ChatListAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference databaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mdataSnapshotList;

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mdataSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity,DatabaseReference reference,String displayName){
        mActivity = activity;
        databaseReference = reference.child("messages");
        // retrive data from firebase
        databaseReference.addChildEventListener(listener);
        mDisplayName = displayName;
        mdataSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView sender;
        TextView messageBody;
        LinearLayout.LayoutParams params;
    }
    @Override
    public int getCount() {
        return mdataSnapshotList.size();
    }

    @Override
    public InstanceMessage getItem(int position) {
          DataSnapshot snapshot = mdataSnapshotList.get(position);
        // converts the Json from snapshots into InstanceMassege Object
        return snapshot.getValue(InstanceMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.sender = (TextView)convertView.findViewById(R.id.author);
            viewHolder.messageBody = (TextView)convertView.findViewById(R.id.message);
            viewHolder.params = (LinearLayout.LayoutParams)viewHolder.sender.getLayoutParams();
            convertView.setTag(viewHolder);
        }

        final InstanceMessage instanceMessage = getItem(position); // getItem returns an Instance Message object everyTime it get into getView();
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        boolean isMe = instanceMessage.getSender().equals(mDisplayName);
        setChatAppearance(isMe,holder);

        String author = instanceMessage.getSender();
        String msg = instanceMessage.getMessage();
        if(!isMe) {
            holder.sender.setText(author);
        }else {
            holder.sender.setText("");
        }
        holder.messageBody.setText(msg);

        return convertView;
    }

    private void setChatAppearance(boolean isItMe,ViewHolder holder){
        if(isItMe){
            holder.params.gravity = Gravity.END;
            holder.messageBody.setBackgroundResource(R.drawable.bubble2);
        }else {

            holder.params.gravity = Gravity.START;
            holder.sender.setTextColor(Color.MAGENTA);
            holder.messageBody.setBackgroundResource(R.drawable.bubble1);
        }
        holder.sender.setLayoutParams(holder.params);
        holder.messageBody.setLayoutParams(holder.params);

    }

    public void cleanUp(){
        databaseReference.removeEventListener(listener);
    }

}
