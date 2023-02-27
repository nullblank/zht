// 
// Decompiled by Procyon v0.5.36
// 

package dev.zomboid.admin;

import zombie.core.Color;
import zombie.ui.UIFont;
import zombie.ui.TextManager;
import zombie.ui.UIEventHandler;
import zombie.core.textures.Texture;
import zombie.ui.UIElement;

public class AdminToolButton extends UIElement
{
    public boolean clicked;
    public UIElement MessageTarget;
    public boolean mouseOver;
    public String name;
    public String text;
    Texture downLeft;
    Texture downMid;
    Texture downRight;
    float origX;
    Texture upLeft;
    Texture upMid;
    Texture upRight;
    private UIEventHandler MessageTarget2;
    
    public AdminToolButton(final UIElement target, final float x, final float y, final String text, final String name) {
        this.clicked = false;
        this.mouseOver = false;
        this.MessageTarget2 = null;
        this.x = x;
        this.y = y;
        this.origX = x;
        this.MessageTarget = target;
        this.upLeft = Texture.getSharedTexture("ButtonL_Up");
        this.upMid = Texture.getSharedTexture("ButtonM_Up");
        this.upRight = Texture.getSharedTexture("ButtonR_Up");
        this.downLeft = Texture.getSharedTexture("ButtonL_Down");
        this.downMid = Texture.getSharedTexture("ButtonM_Down");
        this.downRight = Texture.getSharedTexture("ButtonR_Down");
        this.name = name;
        this.text = text;
        this.width = (float)TextManager.instance.MeasureStringX(UIFont.Small, text);
        this.width += 8.0f;
        if (this.width < 40.0f) {
            this.width = 40.0f;
        }
        this.height = (float)this.downMid.getHeight();
    }
    
    public AdminToolButton(final UIEventHandler var1, final int var2, final int var3, final String var4, final String var5) {
        this.clicked = false;
        this.mouseOver = false;
        this.MessageTarget2 = null;
        this.x = var2;
        this.y = var3;
        this.origX = (float)var2;
        this.MessageTarget2 = var1;
        this.upLeft = Texture.getSharedTexture("ButtonL_Up");
        this.upMid = Texture.getSharedTexture("ButtonM_Up");
        this.upRight = Texture.getSharedTexture("ButtonR_Up");
        this.downLeft = Texture.getSharedTexture("ButtonL_Down");
        this.downMid = Texture.getSharedTexture("ButtonM_Down");
        this.downRight = Texture.getSharedTexture("ButtonR_Down");
        this.name = var5;
        this.text = var4;
        this.width = (float)TextManager.instance.MeasureStringX(UIFont.Small, var4);
        this.width += 8.0f;
        if (this.width < 40.0f) {
            this.width = 40.0f;
        }
        this.height = (float)this.downMid.getHeight();
    }
    
    public Boolean onMouseDown(final double var1, final double var3) {
        if (!this.isVisible()) {
            return false;
        }
        this.clicked = true;
        return Boolean.TRUE;
    }
    
    public Boolean onMouseMove(final double var1, final double var3) {
        this.mouseOver = true;
        return Boolean.TRUE;
    }
    
    public void onMouseMoveOutside(final double var1, final double var3) {
        this.clicked = false;
        this.mouseOver = false;
    }
    
    public Boolean onMouseUp(final double var1, final double var3) {
        if (this.clicked) {
            if (this.MessageTarget2 != null) {
                this.MessageTarget2.Selected(this.name, 0, 0);
            }
            else if (this.MessageTarget != null) {
                this.MessageTarget.ButtonClicked(this.name);
            }
        }
        this.clicked = false;
        return Boolean.TRUE;
    }
    
    public void render() {
        if (this.isVisible()) {
            final boolean var1 = false;
            if (this.clicked) {
                this.DrawTexture(this.downLeft, 0.0, 0.0, 1.0);
                this.DrawTextureScaledCol(this.downMid, (double)this.downLeft.getWidth(), 0.0, (double)(int)(this.getWidth() - this.downLeft.getWidth() * 2), (double)this.downLeft.getHeight(), new Color(255, 255, 255, 255));
                this.DrawTexture(this.downRight, (double)(int)(this.getWidth() - this.downRight.getWidth()), 0.0, 1.0);
                this.DrawTextCentre(this.text, this.getWidth() / 2.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            }
            else {
                this.DrawTexture(this.upLeft, 0.0, 0.0, 1.0);
                this.DrawTextureScaledCol(this.upMid, (double)this.downLeft.getWidth(), 0.0, (double)(int)(this.getWidth() - this.downLeft.getWidth() * 2), (double)this.downLeft.getHeight(), new Color(255, 255, 255, 255));
                this.DrawTexture(this.upRight, (double)(int)(this.getWidth() - this.downRight.getWidth()), 0.0, 1.0);
                this.DrawTextCentre(this.text, this.getWidth() / 2.0, 0.0, 1.0, 1.0, 1.0, 1.0);
            }
            super.render();
        }
    }
}
