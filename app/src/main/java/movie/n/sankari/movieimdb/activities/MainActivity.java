package movie.n.sankari.movieimdb.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import movie.n.sankari.movieimdb.R;
import movie.n.sankari.movieimdb.fragments.MovieListingFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private boolean isLogin = false;
    private Button btnLogin;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private static final String TAG = "MainActivity";
    private FrameLayout frmaeMovieview;
    private DrawerLayout mDrawer;
    public static Toolbar toolbar;
    private NavigationView nvDrawer;
    public static FloatingActionButton btnFilter;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_main);
            context=this;
            btnLogin = (Button) findViewById(R.id.Login);
            frmaeMovieview = (FrameLayout) findViewById(R.id.frameContent);
            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            nvDrawer = (NavigationView) findViewById(R.id.nvView);
            btnFilter = (FloatingActionButton) findViewById(R.id.floatingFilter);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.menu);
            toolbar.setTitle("MovieIMDB");
            nvDrawer.setCheckedItem(R.id.nav_first_fragment);
            setupDrawerContent(nvDrawer);

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            if (btnLogin.getVisibility() == View.VISIBLE) {
                btnFilter.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }
            btnLogin.setOnClickListener(this);
            btnFilter.setOnClickListener(this);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                });


        }catch (Exception e){

        }
    }


    @Override
    public void onBackPressed() {
        try{
                if( btnFilter.getVisibility()==View.GONE){
                    btnFilter.setVisibility(View.VISIBLE);
                }
                super.onBackPressed();
        }catch (Exception e){

        }
    }

    public void changeFragment(Fragment targetFragment) {

        Runtime.getRuntime().gc();
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, targetFragment,
                            targetFragment.getClass().getName())
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
            try{
                view.startAnimation(buttonClick);
                    if(view.getId()==R.id.Login){

                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }else if(view.getId()==R.id.floatingFilter){
                        Toast.makeText(MainActivity.this,"This FEATURE is not implemented ",Toast.LENGTH_LONG).show();
                    }
            }catch (Exception e){

            }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        }catch (Exception e){

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try{
                Log.d(TAG, "handleSignInResult:" + result.isSuccess());
                if (result.isSuccess()) {


                    Fragment fragment = new MovieListingFragment();
                    changeFragment(fragment);
                    btnLogin.setVisibility(View.GONE);
                    frmaeMovieview.setVisibility(View.VISIBLE);
                    btnFilter.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);

                }
        }catch (Exception e){

        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    @Override
    protected void onStart() {
        try{
                mGoogleApiClient.connect();
                super.onStart();
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    Log.d(TAG, "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {

                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStop() {
        stopAutoManage();
        super.onStop();
    }
    private void stopAutoManage() {
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.stopAutoManage(this);
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                mDrawer.closeDrawers();
                Fragment fragment = new MovieListingFragment();
                changeFragment(fragment);
                break;
            case R.id.nav_second_fragment:
                //google+ logout
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                }

                this.finish();
                startActivity(getIntent());

                break;


        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    //disallow the touch events

    public void disallow_touch_events(final boolean b, View layoutView) {
        try{
                layoutView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (b) {
                            //touchevents allow
                            return false;
                        } else {
                            //touchevents not allow
                            return true;
                        }

                    }
                });

        }catch (Exception e){

        }
    }

    public void floatingBtn(View view){
        disallow_touch_events(false, view);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

}


