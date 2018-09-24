package org.gracilianomp.treetemplate;

public enum VariableFilter {

    LOWERCASE("LC"),
    UPPERCASE("UC"),
    PACKAGE("PACK"),
    PATH(),
    PACKAGE2PATH("PACK2PATH"),
    PATH2PACKAGE("PATH2PACK"),
    TITLE()
    ;

    static public VariableFilter getByName(String name) {
        name = name.toUpperCase().trim() ;

        for (VariableFilter filter : values()) {
            if ( filter.name().equals(name) || filter.getAlias().equals(name) ) {
                return filter ;
            }
        }

        return null ;
    }

    private String alias ;

    VariableFilter() {
        this(null) ;
    }

    VariableFilter(String alias) {
        this.alias = alias != null ? alias : name() ;
    }

    public String getAlias() {
        return alias;
    }

    public String filter(String s) {

        switch (this) {
            case LOWERCASE: return filterLowerCase(s) ;
            case UPPERCASE: return filterUpperCase(s) ;
            case PACKAGE: return filterPackage(s) ;
            case PATH: return filterPath(s) ;
            case PACKAGE2PATH: return filterPack2Path(s) ;
            case PATH2PACKAGE: return filterPath2Pack(s) ;
            case TITLE: return filterTitle(s) ;
            default: throw new IllegalStateException("Can't handle filter: "+ this) ;
        }

    }

    static private String filterLowerCase(String s) {
        return s.toLowerCase() ;
    }

    static private String filterUpperCase(String s) {
        return s.toUpperCase() ;
    }

    static private String filterPackage(String s) {
        return s.replaceAll("[\\\\/]+",".")
                .replaceAll("[^\\w\\$.]+", "")
                .replaceAll("\\.\\.",".")
                .trim()
                .replaceFirst("^\\.","")
                .replaceFirst("\\.$","")
                ;
    }

    static private String filterPath(String s) {
        return s.replaceAll("[\\\\]+", "/")
                .replaceAll("/+", "/")
                .trim()
                ;
    }

    static private String filterPack2Path(String s) {
        s = s.replaceAll("\\.", "/") ;
        s = filterPath(s) ;
        return s ;
    }

    static private String filterPath2Pack(String s) {
        s = s.replaceAll("[\\\\/]", ".") ;
        s = filterPackage(s) ;
        return s ;
    }

    static private String filterTitle(String s) {
        s = s.replaceAll("\\W+", " ") ;
        s = s.toLowerCase().trim() ;

        char[] chars = s.toCharArray();

        chars[0] = Character.toUpperCase(chars[0]) ;

        for (int i = 1; i < chars.length; i++) {
            char cPrev = chars[i-1];
            char c = chars[i];

            if (cPrev == ' ') {
                chars[i] = Character.toUpperCase(chars[i]) ;
            }
        }

        s = new String(chars) ;
        return s ;
    }


}
