package sample;

/**
 * Created by a.fedorov on 26.03.2016.
 */

import popup.ssn.NotificationPopup;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class PopupTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            NotificationPopup nf = new NotificationPopup(" Seudo databases \n has been updated \n Time: 12:27:00 23/08/2015 ");
            nf.setWIDTH(400);
            nf.setHEIGHT(150);
            nf.setDisplayTime(2000);
            nf.setBackgroundColor1(Color.WHITE);
            nf.setBackGroundColor2(Color.LIGHT_GRAY);
            nf.setForegroundColor(Color.darkGray);
            nf.display();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}