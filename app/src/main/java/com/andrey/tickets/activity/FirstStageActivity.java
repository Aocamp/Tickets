package com.andrey.tickets.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.andrey.tickets.R;
import com.andrey.tickets.controller.TicketsController;
import com.andrey.tickets.model.Carriage;
import com.andrey.tickets.model.Direction;
import com.andrey.tickets.model.Schedule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstStageActivity extends AppCompatActivity {
    private Button mChooseDirection;
    private Button mChooseSchedule;
    private Button mChooseCarriage;

    private Spinner mSpinnerDirection;
    private Spinner mSpinnerSchedule;
    private Spinner mSpinnerCarriage;

    private LinearLayout linearLayoutSchedule;
    private LinearLayout linearLayoutCarriage;

    private List<Direction> directionItems;
    private List<Schedule> scheduleItems;
    private List<Carriage> carriageItems;

    private Direction mSelectedDirection;
    private Schedule mSelectedSchedule;
    private Carriage mSelectedCarriage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_stage);

        linearLayoutSchedule = findViewById(R.id.linearLayoutSchedule);
        linearLayoutSchedule.setVisibility(View.GONE);

        linearLayoutCarriage = findViewById(R.id.linearLayoutCarriage);
        linearLayoutCarriage.setVisibility(View.GONE);

        mSpinnerDirection = findViewById(R.id.spinner_direction);
        mSpinnerSchedule = findViewById(R.id.spinner_schedule);
        mSpinnerCarriage = findViewById(R.id.spinner_carriage);

        getAllDirections();
        mSpinnerDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedDirection = directionItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseDirection = findViewById(R.id.btn_choose_direction);
        mChooseDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllScheduleByDirectionId(mSelectedDirection.getId());

                linearLayoutSchedule.setVisibility(View.VISIBLE);
                mChooseDirection.setVisibility(View.GONE);
            }
        });

        mSpinnerSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedSchedule = scheduleItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseSchedule = findViewById(R.id.btn_choose_schedule);
        mChooseSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllCarriages();

                linearLayoutCarriage.setVisibility(View.VISIBLE);
                mChooseSchedule.setVisibility(View.GONE);
            }
        });

        mSpinnerCarriage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCarriage = carriageItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChooseCarriage = findViewById(R.id.btn_choose_carriage);
        mChooseCarriage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstStageActivity.this, SecondStageActivity.class);
                intent.putExtra("trainId", mSelectedSchedule.getTrainId());
                intent.putExtra("directionId", mSelectedSchedule.getDirectionId());
                intent.putExtra("carriageId", mSelectedCarriage.getId());
                startActivity(intent);
            }
        });
    }

    private void getAllCarriages() {
        Call<List<Carriage>> carriage = TicketsController
                .getInstance()
                .getCarriageApi()
                .getAll();

        carriage.enqueue(new Callback<List<Carriage>>() {
            @Override
            public void onResponse(@NonNull Call<List<Carriage>> call, @NonNull Response<List<Carriage>> response) {
                carriageItems = response.body();
                populateCarriageSpinner();
            }

            @Override
            public void onFailure(@NonNull Call<List<Carriage>> call, @NonNull Throwable t) {
                Toast.makeText(FirstStageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllScheduleByDirectionId(Long id){
        scheduleItems = new ArrayList<>();

        Call<List<Schedule>> schedule = TicketsController
                .getInstance()
                .getScheduleApi()
                .getByDirectionId(id);

        schedule.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Schedule>> call, @NonNull Response<List<Schedule>> response) {
                scheduleItems = response.body();
                populateScheduleSpinner();
            }

            @Override
            public void onFailure(@NonNull Call<List<Schedule>> call, @NonNull Throwable t) {
                Toast.makeText(FirstStageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllDirections(){
        Call<List<Direction>> directions = TicketsController
                .getInstance()
                .getDirectionApi()
                .getAll();

        directions.enqueue(new Callback<List<Direction>>() {
            @Override
            public void onResponse(@NonNull Call<List<Direction>> call, @NonNull Response<List<Direction>> response) {
                directionItems = response.body();
                populateDirectionSpinner();
            }

            @Override
            public void onFailure(@NonNull Call<List<Direction>> call, @NonNull Throwable t) {
                Toast.makeText(FirstStageActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateDirectionSpinner(){
        String[] items = new String[directionItems.size()];

        for(int i=0; i<directionItems.size(); i++){
            items[i] = directionItems.get(i).getFrom() +"-"+ directionItems.get(i).getTo();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(FirstStageActivity.this, android.R.layout.simple_list_item_1, items);
        mSpinnerDirection.setAdapter(adapter);
    }

    private void populateScheduleSpinner(){
        String[] items = new String[scheduleItems.size()];

        for(int i=0; i<scheduleItems.size(); i++){
            items[i] = scheduleItems.get(i).getDepartureTime() +"-"+ scheduleItems.get(i).getArrivalTime();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(FirstStageActivity.this, android.R.layout.simple_list_item_1, items);
        mSpinnerSchedule.setAdapter(adapter);
    }

    private void populateCarriageSpinner(){
        String[] items = new String[carriageItems.size()];

        for(int i=0; i<carriageItems.size(); i++){
            items[i] = carriageItems.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(FirstStageActivity.this, android.R.layout.simple_list_item_1, items);
        mSpinnerCarriage.setAdapter(adapter);
    }
}
