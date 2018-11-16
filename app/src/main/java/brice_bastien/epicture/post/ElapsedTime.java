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
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		if (elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0) {
			return res.getString(R.string.seconds_elapsed, elapsedSeconds).toUpperCase();
		} else if (elapsedHours == 0 && elapsedDays == 0) {
			return res.getString(R.string.minutes_elapsed, elapsedMinutes).toUpperCase();
		} else if (elapsedDays == 0) {
			return res.getString(R.string.hours_elapsed, elapsedHours).toUpperCase();
		}
		return res.getString(R.string.days_elapsed, elapsedDays).toUpperCase();

	}

}
