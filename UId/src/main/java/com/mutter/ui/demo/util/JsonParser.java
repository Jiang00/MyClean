package com.mutter.ui.demo.util;

import android.content.Context;
import android.util.Log;
import android.text.TextUtils;

import com.mutter.ui.demo.entry.CrossItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public static final String TAG = "JsonParser";

    private static CrossItem getEntry(Context context, String position, JSONObject parent, String tag) {
        JSONObject specify = parent.optJSONObject(position);
        return getEntry(context, specify, tag);
    }

    private static CrossItem getEntry(Context context, JSONObject parent, String tag) {
        if (parent == null) {
            return null;
        }
        CrossItem crossItem = new CrossItem();
        String pkg = parent.optString("package");
        crossItem.setPkgName(pkg);
        if (TextUtils.equals(pkg, context.getPackageName())) {
            return null;
        }
        crossItem.setPkgName(parent.optString("package"));
        crossItem.setIconUrl(parent.optString("icon"));
        crossItem.setTagIconUrl(parent.optString("icon_" + tag));
        crossItem.setHeadUrl(parent.optString("head"));
        crossItem.setAction(parent.optString("action"));
        crossItem.setTitle(parent.optString("title"));
        crossItem.setSubTitle(parent.optString("subTitle"));
        crossItem.setActionTextInstall(parent.optString("actionInstall"));
        crossItem.setActionTextOpen(parent.optString("actionApply"));
        crossItem.setActionLabelUrl(parent.optString("actionLabel"));
        return crossItem;
    }

    public static ArrayList<CrossItem> getCrossData(Context context, String data, String tag) {
        if (data == null || tag == null || context == null) {
            return null;
        }
        ArrayList<CrossItem> list = new ArrayList<>();
        try {
            JSONObject parent = new JSONObject(data);
            JSONObject sequence = parent.optJSONObject(tag);
            JSONArray array = sequence.optJSONArray("cross");
            int size = sequence.optInt("size");
            if (size == 0) {
                return list;
            }
            if (size > 1) {
                if (size > array.length()) {
                    size = array.length();
                }
                for (int i = 0; i < size; i++) {
                    CrossItem crossItem = getEntry(context, (String) array.get(i), parent, tag);
                    if (crossItem != null) {
                        list.add(crossItem);
                    }
                }
            } else if (size == 1) {
                int index = 0;
                try {
                    index = Utils.getCurrentCrossIndex(context, Constant.CROSS_INDEX_2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int length = array.length() - 1;
                if (index > length) {
                    index = length;
                }
                String pos = array.optString(index);
                CrossItem crossItem = getEntry(context, pos, parent, tag);
                if (index >= length) {
                    index = 0;
                } else {
                    ++index;
                }
                try {
                    Utils.updateCrossIndex(context, index, Constant.CROSS_INDEX_2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (crossItem != null) {
                    list.add(crossItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static CrossItem getSpecifyJsonObject(Context context, String data, String tag) {
        if (data == null || tag == null || context == null) {
            return null;
        }
        int index = 0;
        try {
            index = Utils.getCurrentCrossIndex(context, Constant.CROSS_INDEX_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject parent = new JSONObject(data);
            JSONObject sequence = parent.optJSONObject(tag);
            if (sequence == null) {
                Log.e(TAG, "not config tag,tag =" + tag);
                return null;
            }
            JSONArray array = sequence.optJSONArray("cross");
            if (array == null || array.length() == 0) {
                Log.e(TAG, "not config array,tag =" + tag);
                return null;
            }
            int length = array.length() - 1;
            if (index > length) {
                index = length;
            }
            String pos = array.optString(index);
            JSONObject object = parent.optJSONObject(pos);
            CrossItem item = getEntry(context, object, pos);
            if (index >= length) {
                index = 0;
            } else {
                ++index;
            }
            try {
                Utils.updateCrossIndex(context, index, Constant.CROSS_INDEX_1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
