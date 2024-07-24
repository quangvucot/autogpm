package com.vdq.autogpm.util;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
public class WindowPositioner {
    private final int windowWidth;
    private final int windowHeight;
    private final Dimension screenSize;
    private final Queue<Point> availablePositions;
    private int currentX;
    private int currentY;

    public WindowPositioner(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.availablePositions = new LinkedList<>();
        this.currentX = 0;
        this.currentY = 0;
    }

    public synchronized Point getNextPosition() {
        if (!availablePositions.isEmpty()) {
            return availablePositions.poll();
        }

        if (currentX + windowWidth > screenSize.width) {
            currentX = 0;
            currentY += windowHeight;
        }

        if (currentY + windowHeight > screenSize.height) {
            currentX = 0;
            currentY = 0;
        }

        Point position = new Point(currentX, currentY);
        currentX += windowWidth;

        return position;
    }
}