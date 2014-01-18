package com.rbonestell.bcwidget;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.rbonestell.bcwidget.utils.*;

public class BlockchainWidget extends AppWidgetProvider
{
		
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		int frequency = settings.getInt("frequency", 5);
		new Timer().scheduleAtFixedRate(new BlockchainTimer(context, appWidgetManager), 1, frequency * 60000);
	}
	
	// Widget update task
	private class BlockchainTimer extends TimerTask
	{
		
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		int lastBlockTime = 0;
		
		public BlockchainTimer(Context context, AppWidgetManager appWidgetManager)
		{
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
			Intent intent = new Intent(context, SelectFrequencyActivity.class);
		    PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
		    remoteViews.setOnClickPendingIntent(R.id.rlWidgetLayout, pendingintent);
			thisWidget = new ComponentName(context, BlockchainWidget.class);
		}
		
		@Override
		public void run()
		{
			try
			{
				new BlockchainTask().execute();
			}
			catch (Exception e)
			{
				Log.w("BlockchainWidget", e.getMessage());
			}
		}
		
		public void updateValues(BlockchainResponse bResp)
		{
			if (bResp != null)
			{
				lastBlockTime = bResp.time;
				remoteViews.setTextViewText(R.id.tvBlockchainHeight, bResp.height + " blks");
				remoteViews.setTextViewText(R.id.tvOutstanding, "\u0E3F" + (210000*50 + (bResp.height-210000)*25));
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);
				new Timer().scheduleAtFixedRate(new LastBlockTimer(), 1, 60000);
			}
			else 
			{
				remoteViews.setTextViewText(R.id.tvBlockchainHeight, "Error");
				remoteViews.setTextViewText(R.id.tvOutstanding, "Error");
				remoteViews.setTextViewText(R.id.tvMinutes, "Error");
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			}
		}
		
		// Blockchain data loading task
		private class BlockchainTask extends AsyncTask<String, Integer, BlockchainResponse>
		{
			
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();	
			}

			@Override
			protected BlockchainResponse doInBackground(String... params)
			{
				try
				{
					return BlockchainRequest.getBlockchainInfo();
				}
				catch (Exception e)
				{
					Log.w("BlockchainWidget", e.getMessage());
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values)
			{
				super.onProgressUpdate(values);
			}

			@Override
			protected void onPostExecute(BlockchainResponse result)
			{
				super.onPostExecute(result);
				updateValues(result);
			}

		}
		
		// Last block timer task
		private class LastBlockTimer extends TimerTask
		{
			@Override
			public void run()
			{
				int unixNow = (int)(System.currentTimeMillis() / 1000L);
				int diffSeconds = unixNow - lastBlockTime;
				int diffMin = diffSeconds / 60;
				remoteViews.setTextViewText(R.id.tvMinutes, diffMin + ((diffMin > 1) ? " mins" : "min"));
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			}
		}
		
	}
	
	
	
	
}
