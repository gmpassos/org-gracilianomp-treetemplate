package org.gracilianomp.utils;

import java.io.*;

public class StreamUtils {

    static public void write(File file, byte[] data) throws IOException {
        try ( FileOutputStream fout = new FileOutputStream(file) ) {
            write(fout, data);
            fout.flush();
        }
    }

    static public void write(OutputStream out, byte[] data) throws IOException {
        out.write(data);
    }

    static public byte[] read(File file) throws IOException {
        try ( FileInputStream fin = new FileInputStream(file) ) {
            return read(fin);
        }
        catch (IOException e) {
            throw e ;
        }
        catch (Exception e) {
            throw new IOException("Can't read file: "+ file, e) ;
        }
    }

    static public byte[] read(InputStream fin) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[1024*4] ;

        try {
            int r ;
            while ( (r = fin.read(buf)) >= 0 ) {
                bout.write(buf, 0, r);
            }
        }
        catch (IOException e) {
            throw e ;
        }

        return bout.toByteArray() ;
    }


}
