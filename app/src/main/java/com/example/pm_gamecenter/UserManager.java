package com.example.pm_gamecenter;
import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

public class UserManager {
    private ArrayList<User> users = new ArrayList<>();;


    public void parseUsersXML() {

        try {
            File inputFile = new File("path/to/users.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler saxHandler = new SAXHandler();
            saxParser.parse(inputFile, saxHandler);

            users = saxHandler.getUsers();
            // Process the users list as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeUsersXML(Context context, ArrayList<User> users) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            FileOutputStream fos = context.openFileOutput("users.xml", Context.MODE_PRIVATE);

            serializer.setOutput(writer);

            // Start Document
            serializer.startDocument("UTF-8", true);

            // Start a tag called "users"
            serializer.startTag("", "users");

            for (User user : users) {

                // USER
                serializer.startTag("", "user");

                // USERNAME
                serializer.startTag("", "username");
                serializer.text(user.getUsername());
                serializer.endTag("", "username");

                // PASSWORD
                serializer.startTag("", "password");
                serializer.text(user.getPassword());
                serializer.endTag("", "password");

                // HIGHSCORES 2048 and SENKU
                serializer.startTag("", "highscore_2048");
                serializer.text(String.valueOf(user.getHighScore_2048()));
                serializer.endTag("", "highscore_2048");

                serializer.startTag("", "highscore_senku");
                serializer.text(String.valueOf(user.getHighScore_Senku()));
                serializer.endTag("", "highscore_senku");

                serializer.endTag("", "user");
            }

            serializer.endTag("", "users");

            serializer.endDocument();

            // Write the data
            String dataWrite = writer.toString();
            fos.write(dataWrite.getBytes());
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
