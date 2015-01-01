package example.org.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process() {
        boolean operationStatus = true;

        Application currentApplication = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentApplication = new Application();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if (tagName.equalsIgnoreCase("entry")) {
                                inEntry = false;
                                applications.add(currentApplication);
                            }
                            if (tagName.equalsIgnoreCase("name")) {
                                currentApplication.setName(textValue);
                            } else if (tagName.equalsIgnoreCase("artist")) {
                                currentApplication.setArtist(textValue);
                            } else if (tagName.equalsIgnoreCase("releaseDate")) {
                                currentApplication.setReleaseDate(textValue);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            operationStatus = false;
        }

        for (Application app : applications) {
            Log.d("LOG", "*****************");
            Log.d("LOG", app.getName());
            Log.d("LOG", app.getArtist());
            Log.d("LOG", app.getReleaseDate());
        }

        return operationStatus;
    }
}
