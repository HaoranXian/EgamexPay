package com.baidu.BaiduMap.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.baidu.BaiduMap.json.SetEntity;

import android.content.Context;

public class ACacheUtils implements Serializable {
    private static ACacheUtils aCacheUtils = null;
    private static ACache mACache = null;

    public static ACacheUtils getInstance(Context context) {
        if (aCacheUtils == null) {
            aCacheUtils = new ACacheUtils(context);
        }
        return aCacheUtils;
    }

    public ACacheUtils(Context context) {
        mACache = ACache.get(context);
    }

    public void putPhoneData(ArrayList<String> arrayList) {
        if (null != mACache) {
            mACache.put("phoneList", arrayList);
        } else {
            mACache.put("phoneList", "");
        }
    }

    public void putMessageData(ArrayList<String> arrayList) {
        if (null != mACache) {
            mACache.put("messageList", arrayList);
        } else {
            mACache.put("messageList", "");
        }
    }

    public ArrayList<String> getPhoneData() {
        if (null != mACache) {
            return (ArrayList<String>) mACache.getAsObject("phoneList");
        }
        return null;
    }

    public ArrayList<String> getMessageData() {
        if (null != mACache) {
            return (ArrayList<String>) mACache.getAsObject("messageList");
        }
        return null;
    }

    public void putSetEntity(SetEntity setEntity) {
        if (null != mACache) {
            mACache.put("setEntity", setEntity);
        } else {
            mACache.put("setEntity", "");
        }
    }

    public SetEntity getSetEntity() {
        if (null != mACache) {
            return (SetEntity) mACache.getAsObject("setEntity");
        }
        return null;
    }

    public String getOtherNeedUrl() {
        if (null != mACache) {
            return mACache.getAsString("otherNeedUrl");
        }
        return null;
    }

    public String getSendParam() {
        if (null != mACache) {
            return mACache.getAsString("sendParam");
        }
        return null;
    }

    public void putList(List list) {
        if (null != mACache) {
            mACache.put("list", (Serializable) list);
        } else {
            mACache.put("list", "");
        }
    }

    public List<String> getList() {
        if (null != mACache) {
            return (List<String>) mACache.getAsObject("list");
        }
        return null;
    }
}
