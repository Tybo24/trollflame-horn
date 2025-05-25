package com.trollflamehorn;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.sound.sampled.*;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@PluginDescriptor(
	name = "Trollflame Horn",
	description = "Changes the sound played when the Soulflame Horn is used."
)
public class TrollflameHornPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TrollflameHornConfig config;

	private Clip clip;
	private static final String CUSTOM_CREATOR_CONFIG_KEY = "customSound";
	private static final File CUSTOM_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "trollflame-custom");
	private static final int SFH_SPECIAL_ANIMATION_ID_1 = 12159;
	private static final int SFH_SPECIAL_ANIMATION_ID_2 = 12158;
	private boolean soundPlayed = false;
	private Map<String, Runnable> soundMap = new HashMap<>();


	@Provides
	TrollflameHornConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrollflameHornConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		setupSoundMap();
		createCustomDir();
	}

	@Override
	protected void shutDown() throws Exception
	{
		if (clip != null && clip.isOpen())
		{
			clip.close();
		}

		soundMap.clear();
	}

	private void setupSoundMap() {

		for (Sounds creator : Sounds.values())
		{
			if (creator == Sounds.Random || creator == Sounds.Custom)
				continue; // Skip random and custom

			String name = creator.name();
			soundMap.put(name, () -> playSound(name));
		}

		// Custom sound mapping
		String customSound = config.customSound();
		if (!customSound.isEmpty())
		{
			soundMap.put(Sounds.Custom.name(), () -> playSound((customSound)));
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void createCustomDir()
	{
		if (!CUSTOM_DIR.exists())
		{
			CUSTOM_DIR.mkdirs();
		}
	}

	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		// Sound effect that's played when SFH activates on other players
		if (event.getSoundId() == 10236)
		{
			int anim = client.getLocalPlayer().getAnimation();
			if ((anim != SFH_SPECIAL_ANIMATION_ID_1 && anim != SFH_SPECIAL_ANIMATION_ID_2)
					&& !config.playOnOthers())
			{
				return;
			}

			event.consume();
			soundTrigger();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (CUSTOM_CREATOR_CONFIG_KEY.equals(event.getKey()))
		{
			String customSound = config.customSound();

			// Remove existing mapping for Custom enum key
			soundMap.remove(Sounds.Custom.name());

			// Add new mapping for custom sound (config change)
			if (!customSound.isEmpty())
			{
				soundMap.put(Sounds.Custom.name(), () -> playSound(customSound));
			}
		}
	}

	@Subscribe
	public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed event)
	{
		int sound = event.getSoundId();

		if (sound == 10220)
		{
			// Always consume the area sound if custom sound has been played
			if (soundPlayed)
			{
				event.consume();
			}

			// For triggering area effect when no-one is around (area effect only)
			if (!soundPlayed && config.playNoEffect())
			{
				int anim = client.getLocalPlayer().getAnimation();
				if ((anim != SFH_SPECIAL_ANIMATION_ID_1 && anim != SFH_SPECIAL_ANIMATION_ID_2)
						&& !config.playOnOthers())
				{
					return;
				}

				event.consume();
				soundTrigger();
			}

			soundPlayed = false;
		}
	}

	public void soundTrigger()
	{
		Sounds selectedSound = config.sound();
		Runnable soundAction;

		if (selectedSound == Sounds.Random)
		{
			List<Runnable> actions = new ArrayList<>(soundMap.values());
			soundAction = actions.get(ThreadLocalRandom.current().nextInt(actions.size()));
		}
		else if (selectedSound == Sounds.Custom)
		{
			soundAction = soundMap.get(config.customSound());
		}
		else
		{
			soundAction = soundMap.get(selectedSound.name());
		}

		if (soundAction != null)
		{
			soundAction.run();
			soundPlayed = true;
		}
	}

	public void playSound(String _sound)
	{
		try
		{
			if (clip != null)
			{
				clip.close();
			}

			AudioInputStream stream;
			InputStream is = null;

			// Custom file loading for Custom creator
			if (!config.customSound().isEmpty() &&
					(config.sound().equals(Sounds.Custom) || config.sound().equals(Sounds.Random)))
			{
				File externalFile = new File(CUSTOM_DIR + File.separator, _sound + ".wav");
				if (externalFile.exists())
				{
					is = new FileInputStream(externalFile);
				}
			}

			if (is == null)
			{
				// Fallback to resource inside .jar
				String filename = String.format("/%s.wav", _sound);
				is = getClass().getResourceAsStream(filename);
			}

			if (is == null)
			{
				log.debug(String.format("Resource not found for: %s", _sound));
				return;
			}

			BufferedInputStream bis = new BufferedInputStream(is);
			stream = AudioSystem.getAudioInputStream(bis);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);

			clip.open(stream);

			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float volumeValue = volume.getMinimum() + ((50 + (config.volumeLevel()*5)) * ((volume.getMaximum() - volume.getMinimum()) / 100));

			volume.setValue(volumeValue);

			clip.start();
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			log.error(e.getMessage());
		}
	}


}
