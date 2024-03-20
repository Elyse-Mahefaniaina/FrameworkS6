package template.front;

import util.Util;

import java.util.HashMap;

public class TemplateFront {
    private static String templateFront =
            """
                    import React, { useState } from 'react';
                    import axios from 'axios';
                                        
                    function {className}() {
                      const [dataInsert, setDataInsert] = useState({
                    {initfieldInsert1}
                      });
                      const [dataUpdate, setDatadataUpdate] = useState({
                    {initfieldInsert1}
                      });
                      const [dataDelete, setDatadataDelete] = useState({
                    {initfieldInsert1}
                      });
                      const [data, setData] = useState([]);
                      
                      useEffect(() => {
                        const fetchData = async () => {
                          try {
                            const response = await axios.get('http://{host}:{port}/find{className}.do');
                            setData(response.data);
                          } catch (error) {
                            console.error('Erreur lors de la récupération des données:', error);
                          }
                        };
                      
                        fetchData();
                      }, []);
                                        
                      const handleChangeInsert = (e) => {
                        const { name, value } = e.target;
                        setDataInsert((prevDataInsert) => ({
                          ...prevDataInsert,
                          [name]: value,
                        }));
                      };
                      
                      const handleChangeUpdate = (e) => {
                        const { name, value } = e.target;
                        setDatadataUpdate((prevDataUpdate) => ({
                          ...prevDataUpdate,
                          [name]: value,
                        }));
                      };
                      
                      const handleChangeDelete = (e) => {
                        const { name, value } = e.target;
                        setDatadataDelete((prevDataUpdate) => ({
                          ...prevDataUpdate,
                          [name]: value,
                        }));
                      };
                                        
                      const handleSubmitInsert = async (e) => {
                        e.preventDefault();
                        try {
                          const response = await axios.post('http://{host}:{port}/insert{className}.do', dataInsert);
                          console.log('Donnees inserees avec succes:', response.data);
                          setDataInsert({
                    {initfieldInsert2}
                          });
                        } catch (error) {
                          console.error('Erreur lors de l\\'insertion des données:', error);
                        }
                      };
                      
                      const handleSubmitUpdate = async (e) => {
                        e.preventDefault();
                        try {
                          const response = await axios.post('http://{host}:{port}/update{className}.do', dataUpdate);
                          console.log('Donnees inserees avec succes:', response.data);
                          setDataUpdate({
                    {initfieldInsert2}
                          });
                        } catch (error) {
                          console.error('Erreur lors de l\\'insertion des données:', error);
                        }
                      };
                      const handleSubmitDelete = async (e) => {
                        e.preventDefault();
                        try {
                          const response = await axios.post('http://{host}:{port}/delete{className}.do', dataDelete);
                          console.log('Donnees inserees avec succes:', response.data);
                          setDataDelete({
                    {initfieldInsert2}
                          });
                        } catch (error) {
                          console.error('Erreur lors de l\\'insertion des données:', error);
                        }
                      };
                                       
                      return (
                        <div>
                            <div>
                                <h1>Insertion {className}</h1>
                                <form onSubmit={handleSubmitInsert}>
                    {inputInsert}
                                  <button type="submit">Inserer</button>
                                </form>
                            </div>
                            <div>
                                <h1>Update {className}</h1>
                                <form onSubmit={handleSubmitUpdate}>
                    {inputUpdate}
                                  <button type="submit">Mise a jour</button>
                                </form>
                            </div>
                            <div>
                                <h1>Delete {className}</h1>
                                <form onSubmit={handleSubmitDelete}>
                    {inputDelete}
                                  <button type="submit">Supprimer</button>
                                </form>
                            </div>
                            <div>
                              <h2>Liste des données</h2>
                              <ul>
                                {data.map(item => (
                                  <li key={item.id}>
                                    <strong>ID:</strong> {item.id}, <strong>Nom:</strong> {item.nom}, <strong>Date de naissance:</strong> {item.dtn}
                                  </li>
                                ))}
                              </ul>
                            </div>
                        </div>
                      );
                    }
                                        
                    export default {className};
                                        
                    """;

    public static String genererClass(String className, String host, String port, HashMap<String, String> field) throws Exception {
        return templateFront
                .replace("{className}", Util.casse(className))
                .replace("{initfieldInsert1}", initFieldInsert(className, field, 2))
                .replace("{initfieldInsert2}", initFieldInsert(className, field, 4))
                .replace("{host}", host)
                .replace("{port}", port)
                .replace("{inputInsert}", genererAllInputInsert(className, field))
                .replace("{inputUpdate}", genererAllInputUpdate(field))
                .replace("{inputDelete}", genererAllInputDelete(className, field));
    }

    public static String initFieldInsert(String className,HashMap<String, String> field, int nbr) throws Exception {
        StringBuilder result = new StringBuilder();
        for (String s : field.keySet()) {
            result.append("\t".repeat(nbr)).append(s.toLowerCase()).append(":").append(" '',\n");
        }
        return result.toString();
    }

    public static String genererAllInputInsert(String className, HashMap<String, String> field) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (String s : field.keySet()) {
            if (Util.fildIsPrimary(className, s, null)) continue;
            builder.append(genererInput(s, field.get(s))).append("\n");
        }
        return builder.toString();
    }

    public static String genererAllInputDelete(String className, HashMap<String, String> field) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (String s : field.keySet()) {
            if (Util.fildIsPrimary(className, s, null)) {
                builder.append(genererInputDelete(s, field.get(s))).append("\n");
            }
        }
        return builder.toString();
    }

    public static String genererAllInputUpdate(HashMap<String, String> field) {
        StringBuilder builder = new StringBuilder();
        for (String s : field.keySet()) {
            builder.append(genererInputUpdate(s, field.get(s))).append("\n");
        }
        return builder.toString();
    }

    public static String genererInput(String field, String type) {
        String input=
            """
            \t\t\t\t\t\t\t<input
            \t\t\t\t\t\t\t\ttype={type}
            \t\t\t\t\t\t\t\tname={name}
            \t\t\t\t\t\t\t\tvalue={dataInsert.{namedata}}
            \t\t\t\t\t\t\t\tonChange={handleChangeInsert}
            \t\t\t\t\t\t\t\tplaceholder={name}
            \t\t\t\t\t\t\t/>
                    """;
        return input
                .replace("{type}", "\""+mappingField(type)+"\"")
                .replace("{name}", "\""+field+"\"")
                .replace("{namedata}", field);
    }

    public static String genererInputUpdate(String field, String type) {
        String input=
                """
                \t\t\t\t\t\t\t<input
                \t\t\t\t\t\t\t\ttype={type}
                \t\t\t\t\t\t\t\tname={name}
                \t\t\t\t\t\t\t\tvalue={dataUpdate.{namedata}}
                \t\t\t\t\t\t\t\tonChange={handleChangeUpdate}
                \t\t\t\t\t\t\t\tplaceholder={name}
                \t\t\t\t\t\t\t/>
                        """;
        return input
                .replace("{type}", "\""+mappingField(type)+"\"")
                .replace("{name}", "\""+field+"\"")
                .replace("{namedata}", field);
    }

    public static String genererInputDelete(String field, String type) {
        String input=
                """
                \t\t\t\t\t\t\t<input
                \t\t\t\t\t\t\t\ttype={type}
                \t\t\t\t\t\t\t\tname={name}
                \t\t\t\t\t\t\t\tvalue={dataDelete.{namedata}}
                \t\t\t\t\t\t\t\tonChange={handleChangeDelete}
                \t\t\t\t\t\t\t\tplaceholder={name}
                \t\t\t\t\t\t\t/>
                        """;
        return input
                .replace("{type}", "\""+mappingField(type)+"\"")
                .replace("{name}", "\""+field+"\"")
                .replace("{namedata}", field);
    }

    public static String mappingField(String type) {
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put("int", "number");
        dictionary.put("double", "number");
        dictionary.put("String", "text");
        dictionary.put("java.sql.Date", "date");

        return dictionary.get(type);
    }
}
