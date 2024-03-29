package com.boardgame;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.boardgame.assets.RegionNames;

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

    public void Remove() {

        super.remove();
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
    public void addAnimation() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.parallel(
                                //Actions.rotateBy(720, 0.25f),
                                Actions.scaleTo(0, 0, 0.15f)

                        ),
                        Actions.scaleTo(1, 1, 0.15f)
                )
        );
    }

    public boolean isMovable() {
        return true;
    }


    public void selectedAnimation() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.moveBy(0,1,0.1f),
                        Actions.moveBy(0,-1,0.1f),
                        Actions.delay(0.25f)

                )
        );
    }

    public void improperSelection() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.rotateBy(15,0.05f),
                        Actions.rotateBy(-15,0.05f),
                        Actions.rotateBy(-15,0.05f),
                        Actions.rotateBy(15,0.05f),
                        Actions.delay(0.25f)

                )
        );
    }

    public void removeAnimation() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.scaleTo(0, 0, 0.3f),
                        Actions.delay(3.25f)

                )
        );

    }
    public void setDeck(int offset) {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.delay(0.001f),
                        Actions.moveBy(-400f,0f,0.0f),
                        Actions.delay(0.5f-offset*0.1f),
                        Actions.moveBy(400f,0,0.5f)

                )
        );


    }

    public void setLastCards(int offset) {
        //setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.moveBy(-offset*40,-270f,0.01f),
                        Actions.delay(0.2f),
                        Actions.delay(offset*0.15f),
                        Actions.parallel(
                                Actions.rotateBy(360, 1f),
                                Actions.moveBy(+offset*40,270f,1f)

                        )

                )
        );


    }
}

