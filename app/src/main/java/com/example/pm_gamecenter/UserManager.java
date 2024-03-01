package com.example.pm_gamecenter;
import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

public class UserManager {
    private static UserManager instance;
    private ArrayList<User> users;
    private User activeUser;

    private UserManager() {
        users = new ArrayList<>();
    }

    public void addUser(Context context, User user) {
        users.add(user);
        writeUsersXML(context);
        parseUsersXML(context);
    }

    public void parseUsersXML(Context context) {

        try {
            InputStream is = context.openFileInput("users.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler saxHandler = new SAXHandler();
            saxParser.parse(is, saxHandler);

            users = saxHandler.getUsers();
            // Process the users list as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeUsersXML(Context context) {
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

    public void resetUsersXML(Context context) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            FileOutputStream fos = context.openFileOutput("users.xml", Context.MODE_PRIVATE);

            serializer.setOutput(writer);

            // Start Document
            serializer.startDocument("UTF-8", true);

            // Start a tag called "users"
            serializer.startTag("", "users");


            // USER
            serializer.startTag("", "user");

            // USERNAME
            serializer.startTag("", "username");
            serializer.text("admin");
            serializer.endTag("", "username");

            // PASSWORD
            serializer.startTag("", "password");
            serializer.text("admin");
            serializer.endTag("", "password");

            // HIGHSCORES 2048 and SENKU
            serializer.startTag("", "highscore_2048");
            serializer.text("/");
            serializer.endTag("", "highscore_2048");

            serializer.startTag("", "highscore_senku");
            serializer.text(String.valueOf(user.getHighScore_Senku()));
            serializer.endTag("", "highscore_senku");

            serializer.endTag("", "user");

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


    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    public static void setInstance(UserManager instance) {
        UserManager.instance = instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
}
