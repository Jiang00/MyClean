package com.fraumobi.call.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fraumobi.call.Utils.Constants;
import com.fraumobi.call.adapter.BlockObjectListAdapter;
import com.fraumobi.call.database.Database;
import com.fraumobi.call.entries.Contact;
import com.fraumobi.call.record.R;
import com.fraumobi.call.tools.ACache;
import com.fraumobi.call.tools.sort.SideBar;

import java.util.ArrayList;


/**
 * Created by ${} on 2017/8/24.
 */

public class CallNameActivity extends BaseActivity implements SideBar.ISideBarSelectCallBack, TextWatcher {

    RecyclerView mRecyclerView;
    FrameLayout title_left;
    SideBar sideBar;
    private ArrayList<Contact> allContactList;
    private BlockObjectListAdapter blockObjectListAdapter;

    protected void findId() {
        mRecyclerView = (RecyclerView) findViewById(R.id.blockObjectList);
        sideBar = (SideBar) findViewById(R.id.contacts_side_bar);
        title_left = (FrameLayout) findViewById(R.id.title_left);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_call_name);
        findId();
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBlockObjectListData();

        sideBar.setOnStrSelectCallBack(this);
    }

    private void getBlockObjectListData() {
        allContactList = (ArrayList<Contact>) ACache.get(this).getAsObject("contacts");
        if (allContactList == null || allContactList.size() == 0) {
//            Toast.makeText(this, getString(R.string.no_contacts), Toast.LENGTH_SHORT).show();
            return;
        }
        Database database = Database.getInstance(this);
        SQLiteDatabase db = database.getWritableDatabase();
        ArrayList<Contact> blockObjectList = database.getDataFromTableContacts(db, Constants.TABLE_BLOCK);
        db.close();
        ArrayList<String> idList = new ArrayList<>();
        for (Contact contact : blockObjectList) {
            idList.add(contact.phoneNum);
        }
        blockObjectListAdapter = new BlockObjectListAdapter(this, allContactList, idList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(blockObjectListAdapter);
    }

    @Override
    public void onSelectStr(int index, String selectStr) {
        if (allContactList == null) {
            return;
        }
        for (int i = 0; i < allContactList.size(); i++) {
            if (selectStr.equalsIgnoreCase(allContactList.get(i).getFirstLetter())) {
                mRecyclerView.scrollToPosition(i); // select the place where the first word is in;
                return;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
