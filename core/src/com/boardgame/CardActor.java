package com.boardgame;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class CardActor extends Image {

    private DeckValues value;
    private int i,j;
    public CardActor(TextureRegion region, int i , int j ) {
        super(region);
        this.i = i ;
        this.j = j;
    }


    public void setValue(DeckValues value) {
        this.value = value;
    }

    public void setDrawable(TextureRegion region) {
        super.setDrawable(new TextureRegionDrawable(region));
        addAnimation(); // play animation when region changed
    }

    public int returnIndexI(){
        return this.i;
    }

    public int returnIndexJ(){
        return this.j;
    }
    private void addAnimation() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(720, 0.25f),
                                Actions.scaleTo(0, 0, 0.25f)

                        ),
                        Actions.scaleTo(1, 1, 0.25f)
                )
        );
    }

    public boolean isMovable() {
        return true;
    }
}

