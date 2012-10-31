package com.sdk.example.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import com.sdk.example.SdkMainActivity;
import com.sdk.example.adapter.ArrayAdapterImageText;
import com.sdk.example.adapter.ArrayAdapterSimple;

public class AppExamples extends SdkMainActivity {
	protected List<Map<String, Object>> getData() {
		List<Map<String, Object>> nameList = new ArrayList<Map<String, Object>>();

		nameList.add(addItem(AlertDlgExamples.class, "Alert Dialogs"));
		nameList.add(addItem(ActivityExamples.class, "Activity Examples"));

		return nameList;
	}
}
