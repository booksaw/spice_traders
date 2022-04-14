package com.mygdx.pirategame.save;

import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.gameobjects.enemy.SeaMonster;
import com.mygdx.pirategame.gameobjects.entity.Coin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlSaveLoader extends SaveLoader {

    private final File file;

    public XmlSaveLoader(File file) {
        this.file = file;
    }

    private boolean success;

    @Override
    public void load(GameScreen screen) {
        screen.hideTutorial();
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);

            Element root = document.getDocumentElement();

            Element player = (Element) root.getElementsByTagName("player").item(0);
            screen.player = new Player(screen, player);

            Hud.load((Element) root.getElementsByTagName("hud").item(0));


            // Alcuin college
            screen.getColleges().put(CollegeMetadata.ALCUIN, new College(screen, CollegeMetadata.ALCUIN, (Element) root.getElementsByTagName("alcuin").item(0), screen.getInvalidSpawn()));
            // Anne Lister college
            screen.getColleges().put(CollegeMetadata.ANNELISTER, new College(screen, CollegeMetadata.ANNELISTER, (Element) root.getElementsByTagName("annelister").item(0), screen.getInvalidSpawn()));
            // Constantine college
            screen.getColleges().put(CollegeMetadata.CONSTANTINE, new College(screen, CollegeMetadata.CONSTANTINE, (Element) root.getElementsByTagName("constantine").item(0), screen.getInvalidSpawn()));
            // Goodricke college
            screen.getColleges().put(CollegeMetadata.GOODRICKE, new College(screen, CollegeMetadata.GOODRICKE, (Element) root.getElementsByTagName("goodricke").item(0), screen.getInvalidSpawn()));

            for (CollegeMetadata college : CollegeMetadata.values()) {
                screen.getEnemyShips().addAll(screen.getCollege(college).fleet);
            }

            NodeList shipList = root.getElementsByTagName("ship");
            for (int i = 0; i < shipList.getLength(); i++) {
                screen.getEnemyShips().add(new EnemyShip(screen, "college/Ships/unaligned_ship.png", ((Element) shipList.item(i)), null));
            }

            NodeList monsterList = root.getElementsByTagName("monster");
            for (int i = 0; i < shipList.getLength(); i++) {
                screen.getMonsters().add(new SeaMonster(screen, (Element) monsterList.item(i)));
            }

            //Random coins
            for (int i = 0; i < 100; i++) {
                int[] loc = screen.getRandomLocation();
                //Add a coins at the random coords
                screen.getCoins().add(new Coin(screen, loc[0], loc[1]));
            }

            //Random power ups
            screen.addPowerUps();
            success = true;
        } catch (Exception e) {
            success = false;
            JOptionPane.showMessageDialog(null, "Could not load the selected file, starting new game");
        }
    }

    public boolean success() {
        return success;
    }
}
