//David Blackstone
//12/29/21
//Sound board program to play,record, and store sound with specific details about each sound

package src;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BoxLayout;

@SuppressWarnings("serial")
public class MainProgram extends JFrame {
	private JPanel contentPane;
	private JPanel panelInScroll;
	private JList<String> jList;
	private File fileToSave = null;
	private FileOutputStream f = null;
	private FileInputStream fi = null;
	private ObjectOutputStream o = null;
	private ObjectInputStream oi = null;
	private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	private ArrayList<String> soundList = new ArrayList<String>();
	private Sound currentSelectedSound = new Sound("", "", "", "", null);
	private JLabel nameLabel = null;
	private JLabel descLabel = null;
	private JLabel dateLabel = null;
	private File soundFile = null;
	private JPanel panel_1 = new JPanel();
	private JTextField recordName;
	private JTextField recordLength;
	private JTextField recordFilePath = new JTextField("");
	private JTextField soundFilePath = new JTextField("");
	private File folder = null;
	private JTextField recordDesc;
	private JTextField recordDate;
	private JTextField recordCreator;

	// program main to call the MainProgram
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainProgram frame = new MainProgram();
					frame.setVisible(true);
				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// this starts the program loading all components
	public MainProgram() {
		// checks for caches file paths(sounds,recordings)
		if (!checkIfExist()) {
			cacheFilePath(soundFilePath.getText(), recordFilePath.getText());
		}

		// window parameters
		// window labels/variables
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/daveb/eclipse-workspace/Hailey/icon.png"));
		setTitle("Merry Xmas <3 :)");
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 590, 413);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		GridLayout grid = new GridLayout(0, 4, 30, 20);
		panelInScroll = new JPanel(grid);
		panelInScroll.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		System.out.println("test1 " + soundList.size());
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.ORANGE);
		tabbedPane.setForeground(Color.DARK_GRAY);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		JPanel recordPanel = new JPanel();
		recordPanel.setBackground(Color.ORANGE);
		tabbedPane.addTab("Record!", null, recordPanel, null);
		recordPanel.setLayout(null);

		panel_1.setBackground(Color.ORANGE);
		tabbedPane.addTab("Play!", null, panel_1, null);
		tabbedPane.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				refreshListing();
			}
		});
		
		panel_1.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 35, 254, 276);

		scrollPane.setViewportView(panelInScroll);
		panel_1.add(scrollPane);
		panelInScroll.setLayout(new BoxLayout(panelInScroll, BoxLayout.X_AXIS));

		JLabel lblChooseASound = new JLabel("Choose a Sound! ");
		lblChooseASound.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		lblChooseASound.setVerticalAlignment(SwingConstants.TOP);
		lblChooseASound.setBounds(64, 10, 151, 22);
		panel_1.add(lblChooseASound);

		JLabel play_name = new JLabel("Name:");
		play_name.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 14));
		play_name.setBounds(324, 188, 45, 13);
		panel_1.add(play_name);

		JLabel play_desc = new JLabel("Description:");
		play_desc.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 14));
		play_desc.setBounds(324, 212, 97, 30);
		panel_1.add(play_desc);

		JLabel play_date = new JLabel("Date:");
		play_date.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
		play_date.setBounds(324, 298, 45, 13);
		panel_1.add(play_date);

		nameLabel = new JLabel("New label");
		nameLabel.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		nameLabel.setText(currentSelectedSound.getName());
		nameLabel.setBounds(379, 189, 131, 13);
		panel_1.add(nameLabel);

		descLabel = new JLabel("New label");
		descLabel.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 10));
		descLabel.setText(currentSelectedSound.getDesc());
		descLabel.setBounds(324, 224, 175, 63);
		panel_1.add(descLabel);

		dateLabel = new JLabel("New label");
		dateLabel.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 10));
		dateLabel.setText(currentSelectedSound.getDate());
		dateLabel.setBounds(379, 300, 147, 13);
		panel_1.add(dateLabel);

		JLabel lblNewLabel_4 = new JLabel("Record Name:");
		lblNewLabel_4.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 11));
		lblNewLabel_4.setBounds(10, 34, 82, 14);
		recordPanel.add(lblNewLabel_4);

		recordName = new JTextField();
		recordName.setBounds(113, 32, 128, 20);
		recordPanel.add(recordName);
		recordName.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Record Length:");
		lblNewLabel_5.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(318, 142, 97, 20);
		recordPanel.add(lblNewLabel_5);

		recordLength = new JTextField();
		recordLength.setToolTipText("seconds");
		recordLength.setBounds(447, 143, 86, 20);
		recordPanel.add(recordLength);
		recordLength.setColumns(10);

		// recording sound button
		JButton recordButton = new JButton("Record!");
		recordButton.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		recordButton.setBounds(318, 32, 215, 103);
		recordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!recordFilePath.getText().equals("")) {
					SoundRecorder recorder = new SoundRecorder(recordFilePath.getText());
					recorder.setName(recordName.getText());
	
					if (recordLength.getText().equals("")) {
						recordLength.setText("5000");
					}
					
					try {
						if(recordLength.getText() != null){
							long length = ((Long.parseLong(recordLength.getText())) * 1000);
							recorder.setRecordLength(length);
							fileToSave = recorder.record();
						}
					}
	
					catch (LineUnavailableException e1) {
						e1.printStackTrace();
					}
					
					catch(NumberFormatException n) {
						System.out.println("length is not a number");
					}
				}
				
				else {
					System.out.println("No record file path selected");
				}
			}
		});

		recordPanel.add(recordButton);

		// record sound labels
		JLabel lblNewLabel_6 = new JLabel("Record a Sound!");
		lblNewLabel_6.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 18));
		lblNewLabel_6.setBounds(212, 7, 234, 14);
		recordPanel.add(lblNewLabel_6);
		
		recordDesc = new JTextField();
		recordDesc.setBounds(113, 63, 128, 38);
		recordPanel.add(recordDesc);
		recordDesc.setColumns(10);
		
		JLabel lblNewLabel_10 = new JLabel("Record Description:");
		lblNewLabel_10.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 11));
		lblNewLabel_10.setBounds(10, 59, 107, 37);
		recordPanel.add(lblNewLabel_10);
		
		JLabel lblNewLabel_11 = new JLabel("Record Date:");
		lblNewLabel_11.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 11));
		lblNewLabel_11.setBounds(10, 111, 97, 14);
		recordPanel.add(lblNewLabel_11);
		
		recordDate = new JTextField();
		recordDate.setBounds(113, 112, 128, 20);
		recordPanel.add(recordDate);
		recordDate.setColumns(10);
		
		JButton recordSubmit = new JButton("Submit!");
		recordSubmit.setBounds(133, 188, 313, 112);
		recordSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound sound = new Sound(recordName.getText(), recordDesc.getText(), recordCreator.getText(),
						recordDate.getText(), fileToSave);
				try {
					System.out.println(writeObjectToFile(sound));
				}

				catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		recordPanel.add(recordSubmit);
		
		JLabel lblNewLabel_12 = new JLabel("Record Creator:");
		lblNewLabel_12.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 11));
		lblNewLabel_12.setBounds(10, 146, 97, 14);
		recordPanel.add(lblNewLabel_12);
		
		recordCreator = new JTextField();
		recordCreator.setBounds(113, 143, 128, 20);
		recordPanel.add(recordCreator);
		recordCreator.setColumns(10);

		// calls the SoundRecorder class to play a selected sound file
		JButton playButton = new JButton("Play!");
		playButton.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		playButton.setBounds(325, 35, 201, 128);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (soundFile != null) {
					Player player = new Player();
					player.play(soundFile);
				}

				else {
					System.out.println("No sound file path selected");
				}
			}
		});

		panel_1.add(playButton);
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 165, 0));
		tabbedPane.addTab("Settings!", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblNewLabel_7 = new JLabel("Recordings file path:");
		lblNewLabel_7.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		lblNewLabel_7.setBounds(29, 11, 160, 56);
		panel_2.add(lblNewLabel_7);

		recordFilePath.setBounds(199, 31, 220, 20);
		panel_2.add(recordFilePath);
		recordFilePath.setColumns(10);

		JLabel lblNewLabel_8 = new JLabel("Sounds file path:");
		lblNewLabel_8.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		lblNewLabel_8.setBounds(29, 63, 160, 20);
		panel_2.add(lblNewLabel_8);

		soundFilePath.setBounds(199, 62, 220, 20);
		panel_2.add(soundFilePath);
		soundFilePath.setColumns(10);

		JLabel lblNewLabel_9 = new JLabel("Settings");
		lblNewLabel_9.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 14));
		lblNewLabel_9.setBounds(242, 0, 118, 20);
		panel_2.add(lblNewLabel_9);

		// save button to save the file paths entered
		// this will create a new file to store the paths in if not exists.
		// it will use a created file path if exists to store recordings and sounds
		// respectively
		JButton savePathButton = new JButton("Save");
		savePathButton.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		savePathButton.setBounds(429, 30, 120, 53);
		savePathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cacheFilePath(soundFilePath.getText(), recordFilePath.getText());
				folder = new File(soundFilePath.getText());
				listFilesForFolder(folder);
				System.out.println("Paths saved!");
			}
		});

		panel_2.add(savePathButton);
	}

	// refreshes the play page with listings
	private void refreshListing() 
	{
		if (!soundFilePath.getText().equals("")) {
			folder = new File(soundFilePath.getText());
			listFilesForFolder(folder);
		}
	}

	// checks the file system if file path files exist
	// returns true or false
	@SuppressWarnings("hiding")
	private boolean checkIfExist() {
		File file1 = new File("soundPath.txt");
		File file2 = new File("recordPath.txt");
		boolean result = false;

		try {
			Scanner sc1 = new Scanner(file1);
			Scanner sc2 = new Scanner(file2);

			if (sc1.hasNextLine()) {
				soundFilePath.setText(sc1.nextLine());
				result = true;
			}

			else {
				System.out.println("no path in file and no path variable set(sound)");
			}

			if (sc2.hasNextLine()) {
				recordFilePath.setText(sc2.nextLine());
				result = true;
			}

			else {
				System.out.println("no path in file and no path variable set(record)");
			}

			sc1.close();
			sc2.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("file(s) not found.");
			result = false;
		}

		catch (IOException e) {
			result = false;
		}

		return result;
	}

	// will save new file path entries to the file system
	// creates the file and writes to them given their respective entries
	private void cacheFilePath(String sPath, String rPath) {
		File file1 = new File("soundPath.txt");
		File file2 = new File("recordPath.txt");
		try {
			file1.createNewFile();
			file2.createNewFile();
			Scanner sc1 = new Scanner(file1);
			Scanner sc2 = new Scanner(file2);

			if (!sc1.hasNextLine() && !sPath.equals("")) {
				Files.writeString(Paths.get("soundPath.txt"), sPath, StandardOpenOption.WRITE);
			}

			else if (sc1.hasNextLine() && !sPath.equals(sc1.nextLine())) {
				Files.writeString(Paths.get("soundPath.txt"), sPath);
			}

			else {
				System.out.println("no path in file and no path variable set(sound)");
			}

			if (!sc2.hasNextLine() && !rPath.equals("")) {
				Files.writeString(Paths.get("recordPath.txt"), rPath, StandardOpenOption.WRITE);
			}

			else if (sc2.hasNextLine() && !rPath.equals(sc2.nextLine())) {
				Files.writeString(Paths.get("recordPath.txt"), rPath);
			}

			else {
				System.out.println("no path in file and no path variable set(record)");
			}

			sc1.close();
			sc2.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("file(s) not found.");
		}

		catch (IOException e) {
		}
	}

	// fills map with files from directory
	public void listFilesForFolder(final File folder) {
		soundMap.clear();
		Sound newSound = null;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			}

			else {
				try {
					newSound = readObjectFromFile(fileEntry);
				}

				catch (URISyntaxException e) {
					e.printStackTrace();
				}

				soundMap.put(newSound.getName(), newSound);
			}
		}

		itemCreator();

		System.out.println("Refreshed Listing from file system.");
	}

	// uses arraylist names to get sound from map
	// creates an item entry you can select
	// creates descriptive text boxes for selected sound
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void itemCreator() {
		List<Sound> list = new ArrayList<Sound>(soundMap.values());
		ArrayList<String> tmp = new ArrayList<String>();
		Iterator<Sound> iter = list.iterator();
		while (iter.hasNext()) {
			Sound newSoundName = ((Sound) iter.next());
			soundList.add(newSoundName.getName());
			tmp.add(newSoundName.getName());
		}

		System.out.println("Sound list size: " + soundList.size());
		soundList.clear();

		if (jList != null) {
			panelInScroll.remove(jList);
		}

		jList = new JList<String>();
		Collections.sort(tmp);
		jList = new JList(tmp.toArray());
		Dimension d = jList.getPreferredSize();
		d.width = 200;
		panelInScroll.setPreferredSize(d);
		panelInScroll.add(jList);
		panelInScroll.updateUI();

		// jlist listener to gather information from file system
		// uses array list to store names and hashmap for look to fill the list with
		// Sound objects

		jList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				JList<?> tmpList = (JList<?>) e.getSource();
				Sound name = soundMap.get((String) tmpList.getSelectedValue());
				nameLabel.setText(name.getName());
				descLabel.setText(name.getDesc());
				dateLabel.setText(name.getDate());
				soundFile = name.getSoundFile();
				panel_1.updateUI();
			}
		});
	}

	// creates and writes an object to file
	public String writeObjectToFile(Sound obj) throws URISyntaxException {
		String result = "Successfully writen!";

		try {
			f = new FileOutputStream(new File(soundFilePath.getText() + "/" + obj.getName() + ".txt"));
			o = new ObjectOutputStream(f);
			o.writeObject(obj);

		} catch (FileNotFoundException e) {
			result = "Sound folder does not exist";
		} catch (IOException e) {
			result = "Error initializing stream";
		}

		return result;
	}

	// Read sound object from given file
	public Sound readObjectFromFile(File file) throws URISyntaxException {
		Sound sound = null;
		try {
			fi = new FileInputStream(file);
			oi = new ObjectInputStream(fi);

			sound = (Sound) oi.readObject();

			oi.close();
			fi.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream");
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return sound;
	}
}
