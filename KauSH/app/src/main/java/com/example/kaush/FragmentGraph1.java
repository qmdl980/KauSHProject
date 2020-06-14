package com.example.kaush;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FragmentGraph1 extends Fragment
{
    HashMap<String, String> negetive_emotion_map = new HashMap<String, String>();
    HashMap<String, String> positive_emotion_map = new HashMap<String, String>(); //  날짜와 그때의 감정을 저장하는 해시


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference mDBReference = FirebaseDatabase.getInstance().getReference(); // 데이터베이스 접근 객체
    int emotion_count = 0; // 그동안 몇번의 감정이 있었는지 카운팅

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        System.out.println("]]]]]]]]]]]]]]]]]");
        System.out.println(mDBReference);
        mDBReference.addListenerForSingleValueEvent(new ValueEventListener() {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.child("account").child(user.getUid()).child("Emotion").getChildren())
                {
                    negetive_emotion_map.put(dataSnapshot.child("account").child(user.getUid()).child("Emotion").getValue().toString(),
                            snapshot.child("negative").getValue().toString());
                    positive_emotion_map.put(dataSnapshot.child("account").child(user.getUid()).child("Emotion").getValue().toString(),
                            snapshot.child("positive").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());

            }
        });
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_graph1, container, false);
        LineChart lineChart = (LineChart) v.findViewById(R.id.chart);

        Set<String> negative_keySet = negetive_emotion_map.keySet();
        Set<String> positive_keySet = positive_emotion_map.keySet();

        ArrayList<Entry> negative_entries = new ArrayList<>();
        ArrayList<Entry> positive_entries = new ArrayList<>();

        if(negetive_emotion_map.size() != 0 && positive_emotion_map.size() != 0) {
            for (String key : negative_keySet) {
                negative_entries.add(new Entry(Float.parseFloat(key), Float.parseFloat(negetive_emotion_map.get(key))));
                positive_entries.add(new Entry(Float.parseFloat(key), Float.parseFloat(positive_emotion_map.get(key))));
            }
        }
        LineDataSet negative_linedataset = new LineDataSet(negative_entries, "꺽은선1");
        negative_linedataset.setLineWidth(2); // 선 굵기
        negative_linedataset.setColors(ColorTemplate.COLORFUL_COLORS);

        LineDataSet positive_linedataset = new LineDataSet(positive_entries, "꺽은선1");
        positive_linedataset.setLineWidth(2); // 선 굵기
        positive_linedataset.setColors(ColorTemplate.COLORFUL_COLORS);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setLabelCount(5, true);

        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //y축의 활성화를 제거함


        LineData chardata = new LineData();
        chardata.addDataSet(negative_linedataset);
        chardata.addDataSet(positive_linedataset);
        lineChart.setData(chardata);
        //lineChart.setVisibleXRangeMinimum(60 * 60 * 24 * 10); //라인차트에서 최대로 보여질 X축의 데이터 설정

        lineChart.animateY(2000);
        lineChart.invalidate();


        return v;
    }
}
