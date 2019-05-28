package com.andrey.tickets.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrey.tickets.controller.TicketsController;
import com.andrey.tickets.listener.OnItemClick;
import com.andrey.tickets.R;
import com.andrey.tickets.adapter.SeatAdapter;
import com.andrey.tickets.model.Seat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondStageActivity extends AppCompatActivity implements OnItemClick{
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private SeatAdapter mAdapter;
    private SeatAdapter mAdapter2;

    private Button btnNext;
    private TextView seatCount;

    private Long mSeatId;
    private Long mTrainId;
    private Long mDirectionId;
    private Long mCarriageId;

    private String mSeatName;

    List<Seat> mItems = new ArrayList<>();
    List<Seat> mItems2 = new ArrayList<>();
    List<Seat> mItemsFromDatabase = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_stage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
        initRecyclerView2();

        Intent intent = getIntent();
        mTrainId = intent.getLongExtra("trainId", 0);
        mDirectionId = intent.getLongExtra("directionId", 0);
        mCarriageId = intent.getLongExtra("carriageId", 0);

        if (mTrainId != 0){
            getByTrainId();
        }

        seatCount = findViewById(R.id.seat_count);

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seat seat = new Seat();
                seat.setName(mSeatName);
                seat.setTaken(1);
                updateSeat(seat);
                Intent intent = new Intent(SecondStageActivity.this, ThirdStageActivity.class);
                intent.putExtra("trainId", mTrainId);
                intent.putExtra("directionId", mDirectionId);
                intent.putExtra("carriageId", mCarriageId);
                intent.putExtra("seatId", mSeatId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(Seat seat) {
        mSeatName = seat.getName();

    }

    private void initRecyclerView(){
        mRecyclerView = findViewById(R.id.list_items);
        GridLayoutManager manager = new GridLayoutManager(SecondStageActivity.this, 2, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                manager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new SeatAdapter(this, mItems, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRecyclerView2(){
        mRecyclerView2 = findViewById(R.id.list_items2);
        GridLayoutManager manager = new GridLayoutManager(SecondStageActivity.this, 2, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView2.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView2.getContext(),
                manager.getOrientation());
        mRecyclerView2.addItemDecoration(dividerItemDecoration);
        mAdapter2 = new SeatAdapter(this, mItems2, this);
        mRecyclerView2.setAdapter(mAdapter2);
    }

    private void populateView(){
        for (int i=0; i<mItemsFromDatabase.size(); i++){
            if (mItemsFromDatabase.get(i).getName().contains("a")){
                mItems.add(mItemsFromDatabase.get(i));
                mAdapter.notifyDataSetChanged();

            }else {
                mItems2.add(mItemsFromDatabase.get(i));
                mAdapter2.notifyDataSetChanged();
            }

            int count = 0;

            for (Seat seat: mItemsFromDatabase){
                if (seat.getTaken() == 0){
                    count++;
                }
            }
            seatCount.setText("Свободных мест: "+String.valueOf(count));
        }
    }

    private void getByTrainId() {
        Call<List<Seat>> seat = TicketsController
                .getInstance()
                .getSeatApi()
                .getByTrainId(mTrainId);

        seat.enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(@NonNull Call<List<Seat>> call, @NonNull Response<List<Seat>> response) {
                 mItemsFromDatabase = response.body();
                 populateView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Seat>> call, @NonNull Throwable t) {
                Toast.makeText(SecondStageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSeat(Seat seat) {
        Call<Seat> save = TicketsController
                .getInstance()
                .getSeatApi()
                .update(seat);

        save.enqueue(new Callback<Seat>() {
            @Override
            public void onResponse(@NonNull Call<Seat> call, @NonNull Response<Seat> response) {
                Seat seatResponse = response.body();
                assert seatResponse != null;
                mSeatId = seatResponse.getId();
            }

            @Override
            public void onFailure(@NonNull Call<Seat> call, @NonNull Throwable t) {
                Toast.makeText(SecondStageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
