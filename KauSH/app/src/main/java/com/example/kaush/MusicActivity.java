package com.example.kaush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import org.w3c.dom.Text;

public class MusicActivity extends AppCompatActivity {

    MusicInfo sampleMusic;
    MusicInfo sampleMusic2;

    TextView musicEmotionText;
    ImageView musicImageView1;
    ImageView musicImageView2;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    Random random = new Random();
    String rand, rand2;

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

        String textEmotion;
        Intent intent = getIntent();

        textEmotion = intent.getExtras().getString("emotion");
        sampleMusic = intent.getExtras().getParcelable("MUSIC1");
        sampleMusic2 = intent.getExtras().getParcelable("MUSIC2");

        if(textEmotion.equals("love")) {
                setActivityLove();
            }
        else if(textEmotion.equals("solace")){
            setActivitySolace();
            }
        else if(textEmotion.equals("cry")) {
                        setActivityCry();
            }
        else { }


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
        musicImageView1.setImageResource(R.drawable.image_music_love1);
        musicImageView2.setImageResource(R.drawable.image_music_love2);
    }

    public void setActivitySolace(){
        musicEmotionText.setText("이 노래 들으면 위로가 될거에요");
        musicImageView1.setImageResource(R.drawable.image_music_solace1);
        musicImageView2.setImageResource(R.drawable.image_music_solace2);
    }

    public void setActivityCry(){
        musicEmotionText.setText("펑펑 울고 털어내세요");
        musicImageView1.setImageResource(R.drawable.image_music_cry1);
        musicImageView2.setImageResource(R.drawable.image_music_cry2);
    }

}
