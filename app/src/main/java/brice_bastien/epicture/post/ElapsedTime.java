package brice_bastien.epicture.post;

import android.content.res.Resources;


import java.sql.Date;
import java.sql.Timestamp;

import brice_bastien.epicture.R;

public class ElapsedTime {

	private long timeOfPost;

	public ElapsedTime(long timeOfPost) {
		this.timeOfPost = timeOfPost;
	}

	public String getTimeString(Resources res) {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		Date endDate = new Date(stamp.getTime());
		Date startDate = new Date(Long.parseLong(Long.toString(timeOfPost)) * 1000);
		long different = endDate.getTime() - startDate.getTime();
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long yearInMilli = daysInMilli * 365;
		long elapsedYears = different / yearInMilli;
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		if (elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0 && elapsedYears == 0) {
			return res.getQuantityString(R.plurals.seconds_elapsed, (int)elapsedSeconds, elapsedSeconds).toUpperCase();
		} else if (elapsedHours == 0 && elapsedDays == 0 && elapsedYears == 0) {
			return res.getQuantityString(R.plurals.minutes_elapsed, (int)elapsedMinutes, elapsedMinutes).toUpperCase();
		} else if (elapsedDays == 0 && elapsedYears == 0) {
			return res.getQuantityString(R.plurals.hours_elapsed, (int)elapsedHours, elapsedHours).toUpperCase();
		} else if (elapsedYears == 0)
			return res.getQuantityString(R.plurals.days_elapsed, (int)elapsedDays, elapsedDays).toUpperCase();
		return res.getQuantityString(R.plurals.year_elapsed, (int)elapsedYears, elapsedYears).toUpperCase();
	}

	public String getLittleElapsed(Resources res) {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		Date endDate = new Date(stamp.getTime());
		Date startDate = new Date(Long.parseLong(Long.toString(timeOfPost)) * 1000);
		long different = endDate.getTime() - startDate.getTime();
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long yearInMilli = daysInMilli * 365;
		long elapsedYears = different / yearInMilli;
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		if (elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0 && elapsedYears == 0) {
			return res.getQuantityString(R.plurals.small_seconds_elapsed, (int)elapsedSeconds, elapsedSeconds);
		} else if (elapsedHours == 0 && elapsedDays == 0 && elapsedYears == 0) {
			return res.getQuantityString(R.plurals.small_minutes_elapsed, (int)elapsedMinutes, elapsedMinutes);
		} else if (elapsedDays == 0 && elapsedYears == 0) {
			return res.getQuantityString(R.plurals.small_hours_elapsed, (int)elapsedHours, elapsedHours);
		} else if (elapsedYears == 0)
			return res.getQuantityString(R.plurals.small_days_elapsed, (int)elapsedDays, elapsedDays);
		return res.getQuantityString(R.plurals.small_year_elapsed, (int)elapsedYears, elapsedYears);
	}

}
