package com.platformer.battle.animation;

import com.platformer.exceptions.*;

import javax.imagio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class SpriteSheet{
    private final BufferedImage sheet;
    private final int frameWidth;
    private final int frameHeight;

    public SpriteSheet(String resourcePath, int frameWidth, int frameHeight){
        this.frameHeight=frameHeight;
        this.frameWidth=frameWidth;

        BufferedImage loaded = null;
        try{
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream != null){
                loaded = ImageIO.read(inputStream);
            }else{
                loaded = makePlaceholderSheet(frameWidth, frameHeight);
            }
        }catch(IOException e){
            loaded = makePlaceholderSheet(frameWidth, frameHeight);
        }
        this.sheet=loaded;
    }

    public BufferedImage getFrame(int col, int row){
        int x = col * frameWidth;
        int y = row * frameHeight;

        if(x + frameWidth > sheet.getWidth() || y+frameHeight> sheet.getHeight()){
            return makePlaceholderSheet(frameWidth, frameHeight, java.awt.Color.MAGENTA);
        }
        return sheet.getSubimage(x, y, framewidth, frameHeight);
    }

    public BufferedImage[] getRow(int row, int frameCount){
        BufferedImage[] frames = new BufferedImage[frameCount];
        for(int i =0; i<frameCount;i++){
            frames[i]=getFrame(i,row);
        }
        return frames;
    }

    public int getFrameWidth()  { return frameWidth;  }
    public int getFrameHeight() { return frameHeight; }
    public int getSheetWidth()  { return sheet.getWidth();  }
    public int getSheetHeight() { return sheet.getHeight(); }

    private BufferedImage makePlaceholderSheet(int fw, int fh) {
        int cols = 6;
        int rows = 6;
        BufferedImage img = new BufferedImage(fw * cols, fh * rows,
                                              BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color.DARK_GRAY);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(java.awt.Color.LIGHT_GRAY);
        g.fillOval(fw / 4, 2, fw / 2, fw / 2);          // head
        g.fillRect(fw / 4, fw / 2 + 2, fw / 2, fh / 2); // body
        g.dispose();
        return img;
    }

    private BufferedImage makePlaceholderFrame(int fw, int fh,
                                               java.awt.Color color) {
        BufferedImage img = new BufferedImage(fw, fh,
                                             BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, fw, fh);
        g.dispose();
        return img;
    }
}