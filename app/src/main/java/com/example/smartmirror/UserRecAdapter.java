package com.example.smartmirror;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserRecAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    public UserRecAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_user_recommendation, parent, false);
        }

        ListItem listItem = listItems.get(position);
//        final TextView titleTextView = (TextView) convertView.findViewById(R.id.stylingTitle);

        ImageView outer = (ImageView) convertView.findViewById(R.id.stylingRecOuter);
        if(!listItem.getOuter().equals(""))
        {
            Glide.with(context).load(listItem.getOuter()).override(800, 800).into(outer);
        }

        ImageView top = (ImageView) convertView.findViewById(R.id.stylingRecTop);
        Glide.with(context).load(listItem.getTop()).override(800, 800).into(top);

        ImageView bottom = (ImageView) convertView.findViewById(R.id.stylingRecBottom);
        Glide.with(context).load(listItem.getBottom()).override(800, 800).into(bottom);

        //virtual fitting -> 사용자 옷으로 매칭된 추천 스타일링은 가상피팅을 위해 이미지 url을 php로 보냄(-이러면 안됨)(*무신사 스타일링은 id와 table을 php로 보내서 php에서 db에 접근)
        Button fitting = (Button) convertView.findViewById(R.id.virtualFitting);
        fitting.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onClick(View view) {
                //String id = listItem.getID();
                String outer= listItem.getOuterID();
                String top= listItem.getTopID();
                String bottom= listItem.getBottomID();
                String address;
                if(!listItem.getOuter().equals(""))
                {
                    address="http://52.79.59.24/user_clothes_fitting.php?CASE=3&OUTER="+outer+"&TOP="+top+"&BOTTOM="+bottom;
                }
                else
                {
                    address="http://52.79.59.24/userClothesFitting.php?CASE=2&TOP="+top+"&BOTTOM="+bottom;
                }
                virtualfittingThread fitting_thread = new virtualfittingThread();
                try {
                    String result=fitting_thread.execute("lin"+address).get();
                    Log.e("[socket result]",result);
                    if(result.equals("fitting"))
                    {
                        Log.e("[toast]","fitting");
                        Toast.makeText(context.getApplicationContext(), "스마트미러로 스타일링을 확인해보세요!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }
    public void addItem(String outer, String top, String bottom,String outerID,String topID,String bottomID) {
        ListItem listItem = new ListItem();

        listItem.setOuter(outer);
        listItem.setTop(top);
        listItem.setBottom(bottom);

        listItem.setOuterID(outerID);
        listItem.setTopID(topID);
        listItem.setBottomID(bottomID);

        listItems.add(listItem);
    }



}