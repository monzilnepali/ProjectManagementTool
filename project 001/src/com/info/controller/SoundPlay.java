package com.info.controller;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlay extends Thread {

	@Override
	public void run() {
		String musicFile = "E:\\project\\ProjectManagementTool\\project 001\\src\\Resource\\sound\\Elektronomia - Sky High.mp3";     // For example
//
//		Media sound = new Media(new File(musicFile).toURI().toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.setAutoPlay(true);  
//        
        
        AudioClip plonkSound = new AudioClip(musicFile);
        plonkSound.play();
		
	}

}
