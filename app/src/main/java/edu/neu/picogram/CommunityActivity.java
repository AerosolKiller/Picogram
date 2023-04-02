package edu.neu.picogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

public class CommunityActivity extends AppCompatActivity {

  // back button
  private ImageButton backButton;
  private Button popularButton;
  private Button newestButton;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_community);

    backButton = findViewById(R.id.imageButton1);
    popularButton = findViewById(R.id.popular);
    newestButton = findViewById(R.id.newest);

    RecyclerView recyclerView = findViewById(R.id.recyclerView);

    // the recycler view is in horizontal mode
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);

    List<Nonogram> nonogramList = null;

    // the adapter is set to the recycler view
    CommunityAdapter adapter = new CommunityAdapter(this, nonogramList);
    recyclerView.setAdapter(adapter);

    //recyclerview里面装adapter，adapter里面装viewholder，viewholder里面装imageview


  }
}

