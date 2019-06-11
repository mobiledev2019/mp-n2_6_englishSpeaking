package com.lamtt.learnenglish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lamtt.learnenglish.R;
import com.lamtt.learnenglish.object.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    List<Category> categoryList;
    ViewHolder viewHolder;

    public class ViewHolder {
        ImageView iv_Icon;
        TextView tvTitle;
        FrameLayout frameLayout;
    }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public int getCount() {
        return this.categoryList.size();
    }

    public Object getItem(int position) {
        return this.categoryList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            this.viewHolder = new ViewHolder();
            view = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.adapter_category, parent, false);
            this.viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_vi);
            this.viewHolder.iv_Icon = (ImageView) view.findViewById(R.id.iv_icon);
            this.viewHolder.frameLayout = view.findViewById(R.id.frame_layout);
            view.setTag(this.viewHolder);
        } else {
            this.viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(context).load(getImage(categoryList
                .get(position).getTag())).into(viewHolder.iv_Icon);

        this.viewHolder.tvTitle.setText(((Category) this.categoryList.get(position)).getVi());
        if (categoryList.get(position).getIsActive() == 1) {
            viewHolder.frameLayout.setVisibility(View.GONE);
        }else {
            viewHolder.frameLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public Drawable convertStringToDrawable(String abc) {
        return this.context.getResources().getDrawable
                (this.context.getResources().getIdentifier(abc, "drawable", this.context.getPackageName()));
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable",
                context.getPackageName());

        return drawableResourceId;
    }

}
