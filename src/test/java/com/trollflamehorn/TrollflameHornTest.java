package com.trollflamehorn;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TrollflameHornTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TrollflameHornPlugin.class);
		RuneLite.main(args);
	}
}