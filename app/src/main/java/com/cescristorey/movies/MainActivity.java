package com.cescristorey.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.cescristorey.movies.R;
import com.cescristorey.movies.models.MovieFeed;
import com.cescristorey.movies.retrofit.MovieService;
import com.cescristorey.movies.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    /*
    Atributos
     */
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Enlaza el RecyclerView definido en el layout con el objeto recyclerView */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        /* Establece que recyclerView tendrá un layout lineal, en concreto vertical*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        /*  Indica que cada uno de los items va a tener un tamaño fijo*/
        recyclerView.setHasFixedSize(true);

        /* Establece la  decoración por defecto de cada uno de lo items: una línea de división*/
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        /* Instancia un objeto de la clase MovieAdapter */
        movieAdapter = new MovieAdapter(getApplicationContext());

        /* Establece el adaptador asociado a recyclerView */
        recyclerView.setAdapter(movieAdapter);

        /* Pone la animación por defecto de recyclerView */
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadSearch();
    }


    public void loadSearch () {
        /* Crea la instanncia de retrofit */
        MovieService getMovie = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
        /* Se definen los parámetros de la llamada a la función getTopRated */
        Call<MovieFeed> call = getMovie.getTopRated(RetrofitInstance.getApiKey(), "es");
        /* Se hace una llamada asíncrona a la API */
        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    /* En caso de respuesta correcta */
                    case 200:
                        /* En el objeto data de la clase MovieFeed se almacena el JSON convertido a objetos*/
                        MovieFeed data = response.body();
                        /* Se actualizan los datos contenidos en el adaptador */
                        movieAdapter.swap(data.getResults());
                        /* Se notifica un cambio de datos para que se refresque la vista */
                        movieAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<MovieFeed> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error cargando películas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
