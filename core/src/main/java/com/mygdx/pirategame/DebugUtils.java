package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

/**
 * Methods to make debugging bugs easier (for example drawing lines to track pathfinding)
 * @author James McNair
 */
public class DebugUtils {

    private static final ShapeRenderer debugRenderer = new ShapeRenderer();

    /**
     * Used to draw a debug line with the specified parameters
     * @param start The start location of the line
     * @param end The end location of the line
     * @param lineWidth The width of the line
     * @param color The color of the line
     * @param projectionMatrix The projection matrix of the camera
     */
    public static void drawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix) {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    /**
     * Used to draw a debug line with the specified parameters
     * @param start The start location of the line
     * @param end The end location of the line
     * @param projectionMatrix The projection matrix of the camera
     * @param color The color of the line
     */
    public static void drawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix, Color color) {
        Gdx.gl.glLineWidth(5);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    /**
     * Used to draw a debug dot (looks like a triangle for some reason)
     * @param start The start location of the line
     * @param projectionMatrix The end location of the line
     * @param color The color of the line
     */
    public static void drawDebugDot(Vector2 start, Matrix4 projectionMatrix, Color color) {
        Gdx.gl.glLineWidth(5);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(color);
        debugRenderer.circle(start.x, start.y, 0.25f);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    // stop an instance being created of this class
    private DebugUtils() {

    }

}
