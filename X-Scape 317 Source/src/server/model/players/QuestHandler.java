package server.model.players;

import server.*;
import server.model.players.*;
import server.model.items.*;
import server.util.*;
import server.world.*;
import server.model.npcs.*;

import java.io.*;

public class QuestHandler {

	public Client client;
	
	public String q1Name = "Recipe for Disaster";

	public int q1, tqp = 0;

	public QuestHandler(Client c) {
		client = c;
	}
	
	public void questcomplete(Client c, String questN, String reward1, String reward2, String reward3, String reward4, int eqp) {
        tqp += eqp;
		c.getPA().showInterface(12140);
		c.getPA().sendFrame126(questN, 12144);
		c.getPA().sendFrame126(reward1, 12150);
		c.getPA().sendFrame126(reward2, 12151);
		c.getPA().sendFrame126(reward3, 12152);
		c.getPA().sendFrame126(reward4, 12153);
		c.getPA().sendFrame126("Earned QP: " +eqp, 12154);
		c.getPA().sendFrame126("Total QP: " +tqp, 12155);
		c.getPA().sendFrame126("Journal QP: " +tqp, 640);
		c.getPA().sendFrame126("QP: "+tqp, 3985);
	}

	public void questcolorloginchanger(Client c) {
			
	}
	
	public void questsbuttons(Client c, int actionButtonId) {
		switch(actionButtonId) {
			case 113237://q1
				q1(c);	
			break;
		}
	}

	public void q1(Client c) {
		if(q1 == 0){
			loadMenus("q1ps", c);
		}
		if(q1 == 1){
			loadMenus("q1p1", c);
		}
		if(q1 == 2){
			loadMenus("q1p2", c);
		}
		if(q1 == 3){
			loadMenus("q1p3", c);
		}
		if(q1 == 4){
			loadMenus("q1p4", c);
		}
		if(q1 == 5){
			loadMenus("q1p5", c);
		}
		if(q1 == 6){
			loadMenus("q1p6", c);
		}
		if(q1 == 7){
			loadMenus("q1p7", c);
		}
		if(q1 == 8){
			loadMenus("q1p8", c);
		}
		if(q1 == 15){
			loadMenus("q1pf", c);
		}
			//c.getPA().sendFrame79(8143);
			c.getPA().showInterface(8134);
			c.flushOutStream();
	}
	
	public void loadMenus(String FileName, Client c){
		c.clearQuestInterface();
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
				characterfile = new BufferedReader(new FileReader("Data/Quests/"+FileName+".txt"));

			} catch(FileNotFoundException fileex) {

			}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			Misc.println("Menu: error loading file.");
				
		}	
		int line2 = 8144;
		while(EndOfFile == false && line != null) {
			if (!line.equals("[END]")) {
				if(line2 == 8144){
					c.clearQuestInterface();
			}
			if(line2 == 8146) {
				line2 = line2+1;
			}
			c.getPA().sendFrame126(line,line2);
			line2 = line2+1;
		} else if(line.equals("[END]")) {
			try {
				characterfile.close(); 
			} catch(IOException ioexception) { 
			}					
		}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try { 
			characterfile.close();
		} catch(IOException ioexception) {
		}
			//c.getPA().sendFrame79(8143);
			c.getPA().showInterface(8134);
			c.flushOutStream();
	}
	
	
}