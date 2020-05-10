package com.example.kaush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MusicActivity extends AppCompatActivity {

    MusicInfo sampleMusic;
    MusicInfo sampleMusic2;

    TextView musicEmotionText;
    ImageView musicImageView1;
    ImageView musicImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        musicEmotionText = (TextView)findViewById(R.id.text_music_emotion);
        musicImageView1 = (ImageView)findViewById(R.id.image_music_1);
        musicImageView2 = (ImageView)findViewById(R.id.image_music_2);

        TextView musicTitleText = (TextView)findViewById(R.id.text_music_title);
        TextView musicDateText = (TextView)findViewById(R.id.text_music_date);

        TextView musicTitleText2 = (TextView)findViewById(R.id.text_music_title2);
        TextView musicDateText2 = (TextView)findViewById(R.id.text_music_date2);

        LinearLayout musicLinearLayout = (LinearLayout)findViewById(R.id.music_linear_layout);
        LinearLayout musicLinearLayout2 = (LinearLayout)findViewById(R.id.music_linear_layout2);

        String textEmotion = "n";
        Intent intent = getIntent();

        textEmotion = intent.getExtras().getString("emotion");

        if(textEmotion.equals("love")){
            setActivityLove();
        }
        else if(textEmotion.equals("solace")){
            setActivitySolace();
        }
        else if(textEmotion.equals("cry")){
            setActivityCry();
        }
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

    public void setActivityLove(){
        musicEmotionText.setText("같이 이 노래 들어보는거 어때요?");
        sampleMusic = new MusicInfo("조이 - 좋은 사람 있으면 소개 시켜줘", "2016.4.2", "https://www.youtube.com/watch?v=hoLzH1revMg");
        sampleMusic2 = new MusicInfo("에릭남, 치즈 - 사랑인가요", "2017.4.8", "https://www.youtube.com/watch?v=7E1w3BoYm_w");
        musicImageView1.setImageResource(R.drawable.image_music_love1);
        musicImageView2.setImageResource(R.drawable.image_music_love2);
    }

    public void setActivitySolace(){
        musicEmotionText.setText("이 노래 들으면 위로가 될거에요");
        sampleMusic = new MusicInfo("개코(Feat. 헤이즈(Heize)) - 바빠서", "2020.2.25", "https://www.youtube.com/watch?v=N5qqjLj_nHE");
        sampleMusic2 = new MusicInfo("악동뮤지션 - 어떻게 이별까지 사랑하겠어, 널 사랑하는거지", "2019.9.25", "https://www.youtube.com/watch?v=mZz9uYdj_v4");
        musicImageView1.setImageResource(R.drawable.image_music_solace1);
        musicImageView2.setImageResource(R.drawable.image_music_solace2);
    }

    public void setActivityCry(){
        musicEmotionText.setText("펑펑 울고 털어내세요");
        sampleMusic = new MusicInfo("에픽하이 - 우산", "2008.4.17", "https://www.youtube.com/watch?v=NIPtyAKxlRs");
        sampleMusic2 = new MusicInfo("로이킴 - 우리 그만하자", "2018.9.18", "https://www.youtube.com/watch?v=fB406grTgz4");
        musicImageView1.setImageResource(R.drawable.image_music_cry1);
        musicImageView2.setImageResource(R.drawable.image_music_cry2);
    }
}
