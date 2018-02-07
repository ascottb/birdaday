package austinbdev.birdaday;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Austin on 2/5/2018.
 */

public class BirdInfo extends MainActivity {

    //Get random name from birdlist.
    //make sure internet is on
    //if (ConnectivityManager.getActiveNetworkInfo() == null)
    //create search url for wikipedia
        //https://wikipedia.org/w/index.php?search={your-animal-here}
    //Make call to wiki api
    //if call passes
        //display retrieved info
    //if call fails
        //get another bird name
        //if call fails many time, error out
    public String getBirdInfo(Context context) {
        /*if (!isNetworkAvailable()) {
            return null;
        }*/
        Random rand = new Random();
        int  n = rand.nextInt(500) + 1;
        String realBirdName = "";
        try {
            realBirdName = readLineFromFile(n, context);
        }
        catch (Exception e) {
            Log.d("bird", e.getMessage());
        }
        Log.d("bird", realBirdName);
        String birdName = "Test_bird";
        //String wikiSearchUrl = "https://wikipedia.org/w/index.php?search=";
        String wikiSearchUrl = "https://en.wikipedia.org/w/api.php?action=query&titles=" + birdName + "&prop=revisions&rvprop=content&format=json&formatversion=2";
        //String httpResponse = getSendHttpRequest(realBirdName);
        //Log.d("bird", httpResponse);
        return realBirdName;
    }

    private String readLineFromFile(int j, Context context) throws FileNotFoundException, IOException {
        AssetManager am = context.getAssets();
        InputStream is = am.open("birdlist.txt");
        //BufferedReader in = new BufferedReader(new FileReader("birdlist.txt"));
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String info = "";
        int readLine = j;
        try {
            for (int i = 0; i <= readLine; i++) {info = in.readLine();}
            info = info.replace(" ", "_");
            is.close();
            in.close();
            return info;
        }
        catch (Exception e) {
            Log.d("bird", e.getMessage());
        }
        is.close();
        in.close();
        return null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getSendHttpRequest(String birdName) {
        URL url = null;
        String birdUrl = null;
        String response = null;
        try {
            //String parameters = "param1=value1&param2=value2";
            //String parameters = "api.php?action=query&titles=" + birdName + "&prop=revisions&rvprop=content&format=json&formatversion=2";
            String parameters = "api.php?action=query&titles=" + birdName + "&prop=pageimages&format=json&formatversion=2";
            url = new URL("https://en.wikipedia.org/w/" + parameters);
            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            //set the request method to GET
            connection.setRequestMethod("GET");
            //get the output stream from the connection you created
            OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
            //write your data to the ouputstream
            //request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            //create your inputstream
            InputStreamReader isr = new InputStreamReader(
                    connection.getInputStream());
            //read in the data from input stream, this can be done a variety of ways
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                String delims = "[\"]";
                String[] tokens = line.split(delims);
                for (String token : tokens) {
                    Log.d("bird", token);
                    if (token.toLowerCase().contains("https".toLowerCase())) {
                        birdUrl = token;
                    }
                }
            }
            //get the string version of the response data
            response = sb.toString();
            //do what you want with the data now

            //always remember to close your input and output streams
            isr.close();
            reader.close();
        } catch (IOException e) {
            Log.e("HTTP GET:", e.toString());
        }
        return birdUrl;
    }

}


