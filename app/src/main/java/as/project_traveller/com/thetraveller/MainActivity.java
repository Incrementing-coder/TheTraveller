package as.project_traveller.com.thetraveller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import as.project_traveller.com.thetraveller.POJO.Model;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends Activity {

    TextView textView1, textView2, textView3, textView4;
    String url = "http://api.openweathermap.org";


    ImageButton map,attract, hotels;
    public  GPlusfrag status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        textView1=(TextView)findViewById(R.id.textView2);
        textView2=(TextView)findViewById(R.id.textView3);
        textView3=(TextView)findViewById(R.id.textView4);

        getReport();

        status  = new GPlusfrag();
        if(status.getStatus() == 1)
        {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"Logged In",Toast.LENGTH_LONG).show();
        }

        map = (ImageButton) findViewById(R.id.button);
        attract = (ImageButton) findViewById(R.id.button2);
        hotels = (ImageButton) findViewById(R.id.button3);
       /* Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "BeyondWonderland.ttf");
        attract.setTypeface(font);*/

        final Context context = this;

        map.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);
            }
        });

        attract.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, Places.class);
                startActivity(intent1);
            }
        });



    }

    public void getReport(){

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestInterface service = retrofit.create(RestInterface.class);

        retrofit.Call<Model> call = service.getWeatherReport();

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Response<Model> response, Retrofit retrofit) {
                try{
                    String city = response.body().getCity().getName();
                    String msg = response.body().getTemp().toString();
                    String cnt = response.body().getCnt();

                    textView1.setText("City :  "+city);
                    textView2.setText("Status : "+msg);
                    textView3.setText("Humidity : "+cnt);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Try Catch",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
