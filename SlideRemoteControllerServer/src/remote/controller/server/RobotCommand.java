package remote.controller.server;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Pavlo Shenhofer
 * Class for emulate hardware event
 */
public class RobotCommand {
    private Robot robot;

    public RobotCommand(){
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public void pressF5(){
        //robot.delay(5000);
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
    }

    public void pressESC(){
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
    }


    public void pressLeftArrow(){
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
    }

    public void pressRightArrow(){
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
    }
}
