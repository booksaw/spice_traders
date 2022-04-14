package com.mygdx.pirategame.save;

import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Locale;

/**
 * Used as a superclass for all loaders
 * loaders are classes used to load a game state
 * this class is used to handle saving and loading
 */
public abstract class SaveLoader {

    public abstract void load(GameScreen screen);

    public void save(GameScreen screen, File file) {

        try {
            if(!file.exists()){
                file.createNewFile();
            }

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("game");

            // saving player information
            Element player = document.createElement("player");
            screen.getPlayer().save(document, player);
            root.appendChild(player);

            // saving points / gold
            Element hud = document.createElement("hud");
            Hud.save(document, hud);
            root.appendChild(hud);

            // saving the colleges
            for(CollegeMetadata collegeMeta : CollegeMetadata.values()) {
                Element collegeEle = document.createElement(collegeMeta.toString().toLowerCase());
                screen.getCollege(collegeMeta).save(document, collegeEle);
                root.appendChild(collegeEle);
            }

            // saving the ships not tied to a college
            for(EnemyShip ship : screen.getEnemyShips()){
                if(ship.collegeMeta != null){
                    continue;
                }

                Element shipEle = document.createElement("ship");
                ship.save(document, shipEle);
                root.appendChild(shipEle);

            }


            document.appendChild(root);
            // creating the XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);

            transformer.transform(domSource, streamResult);



        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Something went wrong with file saving");
            GameScreen.game.changeScreen(PirateGame.MENU);
        }


    }

}
