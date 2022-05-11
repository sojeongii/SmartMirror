package com.example.smartmirror;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// 사용자 아이템 - top, outer, bottom, onepiece 보여줌

public class UserItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ListItem> listItems = new ArrayList<ListItem>();

    String category; // top, bottom, outer, onepiece -> table name

    public UserItemAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) { return listItems.get(i); }
    public void setCate(String category) {
        this.category = category;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    public void addItem(String url,String ID) // image url, category(top,bottom,onepiece,outer), ID
    {
        ListItem listItem=new ListItem();
        listItem.setImage(url);
        //listItem.setCategory(category);
        listItem.setID(ID);
        listItems.add(listItem);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_stylingitem, parent, false);
        }
        Log.e("categor==========","adapter");
        ListItem listItem = listItems.get(position);
        Log.e("categor==========",category);
        ImageButton doneCheck = (ImageButton) convertView.findViewById(R.id.stylingLink); // stylinglink- 여기서 버튼을 누르게 되면 사용자가 사용자 옷에 대한 속성을 수정할 수 있음
        Glide.with(context).load(listItem.getImage()).override(800,800).into(doneCheck);
        doneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ChangeUserItemProperty.class);

                intent.putExtra("category",category);
                intent.putExtra("ID",listItem.getID());

                context.startActivity(intent);
            }
        });
        return convertView;
    }
}