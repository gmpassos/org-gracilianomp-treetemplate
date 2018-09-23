package org.gracilianomp.treetemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class TreeTemplateRunner {

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
        return line ;
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        File templatFile = new File( ask(scanner, args, 0, "Template Zip file") ) ;
        File saveFile = new File( ask(scanner, args, 1, "Destination of generated Zip file") ) ;

        TreeTemplate treeTemplate = new TreeTemplate(templatFile);

        System.out.println("LOADING: "+ templatFile);
        treeTemplate.load();



        Properties templateProperties = treeTemplate.getTemplateProperties();

        if (templateProperties != null && !templateProperties.isEmpty()) {
            System.out.println("---------------------------------");
            System.out.println("TEMPLATE PROPERTIES:");


            for (Map.Entry<Object, Object> entry : templateProperties.entrySet()) {
                System.out.print("-- "+ entry.getKey() +" ("+ entry.getValue()+"): ");
                String line = scanner.nextLine().trim();

                treeTemplate.putProperty(entry.getKey().toString() , line);
            }
            System.out.println("---------------------------------");
        }

        byte[] ok = treeTemplate.generateToFile(saveFile);

        if (ok != null) System.out.println("Saved: "+ saveFile);

    }

}
