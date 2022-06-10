import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportMaze extends JDialog {
    private ImageConverter convert;
    private BufferedImage[][] imageExports;

    /**
     * Constructor for ExportMaze
     * @param mazesToExport is an array of maze details to be exported
     * @param allMaze is a list of all mazes retrieved from the database
     */
    protected ExportMaze(MazeDetails[] mazesToExport, List<MazeDetails> allMaze) {
        convert = new ImageConverter();
        initDialog(mazesToExport, allMaze);
    }

    /**
     * Opens up a dialog box which has options to be chosen from,
     * maze image or maze solution image
     * @param mazesToExport is an array of maze details to be exported
     * @param allMaze is a list of all mazes retrieved from the database
     */
    protected void initDialog(MazeDetails[] mazesToExport, List<MazeDetails> allMaze) {
        boolean optimalSolutionOption = true;
        imageExports = new BufferedImage[2][mazesToExport.length];
        int i = 0;
        for (MazeDetails maze : mazesToExport) {
            imageExports[0][i] = convert.stringToImage(maze.mazeImage);
            imageExports[1][i] = convert.stringToImage(maze.mazeSolution);
            if (maze.mazeImage == null || maze.mazeSolution == null)
                optimalSolutionOption = false;
            i++;
        }
        JDialog exportDialog = new JDialog();
        exportDialog.setTitle("Export Maze as image");
        JPanel exportPanel = new JPanel();
        JPanel exportContent = new JPanel(new BorderLayout());
        JPanel exportRadioButtons = new JPanel();
        exportRadioButtons.setLayout(new BoxLayout(exportRadioButtons, BoxLayout.Y_AXIS));
        JRadioButton exportMazeOption = new JRadioButton("Export maze", true);
        JRadioButton exportSolutionOption = new JRadioButton( "Export optimal solution");
        exportMazeOption.setAlignmentX(Component.LEFT_ALIGNMENT);
        exportSolutionOption.setAlignmentX(Component.LEFT_ALIGNMENT);
        exportMazeOption.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        exportSolutionOption.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        ButtonGroup exportOptions = new ButtonGroup();
        exportRadioButtons.add(exportMazeOption);
        JButton exportCancel = new JButton();
        exportCancel.setText("Cancel");
        if (optimalSolutionOption) exportRadioButtons.add(exportSolutionOption);
        exportContent.add(exportRadioButtons, BorderLayout.WEST);
        exportContent.setBorder(new EmptyBorder(15, 0, 20, 0));
        JPanel exportButtonPanel = new JPanel();
        JButton exportOk = new JButton("Ok");
        if (mazesToExport.length < 2 ) {
            JLabel exportImage = new JLabel(new ImagePane(new ImageIcon(imageExports[0][0]), 4, 4).getResizedImage());
            exportImage.setPreferredSize(new Dimension(177, 122));
            exportImage.setMaximumSize(new Dimension(177, 122));
            exportContent.add(exportImage, BorderLayout.CENTER);
            exportSolutionOption.addChangeListener(e-> {
                    if (exportSolutionOption.isSelected()) {
                        exportImage.setIcon(new ImagePane(new ImageIcon(imageExports[1][0]), 4, 4).getResizedImage());
                    } else if (exportMazeOption.isSelected()) {
                        exportImage.setIcon(new ImagePane(new ImageIcon(imageExports[0][0]), 4, 4).getResizedImage());
                    }
                });
            for (ChangeListener listener : exportSolutionOption.getChangeListeners()) {
                exportMazeOption.addChangeListener(listener);
            }
        } else {
            JLabel mazesSelected = new JLabel(imageExports[0].length + " mazes selected");
            mazesSelected.setForeground(Color.gray);
            mazesSelected.setBorder(new EmptyBorder(30, 65, 0, 0));
            exportRadioButtons.add(mazesSelected);
        }
        exportOk.addChangeListener(e-> {
                if (exportOk.getModel().isPressed()) {
                    exportDialog.setVisible(false);
                    BufferedImage[] toExport = new BufferedImage[imageExports[0].length];
                    if (exportMazeOption.isSelected()) {
                        toExport = imageExports[0];
                    } else if (exportSolutionOption.isSelected()) {
                        toExport = imageExports[1];
                    }
                    String status;
                    if (exportMaze(toExport, allMaze)) status = "Image(s) successfully exported";
                    else status = "there was an error in exporting your image(s)";
                    JOptionPane.showMessageDialog(this, status);
                }
        });
        exportCancel.addChangeListener(e -> {
                if (exportCancel.getModel().isPressed()) {
                    exportDialog.setVisible(false);
                }
        });
        exportButtonPanel.add(exportOk);
        exportButtonPanel.add(exportCancel);
        exportButtonPanel.setMaximumSize(new Dimension(400, 40));
        exportButtonPanel.setPreferredSize(new Dimension(400, 40));
        exportOptions.add(exportMazeOption);
        if (optimalSolutionOption) exportOptions.add(exportSolutionOption);
        BoxLayout layout = new BoxLayout(exportPanel, BoxLayout.Y_AXIS);
        exportPanel.setLayout(layout);
        exportPanel.add(exportContent);
        exportPanel.add(exportButtonPanel);
        if (mazesToExport.length < 2) {
            exportDialog.setSize(400, 250);
        } else {
            exportDialog.setTitle("Export mazes as image");
            exportMazeOption.setText("Export mazes");
            exportSolutionOption.setText("Export maze optimal solutions");
            exportDialog.setSize(250, 200);
        }
        exportDialog.add(exportPanel);
        exportDialog.setModal(true);
        exportDialog.setLocationRelativeTo(null);
        exportDialog.setVisible(true);
    }

    /**
     * Exports selected mazes as an image file
     * @param bi array of bufferedimages to be exported
     * @param
     * @return boolean value of whether export has been successfull or not
     */
    public boolean exportMaze(BufferedImage[] bi, List<MazeDetails> mazes) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileString = file.toString();
            for (int i = 0; i < bi.length; i++) {
                try {
                    ImageIO.write(bi[i], "png", new File(fileString + "\\" + mazes.get(i).name + ".png"));
                } catch (IOException ex) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
