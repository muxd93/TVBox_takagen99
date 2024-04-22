package com.github.tvbox.osc.util;

import android.content.res.AssetManager;

import com.github.tvbox.osc.base.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class EpgUtil {

    private static JsonObject epgDoc = null;
    private static HashMap<String, JsonObject> epgHashMap = new HashMap<>();
    private static HashMap<String, JsonObject> epgIdHashMap = new HashMap<>();

    public static void init() {
        if (epgDoc != null)
            return;

        //credit by 龍
        try {
            String epg_data = assets2StringBuilder("epg_data.json");
            //String channel_id = assert2StringBuilder("channel_id.json");

            if (!epg_data.isEmpty()) {
                epgDoc = new Gson().fromJson(epg_data, (Type) JsonObject.class);// 从builder中读取了json中的数据。
                //JsonObject channelDoc = new Gson().fromJson(channel_id, (Type) JsonObject.class);// 从builder中读取了json中的数据。
                for (JsonElement opt : epgDoc.get("epgs").getAsJsonArray()) {
                    JsonObject obj = (JsonObject) opt;
                    String name = obj.get("name").getAsString().trim();
                    String epg_id = obj.get("epgid").getAsString().trim();
                    epgIdHashMap.put(epg_id, obj);

                    //String temp_channel_id = obj.get("channel_id") != null ? obj.get("channel_id").getAsString().trim() : "0";
                    //if (obj.get("channel_id") == null) {
                    //    LogUtil.i("channel name with no channel_id: "+name);
                    //}
                    String[] names = name.split(",");
                    /*for (JsonElement jsonElement : channelDoc.get("channels").getAsJsonArray()) {
                        JsonObject jsonObject = (JsonObject) jsonElement;
                        String temp_channel_id = jsonObject.get("channel_id").getAsString().trim();
                        String temp_epd_id = jsonObject.get("epd_id").getAsString().trim();
                        if (epg_id.equals(temp_epd_id)) {
                            obj.addProperty("channel_id", temp_channel_id.isEmpty() ? "unknown" : temp_channel_id);
                            break;
                        }
                    }*/
                    for (String string : names) {
                        epgHashMap.put(string, obj);
                        //LogUtil.v("epgHashMap: string: " +string +", obj: "+obj);
                    }
                }
                //FileIOUtils.writeFileFromString(getExternalDownloadsPath()+"/epg_data.json", epgDoc.toString());
                //LogUtil.v("epgDoc:"+epgDoc);
            }

        } catch (IOException e) {
            LogUtil.e(e);
        }
    }

    private static String assets2StringBuilder(String fileName) throws IOException {
        AssetManager assetManager = App.getInstance().getAssets(); //获得assets资源管理器（assets中的文件无法直接访问，可以使用AssetManager访问）
        InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName),"UTF-8"); //使用IO流读取json文件内容
        BufferedReader br = new BufferedReader(inputStreamReader);//使用字符高效流
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        inputStreamReader.close();
        return builder.toString();
    }

    public static String[] getEpgInfo(String channelName) {
        try {
            if (epgHashMap.containsKey(channelName)) {
                JsonObject obj = epgHashMap.get(channelName);
                String[] epgInfo = null;
                if (obj != null){
                    String logo = obj.get("logo") != null ? obj.get("logo").getAsString() : "no_logo";
                    String epgid = obj.get("epgid") != null ? obj.get("epgid").getAsString() : channelName;
                    String channel_id = obj.get("channel_id") != null ? obj.get("channel_id").getAsString() : "0";
                    String logo_2 = obj.get("logo_2") != null ? obj.get("logo_2").getAsString() : "no_logo_2";
                    epgInfo= new String[]{logo, epgid, channel_id, logo_2};
                }
                return epgInfo;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String getChannelName(String oriChannelName) {
        return oriChannelName.replaceAll(" ", "")
                .replace("测试", "")
                .replace("备用", "");
    }
    public static String getEpgId(String oriChannelName) {
        String channelName = getChannelName(oriChannelName);
        if (epgHashMap.containsKey(channelName)) {
            JsonObject obj = epgHashMap.get(channelName);
            if (obj != null)
                return obj.get("epgid") != null ? obj.get("epgid").getAsString() : channelName;
            else
                return channelName;
        }
        return channelName;
    }
    public static String getEpgChannelId(String epgId) {
        if (epgIdHashMap.containsKey(epgId)) {
            JsonObject obj = epgIdHashMap.get(epgId);
            if (obj != null)
                return obj.get("channel_id") != null ? obj.get("channel_id").getAsString() : "-1";
            else
                return "-1";
        }
        return "-1";
    }
    public static String getEpgLogo(String epgId) {
        if (epgIdHashMap.containsKey(epgId)) {
            JsonObject obj = epgIdHashMap.get(epgId);
            if (obj != null)
                return obj.get("logo") != null ? obj.get("logo").getAsString() : (obj.get("logo_2") != null ? obj.get("logo_2").getAsString():"no_logo");
            else
                return "no_logo";
        }
        return "no_logo";
    }

}