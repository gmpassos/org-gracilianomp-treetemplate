package org.gracilianomp.utils;

import org.gracilianomp.treetemplate.TreeEntryFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StreamUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);

    static public void write(File file, byte[] data) throws IOException {
        LOGGER.info("Writeing to file[{}] {} bytes", file, data.length);

        try ( FileOutputStream fout = new FileOutputStream(file) ) {
            write(fout, data);
            fout.flush();
        }
    }

    static public void write(OutputStream out, byte[] data) throws IOException {
        out.write(data);
    }

    static public byte[] read(File file) throws IOException {
        LOGGER.info("Reading file: {}", file);

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

    static public Properties readProperties(File propertiesFile) throws IOException {
        Properties properties = new Properties();
        properties.load( new ByteArrayInputStream( read(propertiesFile)) );
        return properties;
    }

    static public byte[] generateZip(File directory, TreeEntryFilter filter) throws IOException {
        if (!directory.isDirectory()) throw new IOException("Not a directory: "+ directory) ;

        LOGGER.info("Generating ZIP: {}", directory);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        String rootPath = directory.getPath() ;

        try ( ZipOutputStream zipOut = new ZipOutputStream(bout) ) {

            addDirectory(directory, zipOut, filter, rootPath) ;

            zipOut.flush();
        }

        return bout.toByteArray() ;
    }

    private static void addDirectory(File directory, ZipOutputStream zipOut, TreeEntryFilter filter, String rootPath) throws IOException {

        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String path = pathname.getPath();
                if (path.startsWith(rootPath)) path = path.substring(rootPath.length());
                return filter.accept(path);
            }
        });

        Arrays.sort(files);

        for (File file : files) {
            String path = file.getPath();
            if (path.startsWith(rootPath)) path = path.substring(rootPath.length());

            path = path.replaceFirst("^[\\\\/]+","") ;
            path = path.replaceFirst("[\\\\/]+$","") ;

            boolean isDir = file.isDirectory();

            if (isDir && !path.endsWith("/")) {
                path += "/" ;
            }

            ZipEntry zipEntry = new ZipEntry(path);
            zipOut.putNextEntry(zipEntry);

            if (isDir) {
                LOGGER.info("Add ZIP dir: {}", path);

                addDirectory(file, zipOut, filter, rootPath);
            }
            else {
                LOGGER.info("Add ZIP file: {}", path);

                byte[] data = read(file);
                zipOut.write(data);
                zipOut.flush();
            }
        }

    }


}
