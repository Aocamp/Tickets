package com.andrey.tickets.api;

import com.andrey.tickets.model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScheduleApi {
    @GET("schedule/direction/{id}")
    Call<List<Schedule>> getByDirectionId(@Path("id") Long id);
}
