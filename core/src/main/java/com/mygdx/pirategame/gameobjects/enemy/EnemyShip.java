package com.mygdx.pirategame.gameobjects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.DebugUtils;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.CollegeFire;
import com.mygdx.pirategame.pathfinding.Checkpoint;
import com.mygdx.pirategame.pathfinding.PathFinder;
import com.mygdx.pirategame.pathfinding.pathManager.AttackPath;
import com.mygdx.pirategame.pathfinding.pathManager.PathManager;
import com.mygdx.pirategame.pathfinding.pathManager.PatrolPath;
import com.mygdx.pirategame.pathfinding.pathManager.RandomPath;
import com.mygdx.pirategame.save.GameScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Enemy Ship
 * Generates enemy ship data
 * Instantiates an enemy ship
 *
 * @author Ethan Alabaster, Sam Pearson, Edward Poulter, James McNair
 * @version 1.0
 */
public class EnemyShip extends Enemy {

    public static final int COLLISIONRADIUS = 55;
    public static final int COLLISIONOFFSET = 15;
    public static boolean movement = true;
    public static boolean fire = true;
    // TODO: Stop cannonballs using 'fire'

    private Texture enemyShip;
    public CollegeMetadata collegeMeta;
    private final Sound destroy;
    private final Sound hit;
    private Array<CollegeFire> cannonBalls;

    private PathManager pathManager;

    /**
     * used to delay pathfinding when the ship collides with something
     */
    private int updateDelay = 0;

    private List<Checkpoint> path;

    /**
     * Instantiates enemy ship
     *
     * @param screen      Visual data
     * @param x           x coordinates of entity
     * @param y           y coordinates of entity
     * @param path        path of texture file
     * @param collegeMeta College ship is assigned to
     */
    public EnemyShip(GameScreen screen, float x, float y, String path, CollegeMetadata collegeMeta) {
        super(screen, x, y);
        enemyShip = new Texture(path);
        //Assign college
        this.collegeMeta = collegeMeta;
        //Set audios
        destroy = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-hit.wav"));

        cannonBalls = new Array<>();
        //Set the position and size of the college
        setBounds(0, 0, 64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(enemyShip);
        setOrigin(32 / PirateGame.PPM, COLLISIONRADIUS / PirateGame.PPM);

        // Scale the damage that the entity takes with the difficulty
        damage = 20 * screen.difficulty;

        if (collegeMeta != null) {
            this.pathManager = new PatrolPath(this, screen);
        } else {
            this.pathManager = new RandomPath(this, screen);
        }
    }

    /**
     * Used to load an enemy ship from storage
     * @param screen The GameScreen displaying this ship
     * @param path The path to the aset
     * @param element The element storing loation information
     * @param collegeMeta The metadata for the relevent college
     */
    public EnemyShip(GameScreen screen, String path, Element element, CollegeMetadata collegeMeta){
        super(screen, element);
        enemyShip = new Texture(path);
        //Assign college
        this.collegeMeta = collegeMeta;
        //Set audios
        destroy = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-hit.wav"));

        cannonBalls = new Array<>();
        //Set the position and size of the college
        setBounds(0, 0, 64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(enemyShip);
        setOrigin(32 / PirateGame.PPM, COLLISIONRADIUS / PirateGame.PPM);

        // Scale the damage that the entity takes with the difficulty
        damage = 20 * screen.difficulty;

        if (collegeMeta != null) {
            this.pathManager = new PatrolPath(this, screen);
        } else {
            this.pathManager = new RandomPath(this, screen);
        }
    }

    /**
     * Used to generate a new path from the current location to a random point on the map
     */
    public void generateNewPath() {

        Vector2 destination = pathManager.generateDestination();
        if (destination == null) {
            // destination will be regenerated next update
            return;
        }
        path = screen.getPathFinder().getPath((b2body.getPosition().x * PirateGame.PPM), (b2body.getPosition().y * PirateGame.PPM), destination.x, destination.y, COLLISIONRADIUS + COLLISIONOFFSET, COLLISIONRADIUS + COLLISIONOFFSET);
        if (path != null && path.size() > 1) {
            // removing the start node from the path as ship is already at it
            path.remove(0);
        }
    }

    /**
     * Updates the state of each object with delta time
     * Checks for ship destruction
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
        //Update cannon balls
        for(CollegeFire ball : cannonBalls) {
            ball.update(dt);
            if(ball.isDestroyed())
                cannonBalls.removeValue(ball, true);
        }

        //If ship is set to destroy and isn't, destroy it
        if (destroyed) {
            return;
        }

        if (setToDestroy) {
            //Play death noise
            if (GameScreen.game.getPreferences().isEffectsEnabled()) {
                destroy.play(GameScreen.game.getPreferences().getEffectsVolume());
            }
            world.destroyBody(b2body);
            destroyed = true;
            //Change player coins and points
            Hud.changePoints(20);
            Hud.changeCoins(10);
        }

        //Update position and angle of ship
        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
        float angle = (float) Math.atan2(b2body.getLinearVelocity().y, b2body.getLinearVelocity().x);
        b2body.setTransform(b2body.getWorldCenter(), angle - ((float) Math.PI) / 2.0f);
        setRotation((float) (b2body.getAngle() * 180 / Math.PI));
        //Update health bar
        bar.update();

        if (health <= 0) {
            setToDestroy = true;
        }

        if (updateDelay > 0) {
            updateDelay--;
            return;
        }

        if (!inPlayerRange()) {
            return;
        }

        // updating the pathing manager
        pathManager.update(dt);

        if (path == null || path.isEmpty()) {
            generateNewPath();
            return;
        }

        Checkpoint cp = path.get(0);

        if (cp.getVector2().dst(b2body.getPosition().scl(PirateGame.PPM)) < PirateGame.PPM / 2) {
            path.remove(cp);
            if (path.isEmpty()) {
                generateNewPath();
            }
        }

        final float speed;
        if (movement) {
            speed = 100f * dt;
        } else {
            speed = 0;
        }
        Vector2 v = travelToCheckpoint(speed, cp);
        b2body.setLinearVelocity(v);

        // below code is to move the ship to a coordinate (target)
        //Vector2 target = new Vector2(960 / PirateGame.PPM, 2432 / PirateGame.PPM);
        //target.sub(b2body.getPosition());
        //target.nor();
        //float speed = 1.5f;
        //b2body.setLinearVelocity(target.scl(speed));
    }

    @Override
    protected void saveChild(Document document, Element element) {
        // nothing additional needs to be loaded
    }

    /**
     * Used
     *
     * @param maxDistance The max distance that can be travelled
     * @param cp          The checkpoint to travel towards
     * @return The vector that needs to be applied to travel towards the checkpoint
     */
    private Vector2 travelToCheckpoint(float maxDistance, Checkpoint cp) {
        Vector2 v = new Vector2(cp.x - (b2body.getPosition().x * PirateGame.PPM) - getWidth() / 2, cp.y - (b2body.getPosition().y * PirateGame.PPM) - getHeight() / 2);

        return v.limit(maxDistance);
    }

    /**
     * Constructs the ship batch
     *
     * @param batch The batch of visual data of the ship
     */
    public void draw(Batch batch) {
        for(CollegeFire ball : cannonBalls)
            ball.draw(batch);

        if (!destroyed) {
            super.draw(batch);
            //Render health bar
            bar.render(batch);
            // checking if pathfinding information should be displayed
            if (PathFinder.PATHFINDERDEBUG && path != null && !path.isEmpty()) {
                batch.end();

                // setting the color for the debug output (so the current type of pathing can be identified)
                Color dotColor;
                if (pathManager instanceof PatrolPath) {
                    dotColor = Color.ORANGE;
                } else if (pathManager instanceof AttackPath) {
                    dotColor = Color.RED;
                } else {
                    dotColor = Color.GREEN;
                }

                for (int i = 0; i < path.size() - 1; i++) {

                    DebugUtils.drawDebugLine(path.get(i).getVector2().scl(1 / PirateGame.PPM), path.get(i + 1).getVector2().scl(1 / PirateGame.PPM), batch.getProjectionMatrix(), Color.RED);
                    DebugUtils.drawDebugDot(path.get(i).getVector2().scl(1 / PirateGame.PPM), batch.getProjectionMatrix(), dotColor);
                }
                batch.begin();
            }
        }
    }

    /**
     * Defines the ship as an enemy
     * Sets data to act as an enemy
     */
    @Override
    protected void defineEnemy() {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        //b2body.setLinearDamping(1);
        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(COLLISIONRADIUS / PirateGame.PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.ENEMY_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT | PirateGame.CANNON_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.7f;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Checks contact
     * Changes health in accordance with contact and damage
     */
    @Override
    public void onContact() {
        updateDelay = 50;
        Gdx.app.log("enemy", "collision");
        //Play collision sound
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            hit.play(GameScreen.game.getPreferences().getEffectsVolume());
        }
        System.out.println(damage);
        //Deal with the damage
        health -= damage;
        bar.changeHealth(damage);
        Hud.changePoints(5);
    }

    @Override
    public void onContactOther() {
        updateDelay = 50;
        generateNewPath();
    }

    /**
     * Updates the ship image. Particuarly change texture on college destruction
     *
     * @param alignment Associated college
     * @param path      Path of new texture
     */
    public void updateTexture(Integer alignment, String path) {
        collegeMeta = CollegeMetadata.getCollegeMetaFromId(alignment);
        enemyShip = new Texture(path);
        setRegion(enemyShip);
    }


    /**
     * Checks if the ship should pathfind or just sit still (used to reduce needless load)
     *
     * @return If the ship is in range of the player
     */
    public boolean inPlayerRange() {
        return screen.getPlayerPos().dst(b2body.getPosition()) < 30;
    }

    /**
     * Used to check if the set location is traversable by a ship of this size
     *
     * @param x The x location of the proposed location
     * @param y The y location of the proposed location
     * @return If the ship can go there
     */
    public boolean isTraversable(float x, float y) {
        return screen.getPathFinder().isTraversable(x, y, EnemyShip.COLLISIONRADIUS + EnemyShip.COLLISIONOFFSET, EnemyShip.COLLISIONRADIUS + EnemyShip.COLLISIONOFFSET);
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
        // dumping old path
        path = new ArrayList<>();
        generateNewPath();
    }

    /**
     * Fires cannonballs
     */
    public void fire() {
        cannonBalls.add(new CollegeFire(screen, b2body.getPosition().x, b2body.getPosition().y));
    }
}
