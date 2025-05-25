package com.trollflamehorn;

import net.runelite.client.config.*;

@ConfigGroup("trollflamegeneral")
public interface TrollflameHornConfig extends Config
{
	@ConfigSection(
			name = "General",
			description = "General settings",
			position = 1
	)
	String GENERAL_SECTION = "generalSection";

	@ConfigItem(
			keyName = "playNoEffect",
			name = "Play on unaffected",
			description = "Play the sound if nobody is affected?",
			section = GENERAL_SECTION,
			position = 1
	)
	default boolean playNoEffect()
	{
		return true;
	}

	@ConfigItem(
			keyName = "playOnOthers",
			name = "Play on others",
			description = "Play the custom sound on other players trigger?",
			section = GENERAL_SECTION,
			position = 2
	)
	default boolean playOnOthers()
	{
		return false;
	}

	@ConfigItem(
		keyName = "soundToUse",
		name = "Sound to use",
		description = "Choose which sound you wish to use. Random will randomly play from the list. " +
				"Custom will play the sound from the Custom sound field, providing the sound file exists.",
		section = GENERAL_SECTION,
		position = 2
	)
	default Sounds sound()
	{
		return Sounds.Random;
	}

	@Range(
			min = 1,
			max = 10
	)
	@ConfigItem(
			keyName = "volumeLevel",
			name = "Volume",
			description = "Adjust volume of sound played.",
			section = GENERAL_SECTION,
			position = 3
	)
	default int volumeLevel() {
		return 10;
	}

	@ConfigSection(
			name = "Custom",
			description = "Custom settings",
			position = 2
	)
	String CUSTOM_SECTION = "customSection";

	@ConfigItem(
			keyName = "customSound",
			name = "Custom sound",
			description = "Add the name of the file for the custom sound you want to use. " +
					"The sound file must exist in the custom directory (Check GitHub/Plugin hub instructions) " +
					"and the 'Custom' value must be selected in the Sound to use config. This can override existing sounds.",
			section = CUSTOM_SECTION,
			position = 1
	)
	default String customSound() {
		return "";
	}
}
