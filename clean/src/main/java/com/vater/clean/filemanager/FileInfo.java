/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.vater.clean.filemanager;

public class FileInfo {

    public String fileName;

    public String filePath;

    public long fileSize;

    public boolean isDir;

    public int count;

    public long modifiedDate;

    public boolean isSelected;

    public boolean canRead;

    public boolean canWrite;

    public boolean isHidden;

    public long _id; // id in the database, if is from database

    public FileInfo() {
    }


    public FileInfo(String path, String name, long time, long size) {
        super();
        filePath = path;
        fileName = name;
        this.modifiedDate = time;
        this.fileSize = size;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", isDir=" + isDir +
                ", count=" + count +
                ", modifiedDate=" + modifiedDate +
                ", isSelected=" + isSelected +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                ", isHidden=" + isHidden +
                ", _id=" + _id +
                '}';
    }
}
