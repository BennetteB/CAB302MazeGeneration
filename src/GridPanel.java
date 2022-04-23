import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Creates the Grid Panel with editable cells for Maze construction
 */
public class GridPanel extends JPanel {
    enum Position {HORIZONTAL,VERTICAl}
    private GridBagConstraints cst;
    private int sizeMultiplier = 5;
    private ArrayList<JButton> btnList = new ArrayList<JButton>();

    public GridPanel() {
        super();
        setLayout(new GridBagLayout());
        cst = new GridBagConstraints();
        cst.weightx = 0;
        cst.weighty = 0;
        cst.anchor = GridBagConstraints.CENTER;
        cst.fill = GridBagConstraints.NONE;
    }

    /**
     * Creates an Empty Grid
     * @param width Number of cells wide
     * @param height Number of cells high
     */
    protected void CreateGrid(int width, int height) {
        int x = 0;
        int y = 0;
        for(int i = 0; i < (height * 2) + 1; i++) {
            for(int j = 0; j < (width * 2) + 1; j++) {
                if(i % 2 == 0) {
                    if(j == 0){
                        continue;
                    }
                    cst.gridx = x;
                    cst.gridy = y;
                    cst.gridwidth = 14;
                    cst.gridheight = 2;
                    add(createWall(Position.HORIZONTAL),cst);
                    x += 12;
                    j += 1;
                }
                else{
                    if(j % 2 == 0) {
                        cst.gridx = x;
                        cst.gridy = y - 2;
                        cst.gridwidth = 2;
                        cst.gridheight = 14;
                        add(createWall(Position.VERTICAl),cst);
                        x += 2;
                    }
                    else {
                        cst.gridx = x;
                        cst.gridy = y;
                        cst.gridwidth = 10;
                        cst.gridheight = 10;
                        add(createCell(),cst);
                        x += 10;
                    }
                }
            }
            if(i % 2 == 0) {
                y += 2;
            }
            else {
                y += 10;
            }
            x = 0;
        }
    }

    /**
     * Creates Empty JPanel Cell
     * @return JPanel
     */
    private JPanel createCell() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(10 * sizeMultiplier,10 * sizeMultiplier));
        return panel;
    }

    /**
     * Creates Vertical or Horizontal JButton Walls
     * @param position Vertical or Horizontal decides dimensions of button
     * @return JButton
     */
    private JButton createWall(Position position) {
        JButton btn = new JButton();
        btn.setOpaque(false);
        btn.setBackground(Color.BLACK);
        btn.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {
                for(int i = 0; i < btnList.size(); i++) {
                    if(e.getSource() == btnList.get(i)) {
                        btnList.get(i).setBackground(Color.RED);
                        repaint();
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setBackground(Color.WHITE);
            }
        });
        switch (position) {
            case HORIZONTAL:
                btn.setPreferredSize(new Dimension(14 * sizeMultiplier,2 * sizeMultiplier));
                break;
            case VERTICAl:
                btn.setPreferredSize(new Dimension(2 * sizeMultiplier,14 * sizeMultiplier));
                break;
        }
        btnList.add(btn);
        return btnList.get(btnList.size() - 1);
    }
}
