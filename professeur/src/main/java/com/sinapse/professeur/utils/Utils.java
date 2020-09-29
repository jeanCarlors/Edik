package com.sinapse.professeur.utils;

import com.google.firebase.Timestamp;
import com.sinapse.libmodule.beans.CourseSessionMessage;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, Object> CourseSessionMessaageToMap(final CourseSessionMessage csm) {
        Map<String, Object> map = new HashMap<>();
        map.put("from_name", csm.getFromName());
        map.put("from_uid", csm.getFromUID());
        map.put("type", csm.getType());
        map.put("message", csm.getContent());
        map.put("created_date", Timestamp.now());
        map.put("media_url", csm.getContentUrl());
        return map;
    }
}
