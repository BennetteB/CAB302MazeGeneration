import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class OpenOptions extends JDialog {
    private JButton okButton;
    private JButton delete;
    private JButton cancelButton;
    private JButton exportButton;
    private JList<String> dataOpen;
    private JComboBox sortSelection;
    private JComboBox secondSortSelection;
    private DefaultListModel dataList;
    private MazeDetails mazeDetails;
    private List<MazeDetails> mazeList;

    /** default constructor for OpenOptions
     * @param dsource is the MazeDataSource object that configures connection to database
     * @param frame is the parent MainGUI frame
     */
    protected OpenOptions(MazeDataSource dsource, MainGUI frame) {
        mazeDetails = frame.getMazeDetails();
        initDialog(dsource, frame);
    }

    /**
     * Opens up a dialog box displaying a list of all mazes
     * stored in the database,
     * multiple actions (buttons) - export, delete, open
     * based on selected maze from the list
     * @param dsource is the MazeDataSource object that configures connection to database
     * @param frame is the parent MainGUI frame
     */
    public void initDialog(MazeDataSource dsource, MainGUI frame) {
        JPanel openMazePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setTitle("Open Maze");
        setModal(true);
        delete = new JButton("Delete");
        okButton = new JButton(" Open ");
        cancelButton = new JButton("Cancel");
        JPanel sortedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel mazeDetailsPanel = new JPanel();
        JLabel mazeDetailsLabel = new JLabel();
        mazeDetailsLabel.setMinimumSize(new Dimension(350, 17));
        mazeDetailsLabel.setPreferredSize(new Dimension(350, 17));
        mazeDetailsPanel.add(mazeDetailsLabel);
        dataList = new DefaultListModel();
        String choices[] = {"last edited", "creation date", "author", "maze name"};
        String secondChoices[] = {"ascending", "descending"};
        sortSelection = new JComboBox(choices);
        secondSortSelection = new JComboBox(secondChoices);
        mazeDetails.setList(dsource.getMazeList((String) sortSelection.getSelectedItem(), (String) secondSortSelection.getSelectedItem()));
        mazeList = mazeDetails.getList();
        for (MazeDetails maze : mazeList) {
            dataList.addElement(maze.name);
        }
        exportButton = new JButton("Export");
        exportButton.addChangeListener(e-> {
            if (exportButton.getModel().isPressed()) {
                exportButton.getModel().setEnabled(false);
                if (dataOpen.getSelectedValue() != null && !dataOpen.getSelectedValue().equals("")) {
                    int selectedIndices[] = dataOpen.getSelectedIndices();
                    int selectedMazeIds[] = new int[selectedIndices.length];
                    for (int i = 0; i < selectedIndices.length; i++) {
                        selectedMazeIds[i] = mazeList.get((selectedIndices[i])).id;
                    }
                    ExportMaze exportDialog = new ExportMaze(dsource.getMazeImage(selectedMazeIds), mazeDetails.getList());
                } else {
                    mazeDetailsLabel.setText("please select a maze first");
                }
                exportButton.getModel().setEnabled(true);
            }
        });
        dataOpen = new JList(dataList);
        dataOpen.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dataOpen.addListSelectionListener(e-> {
            if (dataOpen.getSelectedValue() != null
                    && !dataOpen.getSelectedValue().equals("")) {
                String mazeAuthor = mazeList.get((dataOpen.getSelectedIndex())).author;
                Timestamp mazeDate = mazeList.get((dataOpen.getSelectedIndex())).lastEdited;
                mazeDetailsLabel.setText("Last edited: " + mazeDate + "          Author: " + mazeAuthor);
            }
        });
        JScrollPane openList = new JScrollPane(dataOpen);
        JLabel sortLabel = new JLabel("sort by: ");
        sortSelection.addActionListener(e-> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                String item = (String) sortSelection.getSelectedItem();
                dataList.clear();
                mazeDetails.setList(dsource.getMazeList(item, (String) secondSortSelection.getSelectedItem()));
                mazeList = mazeDetails.getList();
                for (MazeDetails maze : mazeList) {
                    dataList.addElement(maze.name);
                }
            }
        });
        secondSortSelection.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                String item = (String) secondSortSelection.getSelectedItem();
                dataList.clear();
                mazeDetails.setList(dsource.getMazeList((String) sortSelection.getSelectedItem(), item));
                mazeList = mazeDetails.getList();
                for (MazeDetails maze : mazeList) {
                    dataList.addElement(maze.name);
                }
            }
        });
        sortedPanel.add(sortLabel);
        sortedPanel.add(sortSelection);
        sortedPanel.add(secondSortSelection);
        sortSelection.setVisible(true);
        openMazePanel.setLayout(new BoxLayout(openMazePanel, BoxLayout.PAGE_AXIS));
        openMazePanel.add(sortedPanel);
        openMazePanel.add(openList);
        openMazePanel.add(mazeDetailsPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 26)));
        buttonPanel.add(okButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(delete);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 40)));
        buttonPanel.add(cancelButton);
        openMazePanel.setSize(400, 200);
        setLayout(new BorderLayout());
        this.add(openMazePanel, BorderLayout.CENTER);
        buttonPanel.setSize(40, 100);
        this.add(buttonPanel, BorderLayout.EAST);
        okButton.addChangeListener(e-> {
                if (okButton.getModel().isPressed()) {
                    if (dataOpen.getSelectedValue() != null && !dataOpen.getSelectedValue().equals("")) {
                        if (dataOpen.getSelectedIndices().length > 1) {
                            mazeDetailsLabel.setText("please select only 1 maze to open");
                            return;
                        }
                        int selectedMazeId = mazeList.get(dataOpen.getSelectedIndex()).id;
                        frame.openSelectedMaze(selectedMazeId);
                        setVisible(false);
                    } else {
                        mazeDetailsLabel.setText("please select a maze first");
                    }
                }
        });
        delete.addChangeListener(e -> {
            if (delete.getModel().isPressed()) {
                if (dataOpen.getSelectedValue() != null && !dataOpen.getSelectedValue().equals("")) {
                    int length = dataOpen.getSelectedIndices().length;
                    int selectedMazeId[] = new int[length];
                    for (int i = 0; i < length; i++) {
                        selectedMazeId[i] = mazeList.get(dataOpen.getSelectedIndices()[i]).id;
                        if (frame.getCurrentMazeId() == selectedMazeId[i])
                            frame.setNewMaze(true);
                    }
                    if (dsource.deleteMaze(selectedMazeId)) {
                        dataList.clear();
                        mazeDetailsLabel.setText("");
                        mazeDetails.setList(dsource.getMazeList((String) sortSelection.getSelectedItem(), (String) secondSortSelection.getSelectedItem()));
                        mazeList = mazeDetails.getList();
                        for (MazeDetails maze : mazeList) {
                            dataList.addElement(maze.name);
                        }
                    }
                } else {
                    mazeDetailsLabel.setText("please select a maze first");
                }
            }

        });
        cancelButton.addChangeListener(e -> {
                if (cancelButton.getModel().isPressed()) {
                    setVisible(false);
                }
            });
        setSize(new Dimension(500, 250));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
