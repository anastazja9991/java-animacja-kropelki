package UdemyJava;

import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AnimacjaKropelki extends JFrame {

    public AnimacjaKropelki(){
        initComponents();
    }

    public void initComponents(){

        this.setTitle("Animacja kropelki");
        this.setBounds(250,300,300,250);
        panelAnimacji.setBackground(Color.GRAY);
        JButton start = (JButton)panelButtonow.add(new JButton("Start"));

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                startAnimation();
            }
        });

        JButton usun = (JButton)panelButtonow.add(new JButton("Usuń"));

        usun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                stopAnimation();
            }
        });

        this.getContentPane().add(panelAnimacji);
        this.getContentPane().add(panelButtonow, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(3);
    }
    public void startAnimation(){

        panelAnimacji.addKropelka();
    }

    public void stopAnimation(){

        panelAnimacji.stop();
    }

    private JPanel panelButtonow = new JPanel();
    private PanelAnimacji panelAnimacji = new PanelAnimacji();

    public static void main(String[] args) {

        new AnimacjaKropelki().setVisible(true);
    }

    class PanelAnimacji extends JPanel{

        public void addKropelka(){

            listaKropelek.add(new Kropelka());
            watek = new Thread(grupaWatkow, new KropelkaRunnable((Kropelka)(listaKropelek.get(listaKropelek.size()-1))));
            watek.start();

            grupaWatkow.list();

        }

        public void stop() {

            grupaWatkow.interrupt();
        }


        public void paintComponent(Graphics g){

            super.paintComponent(g);

            for(int i = 0; i < listaKropelek.size(); i++){
                g.drawImage(Kropelka.getImg(), ((Kropelka)listaKropelek.get(i)).x, ((Kropelka)listaKropelek.get(i)).y, null );

            }
        }

        ArrayList listaKropelek = new ArrayList();
        JPanel ten = this;
        Thread watek;
        ThreadGroup grupaWatkow = new ThreadGroup("Grupa Kropelek");

        public class KropelkaRunnable implements Runnable {

            public KropelkaRunnable(Kropelka kropelka) {

                this.kropelka = kropelka;
            }

            @Override
            public void run() {

                try {

                while (!Thread.currentThread().isInterrupted()) {

                        this.kropelka.ruszKropelka(ten);
                        repaint();
                        //obslugujemy wątek(usypiamy)
                        Thread.sleep(1);
                    }
                }
                catch(InterruptedException e){
                    System.out.println(e.getMessage());
                    listaKropelek.clear();
                    repaint();
                }
            }
            Kropelka kropelka;
        }
    }
}

class Kropelka{

    public static Image getImg(){

        return Kropelka.kropelka;
    }

    public void ruszKropelka(JPanel pojemnik){

        Rectangle granicePanelu = pojemnik.getBounds();

        x+=dx;
        y+=dy;

        if(y + yKropelki >= granicePanelu.getMaxY()){

            y = (int)(granicePanelu.getMaxY() - yKropelki);
            dy = -dy;
        }
        if(x + xKropelki >= granicePanelu.getMaxX()){

            x = (int)(granicePanelu.getMaxX() - xKropelki);
            dx = -dx;
        }
        if(y < granicePanelu.getMinY()){

            y = (int)granicePanelu.getMinY();
            dy = -dy;
        }

        if(x < granicePanelu.getMinX()){

            x = (int)granicePanelu.getMinX();
            dx = -dx;
        }
    }

    public static Image kropelka = new ImageIcon("src/UdemyJava/Ramki/ikonki/kropelka.gif").getImage();

    int x = 0;
    int y = 0;
    int dx = 1;
    int dy = 1;
    int xKropelki = kropelka.getWidth(null);
    int yKropelki = kropelka.getHeight(null);
}
