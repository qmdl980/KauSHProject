package com.example.kaush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MusicActivity extends AppCompatActivity {

    MusicInfo sampleMusic = new MusicInfo("조이 - 좋은사람있으면소개시켜줘", "2016-4-2", "https://www.youtube.com/watch?v=hoLzH1revMg");
    MusicInfo sampleMusic2 = new MusicInfo("에릭남, 치즈 - 사랑인가요", "2017-4-8", "https://www.youtube.com/watch?v=7E1w3BoYm_w");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        TextView musicEmotionText = (TextView)findViewById(R.id.text_music_emotion);

        TextView musicTitleText = (TextView)findViewById(R.id.text_music_title);
        TextView musicDateText = (TextView)findViewById(R.id.text_music_date);

        TextView musicTitleText2 = (TextView)findViewById(R.id.text_music_title2);
        TextView musicDateText2 = (TextView)findViewById(R.id.text_music_date2);

        LinearLayout musicLinearLayout = (LinearLayout)findViewById(R.id.music_linear_layout);
        LinearLayout musicLinearLayout2 = (LinearLayout)findViewById(R.id.music_linear_layout2);

        String textEmotion = "n";
        Intent intent = getIntent();

        textEmotion = intent.getExtras().getString("emotion");

        if(textEmotion.equals("love")){ musicEmotionText.setText("같이 이 노래 들어보는거 어때요?"); }
        else if(textEmotion.equals("solace")){ musicEmotionText.setText("이 노래 들으면 위로가 될거에요"); }
        else if(textEmotion.equals("cry")){ musicEmotionText.setText("펑펑 울고 털어내세요"); }
        else{}

        musicTitleText.setText(sampleMusic.title);
        musicDateText.setText(sampleMusic.date);

        musicTitleText2.setText(sampleMusic2.title);
        musicDateText2.setText(sampleMusic2.date);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.music_linear_layout:
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse(sampleMusic.url)) // edit this url
                                .setPackage("com.google.android.youtube"));	// do not edit
                        break;
                    case R.id.music_linear_layout2:
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse(sampleMusic2.url)) // edit this url
                                .setPackage("com.google.android.youtube"));	// do not edit
                        break;
                }
            }
        };
        musicLinearLayout.setOnClickListener(clickListener);
        musicLinearLayout2.setOnClickListener(clickListener);
    }

    public void MusicLove(){

    }

    public void MusicSolace(){

    }

    public void MusicCry(){

    }
}
