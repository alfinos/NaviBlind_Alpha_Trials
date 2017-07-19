package com.example.salfino.naviblind_110217;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class Text_to_Speech extends AppCompatActivity {
    TextToSpeech t1;
    EditText ed1;
    Button b1;
    private static final String TAG = "Text to Speech";
    Bundle params = new Bundle();
    public ArrayList<RoomWayPointEntry> myData;
    public String mystring;
    public String mystring2;
    public String mystring3;
    //HashMap<String,String> map = new HashMap<String,String>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to__speech);

        Log.d(TAG, "onCreate: starting Asynctask");
        DownloadXML downloadData = new DownloadXML();
        downloadData.execute("http://naviblind.000webhostapp.com/main_rev2.xml");
        Log.d(TAG, "onCreate: done");

        ed1 = (EditText) findViewById(R.id.editText);
        b1 = (Button) findViewById(R.id.button);
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"");
        //map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"123");

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.UK);
                    t1.setSpeechRate(0.8f);
                    t1.setPitch(0.8f);

                    t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                                Log.d(TAG, "Speech Starts: " + utteranceId);

                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.d(TAG, "Speech Finished: " + utteranceId);
                            /*if(utteranceId.equals("124")) {
                                t1.speak(mystring2, TextToSpeech.QUEUE_FLUSH, params, "567");
                            }else if (utteranceId.equals("567")){
                                t1.speak(mystring, TextToSpeech.QUEUE_FLUSH, params, "124");


                            }*/

                        }

                        @Override
                        public void onError(String utteranceId) {
                                Log.d(TAG, "Speech in Error: " + utteranceId);

                        }
                    });
                }

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String toSpeak = ed1.getText().toString();
                t1.speak(mystring2,TextToSpeech.QUEUE_FLUSH,params,"124");
                //t1.speak(mystring2,TextToSpeech.QUEUE_FLUSH,params,"567");
                //t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH,map);
            }
        });


    }

    private class DownloadXML extends AsyncTask<String,Void,String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParseIndoorAtlas parseIndoorAtlas = new ParseIndoorAtlas();
            //parseIndoorAtlas.parse(s); //s is the downloaded XML file
            //myData = parseIndoorAtlas.getData();
           // RoomWayPointEntry output = myData.get(0);
           // RoomWayPointEntry output2 = myData.get(1);
           // mystring = output.getLevelnumber();
           // mystring2 = output.getText();
            Log.d(TAG, "MY DATA: " + mystring);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: starts with " + params[0]);
            String indoorAtlasFeed = downloadXML (params[0]);
            if (indoorAtlasFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading XML data");
            }
            return indoorAtlasFeed;
        }

        private String downloadXML (String urlPath){
            StringBuilder xmlResult = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){//End of stream of data
                        break;
                    }
                    if(charsRead > 0){//Keep count of number of characters read from stream
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));//Append until there is no more data to read
                    }
                }
                reader.close();//All IO object will be closed

                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());

            } catch (IOException e){
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());

            } catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security Exception. Needs Permission! " + e.getMessage());
                //e.printStackTrace();
            }

            return null;
        }

    }

}
