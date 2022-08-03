import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

public class Project extends AudioVideo implements ActionListener {
	private JFrame win;
	private JPanel header;
	private JPanel panel1;
	private JPanel panel2;
	private JButton textFile, image, orderedFileText, extendFile, fxButton, exit;
	private ImageIcon textFileIMG, imageIMG, orderedFileTextIMG, extendFileIMG, fxButtonIMG, exitIMG;
	private JFXPanel fxPanel;
	private JTextArea textArea;
	private JScrollPane scroll;
	private Cursor cursor = new Cursor(Cursor.HAND_CURSOR); 
	private JFileChooser fileChooser;
	private JLabel txt = new JLabel();	
	private ProgressMonitorInputStream ProgressBar;
	private long bb, size;
	
	public void initAndShowGUI() throws Exception{
		win = new JFrame();
		win.setUndecorated(true);
		win.setResizable(true);
		win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		win.setSize(600, 600);
		win.setLocationRelativeTo(null);
		
		textFileIMG = rescale("src\\buttonImages\\txtIcon.png",50, 50);
		imageIMG = rescale("src\\buttonImages\\imageIcon.png", 50, 50);
		orderedFileTextIMG = rescale("src\\buttonImages\\orderedTextIcon.jpg", 50, 50);
		extendFileIMG = rescale("src\\buttonImages\\zipFileIcon.png", 50, 50);
		fxButtonIMG = rescale("src\\buttonImages\\audioVideoIcon.png", 50, 50);
		exitIMG = rescale("src\\buttonImages\\exitIcon.png", 35, 35);
		
		header = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
		        protected void paintComponent(Graphics grphcs) {
					Color color = new Color(100, 50, 200);
					Color color2 = new Color(120, 30, 190);
		            super.paintComponent(grphcs);
		            Graphics2D g2d = (Graphics2D) grphcs;
		            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		            GradientPaint gp = new GradientPaint(0, 0, color, 0, getHeight(), color2);
		            g2d.setPaint(gp);
		            g2d.fillRect(0, 0, getWidth(), getHeight()); 

		       };
		};
		header.setBounds(0, 0, 600, 130);
		header.setLayout(null);
		textFile = new JButton(textFileIMG);
		image = new JButton(imageIMG);
		orderedFileText = new JButton(orderedFileTextIMG);
		extendFile = new JButton(extendFileIMG);
		fxButton = new JButton(fxButtonIMG);
		exit = new JButton(exitIMG);
		
		textFile.setContentAreaFilled(false);
		textFile.setBorderPainted(false);
		
		image.setContentAreaFilled(false);
		image.setBorderPainted(false);
		
		orderedFileText.setContentAreaFilled(false);
		orderedFileText.setBorderPainted(false);
		
		extendFile.setContentAreaFilled(false);
		extendFile.setBorderPainted(false);
		
		fxButton.setContentAreaFilled(false);
		fxButton.setBorderPainted(false);
		
		exit.setContentAreaFilled(false);
		exit.setBorderPainted(false);
			
		panel1 = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
		        protected void paintComponent(Graphics grphcs) {
					Color color = new Color(112, 30, 230);
					Color color2 = new Color(137, 37, 62);
		            super.paintComponent(grphcs);
		            Graphics2D g2d = (Graphics2D) grphcs;
		            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		            GradientPaint gp = new GradientPaint(0, 0, color, 0, getHeight(), color2);
		            g2d.setPaint(gp);
		            g2d.fillRect(0, 0, getWidth(), getHeight()); 

		       };
		};
		panel1.setLayout(null);
		
		panel2 = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
		        protected void paintComponent(Graphics grphcs) {
					Color color = new Color(211, 204, 227);
					Color color2 = new Color(233, 228, 240);
		            super.paintComponent(grphcs);
		            Graphics2D g2d = (Graphics2D) grphcs;
		            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		            GradientPaint gp = new GradientPaint(0, 0, color, 0, getHeight() / 2, color2);
		            g2d.setPaint(gp);
		            g2d.fillRect(0, 0, getWidth(), getHeight()); 

		       };
		};;
		panel2.setBounds(56, 160, 490, 396);
		panel2.setBackground(new Color(204, 157, 255));
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		panel2.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createLineBorder(Color.BLACK, 5)));
		panel2.setLayout(new BorderLayout());
		
		textFile.setBounds(50, 65, 50, 50);
		image.setBounds(110, 65, 50, 50);
		orderedFileText.setBounds(170, 65, 50, 50);
		extendFile.setBounds(230, 65, 50, 50);
		fxButton.setBounds(290, 65, 50, 50);
		exit.setBounds(540, 20, 35, 35);
		
		textFile.addActionListener(this);
		image.addActionListener(this);
		orderedFileText.addActionListener(this);
		extendFile.addActionListener(this);
		fxButton.addActionListener(this);
		exit.addActionListener(this);
		
		textFile.setCursor(cursor);
		image.setCursor(cursor);
		orderedFileText.setCursor(cursor);
		extendFile.setCursor(cursor);
		fxButton.setCursor(cursor);
		exit.setCursor(cursor);
		
		textFile.setToolTipText("Ficheiro texto");
		image.setToolTipText("Imagem");
		orderedFileText.setToolTipText("Ficheiro texto ordenado");
		extendFile.setToolTipText("Ficheiro ZIP, Jar, leitura de outro tipo de ficheiro");
		fxButton.setToolTipText("Audio/Video");
		exit.setToolTipText("Sair");
		
		header.add(textFile);
		header.add(image);
		header.add(orderedFileText);
		header.add(extendFile);
		header.add(fxButton);
		header.add(exit);
		
		textArea = new JTextArea();
		textArea.setOpaque(false);
		textArea.setBackground(new Color(0, 0, 0, 0));
		textArea.setBounds(56, 180, 490, 376);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 20, 20, 5)));
		scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getViewport().setOpaque(false);
		scroll.setOpaque(false);
		panel2.add(scroll);
		
		fxPanel = new JFXPanel();
		fxPanel.setBounds(56, 160, 490, 396);
		fxPanel.setBackground(new Color(204, 157, 255));
		fxPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createLineBorder(Color.BLACK, 5)));
		fxPanel.setLayout(new BorderLayout());
		fxPanel.setVisible(false);
		
		modifyDrop();
		
		win.add(header);
		win.add(fxPanel);
		win.add(panel2);
		win.add(panel1);
		win.setVisible(true);
	}
	
	public void ficheiroTexto(String path) throws IOException{
		Path Path = null;
		File f;
		if(path == null) {
			fileChooser = new JFileChooser("src//resources");
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("txt", "txt");
			fileChooser.addChoosableFileFilter(filtro);
			int r = fileChooser.showOpenDialog(null);
			if(r != JFileChooser.APPROVE_OPTION)return;
			f = fileChooser.getSelectedFile();		
			Path = Paths.get(f.getParent(), f.getName());
		}else {
			Path = Paths.get(path);
		}
		
		try(Stream<String> linha = Files.lines(Path)){
			panelRefresh(0);
			linha.forEach(s -> textArea.append(s + "\n"));
		}catch(IOException | UncheckedIOException e) {
			shakeButton(0, textFile);
			JOptionPane.showMessageDialog(
			        null, "Error in opening the file", "Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void abrirImagem(String path) throws IOException{
		Path Path;
		if(path == null) {
			fileChooser = new JFileChooser("src\\resources");
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Image file", "png", "jpg", "gif");
			fileChooser.addChoosableFileFilter(filtro);
			
			int r = fileChooser.showOpenDialog(null);
			if(r != JFileChooser.APPROVE_OPTION)return;
			
			File f = fileChooser.getSelectedFile();
			
			Path = Paths.get(f.getParent(), f.getName());
		}else{
			Path = Paths.get(path);
		}
		
		Image newImg, img = null;
		InputStream imgInput = new BufferedInputStream(new FileInputStream(Path.toString()));
		
		try {
			img = ImageIO.read(imgInput);
			newImg = img.getScaledInstance(490, 396, java.awt.Image.SCALE_SMOOTH);
			panelRefresh(0x90);
			txt.setIcon(new ImageIcon(newImg));
			panel2.add(txt, BorderLayout.CENTER);
			panel2.validate();
			panel2.repaint();
		}catch(NullPointerException e) {
			shakeButton(1, image);
			JOptionPane.showMessageDialog(
			        null, "Error in opening the image", "Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public ImageIcon rescale(String path, int xPixel, int yPixel) throws IOException{
		Image newImg, img = null;
		InputStream imgInput = new BufferedInputStream(new FileInputStream(path));
		
		img = ImageIO.read(imgInput);
		newImg = img.getScaledInstance(xPixel, yPixel, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newImg);
	}
	
	public void textOrdered(String path) throws IOException{
		Path Path = null;
		if(path == null) {
			fileChooser = new JFileChooser("src\\resources");
			FileNameExtensionFilter filtro = new FileNameExtensionFilter(".txt", "txt");
			fileChooser.addChoosableFileFilter(filtro);
			int r = fileChooser.showOpenDialog(null);
			if(r != JFileChooser.APPROVE_OPTION)return;
			
			File f = fileChooser.getSelectedFile();
			
			Path = Paths.get(f.getParent(), f.getName());
		}else {
			Path = Paths.get(path);
		}
		try(Stream<String> linha = Files.lines(Path)){
			panelRefresh(0);
			linha.sorted().forEach(s -> textArea.append(s + "\n"));
		}catch(IOException | UncheckedIOException e) {
			shakeButton(2, orderedFileText);
			JOptionPane.showMessageDialog(
			        null, "Error in opening the text file", "Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void readOtherFiles(String path) throws IOException{
		Path Path = null;
		if(path == null) {
			fileChooser = new JFileChooser("src\\resources");
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Zip & Jar & others", "zip", "rar", "jar");
			fileChooser.addChoosableFileFilter(filtro);
			int r = fileChooser.showOpenDialog(null);
			if(r != JFileChooser.APPROVE_OPTION)return;
			
			File f = fileChooser.getSelectedFile();
			
			Path = Paths.get(f.getParent(), f.getName());
		}else {
			Path = Paths.get(path);
		}
		
		ProgressMonitor(Path.toString());
	}
	
	//tenho de mudar isto fazer de outra maneira a logica
	private void panelRefresh(int i) {
		panel2.removeAll();
		fxPanel.removeAll();
		if(i == 0) {
			textArea = new JTextArea();
			textArea.setOpaque(false);
			textArea.setBackground(new Color(0, 0, 0));
			textArea.setBounds(56, 180, 490, 376);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			Border border=BorderFactory.createLineBorder(new Color(0,0,0), 2);
			textArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 20, 20, 5)));
			scroll=new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			panel2.add(scroll);
			panel2.validate();
			panel2.repaint();
		}else {
			txt = new JLabel();
		}
		modifyDrop();
	}
	
	public void shakeButton(int i, JButton button) {
		Point point = button.getLocation();
		final int delay = 30;
			new Thread(() -> {
				for(int k = 0; k <= 8; k++){
					try {
						moveButton(new Point(point.x + 2, point.y), i);
						Thread.sleep(delay);
						moveButton(point, i);
				        Thread.sleep(delay);
				        moveButton(new Point(point.x - 2, point.y), i);
				        Thread.sleep(delay);
				        moveButton(point, i);
				        Thread.sleep(delay);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}).start();
	}
	
	private void moveButton(Point p, int i) {
		SwingUtilities.invokeLater(() -> {
			if(i == 0)
				textFile.setLocation(p);
			else if(i == 1)
				image.setLocation(p);
			else if(i == 2)
				orderedFileText.setLocation(p);
			else if(i == 3)
				extendFile.setLocation(p);
			else if(i == 4)
				fxButton.setLocation(p);
			else if(i == 5)
				exit.setLocation(p);
		});
	}
	
	public void modifyDrop() {
		TransferHandler th = new TransferHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean canImport(JComponent comp, DataFlavor[] tranferFlavors) {
				textArea.setCursor(new Cursor(Cursor.HAND_CURSOR));
				txt.setCursor(new Cursor(Cursor.HAND_CURSOR));
				return true;
			}
			
			@Override
			public boolean importData(JComponent comp, Transferable t) {
				try {
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
					for(File file : files) {
						final String Extension = getExtension(file.getName().toString()).toString();
						if(Extension.equals("Optional[txt]")) {
							panel2.setVisible(true);
							fxPanel.setVisible(false);
							fxPanel.removeAll();
							fxPanel.revalidate();
							fxPanel.repaint();
							String[] options = {"Read File", "Order File"};
							int value = JOptionPane.showOptionDialog(null, "which option you would like to use?", "Click Button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
							if(value == 0) ficheiroTexto(file.getAbsolutePath().toString());
							else if(value == 1) textOrdered(file.getAbsolutePath().toString());
						}else if(Extension.equals("Optional[png]") || Extension.equals("Optional[jpg]") || Extension.equals("Optional[gif]")) {
							panel2.setVisible(true);
							fxPanel.setVisible(false);
							fxPanel.removeAll();
							fxPanel.revalidate();
							fxPanel.repaint();
							abrirImagem(file.getAbsolutePath());
						}else if(Extension.equals("Optional[zip]") || Extension.equals("Optional[jar]") || Extension.equals("Optional[rar]")) {
							panel2.setVisible(true);
							fxPanel.setVisible(false);
							fxPanel.removeAll();
							fxPanel.revalidate();
							fxPanel.repaint();
							readOtherFiles(file.getAbsolutePath());
						}else if(Extension.equals("Optional[mp3]")) {
							fxPanel.removeAll();
							fxPanel.revalidate();
							fxPanel.repaint();
							panel2.setVisible(false);
							fxPanel.setVisible(true);
							Platform.runLater(() -> {
								initFX(true, file.getAbsolutePath(), file.getName());
							});
						}else if(Extension.equals("Optional[mp4]")) {
							fxPanel.removeAll();
							fxPanel.revalidate();
							fxPanel.repaint();
							panel2.setVisible(false);
							fxPanel.setVisible(true);
							Platform.runLater(() -> {
								initFX(false, file.getAbsolutePath(), null);
							});
						}else {
							shakeButton(0, textFile);
							shakeButton(1, image);
							shakeButton(2, orderedFileText);
							shakeButton(3, extendFile);
							shakeButton(4, fxButton);
							JOptionPane.showMessageDialog(
							        null, "File is not compatible", "Failure", JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		};
		textArea.setTransferHandler(th);
		txt.setTransferHandler(th);
		fxPanel.setTransferHandler(th);
	}
	
	public Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	public void ProgressMonitor(String path) throws IOException {
		new Thread(() -> {
			try {
				ProgressBar = new ProgressMonitorInputStream(null, "Reading..." + path, new FileInputStream(path));
				size = path.length();
				if(size < 1024) bb = 24; else bb = 64; 
				while(ProgressBar.available() > 0) {
					byte[] data = new byte[(int)bb];
					ProgressBar.read(data);
				}
				ProgressBar.close();
				try(ZipFile file = new ZipFile(path.toString())){
					panelRefresh(0);
					file.stream().forEach(s -> textArea.append(s + "\n"));
				}catch(IOException e) {
					shakeButton(3, extendFile);
					JOptionPane.showMessageDialog(
					        null, "Error in opening the zip file", "Failure", JOptionPane.ERROR_MESSAGE);
				}
			}catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File not found!" + path, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {}
		}).start();
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		if(ev.getSource() == exit) {
			int confirmation = JOptionPane.showConfirmDialog(null, "Exit Program?", "EXIT", JOptionPane.YES_NO_OPTION);
			if(confirmation == JOptionPane.YES_OPTION)
				System.exit(0);
		}
		if(ev.getSource() == textFile) {
			panel2.setVisible(true);
			fxPanel.setVisible(false);
			fxPanel.removeAll();
			fxPanel.revalidate();
			fxPanel.repaint();
			try {
				ficheiroTexto(null);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
				        null, "Error in opening the file", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(ev.getSource() == image) {
			panel2.setVisible(true);
			fxPanel.setVisible(false);
			fxPanel.removeAll();
			fxPanel.revalidate();
			fxPanel.repaint();
			try {
				abrirImagem(null);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
				        null, "Error in opening the image", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(ev.getSource() == orderedFileText) {
			panel2.setVisible(true);
			fxPanel.setVisible(false);
			fxPanel.removeAll();
			fxPanel.revalidate();
			fxPanel.repaint();
			try {
				textOrdered(null);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
				        null, "Error in opening the file", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(ev.getSource() == extendFile) {
			panel2.setVisible(true);
			fxPanel.setVisible(false);
			fxPanel.removeAll();
			fxPanel.revalidate();
			fxPanel.repaint();
			try {
				readOtherFiles(null);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
				        null, "Error in opening the file", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(ev.getSource() == fxButton) {
			File f;
			fileChooser = new JFileChooser("src\\resources");
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Fx files", "mp3", "mp4");
			fileChooser.addChoosableFileFilter(filtro);
		
			int r = fileChooser.showOpenDialog(null);	
			if(r != JFileChooser.APPROVE_OPTION)
				return;
			
			f = fileChooser.getSelectedFile();		
			String Extension = getExtension(f.getName().toString()).toString();
			if(Extension.equals("Optional[mp3]")){
				fxPanel.removeAll();
				fxPanel.revalidate();
				fxPanel.repaint();
				panel2.setVisible(false);
				fxPanel.setVisible(true);
				Platform.runLater(() -> {
					initFX(true, f.getAbsolutePath(), f.getName());
				});
			}else if(Extension.equals("Optional[mp4]")) {
				fxPanel.removeAll();
				fxPanel.revalidate();
				fxPanel.repaint();
				panel2.setVisible(false);
				fxPanel.setVisible(true);
				Platform.runLater(() -> {
					initFX(false, f.getAbsolutePath(), null);
				});
			}else {
				shakeButton(4, fxButton);
				JOptionPane.showMessageDialog(
				        null, "Error in opening the file", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void initFX(boolean isAudio, String path, String name) {
		Scene scene = null;
		try {
			//System.out.println(path);
			scene = createScene(isAudio, path, name);
			fxPanel.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
			        null, "Error loading the scene", "Failure", JOptionPane.ERROR_MESSAGE);
		}
	}
}
