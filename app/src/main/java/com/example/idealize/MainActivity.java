package com.example.idealize;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.idealize.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class MainActivity extends AppCompatActivity {
    private Button matrix;
    private ActivityMainBinding binding;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // on below line displaying a log that admob ads has been initialized.
                Log.i("Admob", "Admob Initialized.");
            }
        });







        binding.btnMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Matrix.class));
            }
        });
        binding.btnLimits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Limits.class));
            }
        });
        binding.btnGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Graphs.class));
            }
        });
        binding.btnVectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Vectors.class));
            }
        });
        binding.btnDifferentiation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Derivative.class));
            }
        });
        binding.btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,General.class));
            }
        });
        binding.btnSimultaneousEquations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,SimultaneousEquations.class));
            }
        });

        binding.btnSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }*/
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(MainActivity.this, "ca-app-pub-1262150100678081/8272583550",
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error.
                                Log.d(TAG, loadAdError.toString());
                                rewardedAd = null;
                                startActivity(new Intent(MainActivity.this,Series.class));
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd ad) {
                                rewardedAd = ad;
                                Log.d(TAG, "Ad was loaded.");
                                if (rewardedAd != null) {
                                    Activity activityContext = MainActivity.this;
                                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            // Handle the reward.
                                            Log.d(TAG, "The user earned the reward.");
                                            int rewardAmount = rewardItem.getAmount();
                                            String rewardType = rewardItem.getType();
                                            startActivity(new Intent(MainActivity.this,Series.class));
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                                    startActivity(new Intent(MainActivity.this,Series.class));
                                }
                            }
                        });

            }
        });

        binding.btnLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Lines_Circles.class));
            }
        });
        binding.btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest2 = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-1262150100678081/7421206358", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                startActivity(new Intent(MainActivity.this,Statictics.class));
            }
        });

    }
}