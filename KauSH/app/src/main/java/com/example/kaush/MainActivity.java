package com.example.kaush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    Button Start;
    TextView Speech;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;
    public String text_data = "입력받지 않음"; // 사용자가 말하는 음성데이터 **
    FileOutputStream outputStream; // 파일 입출력을 위한 파일객체 **
    private TransferUtility transferUtility;
    String probability_list;
    String[] probabilities = new String[3]; // 확률값들만 저장하는 배열
    private CustomDialog customDialog;

    ListView musicYetList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Start = (Button) findViewById(R.id.btn_google_stt);
        Button Next = findViewById(R.id.button_to_emotion);

        musicYetList = findViewById(R.id.list_view_music_yet);
        MusicAdapter musicAdapter = new MusicAdapter();
        musicYetList.setAdapter(musicAdapter);

        for(int i = 0; i <= 10; i++) {
            musicAdapter.addItem("조이 - 좋은사람있으면소개시켜줘", "2020-05-10", "사랑");
        }
        // AMAZONS3CLIENT 객체 생성
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-2:cfe39edd-07c9-4e30-aff8-d313684eb2a3", // Identity Pool ID
                Regions.US_EAST_2 // Region
        );
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3, getApplicationContext());

        s3.setRegion(Region.getRegion(Regions.US_EAST_2));
        s3.setEndpoint("s3.us-east-2.amazonaws.com");


        new Thread() {
            public void run() {
                String nodingHtml = getNodingHtml();

                Bundle bun = new Bundle();
                bun.putString("NODING_HTML", nodingHtml);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String a = "1";

                if (isConnected()) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, REQUEST_CODE); //
                } else {
                    Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }

        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialog = new CustomDialog(MainActivity.this);
                customDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customDialog.cancel();

                        Thread t1 = new Thread() {
                            public void run() {
                                String nodingHtml = getNodingHtml();
                                Bundle bun = new Bundle();
                                bun.putString("NODING_HTML", nodingHtml);
                                probability_list = nodingHtml;
                                Message msg = handler.obtainMessage();
                                msg.setData(bun);
                                handler.sendMessage(msg);
                            }
                        };

                        t1.start();
                        try {
                            t1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        dividedProbability();

                        Float probability1 = Float.parseFloat(probabilities[1]);
                        Float probability2 = Float.parseFloat(probabilities[2]);
                        if (probability1 <= probability2) {
                            Intent eintent = new Intent(getApplicationContext(), EmotionActivity.class);
                            eintent.putExtra("text", text_data);
                            startActivityForResult(eintent, REQUEST_CODE);
                        } else {
                            Intent eintent = new Intent(getApplicationContext(), EmotionActivity2.class);
                            eintent.putExtra("text", text_data);
                            startActivityForResult(eintent, REQUEST_CODE);
                        }
                    }
                }, 5000);
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
//
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String nodingHtml = bun.getString("NODING_HTML");
            probability_list = nodingHtml;
            dividedProbability();
        }
    };

    private String getNodingHtml() {
        String nodingHtml = "";

        URL url = null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            url = new URL("https://43sisn313b.execute-api.us-east-2.amazonaws.com/sage-function");
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3 * 1000);
            http.setReadTimeout(3 * 1000);

            isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                nodingHtml += str + "\n";
            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            if (http != null) {
                try {
                    http.disconnect();
                } catch (Exception e) {
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
        return nodingHtml;
    }

    // [] 를 포함한 확률 리스트에서 확률값들만 뽑아내는 함수
    private void dividedProbability() {
        probabilities = probability_list.split(",");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            match_text_dialog = new Dialog(MainActivity.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = (ListView) match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);

            //선택안하고 첫번째 인덱스의 텍스트를 바로 출력
            // 처음 나타나는 데이터가 가장 높은 확룰의 매칭을 보여주는거 같음.
            text_data = matches_text.get(0);

            String filename = "myfile";
            String string = text_data;

            // 여기서 부터 s3에 저장하는 코드 (아무런 사용자의 activity 없이... 클릭도 안하고... )
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();

                Toast.makeText(this, "this is internal storage save success.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(this, "this is internal storage save fail.", Toast.LENGTH_LONG).show();
            }
            TransferObserver observer = (TransferObserver) transferUtility.upload(
                    "noding2",
                    "myfile.txt",
                    new File("/data/data/com.example.kaush/files/myfile")
            );

            //여기서부터 dialog실행 5초 대기 및 다음 액티비티로 넘어가는 과정 해보기
            //우선 에뮬에서는 확인할수 없기 때문에 출력방식으로 사용 or 버튼누를시 하는걸로

            customDialog = new CustomDialog(MainActivity.this);
            customDialog.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    customDialog.cancel();

                    Thread t1 = new Thread() {
                        public void run() {
                            String nodingHtml = getNodingHtml();
                            Bundle bun = new Bundle();
                            bun.putString("NODING_HTML", nodingHtml);
                            probability_list = nodingHtml;
                            Message msg = handler.obtainMessage();
                            msg.setData(bun);
                            handler.sendMessage(msg);
                        }
                    };

                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    dividedProbability();

                    Float probability1 = Float.parseFloat(probabilities[1]);
                    Float probability2 = Float.parseFloat(probabilities[2]);
                    if (probability1 <= probability2) {
                        Intent eintent = new Intent(getApplicationContext(), EmotionActivity.class);
                        eintent.putExtra("text", text_data);
                        startActivityForResult(eintent, REQUEST_CODE);
                    } else {
                        Intent eintent = new Intent(getApplicationContext(), EmotionActivity2.class);
                        eintent.putExtra("text", text_data);
                        startActivityForResult(eintent, REQUEST_CODE);
                    }
                }
            }, 5000);
        }super.onActivityResult(requestCode,resultCode,data);
    }
}

