package nb.test.gamepackagev1;

import java.awt.*;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {

    int X_coordinate;
    int Y_Coordinate;
    
    public Snake() {
        this.X_coordinate = 0;
        this.Y_Coordinate = 0;
        initUI();
    }
    
    private void initUI() {
        
        add(new NBTestGamePackageV1());
               
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void show(Graphics obj){
        obj.setColor(Color.WHITE); 
        obj.fillOval(X_coordinate,Y_Coordinate,20,20); 
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}