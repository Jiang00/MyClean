package com.security.manager;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.privacy.lock.R;

/**
 * Android自定义SearchView
 * Created by mt on 2016-11-17.
 */

public class SearchView extends LinearLayout implements TextWatcher, View.OnClickListener {
    /**
     * 输入框
     */
    public static KeyPreImeEditText et_search;
    /**
     * 输入框后面的那个清除按钮
     */
    private Button bt_clear;

    private Button search_quan;

    public static OnQueryTextListener mylistener;


    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**加载布局文件*/
        LayoutInflater.from(context).inflate(R.layout.pub_searchview, this, true);
        /***找出控件*/
        et_search = (KeyPreImeEditText) findViewById(R.id.app_search);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        search_quan=(Button) findViewById(R.id.search_quan);
        bt_clear.setVisibility(GONE);
        et_search.addTextChangedListener(this);
        bt_clear.setOnClickListener(this);

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /****
     * 对用户输入文字的监听
     *
     * @param editable
     */
    @Override
    public void afterTextChanged(Editable editable) {
        mylistener.onStartSearch();

        /**获取输入文字**/
        String input = et_search.getText().toString().trim();


        if (input.isEmpty()) {
            bt_clear.setVisibility(GONE);
            search_quan.setVisibility(View.VISIBLE);
        } else {
            search_quan.setVisibility(View.GONE);

            bt_clear.setVisibility(VISIBLE);
            mylistener.onQueryTextChange(input);

        }
    }

    @Override
    public void onClick(View view) {
        et_search.setText("");
        mylistener.onQueryTextChange("");

    }


    public static void clearText(){
        et_search.setText("");

    }

    public interface OnQueryTextListener {

        boolean onStartSearch();


        boolean onQueryTextChange(String newText);
    }


    public void setQueryTextChange(OnQueryTextListener lintener) {
        this.mylistener = lintener;
    }


    public static void clearValur() {
        String input = et_search.getText().toString().trim();
        if (input == null) {
            mylistener.onQueryTextChange("");

        } else {
            mylistener.onQueryTextChange(input);

        }


    }


}
