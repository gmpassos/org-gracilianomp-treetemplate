package org.gracilianomp.treetemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableParser.class);

    static final public String REGEXP_VARIABLE = "[A-Z0-9_]+?" ;
    static final public String REGEXP_FILTER = "[a-z0-9]+?" ;

    static final public Pattern PATTERN_TEMPLATE_VARIABLE = Pattern.compile("(?s:%("+REGEXP_VARIABLE+")(?:%|__("+REGEXP_FILTER+")%))") ;
    static final public Pattern PATTERN_TEMPLATE_PATH_VARIABLE = Pattern.compile("(?s:__("+REGEXP_VARIABLE+")__(?:("+REGEXP_FILTER+")__)?)") ;

    static public boolean containsVariable(byte[] s) {
        String s1 = StringTemplate.toString(s);
        return containsVariable(s1) ;
    }

    static public boolean containsVariable(String s) {
        return PATTERN_TEMPLATE_VARIABLE.matcher(s).find() ;
    }


    static public CharSequence[] parse(String string, boolean pathPattern, Map<String, String> properties) {
        Pattern pattern = pathPattern ? PATTERN_TEMPLATE_PATH_VARIABLE : PATTERN_TEMPLATE_VARIABLE;

        Matcher matcher = pattern.matcher(string);

        ArrayList<CharSequence> parts = new ArrayList<>();

        int pos = 0 ;
        while (matcher.find()) {
            String prev = string.substring(pos, matcher.start()) ;

            if (!prev.isEmpty()) parts.add(prev);

            String varName = matcher.group(1);

            if ( properties != null && !properties.containsKey(varName) ) {
                String part = matcher.group();

                LOGGER.info("Ignore null property '{}' in part: {}", varName, part );

                parts.add(part);
            }
            else {
                String filterName = matcher.group(2);

                VariableFilter filter = null ;
                if (filterName != null) {
                    filter = VariableFilter.getByName(filterName) ;
                }

                LOGGER.info("Parsing variable: {} ; filter: {}", varName, filter );

                Variable var = new Variable(pathPattern, varName, filter);
                parts.add(var);
            }


            pos = matcher.end() ;
        }

        String last = string.substring(pos) ;
        if (!last.isEmpty()) parts.add(last);

        CharSequence[] parsed = parts.toArray(new CharSequence[parts.size()]);
        return parsed ;
    }

}
