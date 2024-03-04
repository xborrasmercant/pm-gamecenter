package com.example.pm_gamecenter.utilities;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SAXHandler extends DefaultHandler {

    private ArrayList<User> users;
    private User user;
    private StringBuilder data;

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public void startDocument() {
        users = new ArrayList<>();
        data = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        data.setLength(0); // Clear the data buffer
        if (qName.equalsIgnoreCase("user")) {
            user = new User();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        data.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (user != null) {
            switch (qName) {
                case "username":
                    user.setUsername(data.toString());
                    break;
                case "password":
                    user.setPassword(data.toString());
                    break;
                case "highscore_2048":
                    user.setHighScore_2048(Integer.parseInt(data.toString()));
                    break;
                case "highscore_senku":
                    user.setHighScore_Senku(Integer.parseInt(data.toString()));
                    break;
                case "user":
                    users.add(user);
                    user = null;
                    break;
            }
        }
    }
}