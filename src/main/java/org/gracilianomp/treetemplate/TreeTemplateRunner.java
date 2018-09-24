package org.gracilianomp.treetemplate;

import org.gracilianomp.utils.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class TreeTemplateRunner {

    static private File askFile(Scanner scanner, String[] args, int idx, String inputDesc) {
        String value = ask(scanner, args, idx, inputDesc);
        return value != null ? new File(value) : null ;
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
        return line.isEmpty() ? null : line ;
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        File templateFile = askFile(scanner, args, 0, "Template Zip file") ;
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
