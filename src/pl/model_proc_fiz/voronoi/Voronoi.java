package pl.model_proc_fiz.voronoi;

import javax.swing.UIManager;

public class Voronoi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			Thread t = new Thread(new Runnable() {
				public void run() {
					Display frame = new Display();
					frame.setVisible(true);
				}
			});
			t.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
