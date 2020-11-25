package com.cescristorey.movies.retrofit;



import com.cescristorey.movies.models.MovieFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/top_rated")
    Call<MovieFeed> getTopRated(@Query("api_key") String apiKey, @Query("language") String language);

}