package org.gracilianomp.treetemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

public class StringTemplate {

    static final public Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    static public String toString(byte[] s) {
        return new String(s, CHARSET_UTF8);
    }

    private CharSequence[] parts ;
    private boolean pathPattern ;

    public StringTemplate(String string, boolean pathPattern) {
        this(string, pathPattern, null) ;
    }

    public StringTemplate(String string, boolean pathPattern, Map<String,String> properties) {
        this.pathPattern = pathPattern ;
        this.parts = VariableParser.parse(string, pathPattern, properties) ;
    }


    public byte[] resolveAsBytes(Map<String,String> properties) {
        return resolve(properties).getBytes(CHARSET_UTF8) ;
    }

    public String resolve(Map<String,String> properties) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            CharSequence part = parts[i];

            if (part instanceof Variable) {
                Variable var = (Variable) part;
                String val = var.resolve(properties);
                str.append(val) ;
            }
            else {
                str.append(part) ;
            }
        }

        return str.toString() ;
    }

    @Override
    public String toString() {
        return "StringTemplate<<" + Arrays.toString(parts) + ">>";
    }
}
