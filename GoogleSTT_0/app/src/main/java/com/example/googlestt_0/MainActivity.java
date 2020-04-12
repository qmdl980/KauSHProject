package com.example.googlestt_0;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
////////////////////////////
public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 1234;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    Button Start;
    TextView Speech;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;
    String text_data;
    private Object m_oMainActivity;
    //private Object m_oMainActivity;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Start = (Button)findViewById(R.id.start_reg);
        Speech = (TextView)findViewById(R.id.speech);


        //checkExternalStorage();

        // AMAZONS3CLIENT 객체 생성
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-2:86ccb826-e4c8-4de2-b167-199c79848012", // Identity Pool ID
                Regions.US_EAST_2 // Region
        );
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());
        //
        s3.setRegion(Region.getRegion(Regions.US_EAST_2)); s3.setEndpoint("s3.us-east-2.amazonaws.com");
        // transferobserver 객체 생성 --> 파일을 업로드 하는 부분

        //

        Start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, REQUEST_CODE); //
                }
                else{
                    Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }

            }

        });
    }
    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            System.out.println("1--1--1--1--1--1--1---1--1--1--");
            return true;

        }
        return false;
    }

    // 외부 메모리 상태 확인 메소드
    boolean checkExternalStorage() {
        state = Environment.getExternalStorageState();
        // 외부메모리 상태
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 읽기 쓰기 모두 가능
            Log.d("test", "외부메모리 읽기 쓰기 모두 가능");
            //tv.setText("외부메모리 읽기 쓰기 모두 가능");
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            //읽기전용
            Log.d("test", "외부메모리 읽기만 가능");
            //tv.setText("외부메모리 읽기만 가능");
            return false;
        } else {
            // 읽기쓰기 모두 안됨
            Log.d("test", "외부메모리 읽기쓰기 모두 안됨 : "+ state);
            //tv.setText("외부메모리 읽기쓰기 모두 안됨 : "+ state);
            return false;
        }
    }
    //출처: https://bitsoul.tistory.com/117 [Happy Programmer~]

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            match_text_dialog = new Dialog(MainActivity.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = (ListView)match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Speech.setText("You have said " + "\n" + matches_text.get(position));
                    text_data = matches_text.get(position);
                    match_text_dialog.hide();
                    isExternalStorageWritable();
                    checkExternalStorage();
                    //
                    System.out.println("|||||||||||||||||||" + matches_text.get(position) + "||||||||||||||");

                }
            });
            match_text_dialog.show();


            try
            {
                AWSService awss = new AWSService();
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(path, "text_data"); // 경로, 파일명
                FileWriter fw = new FileWriter(f, false);
                fw.write(text_data);
                awss.uploadFile(f);
                fw.close();

                System.out.println("**********"+path+"************");

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}