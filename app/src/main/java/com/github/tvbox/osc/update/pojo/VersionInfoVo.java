package com.github.tvbox.osc.update.pojo;

import com.github.tvbox.osc.BuildConfig;

public class VersionInfoVo {
    private String desc;
    private int versionCode;
    private String versionName;
    private boolean forceUpgrade;
    private String buildRepoName;
    private String repoUserName;
    private String repoName;
    private String repoBranch;
    private String repoTag;

    private String buildType = BuildConfig.BUILD_TYPE;

    private String downloadURL;

    public String getDesc() {
        return desc;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public String getDownloadURL() {
        downloadURL ="https://github.com/"+buildRepoName+"/releases/download/"+
                repoUserName+"_"+repoName+"_" + repoBranch + "_"+repoTag+"/"+repoName+"_"+repoUserName+"_"+repoTag+"_"+buildType + ".apk";
        return downloadURL;
    }

    @Override
    public String toString() {
        return "VersionInfoVo{" +
                "desc='" + desc + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", forceUpgrade=" + forceUpgrade +
                ", buildRepoName='" + buildRepoName + '\'' +
                ", repoUserName='" + repoUserName + '\'' +
                ", repoName='" + repoName + '\'' +
                ", repoTag='" + repoTag + '\'' +
                '}';
    }
}
