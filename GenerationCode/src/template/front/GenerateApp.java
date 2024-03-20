package template.front;

import util.Util;

import java.util.List;

public class GenerateApp {
    public static String getNav =
        """
        import React from 'react';
        import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
        {importcomposant}
                
        function App() {
        \treturn (
        \t\t<Router>
        \t\t\t<div>
        \t\t\t\t<nav>
        \t\t\t\t\t<ul>
        {link}
        \t\t\t\t\t</ul>
        \t\t\t\t</nav>
        {route}
        \t\t\t</div>
        \t\t</Router>
        \t);
        }
        
        export default App;
        """;

    public static String generer(String pathout, List<String> tables) {
        return getNav
                .replace("{importcomposant}", genererImportCOmposant(tables))
                .replace("{link}", genererLink(tables))
                .replace("{route}", genererRoute(tables));
    }

    public static String genererRoute(List<String> tables) {
        StringBuilder result = new StringBuilder();
        for (String s : tables) {
            result.append("\t\t\t\t<Route path=\"/").append(Util.casse(s)).append("\" exact component={").append(Util.casse(s)).append("} />");
            result.append("\n");
        }
        return result.toString();
    }

    public static String genererLink(List<String> tables) {
        StringBuilder result = new StringBuilder();
        for (String s : tables){
            result
                    .append("\t\t\t\t\t\t<li>\n")
                    .append("\t\t\t\t\t\t\t<Link to=\"/").append(Util.casse(s)).append("\">").append(Util.casse(s)).append("</Link>\n")
                    .append("\t\t\t\t\t\t</li>\n");
        }
        return result.toString();
    }

    public static String genererImportCOmposant(List<String> tables) {
        StringBuilder result = new StringBuilder();
        for (String s : tables) {
            result.append("import { ").append(Util.casse(s)).append(" } from './").append(Util.casse(s)).append("'");
            result.append("\n");
        }
        return result.toString();
    }
}
