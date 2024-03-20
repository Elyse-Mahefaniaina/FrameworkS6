package util;

import engine.ConnectionBDD;
import template.back.Template;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {

    public static HashMap<String, String> getAttributeTable(String tableName, Template template, Connection connect) throws Exception {
        boolean isOpen = false;

        DatabaseMetaData metaData;
        ResultSet result = null;

        HashMap<String, String> field = new HashMap<>();

        if(connect == null) {
            ConnectionBDD connexion = new ConnectionBDD();
            connect = connexion.getConnection();
            isOpen = true;
        }

        try {
            metaData = connect.getMetaData();
            result = metaData.getColumns(null, null, tableName, null);

            while(result.next()) {
                String columnName = result.getString("COLUMN_NAME");
                String dataType = result.getString("TYPE_NAME");
                field.put(columnName.toLowerCase(), template.mappingField(dataType.toUpperCase()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(isOpen) connect.close();
            if(result != null) result.close();
        }

        return field;
    }

    public static boolean fildIsPrimary(String tableName, String field,Connection connect) throws Exception {
        boolean isOpen = false;
        ResultSet result = null;

        if (connect == null) {
            ConnectionBDD connexion = new ConnectionBDD();
            connect = connexion.getConnection();
            isOpen = true;
        }

        try {
            DatabaseMetaData metaData = connect.getMetaData();
            result = metaData.getPrimaryKeys(null, null, tableName.toLowerCase());

            while (result.next()) {
                String primaryFieldName = result.getString("COLUMN_NAME");
                if (field.equalsIgnoreCase(primaryFieldName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (isOpen) connect.close();
            if (result != null) result.close();
        }

        return false;
    }

    public static List<String> getListeTable(Connection connect) throws Exception {
        List<String> liste = new ArrayList<>();
        boolean isOpen = false;

        DatabaseMetaData metaData = null;
        ResultSet result = null;

        if(connect == null) {
            ConnectionBDD connexion = new ConnectionBDD();
            connect = connexion.getConnection();
            isOpen = true;
        }

        try {
            metaData = connect.getMetaData();
            result = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (result.next()) {
                String tableName = result.getString("TABLE_NAME");
                liste.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(isOpen) connect.close();
            if(result != null) result.close();
        }
        return liste;
    }

    public static String casse(String input) {
        input = input.toLowerCase();
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
