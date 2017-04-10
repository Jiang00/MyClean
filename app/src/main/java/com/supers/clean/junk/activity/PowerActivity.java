package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/4/7.
 */

public class PowerActivity extends BaseActivity {
    RecyclerView power_recycler;
    TextView power_size;
    private GridLayoutManager mLayoutManager;
    private List<String> mDatas;
    private HomeAdapter homeAdapter;

    @Override
    protected void findId() {
        super.findId();
        power_recycler = (RecyclerView) findViewById(R.id.power_recycler);
        power_size = (TextView) findViewById(R.id.power_size);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        mDatas = new ArrayList<>();
        initData();
        power_size.setText(getString(R.string.power_1, mDatas.size() + ""));
        mLayoutManager = new GridLayoutManager(this, 4);//设置为一个4列的纵向网格布局
        power_recycler.setLayoutManager(mLayoutManager);
        power_recycler.setAdapter(homeAdapter = new HomeAdapter());
        // 设置item动画
        power_recycler.setItemAnimator(new DefaultItemAnimator());
        homeAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatas.remove(position);
                        homeAdapter.notifyItemRemoved(position);
                        homeAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initData() {
        mDatas.add("a");
        mDatas.add("b");
        mDatas.add("c");
        mDatas.add("d");
        mDatas.add("e");
        mDatas.add("f");
        mDatas.add("g");
        mDatas.add("h");
        mDatas.add("i");
        mDatas.add("j");
        mDatas.add("k");
        mDatas.add("l");
        mDatas.add("m");
        mDatas.add("n");
        mDatas.add("o");
    }

    interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {


        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PowerActivity.this).inflate(R.layout.layout_power_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(mDatas.get(position));
            if (mOnItemClickLitener != null) {
                mOnItemClickLitener.onItemClick(holder.recyc_item, position);
            }

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            FrameLayout recyc_item;
            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                recyc_item = (FrameLayout) view.findViewById(R.id.recyc_item);
                tv = (TextView) view.findViewById(R.id.tv);
            }
        }
    }
}
