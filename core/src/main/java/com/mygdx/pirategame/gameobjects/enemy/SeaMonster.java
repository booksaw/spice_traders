package com.mygdx.pirategame.gameobjects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.mygdx.pirategame.save.GameScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class SeaMonster extends Enemy {

    public static final int COLLISIONRADIUS = 100;
    public static final int COLLISIONOFFSET = 15;
    public boolean movement = false;
    public boolean fire = true;
    float angle;

    private static Sound destroy;
    private static Sound hit;
    private Array<CollegeFire> projectiles;
    private static Texture waterSlashTexture;

    private PathManager pathManager = null;

    /**
     * used to delay pathfinding when the ship collides with something
     */
    private int updateDelay = 0;

    private List<Checkpoint> path;

    /**
     * Instantiates an enemy
     *
     * @param screen Visual data
     * @param x      x position of entity
     * @param y      y position of entity
     */
    public SeaMonster(GameScreen screen, float x, float y) {
        super(screen, x, y);
        //Set audios
        destroy = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-hit.wav"));

        projectiles = new Array<>();
        waterSlashTexture = new Texture(Gdx.files.internal("sea_monster/waterslash.png"));

        // use idle animation by default
        loadAnimation();

        //Set the position and size of the sea monster
        setBounds(0, 0, 300 / PirateGame.PPM, 300 / PirateGame.PPM);
        setOrigin(90 / PirateGame.PPM, COLLISIONRADIUS / PirateGame.PPM);

        // Scale the damage that the entity takes with the difficulty
        damage = 5 * screen.difficulty;
    }

    public SeaMonster(GameScreen screen, Element element) {
        super(screen, element);

        //Set audios
        destroy = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-hit.wav"));

        projectiles = new Array<>();
        waterSlashTexture = new Texture(Gdx.files.internal("sea_monster/waterslash.png"));

        // use idle animation by default
        loadAnimation();

        //Set the position and size of the sea monster
        setBounds(0, 0, 300 / PirateGame.PPM, 300 / PirateGame.PPM);
        setOrigin(90 / PirateGame.PPM, COLLISIONRADIUS / PirateGame.PPM);

        // Scale the damage that the entity takes with the difficulty
        damage = 5 * screen.difficulty;

    }

    /**
     * Loads the textures used in the movement animations
     */
    private static Animation idleAnimation;
    private static Animation movingAnimation;
    private TextureRegion current_frame;
    private float state_time = 0f;

    public static void loadAnimation() {
        int idleCols = 7;
        int movingCols = 8;
        Texture idleSpriteSheet = new Texture(Gdx.files.internal("sea_monster/idleSpriteSheet.png"));
        Texture movingSpriteSheet =new Texture(Gdx.files.internal("sea_monster/movingSpriteSheet.png"));

        TextureRegion[][] idleTmp = TextureRegion.split(idleSpriteSheet, idleSpriteSheet.getWidth()/idleCols, idleSpriteSheet.getHeight());
        TextureRegion[][] movingTmp = TextureRegion.split(movingSpriteSheet, movingSpriteSheet.getWidth()/movingCols, movingSpriteSheet.getHeight());

        TextureRegion[] idle_animation_frames = new TextureRegion[idleCols];
        TextureRegion[] moving_animation_frames = new TextureRegion[movingCols];
        int index = 0;

        // idle sprite sheet has 7 frames and moving sprite sheet has 8 frames
        for (int x=0; x<idleCols; x++) {
            idle_animation_frames[index++] = idleTmp[0][x];
        }
        index = 0;
        for (int x=0; x<movingCols; x++) {
            moving_animation_frames[index++] = movingTmp[0][x];
        }

        idleAnimation = new Animation(0.1f, idle_animation_frames);
        movingAnimation = new Animation(0.1f, moving_animation_frames);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        movingAnimation.setPlayMode(Animation.PlayMode.LOOP);
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
     * Defines enemy
     */
    @Override
    protected void defineEnemy() {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

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
     * Defines contact
     */
    @Override
    public void onContact() {
        updateDelay = 50;
        Gdx.app.log("sea monster", "collision");
        //Play collision sound
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            hit.play(GameScreen.game.getPreferences().getEffectsVolume());
        }
        //Deal with the damage
        health -= damage;
        bar.changeHealth(damage);
        Hud.changePoints(5);
    }

    @Override
    public void onContactOther() {
        updateDelay = 50;
        if (pathManager != null) generateNewPath();
    }

    /**
     * Checks if the ship should pathfind or just sit still (used to reduce needless load)
     *
     * @return If the ship is in range of the player
     */
    public boolean inPlayerRange() {
        return screen.getPlayerPos().dst(b2body.getPosition()) < 7;
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
     * Shoot projectiles
     */
    public void fire() {

        projectiles.add(new CollegeFire(screen, waterSlashTexture, b2body.getPosition().x, b2body.getPosition().y, 120, 120, angle));
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
        // dumping old path
        path = new ArrayList<>();
        if (pathManager != null) generateNewPath();
    }

    @Override
    public void update(float dt) {
        // Update projectiles
        for(CollegeFire ball : projectiles) {
            ball.update(dt);
            if(ball.isDestroyed())
                projectiles.removeValue(ball, true);
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
            Hud.changePoints(50);
            Hud.changeCoins(50);
        }

        // update texture to current frame in animation
        if (current_frame != null) setRegion(current_frame);

        // Update position and angle of sea monster
        // Sprite is off center when moving animation is playing, so it is offset
        if (movement) {
            // rotate sea monster when in moving animation
            setPosition(b2body.getPosition().x - getWidth() / 4f, b2body.getPosition().y - getHeight() / 2f);
            angle = (float) Math.atan2(b2body.getLinearVelocity().y, b2body.getLinearVelocity().x);
            b2body.setTransform(b2body.getWorldCenter(), angle - ((float) Math.PI) / 2.0f);
            setRotation((float) (b2body.getAngle() * 180 / Math.PI) + 90);
        }
        else {
            // Reset velocity and rotation when in idle animation
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
            b2body.setTransform(b2body.getWorldCenter(), 0);
            setRotation(0);
        }

        //Update health bar
        bar.update();

        if (health <= 0) {
            setToDestroy = true;
        }

        if (updateDelay > 0) {
            updateDelay--;
            return;
        }

        // If out of player range stop moving and play idle animation by setting movement to false
        if (!inPlayerRange() && movement) {
            movement = false;
            b2body.setLinearVelocity(0, 0);
            return;
        }
        // Attack the player and follow them if in range, play moving animation by setting movement to true
        else if (inPlayerRange() && !movement){
            setPathManager(new AttackPath(pathManager, this, screen));
            movement = true;
        }

        // If there is no path set, generate a new one
        if ((path == null || path.isEmpty()) && pathManager != null) {
            generateNewPath();
            return;
        }
        else {
            if (pathManager == null) return;

            // updating the pathing manager
            pathManager.update(dt);

            if (path  == null) return;

            if (path.isEmpty()) return;
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
        }
    }

    @Override
    protected void saveChild(Document document, Element element) {
        // nothing to do here
    }

    /**
     * Constructs the sea monster batch
     *
     * @param batch The batch of visual data of the ship
     */
    public void draw(Batch batch) {
        for (CollegeFire ball : projectiles)
            ball.draw(batch);

        if (!destroyed) {
            if (getTexture() != null) batch.draw(getTexture(), getVertices(), 0, 20);

            //Render health bar
            bar.render(batch);

            // Draw each frame in the animation
            state_time += Gdx.graphics.getDeltaTime();
            if (movement) current_frame = (TextureRegion) movingAnimation.getKeyFrame(state_time, true);
            else current_frame = (TextureRegion) idleAnimation.getKeyFrame(state_time, true);

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

    public static void dispose(){
        waterSlashTexture.dispose();
        hit.dispose();
        destroy.dispose();
    }
}
