package mainapp;

public class Moves {
	
	public static boolean whiteKingMoved;
	public static boolean whiteKingRookMoved;
	public static boolean whiteQueenRookMoved;
	
	public static boolean blackKingMoved;
	public static boolean blackKingRookMoved;
	public static boolean blackQueenRookMoved;
	private static int whiteKingNumberOfMoves;
	

	public static String possibleMoves() {
		String list = "";
		for (int i = 0; i < 64; i++) {
			switch (Main.chessBoard[i/8][i%8]) {
				case 'P': list += possibleP(i);
					break;
				case 'R': list += possibleR(i);
					break;
				case 'N': list += possibleN(i);
					break;
				case 'B': list += possibleB(i);
					break;
				case 'Q': list += possibleQ(i);
					break;
				case 'K': list += possibleK(i);
					break;


			}
		}
		return list;//x1,y1,x2,y2,captured piece
	}
	
	private static String possibleP(int i) {
		
		String list = "";
		char oldPiece;
		int r = i/8, c = i%8;
		
		for (int j = -1; j <= 1; j += 2) {
			try { // capture
				if (Character.isLowerCase(Main.chessBoard[r-1][c+j]) && r >= 2) {
					oldPiece = Main.chessBoard[r-1][c+j];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r-1][c+j] = 'P';
					if (kingSafe()) {
                        list = list + r + c + (r-1) + (c+j) + oldPiece;
                    }
					Main.chessBoard[r][c]='P';
                    Main.chessBoard[r-1][c+j]=oldPiece;
				}
			}
			catch (Exception e) {}
			
			try { // capture & promotion
				if (Character.isLowerCase(Main.chessBoard[r-1][c+j]) && r == 1) {
					char[] t = {'Q','R','B','N'};
					for (int k = 0; k < 4; k++ ) {
						oldPiece = Main.chessBoard[r-1][c+j];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r-1][c+j] = t[k];
						if (kingSafe()) {
							// column1, column2, captured-piece, new-piece, P
	                        list = list + c + (c+j) + oldPiece + t[k] + 'P';
	                    }
						Main.chessBoard[r][c]='P';
	                    Main.chessBoard[r-1][c+j]=oldPiece;
					}
					
				}
			}
			catch (Exception e) {}
		}
		
		try { // move 1 up
			if (Main.chessBoard[r-1][c] == ' ' && r >= 2) {
				oldPiece = Main.chessBoard[r-1][c];
				Main.chessBoard[r][c] = ' ';
				Main.chessBoard[r-1][c] = 'P';
				if (kingSafe()) {
                    list = list + r + c + (r-1) + (c) + oldPiece;
                }
				Main.chessBoard[r][c]='P';
                Main.chessBoard[r-1][c]=oldPiece;
			}
		}
		catch (Exception e) {}
		
		
		try { // move 2 up
			if (r == 6 && Main.chessBoard[r-1][c] == ' ' && Main.chessBoard[r-2][c] == ' ') {
				oldPiece = Main.chessBoard[r-2][c];
				Main.chessBoard[r][c] = ' ';
				Main.chessBoard[r-2][c] = 'P';
				if (kingSafe()) {
                    list = list + r + c + (r-2) + (c) + oldPiece;
                }
				Main.chessBoard[r][c]='P';
                Main.chessBoard[r-2][c]=oldPiece;
			}
		}
		catch (Exception e) {}
		
		try { // promotion without capture
			if (Main.chessBoard[r-1][c] == ' ' && r == 1) {
				char[] t = {'Q','R','B','N'};
				for (int k = 0; k < 4; k++ ) {
					oldPiece = Main.chessBoard[r-1][c];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r-1][c] = t[k];
					if (kingSafe()) {
						// column1, column2, captured-piece, new-piece, P
                        list = list + c + (c) + oldPiece + t[k] + 'P';
	                }
					Main.chessBoard[r][c]='P';
	                Main.chessBoard[r-1][c]=oldPiece;
				}
			}
		}
		catch (Exception e) {}
		
		return list;
	}

	private static String possibleR(int i) {
		String list = "";
		char oldPiece;
		int r = i/8, c = i%8;
		int t = 1;
		
		for (int j = -1; j <= 1; j += 2) {
			try {
				while (Main.chessBoard[r+t*j][c] == ' ') {
					oldPiece = Main.chessBoard[r+t*j][c];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r+t*j][c] = 'R';
					if (kingSafe()) {
                        list=list + r + c + (r+t*j) + (c) + oldPiece;
                    }
					Main.chessBoard[r][c]='R';
                    Main.chessBoard[r+t*j][c]=oldPiece;
					t++;
				}
				if (Character.isLowerCase(Main.chessBoard[r+t*j][c])) {
					oldPiece = Main.chessBoard[r+t*j][c];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r+t*j][c] =  'R';
					if (kingSafe()) {
                        list=list + r + c + (r+t*j) + (c) + oldPiece;
                    }
					Main.chessBoard[r][c]='R';
                    Main.chessBoard[r+t*j][c]=oldPiece;
					
				}
			}
			catch (Exception e) {
			}
			t = 1;	
		}
		
		for (int k = -1; k <= 1; k += 2) {
			try {
				while (Main.chessBoard[r][c+t*k] == ' ') {
					oldPiece = Main.chessBoard[r][c+t*k];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r][c+t*k] =  'R';
					if (kingSafe()) {
                        list=list + r + c + (r) + (c+t*k) + oldPiece;
                    }
					Main.chessBoard[r][c]='R';
                    Main.chessBoard[r][c+t*k]=oldPiece;
					t++;
				}
				if (Character.isLowerCase(Main.chessBoard[r][c+t*k])) {
					oldPiece = Main.chessBoard[r][c+t*k];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r][c+t*k] =  'R';
					if (kingSafe()) {
                        list=list + r + c + (r) + (c+t*k) + oldPiece;
                    }
					Main.chessBoard[r][c]='R';
                    Main.chessBoard[r][c+t*k]=oldPiece;
					
				}
			}
			catch (Exception e) {
			}
			t = 1;
		}
		
		return list;
	}

	private static String possibleN(int i) {
		String list = "";
		char oldPiece;
		int r = i/8, c = i%8;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					if  (Main.chessBoard[r+j][c+2*k] == ' ' || Character.isLowerCase(Main.chessBoard[r+j][c+2*k]) ) {
						oldPiece = Main.chessBoard[r+j][c+2*k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r+j][c+2*k] =  'N';
						if (kingSafe()) {
	                        list=list + r + c + (r+j) + (c+2*k) + oldPiece;
	                    }
						Main.chessBoard[r][c]='N';
	                    Main.chessBoard[r+j][c+2*k]=oldPiece;
						
					}
				}
				catch (Exception e) {}
				
				try {
					if  (Main.chessBoard[r+2*j][c+k] == ' ' || Character.isLowerCase(Main.chessBoard[r+2*j][c+k]) ) {
						oldPiece = Main.chessBoard[r+2*j][c+k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r+2*j][c+k] =  'N';
						if (kingSafe()) {
	                        list=list + r + c + (r+2*j) + (c+k) + oldPiece;
	                    }
						Main.chessBoard[r][c]='N';
	                    Main.chessBoard[r+2*j][c+k]=oldPiece;
						
					}
				}
				catch (Exception e) {}
			}
		}
		return list;
	}

	private static String possibleB(int i) {
		String list = "";
		char oldPiece;
		int r = i/8, c = i%8;
		int t = 1;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					while (Main.chessBoard[r+t*j][c+t*k] == ' ') {
						oldPiece = Main.chessBoard[r+t*j][c+t*k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r+t*j][c+t*k] =  'B';
						if (kingSafe()) {
	                        list=list + r + c + (r+t*j) + (c+t*k) + oldPiece;
	                    }
						Main.chessBoard[r][c]='B';
	                    Main.chessBoard[r+t*j][c+t*k]=oldPiece;
						t++;
					}
					if (Character.isLowerCase(Main.chessBoard[r+t*j][c+t*k])) {
						oldPiece = Main.chessBoard[r+t*j][c+t*k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r+t*j][c+t*k] =  'B';
						if (kingSafe()) {
	                        list=list + r + c + (r+t*j) + (c+t*k) + oldPiece;
	                    }
						Main.chessBoard[r][c]='B';
	                    Main.chessBoard[r+t*j][c+t*k]=oldPiece;
						
					}
				}
				catch (Exception e) {}
				t = 1;
			}
		}
		return list;
	}

	private static String possibleQ(int i) {
		String list = "";
		char oldPiece;
		int r = i/8, c = i%8;
		int t = 1;
		for (int j = -1; j <= 1; j++) {
			for (int k = -1; k <= 1; k++) {
				try {
					while (Main.chessBoard[r+t*j][c+t*k] == ' ') {
						oldPiece = Main.chessBoard[r+t*j][c+t*k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r+t*j][c+t*k] =  'Q';
						if (kingSafe()) {
	                        list=list + r + c + (r+t*j) + (c+t*k) + oldPiece;
	                    }
						Main.chessBoard[r][c]='Q';
	                    Main.chessBoard[r+t*j][c+t*k]=oldPiece;
						t++;
					}
					if (Character.isLowerCase(Main.chessBoard[r+t*j][c+t*k])) {
						oldPiece = Main.chessBoard[r+t*j][c+t*k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r+t*j][c+t*k] =  'Q';
						if (kingSafe()) {
	                        list=list + r + c + (r+t*j) + (c+t*k) + oldPiece;
	                    }
						Main.chessBoard[r][c]='Q';
	                    Main.chessBoard[r+t*j][c+t*k]=oldPiece;
						
					}
				}
				catch (Exception e) {}
				t = 1;
			}
		}
		return list;
	}

	public static String possibleK(int i) {
		String list = "";
		char oldPiece;
		int r = i/8, c = i%8;
		
		for (int j = 0; j < 9; j++) {
			if (j != 4) {
				try {
				if  (Character.isLowerCase(Main.chessBoard[r-1+j/3][c-1+j%3]) || Main.chessBoard[r-1+j/3][c-1+j%3] == ' ') {
					oldPiece = Main.chessBoard[r-1+j/3][c-1+j%3];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r-1+j/3][c-1+j%3] = 'K';
					int kingTemp=Main.kingPositionWhite;
                    Main.kingPositionWhite=i+(j/3)*8+j%3-9;
                    if (kingSafe()) {
                        list=list + r + c + (r-1+j/3) + (c-1+j%3) + oldPiece;
                    }
                    Main.chessBoard[r][c]='K';
                    Main.chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
                    Main.kingPositionWhite=kingTemp;
				}
				}
				catch (Exception e) { }
			}
		}
		
		//Castles
		if (!whiteKingMoved) {
			
			if (!whiteKingRookMoved) {
				if (Main.chessBoard[r][c+1] == ' ' && Main.chessBoard[r][c+2] == ' ') {
					boolean notInCheck = kingSafe();
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r][c+1] = 'K';
					boolean f1Safe = kingSafe();
					Main.chessBoard[r][c+1] = ' ';
					Main.chessBoard[r][c+2] = 'K';
					boolean g1Safe = kingSafe();
					Main.chessBoard[r][c+2] = ' ';
					Main.chessBoard[r][c] = 'K';
					if (notInCheck && f1Safe && g1Safe) {
						list = list + (r) + (c) + (r) + (c+2) + 'C';
					}
				}
			}
			
			if (!whiteQueenRookMoved) {
				if (Main.chessBoard[r][c-1] == ' ' && Main.chessBoard[r][c-2] == ' ') {
					boolean notInCheck = kingSafe();
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r][c-1] = 'K';
					boolean d1Safe = kingSafe();
					Main.chessBoard[r][c-1] = ' ';
					Main.chessBoard[r][c-2] = 'K';
					boolean c1Safe = kingSafe();
					Main.chessBoard[r][c-2] = ' ';
					Main.chessBoard[r][c] = 'K';
					if (notInCheck && d1Safe && c1Safe) {
						list = list + (r) + (c) + (r) + (c-2) + 'C';
					}
				}
			}
		}
		
		return list;
	}
	
	public static boolean kingSafe() {
		//bishop/queen
		int t = 1;
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					while (Main.chessBoard[Main.kingPositionWhite/8 + t*i][Main.kingPositionWhite % 8 + t*j] == ' ') { t++; }
						
					if (Main.chessBoard[Main.kingPositionWhite/8 + t*i][Main.kingPositionWhite % 8 + t*j] == 'b' || 
							Main.chessBoard[Main.kingPositionWhite/8 + t*i][Main.kingPositionWhite % 8 + t*j] == 'q') {
						return false;
					}
				} catch (Exception e) {}
				
				t = 1;
				
			}
		}
		
		//rook/queen
		for (int i = -1; i <= 1; i += 2) {
			
			try {
				while (Main.chessBoard[Main.kingPositionWhite/8][Main.kingPositionWhite % 8 + t*i] == ' ') { t++; }
					
				if (Main.chessBoard[Main.kingPositionWhite/8][Main.kingPositionWhite % 8 + t*i] == 'r' || 
						Main.chessBoard[Main.kingPositionWhite/8][Main.kingPositionWhite % 8 + t*i] == 'q') {
					return false;
				}
			} catch (Exception e) {}
			
			
			t = 1;
			
			
			try {
				while (Main.chessBoard[Main.kingPositionWhite/8 + t*i][Main.kingPositionWhite % 8] == ' ') { t++; }
					
				if (Main.chessBoard[Main.kingPositionWhite/8 + t*i][Main.kingPositionWhite % 8] == 'r' || 
						Main.chessBoard[Main.kingPositionWhite/8 + t*i][Main.kingPositionWhite % 8] == 'q') {
					return false;
				}
			} catch (Exception e) {}
			
			t = 1;
		}
		
		
		// knight
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {	
					if (Main.chessBoard[Main.kingPositionWhite/8 + i][Main.kingPositionWhite % 8 + 2*j] == 'n')
						return false;
					
				} catch (Exception e) {}
				try {	
					if (Main.chessBoard[Main.kingPositionWhite/8 + 2*i][Main.kingPositionWhite % 8 + j] == 'n')
						return false;
					
				} catch (Exception e) {}
				
				
				
			}
		}
		
		
		// pawn
		if (Main.kingPositionWhite >= 16) {
			try {	
				if (Main.chessBoard[Main.kingPositionWhite/8 - 1][Main.kingPositionWhite % 8 - 1] == 'p')
					return false;
				if (Main.chessBoard[Main.kingPositionWhite/8 - 1][Main.kingPositionWhite % 8 + 1] == 'p')
					return false;
				
			} catch (Exception e) {}
		}
		
		//king
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					try {	
						if (Main.chessBoard[Main.kingPositionWhite/8 + i][Main.kingPositionWhite % 8 + j] == 'k')
							return false;
						
					} catch (Exception e) {}
				}	
			}
		}
		
		return true;
	}

	public static void makeMove(String move) {
		if (move.charAt(4) == 'C') {
			int kingDest = Character.getNumericValue(move.charAt(3));
			
			if (kingDest == 6) { // G kolona - mala rokada
				Main.chessBoard[7][7] = ' ';
				Main.chessBoard[7][5] = 'R';
				Main.kingPositionWhite += 2;
			}
			else if (kingDest == 2){ // C kolona - velika rokada
				Main.chessBoard[7][0] = ' ';
				Main.chessBoard[7][3] = 'R';
				Main.kingPositionWhite -= 2;
			}
			
			whiteKingNumberOfMoves++;
			Main.chessBoard[7][kingDest] = 'K';
			Main.chessBoard[7][4] = ' ';
		}
		else if (move.charAt(4) == 'P') {
			
			Main.chessBoard[1][Character.getNumericValue(move.charAt(0))] = ' ';
			Main.chessBoard[0][Character.getNumericValue(move.charAt(1))] = move.charAt(3);
				
			
		}
		else {
			Main.chessBoard[Character.getNumericValue(move.charAt(2))] [Character.getNumericValue(move.charAt(3))] =
					Main.chessBoard[Character.getNumericValue(move.charAt(0))] [Character.getNumericValue(move.charAt(1))];
			
			Main.chessBoard[Character.getNumericValue(move.charAt(0))] [Character.getNumericValue(move.charAt(1))] = ' ';
			
			if ('K' == Main.chessBoard[Character.getNumericValue(move.charAt(2))] [Character.getNumericValue(move.charAt(3))]) {
				Main.kingPositionWhite = 8*Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
				whiteKingMoved = true;
				whiteKingNumberOfMoves++;
			}
		}
		
		if (!(Main.chessBoard[7][0] == 'R')) {
			whiteQueenRookMoved = true;
		}
		
		if (!(Main.chessBoard[7][7] == 'R')) {
			whiteKingRookMoved = true;
		}
		
		if (!(Main.chessBoard[7][4] == 'K')) {
			whiteKingMoved = true;
		}
		
		if (!(Main.chessBoard[0][0] == 'r')) {
			blackQueenRookMoved = true;
		}
		
		if (!(Main.chessBoard[0][7] == 'r')) {
			blackKingRookMoved = true;
		}
		
		if (!(Main.chessBoard[0][4] == 'k')) {
			blackKingMoved = true;
		}
		//Main.flipBoard();
	}
	
	public static void undoMove(String move) {
		// ne radi zbog flipa, doraditi
		//Main.flipBoard();
		if (move.charAt(4) == 'C') {
			int kingDest = Character.getNumericValue(move.charAt(3));
			
			if (kingDest == 6) { // G kolona - mala rokada
				Main.chessBoard[7][7] = 'R';
				Main.chessBoard[7][5] = ' ';
				Main.kingPositionWhite -= 2;
				whiteKingRookMoved = false;
			}
			else if (kingDest == 2){ // C kolona - velika rokada
				Main.chessBoard[7][0] = 'R';
				Main.chessBoard[7][3] = ' ';
				Main.kingPositionWhite += 2;
				whiteQueenRookMoved = false;
			}
			whiteKingNumberOfMoves--;
			whiteKingMoved = false;
			Main.chessBoard[7][kingDest] = ' ';
			Main.chessBoard[7][4] = 'K';
		}
		if (move.charAt(4) == 'P') {// if pawn promotion
			//col1, col2, captured-piece, new-piece, P
			
			Main.chessBoard[1][Character.getNumericValue(move.charAt(0))] = 'P';
			Main.chessBoard[0][Character.getNumericValue(move.charAt(1))] = move.charAt(2);
		}
		else { 
			Main.chessBoard[Character.getNumericValue(move.charAt(0))] [Character.getNumericValue(move.charAt(1))] =
					Main.chessBoard[Character.getNumericValue(move.charAt(2))] [Character.getNumericValue(move.charAt(3))];
			
			Main.chessBoard[Character.getNumericValue(move.charAt(2))] [Character.getNumericValue(move.charAt(3))] = move.charAt(4);
			
			if ('K' == Main.chessBoard[Character.getNumericValue(move.charAt(0))] [Character.getNumericValue(move.charAt(1))]) {
				Main.kingPositionWhite = 8*Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
				whiteKingNumberOfMoves--;
				if (whiteKingNumberOfMoves == 0) {
					whiteKingMoved = false;
				}
				else {
					whiteKingMoved = true;
				}
			}
			
			
		}
		
		if (!(Main.chessBoard[7][0] == 'R')) {
			whiteQueenRookMoved = true;
		}
		
		if (!(Main.chessBoard[7][7] == 'R')) {
			whiteKingRookMoved = true;
		}
		
		if (!(Main.chessBoard[7][4] == 'K')) {
			whiteKingMoved = true;
		}
		
		if (!(Main.chessBoard[0][0] == 'r')) {
			blackQueenRookMoved = true;
		}
		
		if (!(Main.chessBoard[0][7] == 'r')) {
			blackKingRookMoved = true;
		}
		
		if (!(Main.chessBoard[7][0] == 'k')) {
			blackKingMoved = true;
		}
		//Main.flipBoard();
	}
	
	public static String sortMoves(String list) {
        int[] score=new int [list.length()/5];
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i, i+5));
            score[i/5]=Rating.rating(-1, 0);
            undoMove(list.substring(i, i+5));
        }
        String newListA="", newListB=list;
        for (int i=0;i<Math.min(6, list.length()/5);i++) {//first few moves only
            int max=-1000000, maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;
            newListA+=list.substring(maxLocation*5,maxLocation*5+5);
            newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
        }
        return newListA+newListB;
    }
}
