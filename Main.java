package com.company;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.URLConnection;


public class Main {

    public static void main(String[] args)
    {
        String way = "data";
        List<Map<String, String>> tables = new ArrayList<Map<String, String>>();
        for(int i=0;i<2;i++) {
            HashMap<String, String> Table = new HashMap<>();
            try (FileReader rd = new FileReader(way+(i+1)+".txt")) {
                BufferedReader reader = new BufferedReader(rd);
                String line = reader.readLine();

                while (line != null) {
                    String content = null;
                    URLConnection connection = null;
                    try {
                        connection = new URL(line).openConnection();
                        Scanner scanner = new Scanner(connection.getInputStream());
                        scanner.useDelimiter("\\Z");
                        content = scanner.next();
                        scanner.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Table.put(line, content);
                    line = reader.readLine();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            tables.add(i,Table);
        }

        String DeletedPages = new String("");
        String ChangedPages = new String("");
        String AddedPages = new String("");

        for(String key: tables.get(0).keySet())
        {
            if(!tables.get(1).containsKey(key)) {
                DeletedPages = DeletedPages + key + "\n";
            } else {
              if(!(tables.get(0).get(key).equals(tables.get(1).get(key)))) {
                  ChangedPages = ChangedPages + key + "\n";
              }
            }
        }

        for(String key: tables.get(1).keySet()) {
            if(!tables.get(0).containsKey(key)) {
                AddedPages = AddedPages + key + "\n";
            }
        }

        if(AddedPages.isEmpty()) AddedPages = "Добавленных страниц нет\n";
        if(ChangedPages.isEmpty()) ChangedPages = "Измененных страниц нет\n";
        if(DeletedPages.isEmpty()) DeletedPages = "Удаленных страниц нет\n";

        Scanner in = new Scanner(System.in);
        System.out.print("Введите имя секретаря ");
        String name = in.nextLine();
        String out = "Здравствуйте, дорогая " + name + "\nЗа последние сутки во вверенных Вам сайтах произошли следующие изменения:" +
        "\nИсчезли следующие страницы:\n" + DeletedPages + "Появились следующие страницы:\n" + AddedPages + "Изменились следующие страницы:\n" + ChangedPages;
        System.out.print(out);

        try {
            File letter = new File("letter.txt");
            if(!letter.exists()) letter.createNewFile();
            PrintWriter pw = new PrintWriter(letter);
            pw.println(out);
            pw.close();
        } catch(IOException ex) {
            System.out.print(ex.getMessage());
        }

    }
}

