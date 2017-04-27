package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.MyGridLayoutManager;
import com.supers.clean.junk.entity.JunkInfo;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/27.
 */

public class PictureActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    LinearLayout picture_item;

    private MyGridLayoutManager gridLayoutManager;
    private ArrayList<JunkInfo> datalist;


    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        picture_item = (LinearLayout) findViewById(R.id.picture_item);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_picture);
        title_name.setText(R.string.side_picture);
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        initData();

    }

    private void initData() {
        int dp4 = (int) getResources().getDimension(R.dimen.d4);
        int dp6 = (int) getResources().getDimension(R.dimen.d6);
        int dp8 = (int) getResources().getDimension(R.dimen.d8);
        datalist = new ArrayList<>();
//        datalist.add("aaa");
//        datalist.add("aaa");
//        datalist.add("aaa");
//        datalist.add("aaa");
//        datalist.add("aaa");
        addItemView(datalist, dp4, dp6, dp8);
        RecyclerView recycle_1 = new RecyclerView(this);
        recycle_1.setOverScrollMode(View.OVER_SCROLL_NEVER);

        recycle_1.setPadding(dp6, dp4, dp6, dp4);

        recycle_1.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_1.setLayoutManager(gridLayoutManager);
        recycle_1.setAdapter(new HomeAdapter());

        recycle_1.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recycle_2 = new RecyclerView(this);
        recycle_2.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycle_2.setPadding(dp6, dp4, dp6, dp4);
        recycle_2.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));

        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_2.setLayoutManager(gridLayoutManager);
        recycle_2.setAdapter(new HomeAdapter());
        recycle_2.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recycle_3 = new RecyclerView(this);
        recycle_3.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycle_3.setPadding(dp6, dp4, dp6, dp4);

        recycle_3.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_3.setLayoutManager(gridLayoutManager);
        recycle_3.setAdapter(new HomeAdapter());
        recycle_3.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recycle_4 = new RecyclerView(this);
        recycle_4.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycle_4.setPadding(dp6, dp4, dp6, dp4);

        recycle_4.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_4.setLayoutManager(gridLayoutManager);
        recycle_4.setAdapter(new HomeAdapter());
        recycle_4.setItemAnimator(new DefaultItemAnimator());
        picture_item.addView(recycle_1);
        picture_item.addView(recycle_2);
        picture_item.addView(recycle_3);
        picture_item.addView(recycle_4);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycle_1.getLayoutParams();
        layoutParams.bottomMargin = dp8;
        recycle_1.setLayoutParams(layoutParams);
        recycle_3.setLayoutParams(layoutParams);
        recycle_3.setLayoutParams(layoutParams);
        recycle_4.setLayoutParams(layoutParams);


    }

    private void addItemView(ArrayList<JunkInfo> list, int dp4, int dp6, int dp8) {
        RecyclerView recycleView = new RecyclerView(this);
        recycleView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycleView.setPadding(dp6, dp4, dp6, dp4);
        recycleView.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycleView.setLayoutManager(gridLayoutManager);
        recycleView.setAdapter(new HomeAdapter(list));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        picture_item.addView(recycleView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView.getLayoutParams();
        layoutParams.bottomMargin = dp8;
        recycleView.setLayoutParams(layoutParams);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:

                    break;
            }

        }
    };

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        ArrayList<JunkInfo> list;

        public HomeAdapter(ArrayList<JunkInfo> list) {
            this.list = list;

        }

        public HomeAdapter() {
            this.list = list;

        }

        public void upList(ArrayList<JunkInfo> list) {
            this.list = list;
        }

        @Override
        public PictureActivity.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PictureActivity.HomeAdapter.MyViewHolder holder = new PictureActivity.HomeAdapter.MyViewHolder(LayoutInflater.from(
                    PictureActivity.this).inflate(R.layout.layout_picture_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(PictureActivity.HomeAdapter.MyViewHolder holder, final int position) {
            holder.recyc_check.setImageResource(list.get(position).isChecked ? R.mipmap.power_4 : R.mipmap.power_5);
            holder.recyc_icon.setImageDrawable(list.get(position).icon);
            holder.recyc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).isChecked = !list.get(position).isChecked;
                    notifyDataSetChanged();
                }
            });
        }

        public void reChangesData(int position) {

            notifyItemRangeChanged(position, this.list.size() - position); //mList是数据

        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            FrameLayout recyc_item;
            ImageView recyc_icon;
            ImageView recyc_check;

            public MyViewHolder(View view) {
                super(view);
                recyc_item = (FrameLayout) view.findViewById(R.id.recyc_item);
                recyc_icon = (ImageView) view.findViewById(R.id.recyc_icon);
                recyc_check = (ImageView) view.findViewById(R.id.recyc_check);
            }
        }
    }
}
