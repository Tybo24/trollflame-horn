# Trollflame Horn
A fun plugin used to override the sound the Soulflame Horn makes when the special attack is used.

# Additional details
## Config
| Config name | Description |
| ------------- | ------------- |
| Play on unaffected  | Should sound effect be overridden with the plugin sound when the horn effect is not applied to anybody (I.E. nobody is around or everyone was recently buffed already by it). |
| Play on others  | Should the sound effect be overridden if someone in your vicinity uses the horn instead. **WARNING** - I have been unable to test this with huge groups of people and it is therefore defaulted to off. |
| Sound to use | A custom list of sounds has been used from various creators live streams. I obviously could not list every creator that I'd like to, so I choose some of my favourite and allowed a custom sound to be used if you wish (below). Ranom selection will play from the random select and will include your custom sound. |
| Volume | 1-10, lower is quieter. |
| Custom sound | The name of the custom .wav file you'd like to use. Must be used in conjuction with either 'Random' or 'Custom' Sound to use configuration. You can only have one custom sound active at a time. Instructions below. |

## Custom sound
To use the custom functionality, you must first ensure you have a **.wav** file to use. 

**NOTE: just changing the file extension from (e.g) _.mp4_ to _.wav_ does not work. You need to use a converter if the file you wish to use is not already .wav**

### Guide
First of all, put your custom file in the custom directory. This directory is automatically created within the RuneLite root directory. If you're unsure how to get here, the easiest way I find is by right clicking the screenshot button and clicking open screenshot folder (if you have not changed this location yourself). Then go back a folder using the navigation bar
![image](https://github.com/user-attachments/assets/801e68f6-3f7d-4f4f-a383-79e1c033dbdd)
![image](https://github.com/user-attachments/assets/6bd4d1a2-50a7-47b0-b863-c1291bf74025)

Once you are in your RuneLite root folder, you should see a folder called **trollflame-custom**
![image](https://github.com/user-attachments/assets/16e88cc8-2203-4b87-87af-660c23237fa1)

Enter this folder and drop your sound file in there **ensuring it is .wav**.
![image](https://github.com/user-attachments/assets/8f180e54-b041-48cd-9bcc-aa351c91b2cb)

Go back to the plugin settings in Runelite and set them as follows, depending on your requirement.
![image](https://github.com/user-attachments/assets/1d15b530-e988-498b-9e33-1c5af6695aaf)

## Things to note
- You can only have one custom sound active at a time. For example, comma separated names will not work and nothing will play.
- You can have as many files in the custom directory as you wish and when you update the Custom sound configuration box, it will just switch over to the new sound.
- The custom sound will override the static list of sounds if Random is selected (E.G. if you name your custom sound Skillspecs, it will override the default Skillspecs sound when Random is chosen).
