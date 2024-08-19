package ap.gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * The type History panel.
 */
public class HistoryPanel extends JTree {
    private JSplitPane middleSegmentSeperator;
    private MainGUI mainGUI;

    /**
     * Sets middle segment seperator so it can change the content of the middle side.
     *
     * @param middleSegmentSeperator the middle segment seperator
     */
    public void setMiddleSegmentSeperator(JSplitPane middleSegmentSeperator,MainGUI mainGUI) {
        this.middleSegmentSeperator = middleSegmentSeperator;
        this.mainGUI=mainGUI;
    }

    public void saveHistory() throws IOException {
        FileOutputStream fos=new FileOutputStream("savedHistory.shm");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this.getModel());
        oos.close();
        fos.close();
    }
    private void loadHistory() throws IOException, ClassNotFoundException {
        FileInputStream fis=new FileInputStream("savedHistory.shm");
        ObjectInputStream objectInputStream=new ObjectInputStream(fis);
        this.setModel((TreeModel)objectInputStream.readObject());
        objectInputStream.close();
        fis.close();
    }
    /**
     * Instantiates a new History panel.
     */
    public HistoryPanel() {
        super(new DefaultTreeModel(new DefaultMutableTreeNode("/", true), true));
        try{
            loadHistory();
        }
        catch (Exception ex)
        {
        }
        this.rootVisible = false;

        this.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) HistoryPanel.this.getLastSelectedPathComponent();
                if (node == null)
                    return;
                if (!(node.getUserObject() instanceof GraphicalRequest))
                    return;
                middleSegmentSeperator.setRightComponent(((GraphicalRequest) node.getUserObject()).getGraphicalRequest(mainGUI));
            }
        });
        Thread curr=Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                try {
                    saveHistory();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    curr.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        HistoryPanel.this.getModel().getChildCount(this.getModel().getRoot());
        this.setCellRenderer(new MyTreeCellRenderer());
        this.updateUI();

    }

    /**
     * Gets all directories available in this jtree.
     *
     * @param node            the starting node
     * @param startPathString the start path string
     * @param result          the result vector
     */
    void getAllChildren(TreeNode node, String startPathString, Vector<String> result) {
        result.add(startPathString);
        for (int i = 0; i < node.getChildCount(); i++) {
            if (((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject() instanceof String) {
                getAllChildren(node.getChildAt(i), startPathString + node.getChildAt(i).toString() + "/", result);
            }
        }
    }

    @Override
    public void updateUI() {
        LookAndFeel lf = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        super.updateUI();
        try {
            UIManager.setLookAndFeel(lf);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private DefaultMutableTreeNode navigateToDirectory(TreeNode root, String directory) {
        String[] directoryPathSpliced = directory.split("/");
        for (int i = 1; i < directoryPathSpliced.length; i++) {
            for (int j = 0; j < root.getChildCount(); j++) {
                if (((DefaultMutableTreeNode) root.getChildAt(j)).getUserObject() instanceof GraphicalRequest)
                    continue;
                if (root.getChildAt(j).toString().equals(directoryPathSpliced[i])) {
                    root = root.getChildAt(j);
                    break;
                }
            }
        }
        return (DefaultMutableTreeNode) root;
    }

    /**
     * Add new request.
     *
     * @param directory the directory of the request
     * @param request   the request
     */
    public void addNewRequest(String directory, GraphicalRequest request) {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        TreeNode root = (TreeNode) model.getRoot();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(request, false);
        DefaultMutableTreeNode location = navigateToDirectory(root, directory);
        location.insert(newNode, 0);

        this.updateUI();

    }

    /**
     * Add new directory.
     *
     * @param directory the directory
     * @param name      the name
     */
    public void addNewDirectory(String directory, String name) {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        TreeNode root = (TreeNode) model.getRoot();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name, true);
        DefaultMutableTreeNode location = navigateToDirectory(root, directory);


        location.insert(newNode, 0);
        this.expandPath(new TreePath(newNode.getPath()));
        this.setSelectionPath(new TreePath(newNode.getPath()));
        this.updateUI();

    }


    /**
     * The type My tree cell renderer, used to set colors.
     */
    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {


        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
            if (hasFocus) {
                setBackground(ColorCanvas.SECONDARY.getColor());
                setForeground(ColorCanvas.SECONDARY_TEXT_COLOR.getColor());
            } else {
                setBackground(ColorCanvas.PRIMARY.getColor());
                setForeground(ColorCanvas.SECONDARY_TEXT_COLOR.getColor());
            }

            //tree.updateUI();
            this.setOpaque(true);
            return this;
        }
    }

}
