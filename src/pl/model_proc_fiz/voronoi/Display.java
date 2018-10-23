package pl.model_proc_fiz.voronoi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class Display extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Plansza panel_plansza;
	private JSpinner spinner = new JSpinner();

	public Display() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				panel_plansza.refresh();
			}
		});
		setTitle("Voronoi");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 657, 648);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JButton btnStartEuclidean = new JButton("START - EUCLIDEAN");
		btnStartEuclidean.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				panel_plansza.start(1);
			}
		});
		panel.add(btnStartEuclidean);
		
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		panel.add(separator);
		
		JButton btnStartManhatten = new JButton("START - MANHATTEN");
		btnStartManhatten.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				panel_plansza.start(2);
			}
		});
		panel.add(btnStartManhatten);
		
		JSeparator separator_1 = new JSeparator();
		panel.add(separator_1);
		
		JLabel lblZiarna = new JLabel("Ziarna");
		panel.add(lblZiarna);
		
		spinner.setModel(new SpinnerNumberModel(20, 3, 100, 1));
		panel.add(spinner);
		
		JButton btnDodajZiarna = new JButton("DODAJ ZIARNA");
		btnDodajZiarna.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				panel_plansza.setPunkty( (Integer) spinner.getValue() );
				panel_plansza.random(2);
			}
		});
		panel.add(btnDodajZiarna);
		
		panel_plansza = new Plansza(300, 200);

		getContentPane().add(panel_plansza, BorderLayout.CENTER);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(panel_plansza, popupMenu);
		
		JMenuItem mntmResetPlanszy = new JMenuItem("Reset planszy");
		mntmResetPlanszy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				panel_plansza.clean();
			}
		});
		popupMenu.add(mntmResetPlanszy);
	    new Thread(panel_plansza).start();  
		

	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
