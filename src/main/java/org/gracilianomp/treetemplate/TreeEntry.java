package org.gracilianomp.treetemplate;

import java.util.Map;

public class TreeEntry {

    static public String normalizePath(boolean directory, String path) {
        if (directory) {
            if (!path.endsWith("/")) {
                path += "/" ;
            }
        }
        else {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length()-1) ;
            }
        }
        return path;
    }

    final private boolean directory ;
    final private String path ;

    final private long time ;
    final private byte[] data ;

    final private String dataString ;

    public TreeEntry(boolean directory, String path, long time, byte[] data) {
        path = normalizePath(directory, path);

        this.directory = directory;
        this.path = path;
        this.time = time;

        if (!directory) {
            String dataString = StringTemplate.toString(data);

            if (VariableParser.containsVariable(dataString)) {
                this.data = null;
                this.dataString = dataString;
            } else {
                this.data = data;
                this.dataString = null;
            }
        }
        else {
            this.data = null;
            this.dataString = null;
        }
    }

    public boolean isDirectory() {
        return directory;
    }

    public String getPath() {
        return path;
    }

    public long getTime() {
        return time;
    }

    public byte[] getData() {
        return data;
    }

    public int getDataSize() {
        if (directory) return 0 ;
        return data != null ? data.length : dataString.length() ;
    }

    public String resolveName(Map<String,String> properties ) {
        StringTemplate stringThemplate = new StringTemplate(this.path, true);
        String resolved = stringThemplate.resolve(properties);
        return normalizePath( directory, resolved );
    }

    public byte[] resolveData( Map<String,String> properties ) {
        if ( this.dataString != null ) {
            StringTemplate stringThemplate = new StringTemplate(this.dataString, false);
            return stringThemplate.resolveAsBytes(properties);
        }
        else {
            return data ;
        }
    }

    @Override
    public String toString() {
        return "TreeEntry{" +
                "directory='" + directory + '\'' +
                ",path='" + path + '\'' +
                ", time=" + time +
                ", dataSize=" + getDataSize() +
                '}';
    }
}
