package org.gracilianomp.treetemplate;

import org.gracilianomp.utils.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class TreeTemplateRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeTemplateRunner.class);

    static private File askFile(Scanner scanner, String[] args, int idx, String inputDesc) {
        String value = ask(scanner, args, idx, inputDesc);
        return value != null ? new File(value) : null ;
    }

    static private String askOption(Scanner scanner, String[] args, int idx, String inputDesc, String... options) {
        String value = ask(scanner, args, idx, inputDesc +" "+ Arrays.toString(options));
        if (value == null || value.isEmpty()) return options[0] ;

        value = value.toLowerCase().trim() ;

        for (String option : options) {
            String optionLow = option.toLowerCase().trim() ;
            if ( optionLow.equals(value) ) return option ;
        }

        int optionMinLength = options[0].length() ;

        for (int i = 1; i < options.length; i++) {
            String option = options[i];
            if (option.length() < optionMinLength) optionMinLength = option.length() ;
        }

        for (int lng = 1; lng < optionMinLength; lng++) {
            for (String option : options) {
                String init = option.toLowerCase().trim().substring(0,lng) ;
                if ( value.startsWith(init) ) return option ;
            }
        }

        return options[0] ;
    }

    static private String ask(Scanner scanner, String[] args, int idx, String inputDesc) {
        if (args != null && args.length > idx) {
            String val = args[idx];
            if (val != null && !val.trim().isEmpty()) {
                val = val.trim() ;
                return val ;
            }
        }

        System.out.print(inputDesc+"> ");
        String line = scanner.nextLine();
        return line.isEmpty() ? null : line.trim() ;
    }

    private static void checkNotEmpty(Object value, String errorMessage) {
        if ( value == null || value.toString().isEmpty() ) throw new IllegalArgumentException(errorMessage) ;
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        File templateFile = askFile(scanner, args, 0, "Template Zip file") ;

        checkNotEmpty(templateFile, "Empty Template Zip file") ;

        String generateType = askOption(scanner, args, 0, "Generate Template or Properties?", "template", "properties") ;

        if ( generateType.equals("properties") ) {
            generateProperties(args, scanner, templateFile);
        }
        else {
            generateTemplate(args, scanner, templateFile);
        }

    }

    public static void generateProperties(String[] args, Scanner scanner, File templateFile) {
        System.out.println("** GENERATING TEMPLATE PROPERTIES **");

        File outputPropertiesFile = askFile(scanner, args, 2, "Output Properties file") ;

        TreeTemplate treeTemplate = new TreeTemplate(templateFile);

        System.out.println("LOADING: "+ templateFile);
        treeTemplate.load();

        Properties templateProperties = treeTemplate.getTemplateProperties();

        if (templateProperties != null && !templateProperties.isEmpty()) {
            try (FileOutputStream fout = new FileOutputStream(outputPropertiesFile) ) {
                templateProperties.store(fout, "Auto Generated Template Properties from: "+ treeTemplate.getTemplateURI());

                System.out.println("-- Generated Template properties: "+ outputPropertiesFile);
            }
            catch (IOException e) {
                LOGGER.error("Can't store properties at: "+ outputPropertiesFile, e);
            }
        }

    }

    public static void generateTemplate(String[] args, Scanner scanner, File templateFile) throws IOException {
        System.out.println("** GENERATING TEMPLATE **");

        File saveFile = askFile(scanner, args, 1, "Destination of generated Zip file") ;
        File inputPropertiesFile = askFile(scanner, args, 2, "Input Properties file") ;

        TreeTemplate treeTemplate = new TreeTemplate(templateFile);

        System.out.println("LOADING: "+ templateFile);
        treeTemplate.load();

        Properties templateProperties = treeTemplate.getTemplateProperties();

        if (templateProperties != null && !templateProperties.isEmpty()) {

            Properties inputProperties = inputPropertiesFile != null ? StreamUtils.readProperties(inputPropertiesFile) : new Properties() ;

            System.out.println("---------------------------------");
            System.out.println("TEMPLATE PROPERTIES:");

            List<String> templatePropertiesKeys = treeTemplate.getTemplatePropertiesKeys();

            for (String key : templatePropertiesKeys) {
                String val = templateProperties.getProperty(key) ;

                Object inProp = inputProperties.get(key) ;

                if ( inProp != null ) {
                    String input = inProp.toString() ;
                    System.out.println("-- "+ key +" ("+val+") = "+ input);
                    treeTemplate.putProperty(key , input);
                }
                else {
                    System.out.print("-- "+ key +" ("+val+"): ");
                    String line = scanner.nextLine().trim();
                    treeTemplate.putProperty(key , line);
                }

            }
            System.out.println("---------------------------------");
        }

        byte[] ok = treeTemplate.generateToFile(saveFile);

        if (ok != null) {
            System.out.println("---------------------------------");
            System.out.println("Saved: "+ saveFile);
            System.out.println();
            System.out.println("-----------------");
            System.out.println("| Happy Coding! |");
            System.out.println("-----------------");
        }
    }

}
