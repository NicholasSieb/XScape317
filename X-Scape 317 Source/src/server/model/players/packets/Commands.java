package server.model.players.packets;

import server.Config;
import server.Connection;
import server.Server;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.util.Misc;
import server.world.WorldMap;


/**
 * Commands
 **/
public class Commands implements PacketType {

	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
	String playerCommand = c.getInStream().readString();
	if(Config.SERVER_DEBUG)
		Misc.println(c.playerName+" playerCommand: "+playerCommand);
		if (playerCommand.startsWith("/") && playerCommand.length() > 1) {
			if (c.clanId >= 0) {
				System.out.println(playerCommand);
				playerCommand = playerCommand.substring(1);
				Server.clanChat.playerMessageToClan(c.playerId, playerCommand, c.clanId);
			} else {
				if (c.clanId != -1)
					c.clanId = -1;
				c.sendMessage("You are not in a clan.");
			}
			return;
		}
		if(c.playerRights >= 0) {
			
			if (playerCommand.equalsIgnoreCase("players")) {
				c.sendMessage("There are currently "+PlayerHandler.getPlayerCount()+ " players online.");
			}
			if (playerCommand.equalsIgnoreCase("testcluescroll")) {
				c.clueScroll(995, 10, 11694, 1, 11283, 1, 11726, 1, 0);
			}

			if (playerCommand.startsWith("switch")) {
				if (c.inWild())
					return;
				try {
				String[] args = playerCommand.split(" ");
				int spellbook = Integer.parseInt(args[1]);
				if (spellbook == 0) { 
					c.setSidebarInterface(6, 1151);
					c.playerMagicBook = 0;
					c.autocastId = -1;
					c.getPA().resetAutocast();
				} else if (spellbook == 1) {
					c.setSidebarInterface(6, 12855);
					c.playerMagicBook = 1;
					c.autocastId = -1;
					c.getPA().resetAutocast();
				} else if (spellbook == 2) {
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.autocastId = -1;
					c.getPA().resetAutocast();
				}
				} catch (Exception e){}
			}
			
			if (playerCommand.startsWith("yell") && c.playerRights <= 0 && c.memberStatus >= 1) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.sendMessage("[Donator] " + c.playerName + ": <col=16777215>" + Misc.optimizeText(playerCommand.substring(5)));
					}
				}
			}
			if (playerCommand.startsWith("changepassword") && playerCommand.length() > 15) {
				c.playerPass = playerCommand.substring(15);
				c.sendMessage("Your password is now: " + c.playerPass);			
			}
			
			if (playerCommand.equalsIgnoreCase("resetdefence")) {
if(c.playerXP[1] > 100000) {
c.sendMessage("Sorry, You can't have anymore then 100k Defence exp to reset it");
return;}
c.playerXP[1]=0;
				c.getPA().refreshSkill(1);
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("You cannot be wearing anything while trying to reset your defence!");
						return;
					}
				}	
}		
			if (playerCommand.startsWith("crowntest")) {

				c.sendMessage("@cr0@ 0 @cr1@ 1 @cr2@ 2 @cr3@ 3 @cr4@ 4");
				}
			if (playerCommand.startsWith("rules")) {

				c.sendMessage("Welcome to X-Scape");
				c.sendMessage("1.Do not ask staff for any stuff (mute).");
				c.sendMessage("2.No rules in pking, (Gbs fights are your own risk)");
				c.sendMessage("3.Do not use offensive Language. (Mild flaming aloud)");
				c.sendMessage("4.Do not Scam Passwords or Items (IPBAN)");
				c.sendMessage("5.Auto Clickers are NOT Allowed, Auto Typers are");
				c.sendMessage("if you set the timer to 5 + Seconds");




			}
			if (playerCommand.startsWith("donyell") && c.playerRights >= 1) {

				String Message = "<col=16777215>Donate at www.xscapeserver.tk for nice rewards!";
				
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j]; 
						c2.sendMessage(Message);
					}
				}
			}
			
			if (playerCommand.equals("donate")) {
				c.sendMessage("Thank you for thinking about donating to us");
				c.sendMessage("You can donate by Paypal");
				c.sendMessage("Donating can get you great rewards like chaotics, rares or arcane stream");
				c.sendMessage("to donate go to www.xscapeserver.tk");
			}


			if (playerCommand.startsWith("teletome") && c.playerRights >= 2) {
				if (c.inWild())
				return;
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.teleportToX = c.absX;
								c2.teleportToY = c.absY;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported " + c2.playerName + " to you.");
								c2.sendMessage("You have been teleported to " + c.playerName + "");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
						if (playerCommand.startsWith("demote") && c.playerRights >= 3 && c.playerName.equalsIgnoreCase("x")  || c.playerName.equalsIgnoreCase("nameless") && playerCommand.charAt(7) == ' ') { // use as ::prm name
				try {	
					String playerToG = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToG)) {
								Server.playerHandler.players[i].playerRights = 0;
								c.sendMessage("You have given  "+Server.playerHandler.players[i].playerName+" A Promotion Cfrom: "+Server.playerHandler.players[i].connectedFrom);

								Server.playerHandler.players[i].disconnected = true;						
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("promote") && c.playerRights >= 3 && c.playerName.equalsIgnoreCase("x")  || c.playerName.equalsIgnoreCase("nameless") && playerCommand.charAt(8) == ' ') { // use as ::prm name
				try {	
					String playerToG = playerCommand.substring(8);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToG)) {
								Server.playerHandler.players[i].playerRights += 1;
								c.sendMessage("You have given  "+Server.playerHandler.players[i].playerName+" A Promotion Cfrom: "+Server.playerHandler.players[i].connectedFrom);							
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			

			if (playerCommand.startsWith("movehome") && c.playerRights >= 2) {
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.teleportToX = 3096;
								c2.teleportToY = 3468;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported " + c2.playerName + " to Home");
								c2.sendMessage("You have been teleported to home");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}

			if (playerCommand.startsWith("yell") && c.playerRights <= 0 && c.memberStatus <= 0) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.sendMessage("[Player] " + c.playerName + Misc.optimizeText(playerCommand.substring(5)));
					}
				}
			}		

			if (playerCommand.equalsIgnoreCase("10def")) {
if(c.playerXP[1] > 100000) {
c.sendMessage("Sorry, You can't have anymore then 100k Defence exp to reset it");
return;}
c.playerXP[1]=1159;
				c.getPA().refreshSkill(1);
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("You cannot be wearing anything while trying to reset your defence!");
						return;
					}
				}	
}		

			if (playerCommand.equalsIgnoreCase("20def")) {
if(c.playerXP[1] > 100000) {
c.sendMessage("Sorry, You can't have anymore then 100k Defence exp to reset it");
return;}
c.playerXP[1]=4476;
				c.getPA().refreshSkill(1);
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("You cannot be wearing anything while trying to reset your defence!");
						return;
					}
				}	
}			

			if (playerCommand.equalsIgnoreCase("45def")) {
if(c.playerXP[1] > 100000) {
c.sendMessage("Sorry, You can't have anymore then 100k Defence exp to reset it");
return;}
c.playerXP[1]=61518;
				c.getPA().refreshSkill(1);
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("You cannot be wearing anything while trying to reset your defence!");
						return;
					}
				}	
}	

			if (playerCommand.equalsIgnoreCase("25def")) {
if(c.playerXP[1] > 100000) {
c.sendMessage("Sorry, You can't have anymore then 100k Defence exp to reset it");
return;}
c.playerXP[1]=7848;
				c.getPA().refreshSkill(1);
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("You cannot be wearing anything while trying to reset your defence!");
						return;
					}
				}	
}	

			if (playerCommand.equalsIgnoreCase("60att")) {
if(c.playerXP[0] > 100000) {
c.sendMessage("Sorry, You can't have anymore then 100k Attack exp to reset it");
return;}
c.playerXP[0]=27347;
				c.getPA().refreshSkill(1);
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("You cannot be wearing anything while trying to reset your defence!");
						return;
					}
				}	
}	



			
			if (playerCommand.equalsIgnoreCase("lock")) {
    c.expLock = true;
    c.sendMessage("You will NOT gain XP!");
}
if (playerCommand.equalsIgnoreCase("unlock")) {
    c.expLock = false;
    c.sendMessage("You will now gain XP!");
}

if (playerCommand.equalsIgnoreCase("commands")) {
    c.sendMessage("::resetdef ::10def ::20def ::25def ::45def ::60att");
	    c.sendMessage("::taverly ::slayer (tower)  ::goblins");
		c.sendMessage("::dags (kings for spirt shield) ");
	 c.sendMessage("::task (reset task) ::slayer ::kdr ::fightpits ::train");
	 	 c.sendMessage("::hellhounds ::pestcontrol ::lessers ::lock ::unlock");
	 c.sendMessage("::yell ::switch ::players");
	  c.sendMessage("::agility ");
}

if (playerCommand.startsWith("kdr")) {
				double KDR = ((double)c.KC)/((double)c.DC);
				c.forcedChat("My Kill/Death ratio is "+c.KC+"/"+c.DC+"; "+KDR+".");
			}

			
						if (playerCommand.equals("dags")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2911, 4448, 0);			
			}
			
						if (playerCommand.equals("agility")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2474, 3438, 0);			
			}
				
						if (playerCommand.equals("slayer")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(3411, 3537, 0);			
			}
			
						if (playerCommand.equals("dag")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2515, 4636, 0);					
			}
						if (playerCommand.equals("fire")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2732, 9433, 0);			
			}

			if (playerCommand.startsWith("task")) {
				c.taskAmount = -1;
				c.slayerTask = 0;
			}
			if (playerCommand.equals("goblins")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2956, 3506, 0);			
			}
			
		        if (playerCommand.startsWith("suicide"))  {
            c.playerLevel[3] = -1;
			c.playerLevel[2] = -1;
            c.getPA().requestUpdates();;
			c.getPA().refreshSkill(3);
			c.getPA().refreshSkill(2);
        } 
		
			if (playerCommand.equals("fightpits")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2395, 5154, 0);			
			}
			if (playerCommand.equals("pestcontrol")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2658, 2649, 0);			
			}
			if (playerCommand.equals("train")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2708, 3719, 0);			
			}
			if (playerCommand.equals("taverly")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2932, 9848, 0);			
			}
			if (playerCommand.equals("hellhounds")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2867, 9844, 0);			
			}
			if (playerCommand.equals("lessers")) {
					if (c.inWild())
					return;
				c.getPA().movePlayer(2835, 9562, 0);			
			}

}
			if(c.playerRights >= 1) {
			
			if (playerCommand.startsWith("starter")) {
			
				c.getDH().sendDialogues(100, 945);			
			}
			
			if (playerCommand.startsWith("yell") && c.playerRights == 1) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.sendMessage("[Mod] " + c.playerName + ": <col=255>" + Misc.optimizeText(playerCommand.substring(5)));
					}
				}
			}

			if (playerCommand.startsWith("reloadshops")) {
				Server.shopHandler = new server.world.ShopHandler();
			}
			
			if (playerCommand.startsWith("fakels")) {
				int item = Integer.parseInt(playerCommand.split(" ")[1]);
				Server.clanChat.handleLootShare(c, item, 1);
			}
			
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("gfx")) {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("update") && (c.playerName.equalsIgnoreCase("x") || c.playerName.equalsIgnoreCase("nameless"))) {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				PlayerHandler.updateSeconds = a;
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
			}

			if (playerCommand.startsWith("obj")) {
				c.getPA().checkObjectSpawn(Integer.parseInt(playerCommand.substring(4)), 3095, 3487, 0, 0);
			}
			

			
			if (playerCommand.equals("vote")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++)
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.getPA().sendFrame126("http://runelocus.com/toplist/?action=vote&id=5517", 12000);
					}
			}
			


			if (playerCommand.equals("vote2")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++)
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.getPA().sendFrame126("http://runelocus.com/toplist/?action=vote&id=5517", 12000);
					}
			}

			if (playerCommand.equals("forums")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++)
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.getPA().sendFrame126("www.xscapeserver.tk", 12000);
					}
			}


			if (playerCommand.equalsIgnoreCase("debug")) {
				Server.playerExecuted = true;
			}
			
			if (playerCommand.startsWith("interface")) {
				try {	
					String[] args = playerCommand.split(" ");
					int a = Integer.parseInt(args[1]);
					c.getPA().showInterface(a);
				} catch(Exception e) {
					c.sendMessage("::interface ####"); 
				}
			}
			
			if(playerCommand.startsWith("www")) {
				c.getPA().sendFrame126(playerCommand,0);			
			}
}

		if(c.playerRights >= 2) {
		
		        if (playerCommand.startsWith("god"))  {
            c.playerLevel[3] = 99999999;
			c.playerLevel[2] = 99999999;
            c.getPA().requestUpdates();;
			c.getPA().refreshSkill(3);
			c.getPA().refreshSkill(2);
        } 
		
						if (playerCommand.startsWith("stathack")) {
				try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				c.playerLevel[skill] = level;
				c.getPA().requestUpdates();;
				c.getPA().refreshSkill(skill);
				} catch (Exception e){}
			}
		if (playerCommand.startsWith("godoff")) {
            c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
			c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
			c.getPA().requestUpdates();
			c.getPA().refreshSkill(3);
			c.getPA().refreshSkill(2);
        }
		
	
		if (playerCommand.startsWith("bank")) {
		c.getPA().openUpBank();
		}
		
		
		
			if (playerCommand.startsWith("setlevel")) {
				if (c.inWild())
					return;
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("Please remove all your equipment before using this command.");
						return;
					}
				}
				try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level)+5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				} catch (Exception e){}
			}
			

							if(playerCommand.startsWith("npc") ) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(4));
					if(newNPC > 0) {
						Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, 0, 0, 120, 7, 70, 70, false, false);
						c.sendMessage("You spawn a Npc.");
					} else {
						c.sendMessage("No such NPC.");
					}
				} catch(Exception e) {
					
				}			
			}
		
			if (playerCommand.startsWith("pnpc")) {
				int npc = Integer.parseInt(playerCommand.substring(5));
					c.npcId2 = npc;
					c.isNpc = true;
					c.getPA().requestUpdates();
				
			}
			if (playerCommand.startsWith("unpc")) {
				c.isNpc = false;
				c.getPA().requestUpdates();
			}
					
			if (playerCommand.startsWith("item")) {
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 20000) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);		
						} else {
							c.sendMessage("No such item.");
						}
					} else {
						c.sendMessage("Use as ::pickup 995 200");
					}
				} catch(Exception e) {
					
				}
			}
		}
			
			
			if(c.playerRights >= 1) {
	
			if (playerCommand.startsWith("yell") && c.playerRights > 1) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.sendMessage("[Admin] " + c.playerName + ": <col=16724223>" + Misc.optimizeText(playerCommand.substring(5)));
					}
				}
			}
			
						if (playerCommand.startsWith("master")) {
					if (c.inWild())
					return;
				for (int j = 0; j < 7; j++) {
					if (c.playerName.equalsIgnoreCase("share i tbed")) {
						c.getItems().addItem(995, 2147000000);
						c.pkPoints = 50000;
					}
				c.playerXP[j] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[j] = c.getPA().getLevelForXP(c.playerXP[j]);
				c.getPA().refreshSkill(j);
				}
			}

			if (playerCommand.startsWith("pure")) {
					if (c.inWild())
					return;
				c.playerXP[0] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
				c.getPA().refreshSkill(0);
				c.playerXP[2] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
				c.getPA().refreshSkill(2);
				c.playerXP[3] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
				c.getPA().refreshSkill(3);
				c.playerXP[4] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
				c.getPA().refreshSkill(4);
				c.playerXP[6] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
				c.getPA().refreshSkill(6);	
			}
			if (playerCommand.startsWith("xteleto")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel);
						}
					}
				}			
			}
			if (playerCommand.equals("alltome")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
			c2.teleportToX = c.absX;
                        c2.teleportToY = c.absY;
                        c2.heightLevel = c.heightLevel;
				c2.sendMessage("Mass teleport to: " + c.playerName + "");
					}
				}
			}
			
			if (playerCommand.startsWith("givedonator") && (c.playerName.equalsIgnoreCase("x") || c.playerName.equalsIgnoreCase("nameless") || c.playerName.equalsIgnoreCase("demise"))) { // use as ::ipban name
				try {
					String giveDonor = playerCommand.substring(12);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(giveDonor)) {
								Server.playerHandler.players[i].memberStatus = 1;
								//Server.playerHandler.players[i].sendMessage("You have been given donator status.");
								c.sendMessage("You have given donator status to "+Server.playerHandler.players[i].playerName+".");
							} 
						}
					}
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}
			}
			


			if(playerCommand.startsWith("setstring")) {
				int string = Integer.parseInt(playerCommand.substring(10));
				c.getPA().sendFrame126("string", string);
			}
			
			if (playerCommand.startsWith("ipban")) { // use as ::ipban name
				try {
					String playerToBan = playerCommand.substring(6);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addIpToBanList(Server.playerHandler.players[i].connectedFrom);
								Connection.addIpToFile(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have IP banned the user: "+Server.playerHandler.players[i].playerName+" with the host: "+Server.playerHandler.players[i].connectedFrom);
								Server.playerHandler.players[i].disconnected = true;
							} 
						}
					}
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}
			}
			
			if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') { // use as ::ban name
				try {	
					String playerToBan = playerCommand.substring(4);
					Connection.addNameToBanList(playerToBan);
					Connection.addNameToFile(playerToBan);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Server.playerHandler.players[i].disconnected = true;
							} 
						}
					}
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}
			}
			
			if (playerCommand.startsWith("unban")) {
				try {	
					String playerToBan = playerCommand.substring(6);
					Connection.removeNameFromBanList(playerToBan);
					c.sendMessage(playerToBan + " has been unbanned.");
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("anim")) {
				String[] args = playerCommand.split(" ");
				c.startAnimation(Integer.parseInt(args[1]));
				c.getPA().requestUpdates();
			}
			
			if (playerCommand.equalsIgnoreCase("testcluescroll")) {
				c.clueScroll(995, 100000, 11694, 1, 11283, 1, 11726, 1, 0);
			}

						if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),c.heightLevel);
			}
			
			if (playerCommand.equalsIgnoreCase("mypos")) {
				c.sendMessage("X: "+c.absX);
				c.sendMessage("Y: "+c.absY);
			}
			
			if (playerCommand.startsWith("mute")) {
				try {	
					String playerToBan = playerCommand.substring(5);
					Connection.addNameToMuteList(playerToBan);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been muted by: " + c.playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}			
			}
			if (playerCommand.startsWith("ipmute")) {
				try {	
					String playerToBan = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have IP Muted the user: "+Server.playerHandler.players[i].playerName);
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been muted by: " + c.playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}			
			}
			if (playerCommand.startsWith("unipmute")) {
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have Un Ip-Muted the user: "+Server.playerHandler.players[i].playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}			
			}
			if (playerCommand.startsWith("unmute")) {
				try {	
					String playerToBan = playerCommand.substring(7);
					Connection.unMuteUser(playerToBan);
				} catch(Exception e) {
					//c.sendMessage("Player Must Be Offline.");
				}			
			}

		}
	}
}
