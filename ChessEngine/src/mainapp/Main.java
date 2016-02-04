package mainapp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONArray;
import org.json.JSONTokener;

public class Main {
	/*
	static char chessBoard[][]={
        {'r','n','b','q','k','b','n','r'},
        {'p','p','p','p','p','p','p','p'},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {'P','P','P','P','P','P','P','P'},
        {'R','N','B','Q','K','B','N','R'}};
	

	*/
	static char chessBoard[][];
	/*static char chessBoard[][]={
        {'r',' ',' ','q','k',' ',' ','r'},
        {'p','p','p','p','p','p','p','p'},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {'P','P','P','P','P','P','P','P'},
        {'R',' ',' ','Q','K',' ',' ','R'}};*/
	/*
	static char chessBoard[][]={
		{' ',' ',' ',' ',' ',' ',' ',' '},
		{' ',' ',' ',' ',' ',' ',' ',' '},
		{' ',' ',' ',' ','K',' ',' ',' '},
		{' ','B','k','N',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ','N','p','P',' ',' ',' '},
        {' ',' ',' ','P',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '}};*/
	
	static int kingPositionWhite;
	static int kingRookPositionWhite;
	static int kingRookPositionWhiteLast;
	static int queenRookPositionWhite;
	static int queenRookPositionWhiteLast;
	
	static int kingPositionBlack;
	static int kingRookPositionBlack;
	static int kingRookPositionBlackLast;
	static int queenRookPositionBlack;
	static int queenRookPositionBlackLast;
	
    static int globalDepth=2;
    static int player = 0;
    public static JFrame f;
    static String image_path;
    static int indik = 0;
	 public static UserInterface ui = new UserInterface();
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        final JButton btnMove = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (player == 1)
					Main.flipBoard();
				String move = alphaBeta(Main.globalDepth, 1000000,-1000000, "", 0).substring(0,5);
				Moves.makeMove(move);			
				//Moves.makeMove("7476C");
				player = 1 - player;

				if (player == 0) {
					Main.flipBoard();
					if (Moves.possibleMoves().isEmpty()) {
						if (!Moves.kingSafe()) {
							JOptionPane.showMessageDialog(null, "Mat");
							this.setEnabled(false);
						}
						else {
							JOptionPane.showMessageDialog(null, "Pat");
							this.setEnabled(false);
						}
						
					}

				}
				
				else {
					Main.flipBoard();
					if (Moves.possibleMoves().isEmpty()) {
						if (!Moves.kingSafe()) {
							Main.flipBoard();
							JOptionPane.showMessageDialog(null, "Mat");
							this.setEnabled(false);
						}
						else {
							Main.flipBoard();
							JOptionPane.showMessageDialog(null, "Pat");
							this.setEnabled(false);
						}
						
					}
					else {
						Main.flipBoard();
					}
					
				}
					
				
				
				f.repaint();
				/*Main.flipBoard();
				f.repaint();*/
			}
		});
		btnMove.setText("Igraj");
		btnMove.setLocation(700, 500);
		btnMove.setEnabled(true);
		
		JButton btnReset = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				char[][] chessBoard2 = {
				        {'r','n','b','q','k','b','n','r'},
				        {'p','p','p','p','p','p','p','p'},
				        {' ',' ',' ',' ',' ',' ',' ',' '},
				        {' ',' ',' ',' ',' ',' ',' ',' '},
				        {' ',' ',' ',' ',' ',' ',' ',' '},
				        {' ',' ',' ',' ',' ',' ',' ',' '},
				        {'P','P','P','P','P','P','P','P'},
				        {'R','N','B','Q','K','B','N','R'}};
				chessBoard = chessBoard2;
				initialisePositions();
				btnMove.getAction().setEnabled(true);
				player = 0;
				f.repaint();
			}
			
		});
		
		btnReset.setText("Reset position");
		btnReset.setEnabled(true);
		
		JButton loadImage = new JButton(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("./Projekat_OCR/test_pictures"));
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    image_path = selectedFile.getName();
				    System.out.println("Selected file: " + selectedFile.getName());
				    chessBoard = getBoardFromPicture(image_path);
				    indik++;
					f.repaint();
					System.out.println("Repaintovao!");
					initialisePositions();
					btnMove.getAction().setEnabled(true);
					player = 0;
				}
				
				
			}
		});
        
		loadImage.setText("Load_Image");
		loadImage.setEnabled(true);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new BorderLayout());
		panelButtons.add(btnMove,BorderLayout.NORTH);
		
		panelButtons.add(loadImage,BorderLayout.SOUTH);
        f = new JFrame("Chess");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	UserInterface ui = new UserInterface();
		f.add(ui);
		f.setSize(800,800);
		f.setVisible(true);
		//f.add(button,BorderLayout.EAST);
		//f.add(loadImage,BorderLayout.SOUTH);
		f.add(panelButtons,BorderLayout.EAST);
		f.setExtendedState(f.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		
		JPanel panelMode = new JPanel();
		panelMode.setLayout(new GridLayout(10, 1));
		//panelMode.setBackground(Color.WHITE);
		JLabel l = new JLabel("Izaberite stil igranja:");
		final JRadioButton b = new JRadioButton("Normal");
		b.setActionCommand("a");
		b.setSelected(true);
		final JRadioButton b1 = new JRadioButton("Aggressive both");
		b1.setActionCommand("b");
		final JRadioButton b2 = new JRadioButton("Aggressive vs Defensive");
		b2.setActionCommand("c");
		
		ButtonGroup gr = new ButtonGroup();
		gr.add(b); gr.add(b1); gr.add(b2);
		
		final JRadioButton d1 = new JRadioButton("1");
		d1.setActionCommand("1");
		d1.setSelected(true);
		final JRadioButton d2 = new JRadioButton("2");
		d2.setActionCommand("2");
		final JRadioButton d3 = new JRadioButton("3");
		d3.setActionCommand("3");
		final JRadioButton d4 = new JRadioButton("4");
		d4.setActionCommand("4");
		
		ButtonGroup grD = new ButtonGroup();
		grD.add(d1); grD.add(d2); grD.add(d3); grD.add(d4);
		
		JLabel lab = new JLabel("Izaberite dubinu:");
		panelMode.add(lab);
		panelMode.add(d1);
		panelMode.add(d2);
		panelMode.add(d3);
		panelMode.add(d4);
		panelMode.add(l);
		panelMode.add(b);
		panelMode.add(b1);
		panelMode.add(b2);
		panelMode.add(btnReset);
		class DepthActionListener extends AbstractAction{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getActionCommand()) {
				case "1":
					globalDepth = 2;
					break;
				case "2":
					globalDepth = 4;
					break;
				case "3":
					globalDepth = 6;
					break;
				case "4":
					globalDepth = 8;
					break;
				default:
					globalDepth = 2;
					break;
				}
			}
		}
		
		class ModeActionListener extends AbstractAction{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				switch (arg0.getActionCommand()) {
				case "a":
					Rating.aggressivines = 1;
					Rating.positionaliness = 1;
					Rating.moveablility = 1;
					Rating.materialist = 1;
					Rating.mode3 = false;
					System.out.println("Normal");
					break;
				case "b" :
					Rating.aggressivines = 2;
					Rating.moveablility = 2;
					Rating.materialist = 1;
					Rating.positionaliness = 3/5;
					Rating.mode3 = false;
					System.out.println("Aggr both");
					break;
					
				case "c" :
					Rating.aggressivines = 2;
					Rating.moveablility = 2;
					Rating.positionaliness = 1;
					Rating.materialist = 1;
					Rating.mode3 = true;
					System.out.println("aggr vs pos");
					break;

				default:
					Rating.aggressivines = 1;
					Rating.positionaliness = 1;
					Rating.materialist = 1;
					Rating.moveablility = 1;
					Rating.mode3 = false;
					System.out.println("Normal,Default");
					break;
				}
			}
			
		}
		
		d1.addActionListener(new DepthActionListener());
		d2.addActionListener(new DepthActionListener());
		d3.addActionListener(new DepthActionListener());
		d4.addActionListener(new DepthActionListener());
		
		b.addActionListener(new ModeActionListener());
		b1.addActionListener(new ModeActionListener());
		b2.addActionListener(new ModeActionListener());
		
		panelButtons.add(panelMode,BorderLayout.CENTER);
		
		if(indik == 0){
			char[][] chessBoard1 = {
			        {'r','n','b','q','k','b','n','r'},
			        {'p','p','p','p','p','p','p','p'},
			        {' ',' ',' ',' ',' ',' ',' ',' '},
			        {' ',' ',' ',' ',' ',' ',' ',' '},
			        {' ',' ',' ',' ',' ',' ',' ',' '},
			        {' ',' ',' ',' ',' ',' ',' ',' '},
			        {'P','P','P','P','P','P','P','P'},
			        {'R','N','B','Q','K','B','N','R'}};
			
			char[][] chessBoard2 = {
					{'r',' ','b',' ',' ','r',' ',' '},
			        {'p','p','q',' ',' ',' ','P','Q'},
			        {' ',' ','n',' ','k',' ',' ','p'},
			        {' ',' ',' ','p','P',' ',' ',' '},
			        {' ','p',' ','P',' ',' ',' ',' '},
			        {' ',' ',' ','B','b',' ',' ',' '},
			        {'P','B','P','N',' ',' ',' ','P'},
			        {'R',' ',' ',' ',' ','R',' ','K'}};
			
			char chessBoard3[][]={
				{' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ','K',' ',' ',' '},
				{' ','B','k','N',' ',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ',' ',' '},
		        {' ',' ','N','p','P',' ',' ',' '},
		        {' ',' ',' ','P',' ',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ',' ',' '}};
			
			char chessBoard4[][]={
				{' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' '},
		        {' ',' ',' ',' ','K',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ','k',' '},
		        {' ',' ','Q',' ',' ',' ',' ',' '}};
			
			char chessBoard5[][]={
		        {'r',' ',' ','q','k',' ',' ','r'},
		        {'p','p','p','p','p','p','p','p'},
		        {' ',' ',' ',' ',' ',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ',' ',' '},
		        {' ',' ',' ',' ',' ',' ',' ',' '},
		        {'P','P','P','P','P','P','P','P'},
		        {'R',' ',' ','Q','K',' ',' ','R'}};
			
			chessBoard = chessBoard1;
			initialisePositions();
		}		
	}
	
	public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        //return in the form of 1234b##########
        ArrayList<String> list=Moves.possibleMoves();
        //only one possible move
        if (list.size() == 1 && depth == globalDepth) {
        	return list.get(0)+(Rating.rating(list.size(), depth)*(1-player*2)); 
        }
        
        if (depth==0 || list.size()==0) {return move+(Rating.rating(list.size(), depth)*(1-player*2));}

        list=Moves.sortMoves(list);
        player=1-player;//either 1 or 0
        for (int i=0;i<list.size();i++) {
            Moves.makeMove(list.get(i));
            flipBoard();
            String returnString=alphaBeta(depth-1, beta, alpha, list.get(i), player);
            int value=Integer.valueOf(returnString.substring(5));
            /*
            if (depth == globalDepth) {
            	if(Rating.mode3){
            		if(Rating.aggressivines>Rating.positionaliness){
            			System.out.println("aggr");
            			System.out.println(Rating.aggressivines + " " + Rating.positionaliness);
            		}
            		else{
            			System.out.println("deff");
            			System.out.println(Rating.aggressivines + " " + Rating.positionaliness);
            		}
            	}
            }*/
            flipBoard();
            Moves.undoMove(list.get(i));
            if (player==0) {
				if (value <= beta) {
					beta = value;
					if (depth == globalDepth) {
						move = returnString.substring(0, 5);
					}
				}
            } else {
				if (value > alpha) {
					alpha = value;
					if (depth == globalDepth) {
						move = returnString.substring(0, 5);
					}
				}
            }
            if (alpha>=beta) {
				if (player == 0) {
					return move + beta;
				} else {
					return move + alpha;
				}
            }
        }
		if (player == 0) {
			return move + beta;
		} else {
			return move + alpha;
		}
    }
	public static void flipBoard() {
		char t;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 8; j++) {
				if (Character.isUpperCase(chessBoard[i][j])) {
					t = Character.toLowerCase(chessBoard[i][j]);
				}
				else {
					t = Character.toUpperCase(chessBoard[i][j]);	
				}
				
				if (Character.isUpperCase(chessBoard[7-i][j])) {
					chessBoard[i][j] = Character.toLowerCase(chessBoard[7-i][j]);
				}
				else {
					chessBoard[i][j] = Character.toUpperCase(chessBoard[7-i][j]);	
				}
				
				chessBoard[7-i][j] = t;
			}
		
		
		flipPositions();
		Moves.flipEPSquares();
		
	}
	
	private static void flipPositions() {
		// TODO Auto-generated method stub 

		int tmp = kingPositionWhite;
		kingPositionWhite = 8*(7 - kingPositionBlack/8) + kingPositionBlack % 8 ;
		kingPositionBlack = 8*(7 - tmp/8) + tmp % 8 ;
		
		tmp = kingRookPositionWhite;
		kingRookPositionWhite = 8*(7 - kingRookPositionBlack/8) + kingRookPositionBlack % 8 ;
		kingRookPositionBlack = 8*(7 - tmp/8) + tmp % 8 ;
		
		tmp = queenRookPositionWhite;
		queenRookPositionWhite = 8*(7 - queenRookPositionBlack/8) + queenRookPositionBlack % 8 ;
		queenRookPositionBlack = 8*(7 - tmp/8) + tmp % 8 ;
		
		tmp = kingRookPositionWhiteLast;
		kingRookPositionWhiteLast = 8*(7 - kingRookPositionBlackLast/8) + kingRookPositionBlackLast % 8 ;
		kingRookPositionBlackLast = 8*(7 - tmp/8) + tmp % 8 ;
		
		tmp = queenRookPositionWhiteLast;
		queenRookPositionWhiteLast = 8*(7 - queenRookPositionBlackLast/8) + queenRookPositionBlackLast % 8 ;
		queenRookPositionBlackLast = 8*(7 - tmp/8) + tmp % 8 ;
		
		tmp = Moves.whiteKingNumberOfMoves;
		Moves.whiteKingNumberOfMoves = Moves.blackKingNumberOfMoves;
		Moves.blackKingNumberOfMoves = tmp;
		
		tmp = Moves.whiteKingRookNumberOfMoves;
		Moves.whiteKingRookNumberOfMoves = Moves.blackKingRookNumberOfMoves;
		Moves.blackKingRookNumberOfMoves = tmp;
		
		tmp = Moves.whiteQueenRookNumberOfMoves;
		Moves.whiteQueenRookNumberOfMoves = Moves.blackQueenRookNumberOfMoves;
		Moves.blackQueenRookNumberOfMoves = tmp;
		
		boolean t = Moves.whiteCastled;
		Moves.whiteCastled = Moves.blackCastled;
		Moves.blackCastled = t;
		
		boolean mode3 = Rating.mode3;
		int a; int b;
		if(mode3){
			
			a = Rating.positionaliness;
			Rating.positionaliness = Rating.aggressivines;
			Rating.aggressivines = a;
			
			b = Rating.materialist;
			Rating.materialist = Rating.moveablility;
			Rating.moveablility = b;
			
		}
		
	}

	public static void printBoard() {
		for (int i = 0; i < 8; i++) {
			System.out.println(Arrays.toString(chessBoard[i]));
		}
	}
	
	public static char[][] getBoardFromPicture(String path){
		String folder = "test_pictures/";
		ProcessBuilder pb = new ProcessBuilder("python", "Recogniton_chess_board.py", folder+path);
		pb.directory(new File("./Projekat_OCR"));
		pb.redirectError();
		
		try {
			Process p = pb.start();
			//Sleep dok python azurira fajl
			System.out.println("ucitavanje pozicije...........");
			Thread.sleep(5000);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("./Projekat_OCR/output1.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONTokener tokener = new JSONTokener(br);
		JSONArray o = new JSONArray(tokener);
		
		char[][] board = new char[8][8];
		
		for (int i = 0; i<8; i++) {
			for(int j = 0 ; j<8; j++){
				if(((String) o.getJSONArray(i).get(j)).equals(" ")){
					board[i][j] = ' ';
				}
				else{
					board[i][j] = ((String) o.getJSONArray(i).get(j)).charAt(0);
				}
			}
		}
		
		System.out.println(o);
			System.out.println(board[7][7]+"..");
		
		return board;
	}
	
	private static void initialisePositions() {
		kingPositionWhite = 0;
		kingPositionBlack = 0;
		while (!('K' == chessBoard[kingPositionWhite/8][kingPositionWhite%8])) {kingPositionWhite++;}//get white king's location
        while (!('k' == chessBoard[kingPositionBlack/8][kingPositionBlack%8])) {kingPositionBlack++;}//get black king's location
		
        if ('k' == chessBoard[0][4]) {
        	Moves.blackKingNumberOfMoves = 0;
        }
        else {
        	Moves.blackKingNumberOfMoves = 1;
        }
        
        
        if ('K' == chessBoard[7][4]) {
        	Moves.whiteKingNumberOfMoves = 0;
        }
        else {
        	Moves.whiteKingNumberOfMoves = 1;
        }
        
        
        if ('r' == chessBoard[0][0]) {
        	queenRookPositionBlack = 0;
        	Moves.blackQueenRookNumberOfMoves = 0;
        }
        else {
        	Moves.blackQueenRookNumberOfMoves = 1;
        }
        
        if ('r' == chessBoard[0][7]) {
        	kingRookPositionBlack = 7;
        	Moves.blackKingRookNumberOfMoves = 0;
        }
        else {
        	Moves.blackKingRookNumberOfMoves = 1;
        }
        
        if ('R' == chessBoard[7][0]) {
        	queenRookPositionWhite = 56;
        	Moves.whiteQueenRookNumberOfMoves = 0;
        }
        else {
        	Moves.whiteQueenRookNumberOfMoves = 1;
        }
        
        if ('R' == chessBoard[7][7]) {
        	kingRookPositionWhite = 63;
        	Moves.whiteKingRookNumberOfMoves = 0;
        }
        else {
        	Moves.whiteKingRookNumberOfMoves = 1;
        }
	}
}
