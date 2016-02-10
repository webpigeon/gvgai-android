package ontology.effects.unary;

import core.VGDLSprite;
import core.content.InteractionContent;
import core.game.Game;
import ontology.effects.Effect;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 23/10/13
 * Time: 15:21
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class WrapAround extends Effect {

    public double offset;

    public WrapAround(InteractionContent cnt)
    {
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {

        if(sprite1.orientation.x > 0)
        {
            sprite1.pos.x = (int) (offset * sprite1.size.x);
        }
        else if(sprite1.orientation.x < 0)
        {
            sprite1.pos.x = (int) (game.getWidth() - sprite1.size.x * (1+offset));
        }
        else if(sprite1.orientation.y > 0)
        {
            sprite1.pos.y = (int) (offset * sprite1.size.y);
        }
        else if(sprite1.orientation.y < 0)
        {
            sprite1.pos.y = (int) (game.getHeight() - sprite1.size.y * (1+offset));
        }

        sprite1.lastmove = 0;
    }
}
