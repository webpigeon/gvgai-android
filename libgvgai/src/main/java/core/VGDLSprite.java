package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import core.competition.CompetitionParameters;
import core.content.SpriteContent;
import core.game.Game;
import ontology.Types;
import ontology.physics.ContinuousPhysics;
import ontology.physics.GravityPhysics;
import ontology.physics.GridPhysics;
import ontology.physics.NoFrictionPhysics;
import ontology.physics.Physics;
import tools.Utils;
import tools.Vector2d;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 17/10/13
 * Time: 10:59
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public abstract class VGDLSprite {

    /**
     * Name of this sprite.
     */
    public String name;

    /**
     * Indicates if this sprite is static or not.
     */
    public boolean is_static;

    /**
     * Indicates if passive movement is denied for this sprite.
     */
    public boolean only_active;

    /**
     * Indicates if this sprite is the avatar (player) of the game.
     */
    public boolean is_avatar;

    /**
     * Indicates if the sprite has a stochastic behaviour.
     */
    public boolean is_stochastic;

    /**
     * States the pause ticks in-between two moves
     */
    public int cooldown;

    /**
     * Scalar speed of this sprite.
     */
    public double speed;

    /**
     * Mass of this sprite (for Continuous physics).
     */
    public double mass;

    /**
     * Id of the type if physics this sprite responds to.
     */
    public int physicstype_id;

    /**
     * String that represents the physics type of this sprite.
     */
    public String physicstype;

    /**
     * Reference to the physics object this sprite belongs to.
     */
    public Physics physics;

    /**
     * Scale factor to draw this sprite.
     */
    public double shrinkfactor;

    /**
     * Indicates if this sprite has an oriented behaviour.
     */
    public boolean is_oriented;

    /**
     * Tells if an arrow must be drawn to indicate the orientation of the sprite.
     */
    public boolean draw_arrow;

    /**
     * Orientation of the sprite.
     */
    public Vector2d orientation;

    /**
     * Rectangle that this sprite occupies on the screen.
     */
    public Vector2d pos;
    public Vector2d size;

    /**
     * Rectangle occupied for this sprite in the previous game step.
     */
    public Vector2d lastPos;

    /**
     * Tells how many timesteps ago was the last move
     */
    public int lastmove;

    /**
     * Strength measure of this sprite.
     */
    public double strength;

    /**
     * Indicates if this sprite is a singleton.
     */
    public boolean singleton;

    /**
     * Indicates if this sprite is a resource.
     */
    public boolean is_resource;

    /**
     * Indicates if this sprite is a portal.
     */
    public boolean portal;

    /**
     * Indicates if the sprite is invisible. If it is, the effect is that
     * it is not drawn.
     */
    public boolean invisible;

    /**
     * List of types this sprite belongs to. It contains the ids, including itself's, from this sprite up
     * in the hierarchy of sprites defined in SpriteSet in the game definition.
     */
    public ArrayList<Integer> itypes;

    /**
     * Indicates the amount of resources this sprite has, for each type defined as its int identifier.
     */
    public TreeMap<Integer, Integer> resources;

    /**
     * String that represents the image in VGDL.
     */
    public String img;

    /**
     * Indicates if this sprite is an NPC.
     */
    public boolean is_npc;

    /**
     * ID of this sprite.
     */
    public int spriteID;

    /**
     * Indicates if this sprite was created by the avatar.
     */
    public boolean is_from_avatar;

    /**
     * Bucket
     */
    public int bucket;

    /**
     * Bucket remainder.
     */
    public boolean bucketSharp;

    /**
     * Indicates if the sprite is able to rotate in place.
     */
    public boolean rotateInPlace;

    /**
     * Indicates if the sprite is in its first cycle of existence.
     * Passive movement is not allowed in the first tick.
     */
    public boolean isFirstTick;

    /**
     * Initializes the sprite, giving its position and dimensions.
     * @param position position of the sprite
     * @param size dimensions of the sprite on the screen.
     */
    protected void init(Vector2d position, Vector2d size) {
        this.setRect(position, size);
        this.lastPos = new Vector2d(position);
        physicstype_id = Types.PHYSICS_GRID;
        physics = null;
        speed = 0;
        cooldown = 0;
        only_active = false;
        name = null;
        is_static = false;
        is_avatar = false;
        is_stochastic = false;
        is_from_avatar = false;
        mass = 1;
        shrinkfactor = 1.0;
        is_oriented = false;
        draw_arrow = false;
        orientation = Types.NONE;
        lastmove = 0;
        invisible = false;
        rotateInPlace = false;
        isFirstTick = true;
        resources = new TreeMap<Integer, Integer>();
        itypes = new ArrayList<Integer>();

        determinePhysics(physicstype_id, size);
    }

    public void setRect(Vector2d position, Vector2d size)
    {
        this.pos = position;
        this.size = size;

        bucket = (int)(position.y / size.y);
        bucketSharp = (int)(position.y % size.y) == 0;
    }

    /**
     * Loads the default values for this sprite.
     */
    protected void loadDefaults() {
        name = this.getClass().getName();
    }

    /**
     * Parses parameters for the sprite, received as a SpriteContent object.
     * @param content
     */
    public void parseParameters(SpriteContent content) {

        VGDLFactory factory = VGDLFactory.GetInstance();
        factory.parseParameters(content,this);

        //post-process. Some sprites may need to do something interesting (i.e. SpawnPoint) once their
        // parameters have been defined.
        this.postProcess();
    }

    /**
     * Determines the physics type of the game, creating the Physics objects that performs the calculations.
     * @param physicstype identifier of the physics type.
     * @param size dimensions of the sprite.
     * @return the phyics object.
     */
    private Physics determinePhysics(int physicstype, Vector2d size) {
        this.physicstype_id = physicstype;
        switch (physicstype) {
            case Types.PHYSICS_GRID:
                physics = new GridPhysics(size);
                break;
            case Types.PHYSICS_CONT:
                physics = new ContinuousPhysics();
                break;
            case Types.PHYSICS_NON_FRICTION:
                physics = new NoFrictionPhysics();
                break;
            case Types.PHYSICS_GRAVITY:
                physics = new GravityPhysics();
                break;
        }
        return physics;
    }

    /**
     * Updates this sprite, performing the movements and actions for the next step.
     * @param game the current game that is being played.
     */
    public void update(Game game)
    {
        updatePassive();
    }

    /**
     * Applies both the passive and active movement for the avatar in the forward model.
     * This method is DEPRECATED.
     */
    @Deprecated
    public void applyMovement(Game game, boolean[] actionMask) {

        //First, lock orientation, if any.
        Vector2d tmp = orientation.copy();
        orientation = Types.NONE.copy();

        //Then, update the passive movement.
        preMovement();
        updatePassive();

        //Apply action supplied.
        Vector2d action = Utils.processMovementActionKeys(actionMask);
        this.physics.activeMovement(this, action, this.speed);

        //Set orientation of the avatar.
        Vector2d dir = lastDirection();
        if(dir.x == 0 && dir.y == 0)
        {
            //No movement.
            orientation = tmp;
        }else{
            //moved, update:
            dir.normalise();
            orientation = dir;
        }

    }

    /**
     * Prepares the sprite for movement.
     */
    public void preMovement()
    {
        lastPos = new Vector2d(pos);
        lastmove += 1;
    }

    /**
     * Updates this sprite applying the passive movement.
     */
    public void updatePassive() {

        if (!is_static && !only_active) {
            physics.passiveMovement(this);
        }
    }


    /**
     * Updates the orientation of the avatar to match the orientation parameter.
     * @param orientation final orientation the avatar must have.
     * @return true if orientation could be changed. This returns false in two circumstances:
     * the avatar is not oriented (is_oriented == false) or the previous orientation is the
     * same as the one received by parameter.
     */
    public boolean _updateOrientation(Vector2d orientation)
    {
        if(!this.is_oriented) return false;
        if(this.orientation.equals(orientation)) return false;
        this.orientation = orientation.copy();
        return true;
    }

    /**
     * Updates the position of the sprite, giving its orientation and speed.
     * @param orientation the orientation of the sprite.
     * @param speed the speed of the sprite.
     * @return true if the position changed.
     */
    public boolean _updatePos(Vector2d orientation, int speed) {
        if (speed == 0) {
            speed = (int) this.speed;
            if(speed == 0) return false;
        }

        if (cooldown <= lastmove && (Math.abs(orientation.x) + Math.abs(orientation.y) != 0)) {
            pos.set((int) orientation.x * speed, (int) orientation.y * speed);
            bucket = (int)(pos.y / size.y);
            bucketSharp = (pos.y % size.y) == 0;
            lastmove = 0;
            return true;
        }
        return false;
    }

    /**
     * Returns the velocity of the sprite, in a Vector2d object.
     * @return the velocity of the sprite
     */
    public Vector2d _velocity() {
        if (speed == 0 || !is_oriented) {
            return new Vector2d(0, 0);
        } else {
            return new Vector2d(orientation.x * speed, orientation.y * speed);
        }
    }

    /**
     * Returns the last direction this sprite is following.
     * @return the direction.
     */
    public Vector2d lastDirection() {
        Vector2d diff = new Vector2d(pos);
        diff.subtract(lastPos);
        return diff;
    }

    /**
     * Gets the position of this sprite.
     * @return the position as a Vector2d.
     */
    public Vector2d getPosition()
    {
        return new Vector2d(lastPos);
    }

    /**
     * Modifies the amount of resource by a given quantity.
     * @param resourceId id of the resource whose quantity must be changed.
     * @param amount_delta amount of units the resource has to be modified by.
     */
    public void modifyResource(int resourceId, int amount_delta)
    {
        int prev = getAmountResource(resourceId);
        int next = Math.max(0,prev + amount_delta);
        resources.put(resourceId, next);
    }

    /**
     * Returns the amount of resource of a given type this sprite has.
     * @param resourceId id of the resource to check.
     * @return how much of this resource this sprite has.
     */
    public int getAmountResource(int resourceId)
    {
        int prev = 0;
        if(resources.containsKey(resourceId))
            prev = resources.get(resourceId);

        return prev;
    }

    /**
     * Gets the unique and precise type of this sprite
     * @return the type
     */
    public int getType()
    {
        return itypes.get(itypes.size()-1);
    }

    /**
     * Method to perform post processing when the sprite has received its parameters.
     */
    public void postProcess()
    {

        if(this.orientation != Types.NONE)
        {
            //Any sprite that receives an orientation, is oriented.
            this.is_oriented = true;
        }
    }

    /**
     * Used to indicate if this sprite was created by the avatar.
     * @param fromAvatar true if the avatar created this sprite.
     */
    public void setFromAvatar(boolean fromAvatar)
    {
        is_from_avatar = fromAvatar;
    }


    /**
     * Returns a string representation of this string, including its name and position.
     * @return the string representation of this sprite.
     */
    public String toString() {
        return name + " at (" + getPosition() + ")";
    }

    /**
     * Creates a copy of this sprite. To be overwritten in each subclass.
     * @return  a copy of this sprite.
     */
    public abstract VGDLSprite copy();

    /**
     * Copies the attributes of this object to the one passed as parameter.
     * @param toSprite the sprite to copy to.
     */
    public void copyTo(VGDLSprite toSprite)
    {
        //this.color, this.draw_arrow don't need to be copied.
        toSprite.name = this.name;
        toSprite.is_static = this.is_static;
        toSprite.only_active = this.only_active;
        toSprite.is_avatar = this.is_avatar;
        toSprite.is_stochastic = this.is_stochastic;
        toSprite.cooldown = this.cooldown;
        toSprite.speed = this.speed;
        toSprite.mass = this.mass;
        toSprite.physicstype_id = this.physicstype_id;
        toSprite.physics = this.physics; //Object reference, but should be ok.
        toSprite.shrinkfactor = this.shrinkfactor;
        toSprite.is_oriented = this.is_oriented;
        toSprite.orientation = this.orientation.copy();
        toSprite.pos = new Vector2d(pos);
        toSprite.size = new Vector2d(size);
        toSprite.lastPos = new Vector2d(lastPos);
        toSprite.lastmove = this.lastmove;
        toSprite.strength = this.strength;
        toSprite.singleton = this.singleton;
        toSprite.is_resource = this.is_resource;
        toSprite.portal = this.portal;
        toSprite.physicstype = this.physicstype;
        toSprite.draw_arrow = this.draw_arrow;
        toSprite.is_npc = this.is_npc;
        toSprite.spriteID = this.spriteID;
        toSprite.is_from_avatar = this.is_from_avatar;
        toSprite.bucket = this.bucket;
        toSprite.bucketSharp = this.bucketSharp;
        toSprite.invisible = this.invisible;
        toSprite.rotateInPlace = this.rotateInPlace;
        toSprite.isFirstTick = this.isFirstTick;

        toSprite.itypes = new ArrayList<Integer>();
        for(Integer it : this.itypes)
            toSprite.itypes.add(it);

        toSprite.resources = new TreeMap<Integer, Integer>();
        Set<Map.Entry<Integer, Integer>> entries = this.resources.entrySet();
        for(Map.Entry<Integer, Integer> entry : entries)
        {
            toSprite.resources.put(entry.getKey(), entry.getValue());
        }

    }

    /**
     * Determines if two the object passed is equal to this.
     * We are NOT overriding EQUALS on purpose (Costly operation for eventHandling).
     */
    public boolean equiv(Object o)
    {
        if(this == o) return true;
        if(!(o instanceof VGDLSprite)) return false;
        VGDLSprite other = (VGDLSprite)o;

        if(other.name != this.name) return false;
        if(other.is_static != this.is_static) return false;
        if(other.only_active != this.only_active) return false;
        if(other.is_avatar != this.is_avatar) return false;
        if(other.is_stochastic != this.is_stochastic) return false;
        if(other.cooldown != this.cooldown) return false;
        if(other.speed != this.speed) return false;
        if(other.mass != this.mass) return false;
        if(other.physicstype_id != this.physicstype_id) return false;
        if(other.shrinkfactor != this.shrinkfactor) return false;
        if(other.is_oriented != this.is_oriented) return false;
        if(!other.orientation.equals(this.orientation)) return false;
        if(!other.pos.equals(this.pos)) return false;
        if(!other.size.equals(this.size)) return false;
        if(other.lastmove != this.lastmove) return false;
        if(other.strength != this.strength) return false;
        if(other.singleton != this.singleton) return false;
        if(other.is_resource != this.is_resource) return false;
        if(other.portal != this.portal) return false;
        if(other.is_npc != this.is_npc) return false;
        if(other.is_from_avatar != this.is_from_avatar) return false;
        if(other.invisible != this.invisible) return false;
        if(other.spriteID != this.spriteID) return false;
        if(other.isFirstTick != this.isFirstTick) return false;

        int numTypes = other.itypes.size();
        if(numTypes != this.itypes.size()) return false;
        for(int i = 0; i < numTypes; ++i)
            if(other.itypes.get(i) != this.itypes.get(i)) return false;

        return true;
    }

    /**
     * Get all sprites that affect or being affected by the current sprite
     * @return a list of all dependent sprites
     */
    public ArrayList<String> getDependentSprites(){
    	return new ArrayList<String>();
    }
    
}
