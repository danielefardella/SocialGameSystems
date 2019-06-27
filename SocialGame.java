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

    public static Random random = new Random();

    public SocialGame(int h, int w) {
        //super("SGS");
        this.height = h;
        this.width = w;
        MouseMotionAdapter mma = new MouseMotionAdapter() { //mma implementa mouse motion listener, che è preferito a mma solo quando si usano entrambi i metodi
            @Override
            public void mouseDragged(MouseEvent e) { //Aggiungere timer.stop() e timer.start()
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                isLiving = !isLiving;
                repaint(); //Dopo che cambio lo stato della cella devo colorarla o cancellarla
            }
        };
        this.addMouseMotionListener(mma); //Se uso il mouse invoco i metodi mouseDragged o mouseMoved, vedi su
        isLiving = random.nextBoolean(); //Configurazione iniziale casuale
    }

    public boolean isAlive(int neighbors) { //se la cella viva in questione ha 2 o 3 vicini rimane viva, sennò muore. Se la cella non è viva, "nasce" con 3 vicini
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
    } //Faccio nascere una cella

    public boolean isLiving() {
        return this.isLiving;
    } //Ritorna lo stato della cella: alive (true) o false

    @Override
    public void paintComponent(Graphics g) { //Colora le celle vive
        super.paintComponent(g);
        if (this.isLiving) {
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    public void reset() {
        count = 0;
    } //Metodi per il contatore count, non ancora implementato
    public void inc() {
        count++;
    }
    public int getValue() {
        return count;
    }

    public static void main(String[] args) {
        final int height = 40, width = 60;
        final SocialGame[][] society = new SocialGame[height][width]; //Matrice con altezza e larghezza prefissate
        //final JDesktopPane desktop = new JDesktopPane(); //??
        //desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        //desktop.setLayout(new BorderLayout());
        final JFrame window = new JFrame("Social Game System"); //Finestra di background RIVEDERE LAYOUT
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Importante, se mancasse questo il processo rimarrebbe aperto dopo l'uscita
        window.setLayout(new BorderLayout()); //Layout con 5 sezioni: su, giù, destra, sinistra e centro
        final JPanel gui = new JPanel(new GridLayout(height, width, 1, 1));// Del pannello possiamo decidere l'altezza, la larghezza e la distanza tra una cella e l'altra
        final JSlider slider = new JSlider(JSlider.VERTICAL, 100, 1100, 400); // Orientamento, valore minimo, valore massimo e valore iniziale
        slider.setMajorTickSpacing(200); //Trattini grandi ogni 200 ms
        slider.setMinorTickSpacing(40); //Trattini piccoli ogni 40 ms
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Hashtable position = new Hashtable();
        position.put(100, new JLabel("100 ms")); //Stampo alcuni numeri sullo slider
        position.put(300, new JLabel("300 ms"));
        position.put(500, new JLabel("500 ms"));
        position.put(700, new JLabel("700 ms"));
        position.put(900, new JLabel("900 ms"));
        position.put(1100, new JLabel("1100 ms"));
        slider.setLabelTable(position);
        slider.setVisible(true);
        window.add(slider, BorderLayout.LINE_START);          //Aggiungo lo slider a sinistra
        window.getContentPane().add(gui, BorderLayout.CENTER); //e la gui al centro (non funziona bene)
        window.setVisible(true);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    SocialGame cell = new SocialGame(i, j); //Creo la board cella per cella
                    cell.setPreferredSize(new Dimension(9, 9)); // (larghezza, altezza)
                    gui.add(cell); //Aggiungo la cella
                    society[i][j] = cell;
                }
            }
                                                      //In attesa che l'evento ae accada. In quanto ActionListener, in tal caso verrebbe invocato il metodo actionPerformed,
            ActionListener al = (ActionEvent ae) -> { //che non uso. Invece uso "al" con il timer per aggiornare la gui ogni "delay" millisecondi. Vedi più giù le linee gui.repaint() e new Timer(delay, al)
                boolean[][] living = new boolean[height][width]; //Matrice delle celle vive. Da qua in poi conto i vicini vivi, considerando tutte e 8 le celle attorno a quella considerata
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int top = (j > 0 ? j - 1 : width - 1); // if j > 0 then top = j - 1, else j = width - 1
                        int bottom = (j < width - 1 ? j + 1 : 0);
                        int left = (i > 0 ? i - 1 : height - 1);
                        int right = (i < height - 1 ? i + 1 : 0);
                        int neighbors = 0;
                        if (society[i][top].isLiving()) { //Su
                            neighbors++;
                        }
                        if (society[i][bottom].isLiving()) {
                            neighbors++;
                        }
                        if (society[left][top].isLiving()) { //In alto a sx
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
                        if (society[right][top].isLiving()) { //In alto a dx
                            neighbors++;
                        }
                        if (society[right][bottom].isLiving()) {
                            neighbors++;
                        }
                        living[i][j] = society[i][j].isAlive(neighbors); //Qua scelgo il destino della cella, in base al numero dei vicini vivi
                    }
                }
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                       society[i][j].setAlive(living[i][j]); //Se living[i][j] è true, nasce una cella nella posizione i, j (quindi isLiving = alive)
                    }
                }
                gui.repaint(); //Aggiorno lo stato della board
            };

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) { //Se uso lo slider, il delay cambia. Timer da rifare
                    delay = slider.getValue();
                }
            }
        });
            Timer timer = new Timer(delay, al); // Millisecondi tra un ciclo ed il successivo
            timer.start(); //Parte il timer
            JOptionPane.showMessageDialog(null, gui); //Mostra la board
            timer.stop();
        }
    }