package com.open.face2facemanager.factory.bean;

/**
 * Created by onion on 2016/8/24.
 */
public class VersionInfo {
   private  String  device; //android
   private  int  iVersion;
   private  String  version;
   private  String  downloadUrl;
   private  String  description;
   private  String  type;  //free froce;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getiVersion() {
        return iVersion;
    }

    public void setiVersion(int iVersion) {
        this.iVersion = iVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
