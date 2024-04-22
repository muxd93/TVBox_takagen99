package com.github.tvbox.osc.base;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.github.catvod.crawler.JsLoader;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.VodInfo;
import com.github.tvbox.osc.callback.EmptyCallback;
import com.github.tvbox.osc.callback.LoadingCallback;
import com.github.tvbox.osc.data.AppDataManager;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.util.AppManager;
import com.github.tvbox.osc.util.EpgUtil;
import com.github.tvbox.osc.util.FileUtils;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.LocaleHelper;
import com.github.tvbox.osc.util.LogUtil;
import com.github.tvbox.osc.util.OkGoHelper;
import com.github.tvbox.osc.util.PlayerHelper;
import com.github.tvbox.osc.util.SubtitleHelper;
import com.hjq.permissions.XXPermissions;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.hawk.Hawk;
import com.p2p.P2PClass;
import com.undcover.freedom.pyramid.PythonLoader;
import com.whl.quickjs.android.QuickJSLoader;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * @author pj567
 * @date :2020/12/17
 * @description:
 */
public class App extends MultiDexApplication {
    private static App instance;
    private VodInfo vodInfo;

    private static P2PClass p;
    public static String burl;
    private static String dashData;
    public static ViewPump viewPump = null;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SubtitleHelper.initSubtitleColor(this);
        initParams();
        // takagen99 : Initialize Locale
        initLocale();
        // OKGo
        OkGoHelper.init();
        // 关闭检查模式
        XXPermissions.setCheckMode(false);
        // Get EPG Info
        EpgUtil.init();
        // 初始化Web服务器
        ControlManager.init(this);
        LogUtil.i("Web服务器初始化完成！");
        // 初始化数据库
        AppDataManager.init();
        LoadSir.beginBuilder()
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .commit();
        AutoSizeConfig.getInstance().setCustomFragment(true).getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
        PlayerHelper.init();

        // Delete Cache
        /*File dir = getCacheDir();
        FileUtils.recursiveDelete(dir);
        dir = getExternalCacheDir();
        FileUtils.recursiveDelete(dir);*/

        FileUtils.cleanPlayerCache();

        // Add JS support
        QuickJSLoader.init();

        PythonLoader.getInstance().setApplication(this);

        // add font support, my tv embed font not include emoji
        String extStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File fontFile = new File(extStorageDir + "/tvbox.ttf");
        if (fontFile.exists()) {
            viewPump = ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath(fontFile.getAbsolutePath())
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build();
        }

        // 初始化更新服务
        initXUpdate();
    }

    public static P2PClass getp2p() {
        try {
            if (p == null) {
                p = new P2PClass(FileUtils.getExternalCachePath());
            }
            return p;
        } catch (Exception e) {
            LogUtil.e("getp2p error");
            e.printStackTrace();
            return null;
        }
    }


    private void initParams() {
        // Hawk
        Hawk.init(this).build();
        Hawk.put(HawkConfig.DEBUG_OPEN, false);

        // 首页选项
        putDefault(HawkConfig.HOME_SHOW_SOURCE, true);       //数据源显示: true=开启, false=关闭
        putDefault(HawkConfig.HOME_SEARCH_POSITION, false);  //按钮位置-搜索: true=上方, false=下方
        putDefault(HawkConfig.HOME_MENU_POSITION, true);     //按钮位置-设置: true=上方, false=下方
        putDefault(HawkConfig.HOME_REC, 1);                  //推荐: 0=豆瓣热播, 1=站点推荐, 2=观看历史
        putDefault(HawkConfig.HOME_NUM, 4);                  //历史条数: 0=20条, 1=40条, 2=60条, 3=80条, 4=100条
        putDefault(HawkConfig.HOME_REC_STYLE, true);         //首页多行 true=多行，false=单行
        // 播放器选项
        putDefault(HawkConfig.SHOW_PREVIEW, true);           //窗口预览: true=开启, false=关闭
        putDefault(HawkConfig.PLAY_SCALE, 0);                //画面缩放: 0=默认, 1=16:9, 2=4:3, 3=填充, 4=原始, 5=裁剪
        putDefault(HawkConfig.BACKGROUND_PLAY_TYPE, 0);      //后台：0=关闭, 1=开启, 2=画中画
        putDefault(HawkConfig.PLAY_TYPE, 1);                 //播放器: 0=系统, 1=IJK, 2=Exo, 3=MX, 4=Reex, 5=Kodi
        putDefault(HawkConfig.IJK_CODEC, "硬解码");           //IJK解码: 软解码, 硬解码
        putDefault(HawkConfig.IJK_CACHE_PLAY, true);         // IJK缓存
        // 系统选项
        putDefault(HawkConfig.HOME_LOCALE, 0);               //语言: 0=中文, 1=英文
        putDefault(HawkConfig.THEME_SELECT, 0);              //主题: 0=奈飞, 1=哆啦, 2=百事, 3=鸣人, 4=小黄, 5=八神, 6=樱花
        putDefault(HawkConfig.SEARCH_VIEW, 1);               //搜索展示: 0=文字列表, 1=缩略图
        putDefault(HawkConfig.PARSE_WEBVIEW, true);          //嗅探Webview: true=系统自带, false=XWalkView
        putDefault(HawkConfig.DOH_URL, 0);                   //安全DNS: 0=关闭, 1=腾讯, 2=阿里, 3=360, 4=Google, 5=AdGuard, 6=Quad9
        putDefault(HawkConfig.FAST_SEARCH_MODE, true);       //聚搜开关
        putDefault(HawkConfig.AUTO_CHANGE_SOURCE, true);     //自动换源
        // 直播相关
        putDefault(HawkConfig.LIVE_CHANNEL_REVERSE, false);  // 默认换台反转
        putDefault(HawkConfig.LIVE_SHOW_TIME, true);         // 默认显示时间
        putDefault(HawkConfig.LIVE_CROSS_GROUP, false);      // 默认显示时间
        putDefault(HawkConfig.LIVE_SCALE, 1);                // 默认缩放比例
        putDefault(HawkConfig.LIVE_LOCAL_CHANNEL, false);    //
        putDefaultAPI();
    }

    private void putDefaultAPI () {
        LogUtil.d("resetApi");
        // 接口相关
        putDefault(HawkConfig.API_URL, getString(R.string.default_api_url));    // 默认接口
        putDefault(HawkConfig.LIVE_URL, getString(R.string.default_live_url));  // 默认直播
        putDefault(HawkConfig.EPG_URL, getString(R.string.default_epg_url));    // 默认EPG
        putDefault(HawkConfig.PROXY_URL, getString(R.string.default_proxy_url));// 默认代理地址
        ArrayList<String> api_history = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.default_api_history)));
        putDefault(HawkConfig.API_HISTORY, api_history);                        // 接口历史记录
        ArrayList<String> live_history = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.default_live_history)));
        putDefault(HawkConfig.LIVE_HISTORY, live_history);                      // 直播接口历史记录
        ArrayList<String> epg_history = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.default_epg_history)));
        putDefault(HawkConfig.EPG_HISTORY, epg_history);                        // EPG历史记录
        ArrayList<String> proxy_url_history = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.default_proxy_history)));
        putDefault(HawkConfig.PROXY_URL_HISTORY, proxy_url_history);            // 代理历史记录
    }

    private void initLocale() {
        if (Hawk.get(HawkConfig.HOME_LOCALE, 0) == 0) {
            LocaleHelper.setLocale(App.this, "zh");
        } else {
            LocaleHelper.setLocale(App.this, "");
        }
    }

    private void initXUpdate() {
        XUpdate.get()
            .debug(true)
            // 默认设置只在wifi下检查版本更新
            .isWifiOnly(false)
            // 默认设置使用get请求检查版本
            .isGet(true)
            // 默认设置非自动模式，可根据具体使用配置
            .isAutoMode(false)
            // 设置默认公共请求参数
            .param("versionCode", UpdateUtils.getVersionCode(this))
            .param("appKey", getPackageName())
            // 设置版本更新出错的监听
            .setOnUpdateFailureListener(new OnUpdateFailureListener() {
                @Override
                public void onFailure(UpdateError error) {
                    error.printStackTrace();
                    // 对不同错误进行处理
                    if (error.getCode() != CHECK_NO_NEW_VERSION) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            })
            // 设置是否支持静默安装，默认是true
            .supportSilentInstall(false)
            // 这个必须设置！实现网络请求功能。
            // .setIUpdateHttpService(new OkGoUpdateHttpService())
            // 这个必须初始化
            .init(this);
    }

    public static App getInstance() {
        return instance;
    }

    private void putDefault(String key, Object value) {
        if (!Hawk.contains(key)) {
            Hawk.put(key, value);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        JsLoader.load();
    }

    public VodInfo getVodInfo() {
        return this.vodInfo;
    }

    public void setVodInfo(VodInfo vodinfo) {
        this.vodInfo = vodinfo;
    }

    public Activity getCurrentActivity() {
        return AppManager.getInstance().currentActivity();
    }

    public void setDashData(String data) {
        dashData = data;
    }

    public String getDashData() {
        return dashData;
    }
}
