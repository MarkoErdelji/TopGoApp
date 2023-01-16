package com.example.uberapp_tim6.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.DocumentInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.NavItem;

import java.util.ArrayList;

public class DriverDocumentDialogAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<DocumentInfoDTO> mNavItems;


    public DriverDocumentDialogAdapter(Context context, ArrayList<DocumentInfoDTO> navItems){
        mContext = context;
        mNavItems = navItems;
    }
    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.document_dialog_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItems.get(i).getName() );

        Glide.with(mContext).load(mNavItems.get(i).getDocumentImage()).into(iconView);

        return view;
    }
}
