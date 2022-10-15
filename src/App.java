import javax.swing.JFrame;

public class App {
    /**
     * Minh An
     */
    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame();
        window.setTitle("A* Pathfinding - Minh An");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(new DemoPanel());

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
