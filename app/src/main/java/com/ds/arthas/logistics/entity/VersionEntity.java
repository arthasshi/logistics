package com.ds.arthas.logistics.entity;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class VersionEntity  {

    /**
     * id : 5
     * versioncode : 648
     * file : apk/581310fb924ef.apk
     * create_time : 2016-10-28 04:49:00
     */

    private String id;
    private String versioncode;
    private String file;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
