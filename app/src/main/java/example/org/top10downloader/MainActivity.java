package example.org.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    Button btnParse;
    ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParse = (Button) findViewById(R.id.btnParse);
        listApps = (ListView) findViewById(R.id.listApps);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listApps.setVisibility(View.VISIBLE);
            }
        });

        new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        String mXmlData;

        protected String doInBackground(String... urls) {
            try {
                mXmlData = downloadXML(urls[0]);
            } catch (IOException e) {
                return "Unable to download XML file.";
            }
            return "";
        }

        protected void onPostExecute(String result) {
            Log.d("OnPostExecute", mXmlData);
        }

        private String downloadXML(String theUrl) throws IOException {
            int BUFFER_SIZE = 2000;
            InputStream inputStream = null;

            String xmlContents = "";
            try {
                URL url = new URL(theUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(15 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                int response = connection.getResponseCode();
                Log.d("DownloadXML", "The response returned is: " + response);
                inputStream = connection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);
                int charRead;
                char[] inputBuffer = new char[BUFFER_SIZE];
                try {
                    while ((charRead = reader.read(inputBuffer)) > 0) {
                        String readString = String.copyValueOf(inputBuffer, 0, charRead);
                        xmlContents += readString;
                        inputBuffer = new char[BUFFER_SIZE];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return xmlContents;
        }
    }
}
