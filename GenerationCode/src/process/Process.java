package process;


import engine.ConnectionBDD;
import template.back.Template;
import template.back.TemplateJava;
import template.front.GenerateApp;
import template.front.TemplateFront;
import util.Util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class Process {

    public static void genererClassFront(String pathOut, Connection connection) throws Exception {
        Template template = new TemplateJava();

        boolean isOpen = false;
        if (connection == null) {
            connection = new ConnectionBDD().getConnection();
            isOpen = true;
        }

        List<String> tables = Util.getListeTable(connection);
        String className;
        String data_class;
        HashMap<String, String> field;
        for (String s : tables) {
            className = Util.casse(s);
            field = Util.getAttributeTable(s, template, connection);
            data_class = TemplateFront.genererClass(className, "localhost", "8080", field);

            Engine.write(data_class, pathOut+"/"+className+".js");
        }

        if (isOpen) connection.close();
    }

    public static void genererNav(String pathOut, Connection connection) throws Exception {
        boolean isOpen = false;
        if (connection == null) {
            connection = new ConnectionBDD().getConnection();
            isOpen = true;
        }

        List<String> tables = Util.getListeTable(connection);
        String dataindex = GenerateApp.generer(pathOut, tables);
        Engine.write(dataindex, pathOut+"/App.js");
        if (isOpen) connection.close();
    }

    public static void genererBack(String pathOut, String pack, Connection connection) throws Exception {
        Template template = new TemplateJava();

        boolean isOpen = false;
        if (connection == null) {
            connection = new ConnectionBDD().getConnection();
            isOpen = true;
        }

        List<String> tables = Util.getListeTable(connection);
        String className;
        String data_class;
        HashMap<String, String> field;
        for (String s : tables) {
            className = Util.casse(s);
            field = Util.getAttributeTable(s, template, connection);
            data_class = template.str_class(pack, className, field);

            Engine.write(data_class, template.getPathOut(pathOut, pack, className));
        }

        if (isOpen) connection.close();
    }
}
