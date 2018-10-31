package com.neotys.apposite;

import static org.junit.Assert.assertEquals;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;

public class WPT_LaunchTestActionTest {
	@Test
	public void shouldReturnType() {
		final SetDelayAction action = new SetDelayAction();
		assertEquals("Netropy_SetDelay", action.getType());
	}

	@Test
	public void TestResultParser() throws IOException {

	}
}

