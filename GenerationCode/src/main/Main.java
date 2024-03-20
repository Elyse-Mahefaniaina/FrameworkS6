package main;

import engine.ConnectionBDD;
import process.Process;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection connection = new ConnectionBDD().getConnection();
        Process.genererBack(args[2], args[1], connection);
        Process.genererNav(args[2], connection);
        Process.genererClassFront(args[2], connection);
        connection.close();
    }
}
