import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

class PannelloGriglia extends JPanel{

    Gioco gioco;

    private Utente[][] griglia;

    MouseListener gestoreEventi= new MouseListener() {

        boolean trascinamento=false;

        @Override
        public void mouseClicked(MouseEvent e) {
            Utente u= ((Utente) e.getSource());
            u.setStato(u.nuovoStato());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            gioco.pausa();
            trascinamento=true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            trascinamento=false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (trascinamento == true) {
                Utente u= ((Utente) e.getSource());
                u.setStato(u.nuovoStato());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    };

    public PannelloGriglia(int righe, int colonne){
        gioco= new GiocoDellaVita();
        this.setSize(600,600);
        setLayout(new GridLayout(righe,colonne,1,1));
        griglia = new Utente[righe][colonne];
        inizializzaGriglia();

    }


    public void inizializzaGriglia(){
        Color[] colori= {Color.WHITE,Color.BLACK};
        for(int i=0;i<griglia.length;i++) {
            for (int j = 0; j < griglia[0].length; j++) {
                Utente u = new Utente(i, j, 0,2, colori);
                u.addMouseListener(gestoreEventi);
                griglia[i][j] = u;
                add(u);
            }
        }

        for(int i=0;i<griglia.length;i++){
            for(int j=0;j<griglia[0].length;j++){
                griglia[i][j].setVicini(griglia);
            }
        }
        aggiorna();
    }

    public void impostaGioco(Gioco gioco){
        this.gioco= gioco;
        for(int i=0;i<griglia.length;i++) {
            for (int j = 0; j < griglia[0].length; j++) {
                griglia[i][j].setNumStati(gioco.getNumStati());
                griglia[i][j].setColori(gioco.getColori());
            }
        }

        for(int i=0;i<griglia.length;i++){
            for(int j=0;j<griglia[0].length;j++){
                griglia[i][j].setVicini(griglia);
            }
        }
        aggiorna();
    }

    public void svuotaGriglia(){
        for(int i=0;i<griglia.length;i++) {
            for (int j = 0; j < griglia[0].length; j++) {
                griglia[i][j].setStato(0);
            }
        }
    }

    public String toString(){
        String stringa="";
        for(int i=0;i<griglia.length;i++) {
            for (int j = 0; j < griglia[0].length; j++) {
                stringa += griglia[i][j].toString() +" | ";
            }
            stringa ="\n";
        }
        return stringa;
    }

    public Utente[][] getGrigliaUtenti(){
        return  griglia;
    }

    public void aggiorna(){
        for(int i=0;i<griglia.length;i++) {
            for (int j = 0; j < griglia[0].length; j++) {
                griglia[i][j].repaint();
            }
        }
    }

    public void LWSS() {
        int i = griglia.length/2, j = griglia[0].length/2;
        griglia[i][j].setStato(1);
        griglia[i][j+1].setStato(1);
        griglia[i][j-1].setStato(1);
        griglia[i][j-2].setStato(1);
        griglia[i-1][j-2].setStato(1);
        griglia[i-2][j-2].setStato(1);
        griglia[i-3][j-1].setStato(1);
        griglia[i-1][j+2].setStato(1);
        griglia[i-3][j+2].setStato(1);
    }

    public void Weekender() {
        int i = griglia.length/2, j = griglia[0].length/2;
        griglia[i][j].setStato(1);
        griglia[i][j+1].setStato(1);
        griglia[i][j-1].setStato(1);
        griglia[i][j-2].setStato(1);
        griglia[i+1][j+1].setStato(1);
        griglia[i+1][j-1].setStato(1);
        griglia[i+1][j-2].setStato(1);
        griglia[i+1][j].setStato(1);
        griglia[i+2][j+2].setStato(1);
        griglia[i+2][j+3].setStato(1);
        griglia[i+2][j+4].setStato(1);
        griglia[i+2][j+5].setStato(1);
        griglia[i+2][j-3].setStato(1);
        griglia[i+2][j-4].setStato(1);
        griglia[i+2][j-5].setStato(1);
        griglia[i+2][j-6].setStato(1);
        griglia[i+4][j+3].setStato(1);
        griglia[i+4][j-4].setStato(1);
        griglia[i+5][j+1].setStato(1);
        griglia[i+5][j+2].setStato(1);
        griglia[i+5][j-2].setStato(1);
        griglia[i+5][j-3].setStato(1);
        griglia[i][j+5].setStato(1);
        griglia[i][j-6].setStato(1);
        griglia[i-1][j+6].setStato(1);
        griglia[i-1][j-7].setStato(1);
        griglia[i-2][j+6].setStato(1);
        griglia[i-2][j-7].setStato(1);
        griglia[i-3][j+5].setStato(1);
        griglia[i-3][j+7].setStato(1);
        griglia[i-3][j-6].setStato(1);
        griglia[i-3][j-8].setStato(1);
        griglia[i-4][j+6].setStato(1);
        griglia[i-4][j-7].setStato(1);
        griglia[i-5][j+6].setStato(1);
        griglia[i-5][j-7].setStato(1);
    }

    public void impostaCasuale(){
        Random random= new Random();
        if (gioco instanceof GiocoDellaVita){
            for(int i=0;i<griglia.length;i++) {
                for (int j = 0; j < griglia[0].length; j++) {
                    int stato = random.nextInt(gioco.getNumStati());
                    griglia[i][j].setStato(stato);
                }
            }
        }
        else if (gioco instanceof GiocoWaTor){
            int perc_mare=55;
            int perc_pesci=40;
            for(int i=0;i<griglia.length;i++) {
                for (int j = 0; j < griglia[0].length; j++) {
                    int num= random.nextInt(101);
                    if(num < perc_mare)
                        griglia[i][j].setStato(0);
                    if(num >= perc_mare && num < perc_mare+perc_pesci)
                        griglia[i][j].setStato(1);
                    if(num >= perc_mare+perc_pesci)
                        griglia[i][j].setStato(2);
                }
            }
        }

    }

}