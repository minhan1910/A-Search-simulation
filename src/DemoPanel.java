import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DemoPanel extends JPanel {

    // SCREEN SETTINGS 31 14
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    // NODE
    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;
    List<Node> openList = new ArrayList<>();
    List<Node> checkedList = new ArrayList<>();

    // OTHERS
    boolean goalReached = false;
    int step = 0;

    public DemoPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this)); // Cho Enter
        this.setFocusable(true); // Phải có cái này enter mới thấy được

        // PLACE NODES
        int col = 0;
        int row = 0;
        int cnt = 1;
        while (col < maxCol && row < maxRow) {
            node[col][row] = new Node(col, row);
            // System.out.print("[" + col + "]" + "[" + row + "]" + "(" + (cnt++) + ") ");
            this.add(node[col][row]);
            ++col;
            if (col == maxCol) {
                col = 0;
                ++row;
                System.out.println();
            }
        }

        // SET START AND GOAL NODE
        setStartNode(3, 6);
        setGoalNode(11, 3);

        // PLACE SOLID NODES
        setSolidNode(10, 2);
        setSolidNode(10, 3);
        setSolidNode(10, 4);
        setSolidNode(10, 5);
        setSolidNode(10, 6);
        setSolidNode(10, 7);
        setSolidNode(6, 2);
        setSolidNode(7, 2);
        setSolidNode(8, 2);
        setSolidNode(9, 2);
        setSolidNode(11, 7);
        setSolidNode(12, 7);
        setSolidNode(6, 1);

        // SET COST
        setCostOnNodes();
    }

    private void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    private void setGoalNode(int col, int row) {
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    private void setCostOnNodes() {
        int col = 0, row = 0;

        while (col < maxCol && row < maxRow) {
            getCost(node[col][row]);

            ++col;
            if (col == maxCol) {
                col = 0;
                ++row;
            }
        }
    }

    /**
     * @param Node node - The current node
     */
    private void getCost(Node node) {

        // GET G COST (The distance from the start node)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // GET H COST (The distance from goal node)
        xDistance = Math.abs(goalNode.col - node.col);
        yDistance = Math.abs(goalNode.row - node.row);
        node.hCost = xDistance + yDistance;

        // GET F COST (The total cost)
        node.fCost = node.gCost + node.hCost;

        // DISPLAY THE COST ON NODE
        if (node != startNode && node != goalNode) {
            // Using HTML tag for handling line break
            node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "</html>");
        }
    }

    // manual steps
    public void search() {
        if (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // OPEN THE UP NODE
            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }

            // OPEN THE LEFT NODE
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }

            // OPEN THE DOWN NODE
            if (row + 1 < maxRow) {
                openNode(node[col][row + 1]);
            }

            // OPEN THE RIGHT NODE
            if (col + 1 < maxCol) {
                openNode(node[col + 1][row]);
            }

            // FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodeFCode = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); ++i) {

                // Check if this node's F cost is better
                Node openCurrentNode = openList.get(i);
                if (openCurrentNode.fCost < bestNodeFCode) {
                    bestNodeIndex = i;
                    bestNodeFCode = openCurrentNode.fCost;
                }
                // IF F cost is equal, check the G cost
                else if (openCurrentNode.fCost == bestNodeFCode) {
                    if (openCurrentNode.gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // AFTER the loop, we get the best node which is out next step
            currentNode = openList.get(bestNodeIndex);

            // Check the goal node, or not
            if (currentNode == goalNode) {
                goalReached = true;
            }
        }
    }

    // auto steps
    public void autoSearch() {
        while (goalReached == false && step < 300) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // OPEN THE UP NODE
            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }

            // OPEN THE LEFT NODE
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }

            // OPEN THE DOWN NODE
            if (row + 1 < maxRow) {
                openNode(node[col][row + 1]);
            }

            // OPEN THE RIGHT NODE
            if (col + 1 < maxCol) {
                openNode(node[col + 1][row]);
            }

            // FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodeFCode = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); ++i) {

                // Check if this node's F cost is better
                Node openCurrentNode = openList.get(i);
                if (openCurrentNode.fCost < bestNodeFCode) {
                    bestNodeIndex = i;
                    bestNodeFCode = openCurrentNode.fCost;
                }
                // IF F cost is equal, check the G cost
                else if (openCurrentNode.fCost == bestNodeFCode) {
                    if (openCurrentNode.gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // AFTER the loop, we get the best node which is out next step
            currentNode = openList.get(bestNodeIndex);

            // Check the goal node, or not
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            ++step;
        }
    }

    private void openNode(Node node) {
        if (node.open == false && node.checked == false &&
                node.solid == false) {
            // If the node is not opened yet, add it to the open list
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {

        // Backtrack and draw the best path
        Node current = goalNode;

        while (current != startNode) {

            current = current.parent;

            if (current != startNode) {
                current.setAsPath();
            }
        }
    }
}
