package template.back;

import java.util.HashMap;

public interface Template {
    String str_class(String pckg, String className, HashMap<String, String> field) throws Exception;
    String crud(String className);
    String getter(String type, String fieldName);
    String setter(String type, String fieldName);
    String generateGetSet(HashMap<String, String> field);
    String generateField(String className, HashMap<String, String> field) throws Exception;
    String mappingField(String type);
    String getPathOut(String pathOut, String pack, String className);
}
