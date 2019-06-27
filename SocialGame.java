/**
 * Created by danie on 13/06/2019.
 */
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import java.awt.event.*;
import java.awt.*;

public class SocialGame extends JPanel {

    private final int height, width;
    private boolean isLiving;
    private static int delay = 400;
    private int count; //generazioni

    /*private static ChangeListener listener;
    private JPanel sliderPanel;
    private JTextField textField;*/

    public static Random random = new Random();

    public SocialGame(int h, int w) {
        //super("SGS");
        this.height = h;
        this.width = w;
        MouseMotionAdapter mma = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setAlive(true);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                isLiving = !isLiving;
                repaint();
            }
        };
        this.addMouseMotionListener(mma);
        isLiving = random.nextBoolean();
    }

    public boolean isAlive(int neighbors) {
        boolean alive = false;
        if (this.isLiving) {
            if (neighbors < 2 || neighbors > 3) {
                alive = false;
            } else if (neighbors == 2 || neighbors == 3) {
                alive = true;
            }
        } else {
            if (neighbors == 3) {
                alive = true;
            }
        }
        return alive;
    }

    public void setAlive(boolean alive) {
        isLiving = alive;
    }

    public boolean isLiving() {
        return this.isLiving;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isLiving) {
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /*public void addSlider (JSlider s, String job) {
        s.addChangeListener(listener);
        this.add(s);
        this.add(new JLabel(job));
        sliderPanel.add(this);
    }*/

    public void reset() {
        count = 0;
    }
    public void inc() {
        count++;
    }
    public int getValue() {
        return count;
    }

    public static void main(String[] args) {
        final int height = 40, width = 60;
        final SocialGame[][] society = new SocialGame[height][width];
        //final JDesktopPane desktop = new JDesktopPane(); //??
        //desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        //desktop.setLayout(new BorderLayout());
        final JFrame window = new JFrame("Social Game System");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        final JPanel gui = new JPanel(new GridLayout(height, width, 1, 1));// Del pannello possiamo decidere l'altezza, la larghezza e la distanza tra una cella e l'altra
        final JSlider slider = new JSlider(JSlider.VERTICAL, 100, 1100, 400); // Orientamento, valore minimo, valore massimo e valore iniziale
        slider.setMajorTickSpacing(200);
        slider.setMinorTickSpacing(40);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Hashtable position = new Hashtable();
        position.put(100, new JLabel("100 ms"));
        position.put(300, new JLabel("300 ms"));
        position.put(500, new JLabel("500 ms"));
        position.put(700, new JLabel("700 ms"));
        position.put(900, new JLabel("900 ms"));
        position.put(1100, new JLabel("1100 ms"));
        slider.setLabelTable(position);
        slider.setVisible(true);
        window.add(slider, BorderLayout.LINE_START);
        window.getContentPane().add(gui, BorderLayout.CENTER);
        //desktop.setVisible(true);
        window.setVisible(true);
        //slider.addChangeListener(new ChangeListener() {
        //    public void stateChanged(ChangeEvent e) {delay = ((JSlider)e.getSource()).getValue();}
        //});
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    delay = slider.getValue();
                }
            }
        });
        /*listener = new ChangeListener()
        {
            public void stateChanged(ChangeEvent event)
            {
                // update text field when the slider value changes
                JSlider source = (JSlider) event.getSource();
                textField.setText("" + source.getValue());
            }
        };
        JSlider slider = new JSlider();
        slider = new JSlider();
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(50);
        slider.setMajorTickSpacing(1000);
        gui.addSlider (slider, "delay");*/
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    SocialGame cell = new SocialGame(i, j);
                    cell.setPreferredSize(new Dimension(9, 9));
                    gui.add(cell);
                    society[i][j] = cell;
                }
            }

            ActionListener al = (ActionEvent ae) -> {
                boolean[][] living = new boolean[height][width];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int top = (j > 0 ? j - 1 : width - 1); // if jj > 0 then top = jj - 1, else jj = height - 1
                        int bottom = (j < width - 1 ? j + 1 : 0);
                        int left = (i > 0 ? i - 1 : height - 1);
                        int right = (i < height - 1 ? i + 1 : 0);
                        int neighbors = 0;
                        if (society[i][top].isLiving()) {
                            neighbors++;
                        }
                        if (society[i][bottom].isLiving()) {
                            neighbors++;
                        }
                        if (society[left][top].isLiving()) {
                            neighbors++;
                        }
                        if (society[left][bottom].isLiving()) {
                            neighbors++;
                        }
                        if (society[left][j].isLiving()) {
                            neighbors++;
                        }
                        if (society[right][j].isLiving()) {
                            neighbors++;
                        }
                        if (society[right][top].isLiving()) {
                            neighbors++;
                        }
                        if (society[right][bottom].isLiving()) {
                            neighbors++;
                        }
                        living[i][j] = society[i][j].isAlive(neighbors);
                    }
                }
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        society[i][j].setAlive(living[i][j]);
                    }
                }
                gui.repaint();
            };

            Timer timer = new Timer(delay, al); // Millisecondi tra un ciclo ed il successivo
            timer.start();

            JOptionPane.showMessageDialog(null, gui);
            timer.stop();
        }
    }