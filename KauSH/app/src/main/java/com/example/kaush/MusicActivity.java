package com.example.kaush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        TextView musicEmotionText = findViewById(R.id.text_music_emotion);
        String textEmotion = "n";
        Intent intent = getIntent();

        textEmotion = intent.getExtras().getString("emotion");

        if(textEmotion.equals("love")){ musicEmotionText.setText("같이 이 노래 들어보는거 어때요?"); }
        else if(textEmotion.equals("solace")){ musicEmotionText.setText("이 노래 들으면 위로가 될거에요"); }
        else if(textEmotion.equals("cry")){ musicEmotionText.setText("펑펑 울고 털어내세요"); }
    }
}
