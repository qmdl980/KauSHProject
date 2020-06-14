package com.example.kaush;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
/*
public class FragmentGraph2 extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_graph2, container, false);
    }
}
*/

public class FragmentGraph2 extends Fragment
{
    PieChart pieChart;
    HashMap<Integer, String> negetive_emotion_map = new HashMap<Integer, String>();
    HashMap<Integer, String> positive_emotion_map = new HashMap<Integer, String>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference mDBReference = FirebaseDatabase.getInstance().getReference(); // 데이터베이스 접근 객체
    int emotion_count = 0; // 그동안 몇번의 감정이 있었는지 카운팅
    float negative_emotion_total;
    float positive_emotion_total;
    float negative_emotion_avg;
    float positive_emotion_avg;

    /*
    public void onDateChange(@NonNull DataSnapshot dataSnapshot)
    {

        for (DataSnapshot snapshot : dataSnapshot.child("account").child(user.getUid()).child("Emotion").getChildren())
        {
            emotion_count++;
            negative_emotion_total = negative_emotion_total + Integer.parseInt(snapshot.child("negative").getValue().toString());
            positive_emotion_total = positive_emotion_total + Integer.parseInt(snapshot.child("positive").getValue().toString());
            negetive_emotion_map.put(emotion_count, snapshot.child("negative").getValue().toString()); // 혹시 사용할 일이 있을까... map<횟수, 감정수치>
            positive_emotion_map.put(emotion_count, snapshot.child("positive").getValue().toString());
        }
        negative_emotion_avg = negative_emotion_total / emotion_count;
        positive_emotion_avg = positive_emotion_total / emotion_count;

    }
    //mDBReference.child("account").child(user.getUid()).child("MusicList").child(TIME).setValue(sampleMusic);
    */
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mDBReference.addValueEventListener(new ValueEventListener() {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.child("account").child(user.getUid()).child("Emotion").getChildren())
                {
                    emotion_count++;
                    negative_emotion_total = negative_emotion_total + Float.parseFloat(snapshot.child("negative").getValue().toString());
                    positive_emotion_total = positive_emotion_total + Float.parseFloat(snapshot.child("positive").getValue().toString());
                    negetive_emotion_map.put(emotion_count, snapshot.child("negative").getValue().toString()); // 혹시 사용할 일이 있을까... map<횟수, 감정수치>
                    positive_emotion_map.put(emotion_count, snapshot.child("positive").getValue().toString());
                }
                negative_emotion_avg = negative_emotion_total / emotion_count;
                positive_emotion_avg = positive_emotion_total / emotion_count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        View v = inflater.inflate(R.layout.fragment_graph2, container, false);
        PieChart pieChart = (PieChart) v.findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(negative_emotion_avg, "negative"));
        yValues.add(new PieEntry(positive_emotion_avg,"positive"));

        System.out.println("-------------");
        System.out.println(negative_emotion_avg);
        System.out.println(positive_emotion_avg);
        System.out.println("-------------");
        /*
        yValues.add(new PieEntry(14f,"UK"));
        yValues.add(new PieEntry(35f,"India"));
        yValues.add(new PieEntry(40f,"Russia"));
        yValues.add(new PieEntry(40f,"Korea"));
        */
        //Description description = new Description();
        //description.setText("감정 상태"); //라벨
        //description.setTextSize(15);
        //pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "emotion");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.invalidate(); // 그래프 갱신

        return v;

    }


}