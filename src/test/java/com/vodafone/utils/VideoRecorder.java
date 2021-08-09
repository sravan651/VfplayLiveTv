package com.vodafone.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import io.appium.java_client.screenrecording.CanRecordScreen;

public class VideoRecorder {
	public void startRecording() {
		((CanRecordScreen) new DriverUtil().getMDriver()).startRecordingScreen();
	}

	public void stopRecording() {
		// SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_YY_HH_mm_ss");
		// String date = sdf.format(new Date());
		GlobalParams params = new GlobalParams();
		String media = ((CanRecordScreen) new DriverUtil().getMDriver()).stopRecordingScreen();
		File videoDir = new File("AppiumLogs/Videos/" + params.getPlatformName() + "_" + params.getDeviceName());
		synchronized (videoDir) {
			if (!videoDir.exists()) {
				videoDir.mkdirs();
			}
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(videoDir + File.separator + "VodafoneEvidence.mp4");
			stream.write(Base64.decodeBase64(media));
			stream.close();
			System.out.println("video path: " + videoDir + File.separator + "VodafoneEvidence.mp4");
		} catch (Exception e) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
