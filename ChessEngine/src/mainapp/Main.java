package mainapp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Main {
	
	static char chessBoard[][]={
        {'r','n','b','q','k','b','n','r'},
        {'p','p','p','p','p','p','p','p'},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {'P','P','P','P','P','P','P','P'},
        {'R','N','B','Q','K','B','N','R'}};
	
	/*
	static String chessBoard2[][]={
        {"r","n","b","q","k","b","n","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","N","B","Q","K","B","N","R"}};
	
	static char chessBoard[][]={
        {'r',' ',' ',' ','k',' ',' ','r'},
        {'p','p','p','p','p','p','p','p'},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {'P','P','P','P','P','P','P','P'},
        {'R',' ',' ',' ','K',' ',' ','R'}};
	/*
	static String chessBoard[][]={
		{" "," "," "," "," "," "," "," "},
		{" "," "," "," "," "," "," "," "},
		{" "," "," "," ","K"," "," "," "},
		{" ","B","k","N"," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," ","N","p","P"," "," "," "},
        {" "," "," ","P"," "," "," "," "},
        {" "," "," "," "," "," "," "," "}};
	*/
	static int kingPositionWhite;
	static int kingPositionBlack;
	//static int humanAsWhite=-1;//1=human as white, 0=human as black
    static int globalDepth=4;
    static int player = 0;
    public static JFrame f; 
	public static void main(String[] args) {
		while (!('K' == chessBoard[kingPositionWhite/8][kingPositionWhite%8])) {kingPositionWhite++;}//get King's location
        while (!('k' == chessBoard[kingPositionBlack/8][kingPositionBlack%8])) {kingPositionBlack++;}//get king's location
		
        
        
        JButton button = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (player == 1)
					Main.flipBoard();
				
				Moves.makeMove(Main.alphaBeta(Main.globalDepth, 1000000,-1000000, "", 0));
				//Moves.makeMove("7476C");
				player = 1 - player;
				System.out.println(Moves.possibleMoves());

				if (player == 0) {
					Main.flipBoard();
				}
				
				f.repaint();
				/*Main.flipBoard();
				f.repaint();*/
			}
		});
		button.setText("Igraj");
		button.setLocation(700, 500);
		button.setEnabled(true);
		
        
        f = new JFrame("Chess");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInterface ui = new UserInterface();
		f.add(ui);
		f.setSize(500,500);
		f.setVisible(true);
		f.add(button,BorderLayout.EAST);
		//System.out.println(Moves.possibleMoves());
		//Moves.makeMove("7476C");
		//flipBoard();
		//System.out.println(Moves.possibleMoves());
		//System.out.println(Moves.sortMoves(Moves.possibleMoves()));
        /*
		Object[] option={"Computer","Human"};
        humanAsWhite=JOptionPane.showOptionDialog(null, "Who should play as white?", "ABC Options", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        if (humanAsWhite==0) {
            long startTime=System.currentTimeMillis();
            Moves.makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));
            long endTime=System.currentTimeMillis();
            System.out.println("That took "+(endTime-startTime)+" milliseconds");
            //flipBoard();
            f.repaint();
        }
        */
        //printBoard();
        //flipBoard();
        //printBoard();
        
        /*
		printBoard();
	
		System.out.println(kingPositionC);
		System.out.println(kingPositionL);
		flipBoard();
		printBoard();
		System.out.println(kingPositionC);
		System.out.println(kingPositionL);
		*/
        /*Moves.makeMove("6444 ");
        Moves.makeMove("7655 ");
        Moves.makeMove("4434 ");
        Moves.makeMove("5543 ");
        Moves.undoMove("5543 ");
        System.out.println(Moves.kingSafe());
        System.out.println(Moves.possibleMoves());
        
        printBoard();*/
		
	}
	
	public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        //return in the form of 1234b##########
        String list=Moves.possibleMoves();
        //only one possible move
        if (list.length() == 5 && depth == globalDepth) {
        	return list.substring(0,5)+(Rating.rating(list.length(), depth)*(1-player*2)); 
        }
        
        if (depth==0 || list.length()==0) {return move+(Rating.rating(list.length(), depth)*(1-player*2));}

        list=Moves.sortMoves(list);
        player=1-player;//either 1 or 0
        for (int i=0;i<list.length();i+=5) {
            Moves.makeMove(list.substring(i,i+5));
            flipBoard();
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.valueOf(returnString.substring(5));
            flipBoard();
            Moves.undoMove(list.substring(i,i+5));
            if (player==0) {
                if (value<=beta) {beta=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            } else {
                if (value>alpha) {alpha=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            }
            if (alpha>=beta) {
                if (player==0) {return move+beta;} else {return move+alpha;}
            }
        }
        if (player==0) {return move+beta;} else {return move+alpha;}
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
		
		
		flipKingPositions();
		
		
	}
	
	private static void flipKingPositions() {
		// TODO Auto-generated method stub 

		int kingTemp = kingPositionWhite;
		kingPositionWhite = 8*(7 - kingPositionBlack/8) + kingPositionBlack % 8 ;
		kingPositionBlack = 8*(7 - kingTemp/8) + kingTemp % 8 ;
		
		boolean tmp = Moves.blackKingMoved;
		Moves.blackKingMoved = Moves.whiteKingMoved;
		Moves.whiteKingMoved = tmp;
		
		tmp = Moves.blackKingRookMoved;
		Moves.blackKingRookMoved = Moves.whiteKingRookMoved;
		Moves.whiteKingRookMoved = tmp;
		
		tmp = Moves.blackQueenRookMoved;
		Moves.blackQueenRookMoved = Moves.whiteQueenRookMoved;
		Moves.whiteQueenRookMoved = tmp;
	}

	public static void printBoard() {
		for (int i = 0; i < 8; i++) {
			System.out.println(Arrays.toString(chessBoard[i]));
		}
	}
}
