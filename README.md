# Box
#### 参考项目
#### https://github.com/CatVodTVOfficial/TVBoxOSC
#### https://github.com/takagen99/Box
#### https://github.com/q215613905/TVBoxOS
#### https://github.com/mlabalabala/box
#### https://github.com/chengxue2020/takagen99/

TVBox 缝合怪，自用
默认源配置：
app\src\main\res\values\config.xml
/src/main/java/com/github/tvbox/osc/base/App.java

参考：\app\src\main\java\com\github\tvbox\osc\util\HawkConfig.java

    private void initParams() {

        putDefault(HawkConfig.HOME_REC, 2);       // Home Rec 0=豆瓣, 1=推荐, 2=历史
        putDefault(HawkConfig.PLAY_TYPE, 1);      // Player   0=系统, 1=IJK, 2=Exo
        putDefault(HawkConfig.IJK_CODEC, "硬解码");// IJK Render 软解码, 硬解码
        putDefault(HawkConfig.HOME_SHOW_SOURCE, true);  // true=Show, false=Not show
        putDefault(HawkConfig.HOME_NUM, 2);       // History Number
        putDefault(HawkConfig.DOH_URL, 2);        // DNS
        putDefault(HawkConfig.SEARCH_VIEW, 2);    // Text or Picture

    }