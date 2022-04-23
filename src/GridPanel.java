import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Creates the Grid Panel with editable cells for Maze construction
 */
public class GridPanel extends JLayeredPane {
    private int sizeMultiplier = 5;
    private Color GridColor = Color.WHITE;
    enum Position {HORIZONTAL,VERTICAl}
    private GridBagConstraints cst;
    private ArrayList<JToggleButton> btnList = new ArrayList<JToggleButton>();

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
                    add(createWall(Position.HORIZONTAL),cst,1);
                    x += 12;
                    j += 1;
                }
                else{
                    if(j % 2 == 0) {
                        cst.gridx = x;
                        cst.gridy = y - 2;
                        cst.gridwidth = 2;
                        cst.gridheight = 14;
                        add(createWall(Position.VERTICAl),cst,1);
                        x += 2;
                    }
                    else {
                        cst.gridx = x;
                        cst.gridy = y;
                        cst.gridwidth = 10;
                        cst.gridheight = 10;
                        add(createCell(),cst,1);
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
        panel.setBackground(GridColor);
        panel.setPreferredSize(new Dimension(10 * sizeMultiplier,10 * sizeMultiplier));
        return panel;
    }

    /**
     * Creates Vertical or Horizontal JButton Walls
     * @param position Vertical or Horizontal decides dimensions of button
     * @return JButton
     */
    private JToggleButton createWall(Position position) {
        JToggleButton btn = new JToggleButton();
        btn.setBorderPainted(false);
        btn.setBackground(GridColor);
        btn.setRolloverEnabled(true);
        btn.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.BLACK;
            }
        });
        btn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(btn.getModel().isRollover()) {
                    btn.setBackground(Color.RED);
                }
                else{
                    btn.setBackground(GridColor);
                }
                if(btn.isSelected()) {

                }
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
