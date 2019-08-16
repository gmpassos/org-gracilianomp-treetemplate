package org.gracilianomp.treetemplate;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class TreeTemplateTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TreeTemplateTest.class);

    @Test
    public void test() throws URISyntaxException, IOException {

        URL templateUrl = TreeTemplateTest.class.getResource("/org/gracilianomp/treetemplate/template-test1.zip");

        TreeTemplate treeThemplate = new TreeTemplate(templateUrl);

        Assert.assertFalse(treeThemplate.isLoaded());

        treeThemplate.load();

        Assert.assertTrue(treeThemplate.isLoaded());

        Assert.assertEquals( 9 , treeThemplate.getEntriesSize() );

        Assert.assertEquals( 0 , treeThemplate.getPropertiesSize() );

        treeThemplate.putProperty("PROJECT_NAME", "simple-project");
        treeThemplate.putProperty("TEST", "SimpleTest1");
        treeThemplate.putProperty("MAIN_CLASS", "SimpleMain");
        treeThemplate.putProperty("ROOT_PACKAGE", "org.simple");

        Assert.assertEquals( 4 , treeThemplate.getPropertiesSize() );

        List<TreeEntry> entries = treeThemplate.getEntries();

        assertEqualEntry(entries.get(0) , "__PROJECT_NAME__/", 0) ;
        assertEqualEntry(entries.get(1) , "__PROJECT_NAME__/README.md", 54) ;
        assertEqualEntry(entries.get(7) , "__PROJECT_NAME__/src/main/java/__ROOT_PACKAGE__pack2path__/test/Foo.java", 54) ;
        assertEqualEntry(entries.get(8) , "__PROJECT_NAME__/src/main/java/__ROOT_PACKAGE__pack2path__/test/__MAIN_CLASS__.java", 64) ;

        ////////////////////////////

        byte[] templateDataZip1 = treeThemplate.generate();

        //treeThemplate.generateToFile(new File("/tmp/template-gen1.zip"));

        org.gracilianomp.treetemplate.TreeTemplate treeThemplateZip1 = new org.gracilianomp.treetemplate.TreeTemplate(templateDataZip1);

        treeThemplateZip1.load();

        System.out.println( "---------------------------" );
        for(TreeEntry entry : treeThemplateZip1.getEntries()) {
          System.out.println( entry );
        }
        System.out.println( "---------------------------" );

        Assert.assertEquals(10 , treeThemplateZip1.getEntriesSize() );

        List<TreeEntry> entriesZip1 = treeThemplateZip1.getEntries();

        Assert.assertEquals( 10 , entriesZip1.size() );

        assertEqualEntry(entriesZip1.get(0) , "simple-project/", 0) ;
        assertEqualEntry(entriesZip1.get(1) , "simple-project/README.md", 59) ;
        assertEqualEntry(entriesZip1.get(7) , "simple-project/src/main/java/org/simple/test/Foo.java", 50) ;
        assertEqualEntry(entriesZip1.get(8) , "simple-project/src/main/java/org/simple/test/SimpleMain.java", 58) ;
        assertEqualEntry(entriesZip1.get(9) , "treetemplate-generation.properties", -1) ;
    }

    static private void assertEqualEntry( TreeEntry entry , String path , int size ) {

        LOGGER.info("ASSERT EQ> {} == {} ; {}", entry , path, size );

        Assert.assertEquals( path , entry.getPath() );

        if (size >= 0) {
            Assert.assertEquals(size, entry.getDataSize());
        }

    }

}
