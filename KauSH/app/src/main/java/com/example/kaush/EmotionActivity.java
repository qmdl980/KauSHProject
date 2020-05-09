package com.example.kaush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmotionActivity extends AppCompatActivity {

    String userText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        Intent intent = getIntent();

        userText = intent.getExtras().getString("text");

        //TextView emotionText = findViewById(R.id.text_emotion);

        //emotionText.setText(userText);

        Button btnLoveMusic = findViewById(R.id.btn_music_love);
        btnLoveMusic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent(getApplicationContext(),MusicActivity.class);
                mintent.putExtra("emotion", "love");
                startActivityForResult(mintent,1);
            }
        });


    }
}
