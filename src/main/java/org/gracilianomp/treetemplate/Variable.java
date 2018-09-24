package org.gracilianomp.treetemplate;

import java.util.Map;
import java.util.stream.IntStream;

public class Variable implements CharSequence{
    final private boolean pathPattern;
    final private String name ;
    final private VariableFilter filter ;

    public Variable(boolean pathPattern, String name, VariableFilter filter) {
        this.pathPattern = pathPattern;
        this.name = name.toUpperCase();
        this.filter = filter;
    }

    public boolean isPathPattern() {
        return pathPattern;
    }

    public String getName() {
        return name;
    }

    public VariableFilter getFilter() {
        return filter;
    }

    @Override
    public int length() {
        return name.length();
    }

    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return name.subSequence(start, end);
    }

    @Override
    public IntStream chars() {
        return name.chars();
    }

    @Override
    public IntStream codePoints() {
        return name.codePoints() ;
    }

    public String resolve(Map<String,String> properties) {
        String val = properties.get(name) ;

        if (val == null) {
            if (filter == null) {
                if (pathPattern) {
                    return "__"+name+"__" ;
                }
                else {
                    return "%"+name+"%" ;
                }
            }
            else {
                throw new IllegalStateException("Null variable: "+ name) ;
            }
        }

        if (filter != null) {
            val = filter.filter(val) ;
        }

        return val ;
    }

    @Override
    public String toString() {
        return pathPattern ? "__"+name+"__" : "%"+name+"%" ;
    }
}
