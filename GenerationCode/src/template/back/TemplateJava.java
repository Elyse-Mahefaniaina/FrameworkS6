package template.back;

import engine.ConnectionBDD;
import util.Util;

import java.util.HashMap;

import static util.Util.casse;

public class TemplateJava implements Template {

    @Override
    public String str_class(String pckg, String className, HashMap<String, String> field) throws Exception {
        String result = """
                package {pckg};
                                
                import persist.annotation.Id;
                import persist.exception.PersistException;
                import persist.insertable.Insertable;
                                
                import java.lang.reflect.InvocationTargetException;
                import java.sql.Connection;
                import java.sql.SQLException;
                import java.util.List;
                
                import etu1784.framework.annotation.ActionMethod;
                import etu1784.framework.annotation.restAPI;
                
                import com.google.gson.Gson;
                                
                public class {className} extends Insertable<{className}> {
                {field}
                                
                {function}
                
                    public {className}() {
                        this.setDatabaseName(\"postgres\");
                    }
                    
                {GetSet}
                }
                """;

        return result.
                replace("{pckg}", pckg).
                replace("{className}", casse(className)).
                replace("{function}", crud(casse(className))).
                replace("{field}", generateField(className, field)).
                replace("{GetSet}", generateGetSet(field));
    }

    @Override
    public String crud(String className) {
        String result = """
                {create}
                {find}
                {update}
                {delete}
                """;
        return result.replace("{create}", create(className))
                .replace("{find}", findAll(className))
                .replace("{update}", update(className))
                .replace("{delete}", delete(className));
    }

    public String template() {
        return """
                \t@restAPI
                \t@ActionMethod( url = "{method}.do", paramName="data")
                \tpublic HashMap<String, String> {methodName} (String data) {
                \t\ttry {
                \t\t\tGson gson = new Gson();
                \t\t\t{className} {classNameLower} = gson.fromJson(data, {className}.class);
                \t\t\t{classNameLower}.{method}();
                    
                \t\t}catch(Exception e) {
                \t\t\tHashMap<String, String> result = new HashMap<>();
                \t\t\tresult.put("status", "500");
                \t\t\tresult.put("error", e.getMessage());
                \t\t\treturn result;
                \t\t}
                \t\tHashMap<String, String> result = new HashMap<>();
                \t\tresult.put("status", "OK");
                \t\treturn result;
                \t}
                """;
    }

    public String create(String className) {
        return template().replace("{methodName}", "save")
                .replace("{className}", className)
                .replace("{method}", "insert"+casse(className))
                .replace("{classNameLower}", className.toLowerCase());
    }

    public String findAll(String className) {
        String result= """
                \t@restAPI
                \t@ActionMethod( url = "{method}.do")
                \tpublic HashMap<String, String> findAll() {
                \t\ttry {
                \t\t\t{className} {classNameLower} = new {className}();
                \t\t\treturn {classNameLower}.findAll();
                    
                \t\t}catch(Exception e) {
                \t\t\tHashMap<String, String> result = new HashMap<>();
                \t\t\tresult.put("status", "500");
                \t\t\tresult.put("error", e.getMessage());
                \t\t\treturn result;
                \t\t}
                \t\tHashMap<String, String> result = new HashMap<>();
                \t\tresult.put("status", "OK");
                \t\treturn result;
                \t}
                """;
        return result.replace("{className}", className)
                .replace("{method}", "findAll"+casse(className))
                .replace("{classNameLower}", className.toLowerCase());
    }

    public String update(String className) {
        return template().replace("{methodName}", "update")
                .replace("{className}", className)
                .replace("{method}", "update"+casse(className))
                .replace("{classNameLower}", className.toLowerCase());
    }

    public String delete(String className) {
        return template().replace("{methodName}", "delete")
                .replace("{className}", className)
                .replace("{method}", "delete"+casse(className))
                .replace("{classNameLower}", className.toLowerCase());
    }

    @Override
    public String getter(String type, String fieldName) {
        String result = """
                    \tpublic {type} get{funName}() {
                    \t\treturn this.{fieldName};
                    \t}""";

        return result.
                replace("{type}", type).
                replace("{fieldName}", fieldName).
                replace("{funName}", casse(fieldName));
    }

    @Override
    public String setter(String type, String fieldName) {
        String result = """
                   \tpublic void set{funName}({type} {fieldName}) {
                   \t\tthis.{fieldName} = {fieldName};
                   \t}""";

        return result.
                replace("{type}", type).
                replace("{fieldName}", fieldName).
                replace("{funName}", casse(fieldName));
    }

    @Override
    public String generateGetSet(HashMap<String, String> field) {
        StringBuilder result = new StringBuilder();
        for (String s : field.keySet()) {
            result.append(getter(field.get(s), s).concat("\n"));
            result.append(setter(field.get(s), s).concat("\n"));
        }

        return result.toString();
    }

    @Override
    public String generateField(String className, HashMap<String, String> field) throws Exception {
        StringBuilder result = new StringBuilder();
        for (String s : field.keySet()) {
            if (Util.fildIsPrimary(className, s, new ConnectionBDD().getConnection())){
                result.append("\t@Id\n");
            }
            result.append("\tprivate ".concat(field.get(s)).concat(" ").concat(s).concat(";").concat("\n"));
        }

        return result.toString();
    }

    @Override
    public String mappingField(String type) {
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put("SERIAL", "int");
        dictionary.put("FLOAT8", "double");
        dictionary.put("INT", "int");
        dictionary.put("INT4", "int");
        dictionary.put("VARCHAR", "String");
        dictionary.put("DATE", "java.sql.Date");
        dictionary.put("TIME", "java.sql.Time");
        dictionary.put("TIMESTAMP", "java.sql.Timestamp");

        return dictionary.get(type);
    }

    @Override
    public String getPathOut(String pathOut, String pack, String className) {
        String result = pathOut;

        pack = pack.replace(".", "/");
        result+=pack+"/"+className+".java";

        return result;
    }
}
