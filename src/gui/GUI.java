package gui;

import javax.swing.*;
import java.util.List;


import logika.Igrica;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**Inicijalizira novi objekt klase Igrica i polje labelMatrica.
	  * Postavlja naslov prozora na "2048 Igra".
	  * Postavlja velicinu prozora na 600x400 piksela.
	  * Postavlja akciju za dogadaj zatvaranja prozora, koja poziva metodu zatvoriAplikaciju().
	  * Postavlja raspored komponenata prozora na BorderLayout.
	  * Dodaje dugme "Vrati Nazad" na sjeverni dio prozora i postavlja akciju za vracanje unazad.
	  * Kreira i postavlja matricu labela za prikaz brojeva igre u centru prozora.
	  * Kreira i postavlja tekstualno podrucje za prikaz top rezultata na lijevoj strani prozora.
	  * Postavlja osluskivanje tastature na prozoru.
	  * Postavlja prozor kao fokusabilan kako bi mogao primati događaje tastature.
	  * Poziva metodu updateGUI() kako bi ažurirao prikaz igre.
	  * Postavlja prozor da bude vidljiv.*/
    private Igrica igrica;
    private JLabel[][] labelMatrica;
    private JTextArea topRezultatiTextArea;
    private JButton btnVratiNazad;

    public GUI() {
        igrica = new Igrica();
        labelMatrica = new JLabel[4][4];

        setTitle("2048 Igra");
        setSize(600, 400);  
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                zatvoriAplikaciju();
            }
        });

        setLayout(new BorderLayout());
        
        btnVratiNazad = new JButton("Vrati Nazad");
        add(btnVratiNazad, BorderLayout.NORTH);
        btnVratiNazad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	updateGUI();
                igrica.vratiNaPrethodnoStanje();
                updateGUI();
                return;
            }
        });

        JPanel igraPanel = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                labelMatrica[i][j] = new JLabel("");
                labelMatrica[i][j].setHorizontalAlignment(JLabel.CENTER);
                labelMatrica[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                igraPanel.add(labelMatrica[i][j]);
            }
        }
        add(igraPanel, BorderLayout.CENTER);

        topRezultatiTextArea = new JTextArea(10, 20);
        topRezultatiTextArea.setEditable(false);
        add(new JScrollPane(topRezultatiTextArea), BorderLayout.WEST); 

        addKeyListener(this);
        setFocusable(true);

        updateGUI();

        setVisible(true);
    }
    public Igrica getIgrica() {
        return igrica;
    }


    public void updateGUI() {
    	/**Dohvaca trenutnu matricu brojeva iz igre pomocu igrica.getMatrica().
		  * Prolazi kroz svaki element matrice i postavlja tekst svakog odgovarajućeg JLabel-a 
		  * u prozoru igre.
		  * Omogucava ili onemogucava dugme "Vrati Nazad" (btnVratiNazad) ovisno o 
		  * mogucnosti poteza u svim smjerovima.
		  * Dohvaca listu top rezultata iz igre pomocu igrica.getTopRezultati().
		  * Stvara tekstualni prikaz top rezultata, numerirajuci ih i formatirajuci ih u 
		  * odgovarajuci string.
		  * Postavlja tekstualni prikaz top rezultata u JTextArea (topRezultatiTextArea) na 
		  * lijevoj strani prozora.
		  * Postavlja naslov prozora na "2048 Igra - Bodovi: " s trenutnim brojem bodova iz 
		  * igre (igrica.dajBodove()).*/
        int[][] trenutnaMatrica = igrica.getMatrica();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                labelMatrica[i][j].setText(String.valueOf(trenutnaMatrica[i][j]));
                labelMatrica[i][j].setBackground(bojaZaBroj(Integer.valueOf(trenutnaMatrica[i][j])));
                labelMatrica[i][j].setOpaque(true);
            }
        }
        btnVratiNazad.setEnabled(igrica.jeMogucPotezLijevo() || igrica.jeMogucPotezDesno() || igrica.jeMogucPotezGore() || igrica.jeMogucPotezDole());
        List<Integer> topRezultati = igrica.getTopRezultati();
        StringBuilder topRezultatiText = new StringBuilder("Top Rezultati:\n");
        for (int i = 0; i < topRezultati.size(); i++) {
            topRezultatiText.append(i + 1).append(". ").append(topRezultati.get(i)).append("\n");
        }
        topRezultatiTextArea.setText(topRezultatiText.toString());
        setTitle("2048 Igra - Bodovi: " + igrica.dajBodove());
    }
    
    

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            igrica.potezLijevo();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            igrica.potezDesno();
        } else if (keyCode == KeyEvent.VK_UP) {
            igrica.potezGore();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            igrica.potezDole();
        }

        updateGUI();

        if (!igrica.nemaLegalnihPoteza() || igrica.najvecaVrijednostPlocice() == 2048) {
            JOptionPane.showMessageDialog(this, igrica.najvecaVrijednostPlocice() == 2048 ? "Čestitamo, došli ste do 2048!" : "Nema više legalnih poteza, kraj igre.");
            igrica.azurirajRangListu(igrica.dajBodove()); 
            //System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
    
    private void zatvoriAplikaciju() {
    	/**Ova metoda sluzi za zatvaranje aplikacije, ali prije zatvaranja pita 
    	 * korisnika zeli  li spremiti trenutno stanje igre. Ovisno o odgovoru korisnika, 
    	 * aplikacija se zatvara ili ostaje otvorena. Metoda koristi JOptionPane za prikaz
    	 * dijaloga s opcijama "Da", "Ne" i "Otkaži". Također, kopira trenutno stanje igre 
    	 * prije spremanja kako ne bi utjecala na trenutno stanje igre unutar aplikacije.*/
        int odgovor = JOptionPane.showConfirmDialog(this, "Da li želite spremiti trenutno stanje igre?", "Zatvori aplikaciju", JOptionPane.YES_NO_CANCEL_OPTION);

        if (odgovor == JOptionPane.YES_OPTION) {
            Igrica trenutnoStanje = getIgrica().kopirajStanje();
            trenutnoStanje.spremiStanjeIgre();
        } else if (odgovor == JOptionPane.CANCEL_OPTION) {
            return;
        }

        dispose();
    }
    
    private Color bojaZaBroj(int broj) {
        switch (broj) {
            case 2:
                return new Color(0, 255, 0);  
            case 4:
                return new Color(0, 128, 0);   
            case 8:
                return new Color(50, 205, 50);
            case 16:
                return new Color(34, 139, 34);
            case 32:
                return new Color(100, 255, 0); 
            case 64:
                return new Color(0, 100, 0);  
            case 128:
                return new Color(173, 255, 47);
            case 256:
                return new Color(107, 142, 35);
            case 512:
                return new Color(46, 139, 87);
            case 1024:
                return new Color(60, 179, 113);
            case 2048:
                return new Color(0, 255, 127);
            default:
                return new Color(255, 255, 255); 
        }
    }
    
    
}
