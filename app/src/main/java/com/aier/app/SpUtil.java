package com.aier.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

public class SpUtil {
    private static final String SP= "sharedpreference";

    private SpUtil() {
    }

    private static SpUtil instace = new SpUtil();
    private static SharedPreferences mSp = null;

    public static SpUtil getInstace() {

        if (mSp == null) {
            mSp = MyApplication.getContext().getSharedPreferences(SP, Context.MODE_PRIVATE);
        }
        return instace;
    }

    /**
     * 保存数据
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, Object value) {

        if (value instanceof String) {
            mSp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    // 读取String类型数据
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    // 读取boolean类型数据
    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    // 读取int类型数据
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

}
