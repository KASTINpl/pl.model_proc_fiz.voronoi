package pl.model_proc_fiz.voronoi;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class Plansza extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private int pixelSize = 5;
	private int width = 600;
	private int height = 500;
	private int tabSizeX = this.width / this.pixelSize;
	private int tabSizeY = this.height / this.pixelSize;
	private P[][] P = new P[this.tabSizeX][this.tabSizeY];
	
	private List<P> kolory = new ArrayList<P>();
	
	private Integer punkty = 20;
	
	/**
	 * 1 - EUCLIDEAN
	 * 2 - MANHATTEN
	 */
	private Integer startMode = 1; 

	/**
	 * uruchomienie planszy w nowym watku, inicjalizacja iterakcji
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("Odpalam Plansza! ");
		refresh();
	}
	/**
	 * inicjalizacja macierzy oraz planszy
	 */
	public Plansza(int x, int y) {
		this.width = x;
		this.height = y;

		setPreferredSize(new Dimension(x, y));

		//====
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				this.P[i][j] = new P();
			}//j
		}//i


	}//--
	
	public void start(Integer _sm) {
		this.startMode = _sm;
		System.out.println("start("+_sm+")\n");

		//====
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				
				Double dist = Double.MAX_VALUE;
                P min = null;
                
				//====
				for (int x = 0; x < this.tabSizeX; x++) {
					for (int y = 0; y < this.tabSizeY; y++) {
						if ( this.P[x][y].act != 1 ) continue;
						
						Double d = Double.MAX_VALUE;

	                    switch (this.startMode) {
	                        case 1:
	                        	Point2D p2d = new Point2D.Double(i,j);
	                            d = p2d.distance(x, y);
	                            break;

	                        case 2:
	                            d = (double) (Math.abs(i - x) + Math.abs(j - y));
	                            break;
	                    }

	                    if (d > dist)
	                        continue;

	                    dist = d;
	                    min = this.P[x][y];
					}//j
				}//i
				
				if ( min instanceof P ) {//p
					this.P[i][j].recolor(min);
				}//p
				else {
					System.out.print("bug on P("+i+","+j+")\n");
				}
			}//j
		}//i
		
		//====
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				
				if ( this.P[i][j].act == 1 ) {
					this.P[i][j].recolor(0,0,0);
				}
				
			}//j
		}//i
		
		refresh();
	}
    
	public void setPunkty(Integer _p) {
		this.punkty = _p;
	}
	public Integer getPunkty() {
		return this.punkty;
	}

	/**
	 * iterakcja programu
	 */
	public void refresh() {
		
		resizeP();
		// ===== rysuj =========
		repaint();
	}//--



	/**
	 * wyczysc macierz
	 */
	public void clean() {
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				this.P[i][j].set(0);
			}
		}
		refresh();
	}

	/**
	 * rozmieszczenie zarodkow na macierzy
	 */
	@SuppressWarnings("unused")
	public void random(Integer _mode) {
		System.out.print("Losuje punkty! ("+this.getPunkty()+" sztuk)");
		
		clean();
		
		Random r = new Random();
		int x, y;
		int srodek_x = this.tabSizeX / 2;
		int srodek_y = this.tabSizeY / 2;

		switch (_mode) {
		case 1: // losowe rownomierne
			double px,py;
			
			py = Math.sqrt(this.tabSizeY*this.punkty/this.tabSizeX);
			px = ((double)this.punkty)/py;
			int x_size = (int)px;
			int y_size = (int)py;
			int x_width = this.tabSizeX/x_size;
			int y_height = this.tabSizeY/y_size;
			srodek_x = x_size / 2;
			srodek_y = y_size / 2;

			//System.out.println("x_size= "+x_size+"; y_size = "+y_size);
			for (int i=0;i<x_size;i++) {
				for(int j=0;j<y_size;j++) {
					int t_x = (i*2+1)*x_width/2, t_y = (j*2+1)*y_height/2;
					
					do {
						this.P[t_x][t_y].set_random(1);
					} while ( this.colorExists(this.P[t_x][t_y],t_x,t_y) );

					kolory.add(new P(this.P[t_x][t_y]));
				}//j
			}//i
			
			break;// 1

		case 2: // losowe przypadkowe
			for (int i=0;i<this.punkty;i++) {
				x = r.nextInt(this.tabSizeX);
				y = r.nextInt(this.tabSizeY);
				this.P[x][y].set_random(1);
				while (this.colorExists(this.P[x][y],x,y)) this.P[x][y].set_random(1); // losuj nowe az do uzyskania unikatu

				kolory.add(new P(this.P[x][y]));
			}
			break;// 3

		}
		
		refresh();
	}// --
	
	/***
	 * sprawdz czy dany kolor wystepuje juz na planszy
	 * 
	 * @param p punkt na planszy
	 * @parm x,y współrzędne punktu (jeśli >=0 pomijaj przy sprawdzaniu koloru)
	 */
	private boolean colorExists(P p, int x, int y) {
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				if (x==i && y==j && x>=0 && y>=0) continue; // nie sprawdzam sama siebie bo sie zapetle ;)
				if (this.P[i][j].colorMatch(p)) return true;
			}//j
		}//i
		return false;
	}

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);

		for (int x = 0; x < this.tabSizeX; x++) {
			for (int y = 0; y < this.tabSizeY; y++) {

				if (this.P[x][y].RR>0 && this.P[x][y].GG>0 && this.P[x][y].BB>0) 
					g.setColor(new Color(this.P[x][y].RR, this.P[x][y].GG,
							this.P[x][y].BB));
				else
				g.setColor(new Color(this.P[x][y].R, this.P[x][y].G,
						this.P[x][y].B));

				g.fillRect(x * this.pixelSize, // position X
						y * this.pixelSize, // position Y
						this.pixelSize, // width
						this.pixelSize // height
				);
			}
		}
	}// g
	

	/**
	 * sprawdzaj czy rozmiar panszy (JPanel) sie zmienil i uaktualij rozmiar macierzy
	 */
	private void resizeP() {

		int w = this.width;
		int h = this.height;

		if (this.getHeight() > 0)
			h = this.getHeight();
		if (this.getWidth() > 0)
			w = this.getWidth();

		this.tabSizeX = this.width / this.pixelSize;
		this.tabSizeY = this.height / this.pixelSize;

		if (h != this.height || w != this.width) { // zmiana rozmiary planszy -  przebuduj tab [t]
			
		
		System.out.print("Zmieniam rozmiar planszy z: " + this.tabSizeX
				+ " x " + this.tabSizeY + ";"); // --------

		P[][] P_new = new P[w / this.pixelSize][h / this.pixelSize];
		for (int i = 0; i < w / this.pixelSize; i++) {
			for (int j = 0; j < h / this.pixelSize; j++) {
				P_new[i][j] = new P();
			}
		}
		// foreach old array
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				if (i >= (w / this.pixelSize) || j >= (h / this.pixelSize))
					continue;
				P_new[i][j] = this.P[i][j];
			}
		}

		// set new size
		this.width = w;
		this.height = h;
		this.tabSizeX = this.width / this.pixelSize;
		this.tabSizeY = this.height / this.pixelSize;

		System.out.print("na: " + this.tabSizeX + " x " + this.tabSizeY
				+ "; \n"); // -------------------------
		// odswiez tab
		this.P = P_new;
		}//[t]
	}

	
}//$
