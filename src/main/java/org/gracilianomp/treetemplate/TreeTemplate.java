package org.gracilianomp.treetemplate;

import org.gracilianomp.utils.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TreeTemplate {

    static final public String TREE_TEMPLATE_PROPERTIES_FILENAME = "treetemplate.properties" ;

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeTemplate.class);

    private final URI templateURI;
    private final byte[] templateData;

    public TreeTemplate(URL templateURL) throws URISyntaxException {
        this( templateURL.toURI() ) ;
    }

    public TreeTemplate(File templateFile) {
        this( templateFile.toURI() ) ;
    }

    public TreeTemplate(URI templateURI) {
        this.templateURI = templateURI;
        this.templateData = null ;
    }

    public TreeTemplate(byte[] templateData) {
        this.templateURI = null;
        this.templateData = templateData;
    }

    public URI getTemplateURI() {
        return templateURI;
    }

    public byte[] getTemplateData() {
        return templateData;
    }

    private HashMap<String,String> properties = new HashMap<>();

    public int getPropertiesSize() {
        return properties.size();
    }

    public Map<String, String> getProperties() {
        return (Map<String, String>) properties.clone();
    }

    public void putProperty(String key, String value) {
        properties.put(key.toUpperCase(), value) ;
    }

    public String getProperty(String key) {
        return  properties.get(key) ;
    }

    private LinkedHashMap<String , org.gracilianomp.treetemplate.TreeEntry> entries = new LinkedHashMap<>();

    public int getEntriesSize() {
        return entries.size() ;
    }

    public List<org.gracilianomp.treetemplate.TreeEntry> getEntries() {
        return new ArrayList<>( entries.values() ) ;
    }

    TreeEntryFilter treeEntryFilter = TreeEntryFilter.GITIGNORE_FILTER ;

    public void setTreeEntryFilter(TreeEntryFilter treeEntryFilter) {
        this.treeEntryFilter = treeEntryFilter != null ? treeEntryFilter : TreeEntryFilter.NO_FILTER ;
    }

    public TreeEntryFilter getTreeEntryFilter() {
        return treeEntryFilter;
    }

    private boolean loaded = false ;

    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        if (loaded) return ;
        loaded = true ;

        try (InputStream in = openStream() ; ZipInputStream zipIn = new ZipInputStream(in) ) {

            ZipEntry entry;
            while ( (entry =  zipIn.getNextEntry()) != null ) {

                String fileName = entry.getName() ;

                if (fileName.toLowerCase().matches("^(?:|.*?/)treetemplate\\.properties$")) {
                    byte[] data = entry.isDirectory() ? null : StreamUtils.read(zipIn);
                    loadTemplateProperties(data);
                    continue ;
                }

                if ( !treeEntryFilter.accept(fileName) ) {
                    LOGGER.info("Ignore: {}", fileName);
                    continue;
                }

                long time = entry.getLastModifiedTime().toMillis() ;

                byte[] data = entry.isDirectory() ? null : StreamUtils.read(zipIn);

                TreeEntry treeEntry = loadEntry( fileName , time, data) ;

                LOGGER.info("Loaded: {}", treeEntry);

                entries.put( treeEntry.getPath() , treeEntry ) ;
            }

        }
        catch (IOException e) {
            throw new IllegalStateException("Can't load template: "+ templateURI) ;
        }

    }



    private Properties templateProperties ;

    public Properties getTemplateProperties() {
        return templateProperties;
    }

    private void loadTemplateProperties(byte[] data) throws IOException {
        Properties templateProperties = new Properties();
        templateProperties.load(new ByteArrayInputStream(data));

        LOGGER.info("LOADED {}:", TREE_TEMPLATE_PROPERTIES_FILENAME);

        for (Map.Entry<Object,Object> entry : templateProperties.entrySet()) {
            LOGGER.info("  - {}: {}", entry.getKey(), entry.getValue());
        }

        this.templateProperties = templateProperties ;
    }

    private TreeEntry loadEntry(String fileName, long time, byte[] data) {
        return new TreeEntry(data == null, fileName , time , data) ;
    }

    private InputStream openStream() throws IOException {
        if (templateData != null) {
            return new ByteArrayInputStream(templateData);
        }

        String scheme = templateURI.getScheme();

        if ( scheme.startsWith("http") || scheme.startsWith("https") ) {
            return templateURI.toURL().openStream() ;
        }
        else if ( scheme.startsWith("file") ) {
            return new FileInputStream(new File(templateURI)) ;
        }
        else {
            throw new UnsupportedOperationException("Unsupported scheme: "+ scheme);
        }
    }

    public byte[] generateToFile( File templateFile) throws IOException {
        byte[] templateData = generate();
        StreamUtils.write(templateFile, templateData);
        return templateData;
    }

    public byte[] generate() {
        if ( !isLoaded() ) throw new IllegalStateException("Not loaded yet!") ;

        LOGGER.info("Generating using properties:");

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            LOGGER.info("-- {}: {}", entry.getKey(), entry.getValue());
        }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try ( ZipOutputStream zipOut = new ZipOutputStream(bout) ) {

            for (Map.Entry<String,TreeEntry> entry : entries.entrySet()) {

                TreeEntry treeEntry = entry.getValue();

                byte[] data = treeEntry.resolveData(properties) ;

                String resolvedName = treeEntry.resolveName(properties);

                ZipEntry zipEntry = new ZipEntry(resolvedName);
                zipOut.putNextEntry(zipEntry);

                if ( !treeEntry.isDirectory() ) {
                    zipOut.write(data);
                    zipOut.flush();
                }

                LOGGER.info("Generated: {}", resolvedName);
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Can't generate template: "+ templateURI) ;
        }

        return bout.toByteArray() ;
    }


}
