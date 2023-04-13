package edu.neu.picogram;

import static edu.neu.picogram.NonogramUtils.drawNonogram;
import static edu.neu.picogram.gamedata.UserNonogramConstants.getUserGames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CommunityActivity extends AppCompatActivity {

  // back button
  private ImageButton backButton;
  private Button popularButton;
  private Button newestButton;

  private Button favoriteButton;


  RecyclerView recyclerView;

  public ArrayList<UserNonogram> nonogramList;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_community);

    backButton = findViewById(R.id.imageButton1);
    popularButton = findViewById(R.id.popular);
    newestButton = findViewById(R.id.newest);
    favoriteButton = findViewById(R.id.favorite);


    // 找到控件recyclerview
    recyclerView = findViewById(R.id.recyclerView);

    // 准备数据
    initData();

    // the recycler view is in horizontal mode
    LinearLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);


    // the adapter is set to the recycler view
    CommunityAdapter adapter = new CommunityAdapter(this, nonogramList);
    recyclerView.setAdapter(adapter);

    popularButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //sortByPopularity(nonogramList);
        adapter.sortByPopular();
//        nonogramList = nonogramList.stream().sorted((a, b) -> b.getLikedNum() - a.getLikedNum()).collect(Collectors.toList());

//        Log.d("TAG", nonogramList.toString());

      }
    });

    newestButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //sortByNewest(nonogramList);
        nonogramList.stream().sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()));
        Log.d("TAG", nonogramList.toString());
      }
    });

    favoriteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //open the new activity of all favorite games
      }
    });
  }

  private void initData() {
    // List<Data> list ---> Apdater ---> setAdapter ---> 显示数据
    nonogramList = new ArrayList<>();

    // 创建模拟数据

      //创建数据对象；
    nonogramList = getUserGames();


    //创建适配器
    CommunityAdapter adapter = new CommunityAdapter(this, nonogramList);
    //设置到Recycler view里面去
    recyclerView.setAdapter(adapter);
  }
}

class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

  private Context context;
  private ArrayList<UserNonogram> nonogramList;

  public CommunityAdapter(Context context, ArrayList<UserNonogram> nonogramList) {
    this.context = context;
    this.nonogramList = nonogramList;
  }

  // update the nonogram list
  public void sortByPopular() {
    this.nonogramList = (ArrayList<UserNonogram>) nonogramList.stream()
            .sorted((a, b) -> b.getLikedNum() - a.getLikedNum())
            .collect(Collectors.toList());
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // 这里view就是单个item条目的界面
    View view = LayoutInflater.from(context).inflate(R.layout.activity_item_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // 绑定数据
    Nonogram nonogram = nonogramList.get(position);
    holder.setData(nonogram);

    holder.likeButton.setOnClickListener(new View.OnClickListener()

    {
      @Override
      public void onClick (View v){
        // add 1 to the nonograms like number


    }
    });
  }

  // 返回数据的个数
  @Override
  public int getItemCount() {
    if (nonogramList != null) return nonogramList.size();
    else return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView textView;
    private ImageButton likeButton;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      // 找到控件
      imageView = itemView.findViewById(R.id.image);
      textView = itemView.findViewById(R.id.name);
      likeButton = itemView.findViewById(R.id.like_button);
    }

    public void setData(Nonogram nonogram) {
      imageView.setImageBitmap(drawNonogram(nonogram));
      textView.setText(nonogram.getName());
    }


  }
}

