import javax.swing.SwingUtilities;

public class Main{
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	try {
            		Project window = new Project();
					window.initAndShowGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
	}
}
