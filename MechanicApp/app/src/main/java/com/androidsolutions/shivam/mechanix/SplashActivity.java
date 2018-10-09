package com.androidsolutions.shivam.mechanix;


import android.app.Activity;
import android.util.Log;

import com.androidsolutions.shivam.mechanix.adapter.FontsAdapter;
import com.androidsolutions.shivam.mechanix.helpers.ChangeActivityHelper;
import com.androidsolutions.shivam.mechanix.util.Constants;
import com.badoo.mobile.util.WeakHandler;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash
{
	int st;
	 private static int SPLASH_TIME_OUT = 4000;
	 SessionManager manager;
	 FontsAdapter mFontAdapter = new FontsAdapter(this);

	@Override
	public void initSplash(ConfigSplash configSplash)
	{
	/*	configSplash.setPathSplashStrokeSize(20);
	*/	configSplash.setTitleSplash("AutoBoys"+"\n"+"-Powered By AndroidSolutions"+"\n"+"9044224967");
		configSplash.setTitleTextSize(20);
		configSplash.setBackgroundColor(getResources().getColor(R.color.colorGreen));
		configSplash.setTitleTextColor(getResources().getColor(R.color.Orange));
		configSplash.setPathSplashStrokeColor(getResources().getColor(R.color.Orange));
	/*	configSplash.setTitleFont((String) mFontAdapter.getItem(4));
		configSplash.setAnimCircularRevealDuration(3000);
		configSplash.setAnimTitleDuration(3000);
		configSplash.setAnimPathStrokeDrawingDuration(3000);
		configSplash.setAnimPathFillingDuration(3000);
		configSplash.setLogoSplash(R.drawable.ic_splash);
		configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);
	*/	getAndSetSplashValues(configSplash);
	}

	@Override
	public void animationsFinished() {
		//wait 5 sec and then go back to MainActivity
		final Activity a = this;
		manager=new SessionManager();
		WeakHandler handler = new WeakHandler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				String status=manager.getPreferences(SplashActivity.this,"name");
				String uid=manager.getPreferences(SplashActivity.this,"uid");
				String stat=manager.getPreferences(SplashActivity.this,"status");
				if(!stat.matches("")&&stat!="")
					st=Integer.parseInt(stat.trim());
				Log.d("Email",status);
				if (status.matches("out")&&uid.matches("out")||status=="out"&&uid=="out"||status.matches("")&&uid.matches("")||status==""&&uid=="")
				{
					ChangeActivityHelper.changeActivity(a, LoginActivity.class, true);
				}
				else if(st==0)
				{
					ChangeActivityHelper.changeActivity(a, Verification.class, true);
				}
				else
				{
					ChangeActivityHelper.changeActivity(a, MainActivity.class, true);
				}
				//ChangeActivityHelper.changeActivity(a, MainActivity.class, true);
			}
		}, Constants.SPLASH_DELAY);
	}
	public void getAndSetSplashValues(ConfigSplash cs1) {
		ConfigSplash cs2 = (ConfigSplash) getIntent().getExtras().getSerializable(Constants.CONFIG);
		if (cs2 != null) {
			cs1.setAnimCircularRevealDuration(cs2.getAnimCircularRevealDuration());
			cs1.setRevealFlagX(cs2.getRevealFlagX());
			cs1.setRevealFlagY(cs2.getRevealFlagY());
			cs1.setBackgroundColor(cs2.getBackgroundColor());

			cs1.setLogoSplash(cs2.getLogoSplash());
			cs1.setAnimLogoSplashTechnique(cs2.getAnimLogoSplashTechnique());
			cs1.setAnimLogoSplashDuration(cs2.getAnimLogoSplashDuration());

			cs1.setPathSplash(cs2.getPathSplash());
			cs1.setPathSplashStrokeSize(cs2.getPathSplashStrokeSize());
			cs1.setPathSplashStrokeColor(cs2.getPathSplashStrokeColor());
			cs1.setPathSplashFillColor(cs2.getPathSplashFillColor());
			cs1.setOriginalHeight(cs2.getOriginalHeight());
			cs1.setOriginalWidth(cs2.getOriginalWidth());
			cs1.setAnimPathStrokeDrawingDuration(cs2.getAnimPathStrokeDrawingDuration());
			cs1.setAnimPathFillingDuration(cs2.getAnimPathFillingDuration());

			cs1.setTitleSplash(cs2.getTitleSplash());
			cs1.setAnimTitleDuration(cs2.getAnimTitleDuration());
			cs1.setAnimTitleTechnique(cs2.getAnimTitleTechnique());
			cs1.setTitleTextSize(cs2.getTitleTextSize());
			cs1.setTitleTextColor(cs2.getTitleTextColor());
			cs1.setTitleFont(cs2.getTitleFont());
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ChangeActivityHelper.changeActivity(this, MainActivity.class, true);
	}
}
