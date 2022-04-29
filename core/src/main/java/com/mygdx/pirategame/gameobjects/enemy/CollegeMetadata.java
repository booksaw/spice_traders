package com.mygdx.pirategame.gameobjects.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.PirateGame;

/**
 * Enum used to store metadata about colleges
 * @author James McNair, Robert Murphy, Dan Wade
 */
public enum CollegeMetadata {

    /**
     * Define all the colleges within the game along with any constants associated with them
     */
    ALCUIN(0, "alcuin", 1900 / PirateGame.PPM, 2100 / PirateGame.PPM, 1800 / PirateGame.PPM, 2100 / PirateGame.PPM, 7),
    ANNELISTER(1, "anne_lister", 6304 / PirateGame.PPM, 1199 / PirateGame.PPM, 6200 / PirateGame.PPM, 1100 / PirateGame.PPM, 8),
    CONSTANTINE(2, "constantine", 6240 / PirateGame.PPM, 6703 / PirateGame.PPM, 6400 / PirateGame.PPM, 6700 / PirateGame.PPM, 8),
    GOODRICKE(3, "goodricke", 1760 / PirateGame.PPM, 6767 / PirateGame.PPM, 1700 / PirateGame.PPM, 6700 / PirateGame.PPM, 8);

    /**
     * Method for getting college data from ID
     * @param id ID number of college (0-3)
     * @return College data
     */
    public static CollegeMetadata getCollegeMetaFromId(Integer id) {
        switch (id) {
            case 0:
                return ALCUIN;
            case 1:
                return ANNELISTER;
            case 2:
                return CONSTANTINE;
            case 3:
                return GOODRICKE;
        }
        throw new Error("Invalid college ID provided");
    }

    private final int collegeID;
    private final String filePath;
    private final float x;
    private final float y;
    private final float centreX;
    private final float centreY;
    private final int distance;

    /**
     * Add another college to collegeMetadata with required constants
     * @param collegeID The id of this college
     * @param filePath  college name as used in the file path
     * @param x The x location of the college
     * @param y The y location of the college
     * @param centreX The x location of the centre of the college
     * @param centreY The y location of the centre of the college
     * @param distance The distance where the college can be accessed
     */
    CollegeMetadata(int collegeID, String filePath, float x, float y, float centreX, float centreY, int distance) {
        this.collegeID = collegeID;
        this.filePath = filePath;
        this.x = x;
        this.y = y;
        this.centreX = centreX;
        this.centreY = centreY;
        this.distance = distance;
    }

    /**
     * @return college name as used in the file paths
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return The X location of the college
     */
    public float getX() {
        return x;
    }

    /**
     * @return The Y location of the college
     */
    public float getY() {
        return y;
    }

    /**
     * @return The college location as a position vector
     */
    public Vector2 getPosition(){
        return new Vector2(x, y);
    }

    /**
     * @return The college location as a position vector
     */
    public Vector2 getCentrePosition(){
        return new Vector2(centreX, centreY);
    }

    /**
     * @return The distance the college is reachable
     */
    public int getDistance(){
        return distance;
    }

    /**
     * Used to check if this college is the college controlled by the player
     * @return If this college is controlled by the player
     */
    public boolean isPlayer() {
        return this == ALCUIN;
    }
}

