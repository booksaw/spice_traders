package com.mygdx.pirategame.tests;

import com.badlogic.gdx.Gdx;
import com.mygdx.pirategame.PirateGameTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * This class is used to target the class pirategame with file assertion tests
 * @author Marc Perales Salomo
 */
@RunWith(PirateGameTest.class)
public class FileExistance {

    /**
     * mapbase.txt is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void mapbasetxt() {
        assertTrue("This test will pass if mapbase.txt exists", Gdx.files
                .internal("../core/assets/mapBase.txt").exists());
    }

    /**
     * map_blurred.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void map_blurred() {
        assertTrue("This test will pass if map_blurred.png exists", Gdx.files
                .internal("../core/assets/map_blurred.png").exists());
    }

    /**
     * hudBG.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void hudBG() {
        assertTrue("This test will pass if hudBG.png exists", Gdx.files
                .internal("../core/assets/hudBG.png").exists());
    }

    /**
     * mapbase.txt is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void hp() {
        assertTrue("This test will pass if hp.png exists", Gdx.files
                .internal("../core/assets/hp.png").exists());
    }

    /**
     * HealthBar.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void HealthBar() {
        assertTrue("This test will pass if mapbase.txt exists", Gdx.files
                .internal("../core/assets/HealthBar.png").exists());
    }

    /**
     * default.fnt is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void defaultfnt() {
        assertTrue("This test will pass if default.fnt exists", Gdx.files
                .internal("../core/assets/default.fnt").exists());
    }

    /**
     * coin.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void coin() {
        assertTrue("This test will pass if coin.png exists", Gdx.files
                .internal("../core/assets/entity/coin.png").exists());
    }

    /**
     * cannonBall.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void cannonBall() {
        assertTrue("This test will pass if cannonBall.png exists", Gdx.files
                .internal("../core/assets/entity/cannonBall.png").exists());
    }

    //assets in /skin/

    /**
     * skin/default.fnt is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void skindefault() {
        assertTrue("This test will pass if skin/default.fnt exists", Gdx.files
                .internal("../core/assets/skin/default.fnt").exists());
    }

    /**
     * skin/uiskin.atlas is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void skinuiskinatlas() {
        assertTrue("This test will pass if skin/uiskin.atlas exists", Gdx.files
                .internal("../core/assets/skin/uiskin.atlas").exists());
    }

    /**
     * skin/uiskin.json is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void skinuiskinjson() {
        assertTrue("This test will pass if skin/uiskin.json exists", Gdx.files
                .internal("../core/assets/skin/uiskin.json").exists());
    }

    /**
     * skin/uiskin.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void skinuiskinpng() {
        assertTrue("This test will pass if skin/uiskin.png exists", Gdx.files
                .internal("../core/assets/skin/uiskin.png").exists());
    }

    //assets in sfx_and_music

    /**
     * sfx_and_music/coin-pickup.mp3 is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void coinpickup() {
        assertTrue("This test will pass if /sfx_and_music/coin-pickup.mp3 exists", Gdx.files
                .internal("../core/assets/sfx_and_music/coin-pickup.mp3").exists());
    }

    /**
     * sfx_and_music/explode.mp3 is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void explodemp3() {
        assertTrue("This test will pass if /sfx_and_music/explode.mp3 exists", Gdx.files
                .internal("../core/assets/sfx_and_music/explode.mp3").exists());
    }

    /**
     * sfx_and_music/explosion.wav is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void explodewav() {
        assertTrue("This test will pass /sfx_and_music/explosion.wav exists", Gdx.files
                .internal("../core/assets/sfx_and_music/explosion.wav").exists());
    }

    /**
     * sfx_and_music/pirate-music.mp3 is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void piratemusicmp3() {
        assertTrue("This test will pass /sfx_and_music/pirate-music.mp3", Gdx.files
                .internal("../core/assets/sfx_and_music/pirate-music.mp3").exists());
    }

    /**
     * sfx_and_music/ship-explosion-2.wav is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void shipexplosion2wav() {
        assertTrue("This test will pass /sfx_and_music/ship-explosion-2.wav", Gdx.files
                .internal("../core/assets/sfx_and_music/ship-explosion-2.wav").exists());
    }

    /**
     * sfx_and_music/ship-hit.wav is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void shiphit() {
        assertTrue("This test will pass /sfx_and_music/ship-hit.wav", Gdx.files
                .internal("../core/assets/sfx_and_music/ship-hit.wav").exists());
    }

    /**
     * sfx_and_music/wood-bump.mp3 is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void woodbumpmp3() {
        assertTrue("This test will pass /sfx_and_music/wood-bump.mp3", Gdx.files
                .internal("../core/assets/sfx_and_music/wood-bump.mp3").exists());
    }

    //Map rendering

    /**
     * Will check that every asset for the map renders properly
     * Note: for loop used up to 88 but since tiles 89, 90, 91 and 92 don't exist originally,
     *      the specific tiles after that are tested
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void maprender() {
        assertTrue("This test will pass /map/islands.tsx", Gdx.files
                .internal("../core/assets/map/islands.tsx").exists());
        assertTrue("This test will pass /map/map.tmx", Gdx.files
                .internal("../core/assets/map/map.tmx").exists());
        assertTrue("This test will pass /map/rocks.tsx", Gdx.files
                .internal("../core/assets/map/rocks.tsx").exists());
        assertTrue("This test will pass /map/water.tsx", Gdx.files
                .internal("../core/assets/map/water.tsx").exists());
        for (int i=1; i<=88; i++) {
            if (Integer.toString(i).length() == 1) {
                assertTrue("This test will pass /map/tile_0" + i + ".png", Gdx.files
                        .internal("../core/assets/map/tile_0" + i + ".png").exists());
            } else {
                assertTrue("This test will pass /map/tile_" + i + ".png", Gdx.files
                        .internal("../core/assets/map/tile_" + i + ".png").exists());
            }
        }
        assertTrue("This test will pass /map/tile_93.png", Gdx.files
                .internal("../core/assets/map/tile_93.png").exists());
        assertTrue("This test will pass /map/tile_94.png", Gdx.files
                .internal("../core/assets/map/tile_94.png").exists());
    }

    //assets in /college/

    //assets in /college/Flags
    /**
     * college/Flags/alcuin_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void alcuinflag() {
        assertTrue("This test will pass /college/Flags/alcuin_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/alcuin_flag.png").exists());
    }

    /**
     * college/Flags/anne_lister_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void annelisterflag() {
        assertTrue("This test will pass /college/Flags/anne_lister_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/anne_lister_flag.png").exists());
    }

    /**
     * college/Flags/constantine_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void constantineflag() {
        assertTrue("This test will pass /college/Flags/constantine_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/constantine_flag.png").exists());
    }

    /**
     * college/Flags/derwent_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void derwentflag() {
        assertTrue("This test will pass /college/Flags/derwent_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/derwent_flag.png").exists());
    }

    /**
     * college/Flags/goodricke_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void goodrickeflag() {
        assertTrue("This test will pass /college/Flags/goodricke_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/goodricke_flag.png").exists());
    }

    /**
     * college/Flags/halifax_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void halifaxflag() {
        assertTrue("This test will pass /college/Flags/halifax_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/halifax_flag.png").exists());
    }

    /**
     * college/Flags/james_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void jamesflag() {
        assertTrue("This test will pass /college/Flags/james_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/james_flag.png").exists());
    }

    /**
     * college/Flags/langwith_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void langwithflag() {
        assertTrue("This test will pass /college/Flags/langwith_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/langwith_flag.png").exists());
    }

    /**
     * college/Flags/vanbrugh_flag.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void vanbrughflag() {
        assertTrue("This test will pass /college/Flags/vanbrugh_flag.png", Gdx.files
                .internal("../core/assets/college/Flags/vanbrugh_flag.png").exists());
    }

    //assets in /college/Ships

    /**
     * college/Ships/alcuin_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void alcuinship() {
        assertTrue("This test will pass /college/Ships/alcuin_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/alcuin_ship.png").exists());
    }

    /**
     * college/Ships/anne_lister_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void annelistership() {
        assertTrue("This test will pass /college/Ships/anne_lister_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/anne_lister_ship.png").exists());
    }

    /**
     * college/Ships/constantine_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void constantineship() {
        assertTrue("This test will pass /college/Ships/constantine_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/constantine_ship.png").exists());
    }

    /**
     * college/Ships/derwent_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void derwentship() {
        assertTrue("This test will pass /college/Ships/derwent_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/derwent_ship.png").exists());
    }

    /**
     * college/Ships/enemyShip1.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void enemyShip1() {
        assertTrue("This test will pass /college/Ships/enemyShip1.png", Gdx.files
                .internal("../core/assets/college/Ships/enemyShip1.png").exists());
    }

    /**
     * college/Ships/goodricke_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void goodrickeship() {
        assertTrue("This test will pass /college/Ships/goodricke_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/goodricke_ship.png").exists());
    }

    /**
     * college/Ships/player_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void playership() {
        assertTrue("This test will pass /college/Ships/player_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/player_ship.png").exists());
    }

    /**
     * college/Ships/ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void ship1() {
        assertTrue("This test will pass /college/Ships/ship.png", Gdx.files
                .internal("../core/assets/college/Ships/ship.png").exists());
    }

    /**
     * college/Ships/ship1.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void ship2() {
        assertTrue("This test will pass /college/Ships/ship1.png", Gdx.files
                .internal("../core/assets/college/Ships/ship1.png").exists());
    }

    /**
     * college/Ships/unaligned_ship.png is an asset file
     *
     * @throws AssertionError passes the message for specific test that didn't pass and raises an error
     */
    @Test
    public void unalignedship() {
        assertTrue("This test will pass /college/Ships/unaligned_ship.png", Gdx.files
                .internal("../core/assets/college/Ships/unaligned_ship.png").exists());
    }
}
