package gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class igraj2048 {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();

            int nastavakIgre = JOptionPane.showConfirmDialog(null, "Da li želite nastaviti spašenu igru?", "Nastavi igru", JOptionPane.YES_NO_OPTION);

            if (nastavakIgre == JOptionPane.YES_OPTION) {
                igrajSpasenuIgru(gui);
            }
        });
    }

    private static void igrajSpasenuIgru(GUI gui) {
        gui.getIgrica().ucitajStanjeIgre();

        gui.updateGUI();
    }

}
