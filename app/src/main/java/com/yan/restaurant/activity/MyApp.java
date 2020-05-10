package com.yan.restaurant.activity;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

public class MyApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
		Fresco.initialize(this, frescoConfig);
	}
}
