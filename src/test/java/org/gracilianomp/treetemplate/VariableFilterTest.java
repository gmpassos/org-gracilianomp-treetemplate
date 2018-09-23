package org.gracilianomp.treetemplate;

import org.junit.Assert;
import org.junit.Test;

public class VariableFilterTest {

    @Test
    public void test1() {

        Assert.assertEquals( "ABC" , VariableFilter.UPPERCASE.filter("abc") );
        Assert.assertEquals( "abc" , VariableFilter.LOWERCASE.filter("abc") );

        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PACKAGE.filter("foo.bar.baz") );
        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PACKAGE.filter("foo.bar..baz") );
        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PACKAGE.filter(".foo.bar.baz.") );
        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PACKAGE.filter("foo/bar/baz") );
        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PACKAGE.filter("/foo/bar/baz") );

        Assert.assertEquals( "foo/bar/baz" , VariableFilter.PATH.filter("foo/bar/baz") );
        Assert.assertEquals( "foo/bar/baz" , VariableFilter.PATH.filter(" foo/bar/baz ") );
        Assert.assertEquals( "foo/bar/baz" , VariableFilter.PATH.filter("foo\\bar\\baz") );
        Assert.assertEquals( "foo/bar/baz" , VariableFilter.PATH.filter(" foo//bar/baz ") );
        Assert.assertEquals( "foo/bar/baz" , VariableFilter.PATH.filter(" foo\\\\bar/baz ") );

        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PATH.filter("foo.bar.baz") );

        Assert.assertEquals( "foo/bar/baz" , VariableFilter.PACKAGE2PATH.filter("foo.bar.baz") );

        Assert.assertEquals( "foo.bar.baz" , VariableFilter.PATH2PACKAGE.filter("foo/bar/baz") );

    }

}
