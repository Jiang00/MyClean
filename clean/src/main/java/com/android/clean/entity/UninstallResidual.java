package com.android.clean.entity;

/**
 * Created by renqingyou on 2017/5/15.
 */

public class UninstallResidual {
    public String pkg;
    public String name;
    public String path;
    public long size;

    public UninstallResidual() {
    }

    public UninstallResidual(String pkg, String name, String path,long size) {
        this.pkg = pkg;
        this.name = name;
        this.path = path;
        this.size = size;
    }

    @Override
    public String toString() {
        return "UninstallResidual{" +
                "pkg='" + pkg + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
