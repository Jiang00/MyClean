package com.eos.manager.page;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.client.AndroidSdk;
//import com.android.client.ClientNativeAd;
import com.android.client.AndroidSdk;
import com.eos.lib.customview.MyWidgetContainer;
import com.eos.lib.customview.SecurityBaseFragment;
import com.eos.manager.App;
import com.eos.manager.AppLock;
import com.eos.manager.SearchView;
import com.eos.manager.Tracker;
import com.eos.manager.lib.Utils;
import com.eos.manager.lib.controller.CListViewAdaptor;
import com.eos.manager.lib.controller.CListViewScroller;
import com.eos.lib.customview.SecurityloadImage;
import com.privacy.lock.R;
import com.eos.manager.SearchThread;
import com.privacy.lock.aidl.IWorker;
import com.eos.manager.db.SecurityProfileHelper;
import com.eos.manager.lib.io.RefreshList;
import com.eos.manager.meta.MApps;
import com.eos.manager.meta.SecuritProfiles;
import com.eos.manager.meta.SecurityMyPref;
import com.sample.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by SongHualin on 6/24/2015.
 */
public class AppFragementSecurity extends SecurityBaseFragment implements RefreshList {
    public static final String PROFILE_ID_KEY = "profile_id";
    public static final String PROFILE_NAME_KEY = "profile_name";
    public static final String PROFILE_HIDE = "hide";
    private SearchView searchview;

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;

    private CListViewScroller scroller;
    private CListViewAdaptor adaptor;
    private SecurityProfileHelper.ProfileEntry profileEntry;
    private SQLiteDatabase db;

    private View headerView;

    private SecuritySharPFive shareFive;

    private View headPlan;

    public static String SearchText = "";

    private static List<SearchThread.SearchData> apps;
    private List<SearchThread.SearchData> searchResult;

    private int count = 0;
    private boolean hide;
    protected String tuiguang = "com.eosmobi.applock";
    protected String tuiguang1 = "com.eosmobi.flashlight.free";
    private LottieAnimationView lot_applock;
    private FrameLayout fl_lot_applock;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PROFILE_NAME_KEY, profileEntry.name);
        outState.putLong(PROFILE_ID_KEY, profileEntry.id);
        outState.putBoolean(PROFILE_HIDE, hide);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        onArguments(savedInstanceState);

    }

    @Override
    protected void onArguments(Bundle arguments) {
        profileEntry = new SecurityProfileHelper.ProfileEntry();
        profileEntry.id = arguments.getLong(PROFILE_ID_KEY);
        profileEntry.name = arguments.getString(PROFILE_NAME_KEY);
        hide = arguments.getBoolean(PROFILE_HIDE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        updateLocks();
    }


    private void updateLocks() {
        db = SecurityProfileHelper.singleton(getActivity()).getWritableDatabase();
        if (profileEntry.name != null) {
            try {
                locks = SecurityProfileHelper.ProfileEntry.getLockedApps(db, profileEntry.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.security_myapp_list, container, false);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        listView = (ListView) v.findViewById(R.id.abs_list);
        shareFive = new SecuritySharPFive(getActivity());
        headerView = inflater.inflate(R.layout.security_main_title_rate, null);
        refreshLayout.setColorSchemeResources(R.color.security_theme_accent_2, R.color.security_theme_accent_1);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MApps.setWaiting(action);
            }
        });
        scroller = new CListViewScroller(listView);

        showAdOrFive();
        setAdaptor();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                {
                    int count = listView.getHeaderViewsCount();
                    if (count > 0) {
                        which--;
                    }
                    List<SearchThread.SearchData> list = searchResult == null ? apps : searchResult;
                    if (which >= list.size()) return;
                    SearchThread.SearchData data = list.get(which);
                    dirty = true;
                    if (toast != null) {
                        toast.cancel();
                    }
                    String pkgName = data.pkg;

                    if (locks.containsKey(pkgName)) {
                        locks.remove(pkgName);
                        toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.security_unlock_success, data.label), Toast.LENGTH_SHORT);
                        Tracker.sendEvent(Tracker.ACT_APPLOCK, Tracker.ACT_APPLOCK_UNLOCK, data.label + "", 1L);

                    } else {

                        toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.security_lock_success, data.label), Toast.LENGTH_SHORT);
                        locks.put(pkgName, true);
                        Tracker.sendEvent(Tracker.ACT_APPLOCK, Tracker.ACT_APPLOCK_LOCK, data.label + "", 1L);

                    }
                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.lock.setEnabled(locks.containsKey(pkgName));
                    toast.show();
                }

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lot_applock != null) {
            lot_applock.playAnimation();
        }
        MApps.setWaiting(action);
        updateLocks();
    }

    public boolean forOnback() {

        if (SearchText.equals("")) {
            return false;
        } else {
            searchview.clearText();
            SearchText = "";
            searchResult = filter((ArrayList<SearchThread.SearchData>) MApps.getApps(locks), "");
            refreshUI(refreshSearchResult);
            if (!shareFive.getFiveRate()) {
                headPlan.setVisibility(View.VISIBLE);
            }
            return true;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (lot_applock != null) {
            lot_applock.pauseAnimation();
        }
    }

    void showAdOrFive() {
        listView.addHeaderView(headerView);

        headerClick(headerView);

    }

    public void saveOrCreateProfile(String profileName, IWorker server) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(locks.keySet());
        try {
            if (profileEntry.name == null) {
                profileEntry.id = SecurityProfileHelper.ProfileEntry.createProfile(db, profileName, list);
                profileEntry.name = profileName;
                SecuritProfiles.addProfile(profileEntry);
            } else {
                SecurityProfileHelper.ProfileEntry.updateProfile(db, profileEntry.id, list);
                if (server != null) {
                    server.notifyApplockUpdate();
                }
            }
            dirty = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        db = null;
        super.onDestroy();
    }


    private void setAdaptor() {

        adaptor = new CListViewAdaptor(scroller, R.layout.security_apps_item) {

            private void updateUI(int position, ViewHolder h, boolean forceLoading) {
                apps = MApps.getApps(locks);
                if (apps.size() != 0) {
                    List<SearchThread.SearchData> list = searchResult == null ? apps : searchResult;
                    if (position >= list.size()) return;
                    SearchThread.SearchData data = list.get(position);
                    String pkgName = data.pkg;
                    h.icon.setImageIcon(pkgName, forceLoading);
                    h.name.setText(data.label);

                    if (SecurityMyPref.getVisitor()) {
                        h.lock.setImageResource(R.drawable.security_lock_bg);

                    } else {
                        h.lock.setImageResource(R.drawable.security_lock_bg_two);
                    }
                    h.lock.setImageResource(R.drawable.security_lock_bg);
                    h.lock.setEnabled(locks.containsKey(pkgName));
                }

            }

            @Override
            protected void onUpdate(int position, Object holder, boolean scrolling) {
                ViewHolder h = (ViewHolder) holder;
                updateUI(position, h, !scrolling);
            }

            @Override
            protected Object getHolder(View root) {
                return new ViewHolder(root);
            }

            @Override
            public int getCount() {
                if (searchResult == null) {
                    return hide ? count : (count);
                } else {
                    return searchResult.size();
                }
            }
        };

        listView.setAdapter(adaptor);

    }

    @Override
    public void refresh() {
        if (adaptor != null) {
            adaptor.notifyDataSetChanged();
        }
    }


    class ViewHolder {
        public SecurityloadImage icon;

        public TextView name;

        public ImageView lock;

        public ViewHolder(View root) {
            icon = (SecurityloadImage) root.findViewById(R.id.icon);
            name = (TextView) root.findViewById(R.id.name);
            lock = (ImageView) root.findViewById(R.id.lock);
        }
    }

    Map<String, Boolean> locks = new HashMap<>();
    boolean dirty = false;

    Toast toast;


    Runnable action = new Runnable() {
        @Override
        public void run() {
            apps = hide ? MApps.getHiddenApps(locks) : MApps.getApps(locks);
            count = apps.size();
            listView.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
            Utils.notifyDataSetChanged(listView);
        }
    };

    public static void refreshUI(Runnable action) {
        App.runOnUiThread(action);
    }


    Runnable refreshSearchResult = new Runnable() {
        @Override
        public void run() {
            Utils.notifyDataSetChanged(listView);
        }
    };


    private void headerClick(final View headerView) {

        headPlan = headerView.findViewById(R.id.five_plan);


        if (!shareFive.getFiveRate()) {
            headPlan.setVisibility(View.VISIBLE);
        } else {
            headPlan.setVisibility(View.GONE);

        }


        headerView.findViewById(R.id.security_bad_tit).setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(final View v) {
                headPlan.setVisibility(View.GONE);
                shareFive.setFiveRate(true);
                getActivity().finish();
                Intent intent = new Intent(getActivity(), AppLock.class);
                startActivity(intent);
                Tracker.sendEvent(Tracker.ACT_GOOD_RATE, Tracker.ACT_GOOD_RATE_GOOD, Tracker.ACT_PERMISSION_CANCLE, 1L);


            }
        });


        headerView.findViewById(R.id.security_good_tit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                shareFive.setFiveRate(true);
                Utils.rate(getActivity());

                if (!Utils.isEMUI()) {
                    View alertDialogView = View.inflate(v.getContext(), R.layout.security_rate_result, null);
                    final MyWidgetContainer w = new MyWidgetContainer(getActivity(), MyWidgetContainer.MATCH_PARENT, MyWidgetContainer.MATCH_PARENT, MyWidgetContainer.PORTRAIT);
                    w.addView(alertDialogView);
                    w.addToWindow();

                    w.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            w.removeAllViews();
                            w.removeFromWindow();

                        }
                    });
                }


                headPlan.setVisibility(View.GONE);

                Tracker.sendEvent(Tracker.ACT_GOOD_RATE, Tracker.ACT_GOOD_RATE_GOOD, Tracker.ACT_GOOD_RATE_GOOD, 1L);

            }
        });

        searchview = (SearchView) headerView.findViewById(R.id.search_id);
        lot_applock = (LottieAnimationView) headerView.findViewById(R.id.lot_applock);
        fl_lot_applock = (FrameLayout) headerView.findViewById(R.id.fl_lot_applock);
        tuiGuang();
        lot_applock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPkgInstalled(tuiguang, getActivity().getPackageManager())) {
                    AndroidSdk.track("applock页面", "推广applock点击", "", 1);
                    Utils.openPlayStore(getActivity(), tuiguang);
                } else if (!isPkgInstalled(tuiguang1, getActivity().getPackageManager())) {
                    AndroidSdk.track("applock页面", "推广手电筒点击", "", 1);
                    Utils.openPlayStore(getActivity(), tuiguang1);
                }
            }
        });
        searchview.setQueryTextChange(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onStartSearch() {
                headPlan.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResult = filter((ArrayList<SearchThread.SearchData>) MApps.getApps(locks), newText);
                refreshUI(refreshSearchResult);
                SearchText = newText;
                return true;
            }
        });


    }

    public void tuiGuang() {
        String extraData = AndroidSdk.getExtraData();
        try {
            JSONObject json = new JSONObject(extraData);
            if (json.has("tuiguang")) {
                JSONArray array = json.getJSONArray("tuiguang");
                tuiguang = array.getString(0);
                tuiguang1 = array.getString(1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!isPkgInstalled(tuiguang, getActivity().getPackageManager())) {
            lot_applock.setImageAssetsFolder(null, "images/applocks/");
            lot_applock.setAnimation(null, "applocks.json");
            lot_applock.loop(true);
            lot_applock.playAnimation();

        } else if (!isPkgInstalled(tuiguang1, getActivity().getPackageManager())) {
            lot_applock.setImageAssetsFolder(null, "images/flashs/");
            lot_applock.setAnimation(null, "flashs.json");
            lot_applock.loop(true);
            lot_applock.playAnimation();

        } else {
            fl_lot_applock.setVisibility(View.GONE);
        }
    }

    //是否安装该应用
    public static boolean isPkgInstalled(String pkgName, PackageManager pm) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<SearchThread.SearchData> filter(ArrayList<SearchThread.SearchData> models, String query) {
        query = query.toLowerCase();
        ArrayList<SearchThread.SearchData> oneEntity = null;
        for (int i = 0; i < models.size(); i++) {
            oneEntity = new ArrayList<SearchThread.SearchData>();
            ArrayList<SearchThread.SearchData> app = (ArrayList<SearchThread.SearchData>) MApps.getApps(locks);
            for (SearchThread.SearchData enity : app) {
                final String text = enity.label.toLowerCase();
                if (text.contains(query)) {
                    oneEntity.add(enity);
                }
            }
            searchResult = oneEntity;
        }

        return oneEntity;
    }


}



