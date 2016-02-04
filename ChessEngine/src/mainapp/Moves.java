package mainapp;

import java.util.ArrayList;
import java.util.Stack;

public class Moves {

	public static int whiteKingNumberOfMoves;
	public static int whiteKingRookNumberOfMoves;
	public static int whiteQueenRookNumberOfMoves;
	public static boolean whiteCastled = false;

	public static int blackKingNumberOfMoves;
	public static int blackKingRookNumberOfMoves;
	public static int blackQueenRookNumberOfMoves;
	public static boolean blackCastled = false;

	public static Stack<EnPassantStruct> epStack = new Stack<EnPassantStruct>();

	public static ArrayList<String> possibleMoves() {
		// String list = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 64; i++) {
			switch (Main.chessBoard[i / 8][i % 8]) {
			case 'P':
				list.addAll(possibleP(i));
				break;
			case 'R':
				list.addAll(possibleR(i));
				break;
			case 'N':
				list.addAll(possibleN(i));
				break;
			case 'B':
				list.addAll(possibleB(i));
				break;
			case 'Q':
				list.addAll(possibleQ(i));
				break;
			case 'K':
				list.addAll(possibleK(i));
				break;

			}
		}
		return list;// x1,y1,x2,y2,captured piece
	}

	private static ArrayList<String> possibleP(int i) {

		ArrayList<String> list = new ArrayList<String>();
		char oldPiece;
		int r = i / 8, c = i % 8;
		// potrebno je razdvojiti jedenje pesakom, od anpasana!
		for (int j = -1; j <= 1; j += 2) {
			try { // capture
				if ((Character.isLowerCase(Main.chessBoard[r - 1][c + j]))
						&& r >= 2) {
					oldPiece = Main.chessBoard[r - 1][c + j];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r - 1][c + j] = 'P';
					if (kingSafe()) {
						String tmp = String.valueOf(r) + String.valueOf(c)
								+ String.valueOf(r - 1) + String.valueOf(c + j)
								+ oldPiece;
						list.add(tmp);
					}
					Main.chessBoard[r][c] = 'P';
					Main.chessBoard[r - 1][c + j] = oldPiece;
				}
			} catch (Exception e) {
			}

			try { // capture enpassant
				if (!epStack.empty()) {

					if (epStack.peek().square == 8 * (r - 1) + (c + j)
							&& epStack.peek().depth == 0 && r == 3) {

						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r - 1][c + j] = 'P';
						Main.chessBoard[r][c + j] = ' ';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r - 1)
									+ String.valueOf(c + j) + 'e';
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'P';
						Main.chessBoard[r - 1][c + j] = ' ';
						Main.chessBoard[r][c + j] = 'p';
					}
				}
			}

			catch (Exception e) {
			}

			try { // capture & promotion
				if (Character.isLowerCase(Main.chessBoard[r - 1][c + j])
						&& r == 1) {
					char[] t = { 'Q', 'R', 'B', 'N' };
					for (int k = 0; k < 4; k++) {
						oldPiece = Main.chessBoard[r - 1][c + j];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r - 1][c + j] = t[k];
						if (kingSafe()) {
							// column1, column2, captured-piece, new-piece, P
							String tmp = String.valueOf(c)
									+ String.valueOf(c + j) + oldPiece + t[k]
									+ 'P';
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'P';
						Main.chessBoard[r - 1][c + j] = oldPiece;
					}

				}
			} catch (Exception e) {
			}
		}

		try { // move 1 up
			if (Main.chessBoard[r - 1][c] == ' ' && r >= 2) {
				Main.chessBoard[r][c] = ' ';
				Main.chessBoard[r - 1][c] = 'P';
				if (kingSafe()) {
					String tmp = String.valueOf(r) + String.valueOf(c)
							+ String.valueOf(r - 1) + String.valueOf(c) + ' ';
					list.add(tmp);
				}
				Main.chessBoard[r][c] = 'P';
				Main.chessBoard[r - 1][c] = ' ';
			}
		} catch (Exception e) {
		}

		try { // move 2 up
			if (r == 6 && Main.chessBoard[r - 1][c] == ' '
					&& Main.chessBoard[r - 2][c] == ' ') {
				Main.chessBoard[r][c] = ' ';
				Main.chessBoard[r - 2][c] = 'P';
				if (kingSafe()) {
					String tmp = String.valueOf(r) + String.valueOf(c)
							+ String.valueOf(r - 2) + String.valueOf(c) + ' ';
					list.add(tmp);
				}
				Main.chessBoard[r][c] = 'P';
				Main.chessBoard[r - 2][c] = ' ';
			}
		} catch (Exception e) {
		}

		try { // promotion without capture
			if (Main.chessBoard[r - 1][c] == ' ' && r == 1) {
				char[] t = { 'Q', 'R', 'B', 'N' };
				for (int k = 0; k < 4; k++) {
					oldPiece = Main.chessBoard[r - 1][c];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r - 1][c] = t[k];
					if (kingSafe()) {
						// column1, column2, captured-piece, new-piece, P
						String tmp = String.valueOf(c) + String.valueOf(c)
								+ oldPiece + t[k] + 'P';
						list.add(tmp);
					}
					Main.chessBoard[r][c] = 'P';
					Main.chessBoard[r - 1][c] = oldPiece;
				}
			}
		} catch (Exception e) {
		}

		return list;
	}

	private static ArrayList<String> possibleR(int i) {
		ArrayList<String> list = new ArrayList<String>();
		char oldPiece;
		int r = i / 8, c = i % 8;
		int t = 1;

		for (int j = -1; j <= 1; j += 2) {
			try {
				while (Main.chessBoard[r + t * j][c] == ' ') {
					oldPiece = Main.chessBoard[r + t * j][c];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r + t * j][c] = 'R';
					if (kingSafe()) {
						String tmp = String.valueOf(r) + String.valueOf(c)
								+ String.valueOf(r + t * j) + String.valueOf(c)
								+ (oldPiece == 'e' ? ' ' : oldPiece);
						list.add(tmp);
					}
					Main.chessBoard[r][c] = 'R';
					Main.chessBoard[r + t * j][c] = oldPiece;
					t++;
				}
				if (Character.isLowerCase(Main.chessBoard[r + t * j][c])) {
					oldPiece = Main.chessBoard[r + t * j][c];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r + t * j][c] = 'R';
					if (kingSafe()) {
						String tmp = String.valueOf(r) + String.valueOf(c)
								+ String.valueOf(r + t * j) + String.valueOf(c)
								+ (oldPiece == 'e' ? ' ' : oldPiece);
						list.add(tmp);
					}
					Main.chessBoard[r][c] = 'R';
					Main.chessBoard[r + t * j][c] = oldPiece;

				}
			} catch (Exception e) {
			}
			t = 1;
		}

		for (int k = -1; k <= 1; k += 2) {
			try {
				while (Main.chessBoard[r][c + t * k] == ' ') {
					oldPiece = Main.chessBoard[r][c + t * k];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r][c + t * k] = 'R';
					if (kingSafe()) {
						String tmp = String.valueOf(r) + String.valueOf(c)
								+ String.valueOf(r) + String.valueOf(c + t * k)
								+ (oldPiece == 'e' ? ' ' : oldPiece);
						list.add(tmp);
					}
					Main.chessBoard[r][c] = 'R';
					Main.chessBoard[r][c + t * k] = oldPiece;
					t++;
				}
				if (Character.isLowerCase(Main.chessBoard[r][c + t * k])) {
					oldPiece = Main.chessBoard[r][c + t * k];
					Main.chessBoard[r][c] = ' ';
					Main.chessBoard[r][c + t * k] = 'R';
					if (kingSafe()) {
						String tmp = String.valueOf(r) + String.valueOf(c)
								+ String.valueOf(r) + String.valueOf(c + t * k)
								+ (oldPiece == 'e' ? ' ' : oldPiece);
						list.add(tmp);
					}
					Main.chessBoard[r][c] = 'R';
					Main.chessBoard[r][c + t * k] = oldPiece;

				}
			} catch (Exception e) {
			}
			t = 1;
		}

		return list;
	}

	private static ArrayList<String> possibleN(int i) {
		ArrayList<String> list = new ArrayList<String>();
		char oldPiece;
		int r = i / 8, c = i % 8;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					if (Main.chessBoard[r + j][c + 2 * k] == ' '
							|| Character.isLowerCase(Main.chessBoard[r + j][c
									+ 2 * k])) {
						oldPiece = Main.chessBoard[r + j][c + 2 * k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r + j][c + 2 * k] = 'N';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r + j)
									+ String.valueOf(c + 2 * k)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'N';
						Main.chessBoard[r + j][c + 2 * k] = oldPiece;

					}
				} catch (Exception e) {
				}

				try {
					if (Main.chessBoard[r + 2 * j][c + k] == ' '
							|| Character
									.isLowerCase(Main.chessBoard[r + 2 * j][c
											+ k])) {
						oldPiece = Main.chessBoard[r + 2 * j][c + k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r + 2 * j][c + k] = 'N';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r + 2 * j)
									+ String.valueOf(c + k)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'N';
						Main.chessBoard[r + 2 * j][c + k] = oldPiece;

					}
				} catch (Exception e) {
				}
			}
		}
		return list;
	}

	private static ArrayList<String> possibleB(int i) {
		ArrayList<String> list = new ArrayList<String>();
		char oldPiece;
		int r = i / 8, c = i % 8;
		int t = 1;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					while (Main.chessBoard[r + t * j][c + t * k] == ' ') {
						oldPiece = Main.chessBoard[r + t * j][c + t * k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r + t * j][c + t * k] = 'B';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r + t * j)
									+ String.valueOf(c + t * k)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'B';
						Main.chessBoard[r + t * j][c + t * k] = oldPiece;
						t++;
					}
					if (Character.isLowerCase(Main.chessBoard[r + t * j][c + t
							* k])) {
						oldPiece = Main.chessBoard[r + t * j][c + t * k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r + t * j][c + t * k] = 'B';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r + t * j)
									+ String.valueOf(c + t * k)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'B';
						Main.chessBoard[r + t * j][c + t * k] = oldPiece;
					}
				} catch (Exception e) {
				}
				t = 1;
			}
		}
		return list;
	}

	private static ArrayList<String> possibleQ(int i) {
		ArrayList<String> list = new ArrayList<String>();
		char oldPiece;
		int r = i / 8, c = i % 8;
		int t = 1;
		for (int j = -1; j <= 1; j++) {
			for (int k = -1; k <= 1; k++) {
				try {
					while (Main.chessBoard[r + t * j][c + t * k] == ' ') {
						oldPiece = Main.chessBoard[r + t * j][c + t * k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r + t * j][c + t * k] = 'Q';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r + t * j)
									+ String.valueOf(c + t * k)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'Q';
						Main.chessBoard[r + t * j][c + t * k] = oldPiece;
						t++;
					}
					if (Character.isLowerCase(Main.chessBoard[r + t * j][c + t
							* k])) {
						oldPiece = Main.chessBoard[r + t * j][c + t * k];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r + t * j][c + t * k] = 'Q';
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r + t * j)
									+ String.valueOf(c + t * k)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'Q';
						Main.chessBoard[r + t * j][c + t * k] = oldPiece;

					}
				} catch (Exception e) {
				}
				t = 1;
			}
		}
		return list;
	}

	public static ArrayList<String> possibleK(int i) {
		ArrayList<String> list = new ArrayList<String>();
		char oldPiece;
		int r = i / 8, c = i % 8;

		for (int j = 0; j < 9; j++) {
			if (j != 4) {
				try {
					if (Character.isLowerCase(Main.chessBoard[r - 1 + j / 3][c
							- 1 + j % 3])
							|| Main.chessBoard[r - 1 + j / 3][c - 1 + j % 3] == ' ') {
						oldPiece = Main.chessBoard[r - 1 + j / 3][c - 1 + j % 3];
						Main.chessBoard[r][c] = ' ';
						Main.chessBoard[r - 1 + j / 3][c - 1 + j % 3] = 'K';
						int kingTemp = Main.kingPositionWhite;
						Main.kingPositionWhite = i + (j / 3) * 8 + j % 3 - 9;
						if (kingSafe()) {
							String tmp = String.valueOf(r) + String.valueOf(c)
									+ String.valueOf(r - 1 + j / 3)
									+ String.valueOf(c - 1 + j % 3)
									+ (oldPiece == 'e' ? ' ' : oldPiece);
							list.add(tmp);
						}
						Main.chessBoard[r][c] = 'K';
						Main.chessBoard[r - 1 + j / 3][c - 1 + j % 3] = oldPiece;
						Main.kingPositionWhite = kingTemp;
					}
				} catch (Exception e) {
				}
			}
		}

		// Castles
		if (whiteKingNumberOfMoves == 0 && Main.chessBoard[7][4] == 'K') {

			if (whiteKingRookNumberOfMoves == 0
					&& Main.kingRookPositionWhite == 63) {
				if (Main.chessBoard[7][5] == ' '
						&& Main.chessBoard[7][6] == ' ') {
					boolean notInCheck = kingSafe();
					Main.chessBoard[7][4] = ' ';
					Main.chessBoard[7][5] = 'K';
					boolean f1Safe = kingSafe();
					Main.chessBoard[7][5] = ' ';
					Main.chessBoard[7][6] = 'K';
					boolean g1Safe = kingSafe();
					Main.chessBoard[7][6] = ' ';
					Main.chessBoard[7][4] = 'K';
					if (notInCheck && f1Safe && g1Safe) {
						String tmp = "7476C";
						list.add(tmp);
					}
				}
			}

			if (whiteQueenRookNumberOfMoves == 0
					&& Main.queenRookPositionWhite == 56) {
				if (Main.chessBoard[7][3] == ' '
						&& Main.chessBoard[7][2] == ' '
						&& Main.chessBoard[7][1] == ' ') {
					boolean notInCheck = kingSafe();
					Main.chessBoard[7][4] = ' ';
					Main.chessBoard[7][3] = 'K';
					boolean d1Safe = kingSafe();
					Main.chessBoard[7][3] = ' ';
					Main.chessBoard[7][2] = 'K';
					boolean c1Safe = kingSafe();
					Main.chessBoard[7][2] = ' ';
					Main.chessBoard[7][4] = 'K';
					if (notInCheck && d1Safe && c1Safe) {
						String tmp = "7472C";
						list.add(tmp);
					}
				}
			}
		}

		return list;
	}

	public static boolean kingSafe() {
		// bishop/queen
		int t = 1;
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					while (Main.chessBoard[Main.kingPositionWhite / 8 + t * i][Main.kingPositionWhite
							% 8 + t * j] == ' ') {
						t++;
					}

					if (Main.chessBoard[Main.kingPositionWhite / 8 + t * i][Main.kingPositionWhite
							% 8 + t * j] == 'b'
							|| Main.chessBoard[Main.kingPositionWhite / 8 + t
									* i][Main.kingPositionWhite % 8 + t * j] == 'q') {
						return false;
					}
				} catch (Exception e) {
				}

				t = 1;

			}
		}

		// rook/queen
		for (int i = -1; i <= 1; i += 2) {

			try {
				while (Main.chessBoard[Main.kingPositionWhite / 8][Main.kingPositionWhite
						% 8 + t * i] == ' ') {
					t++;
				}

				if (Main.chessBoard[Main.kingPositionWhite / 8][Main.kingPositionWhite
						% 8 + t * i] == 'r'
						|| Main.chessBoard[Main.kingPositionWhite / 8][Main.kingPositionWhite
								% 8 + t * i] == 'q') {
					return false;
				}
			} catch (Exception e) {
			}

			t = 1;

			try {
				while (Main.chessBoard[Main.kingPositionWhite / 8 + t * i][Main.kingPositionWhite % 8] == ' ') {
					t++;
				}

				if (Main.chessBoard[Main.kingPositionWhite / 8 + t * i][Main.kingPositionWhite % 8] == 'r'
						|| Main.chessBoard[Main.kingPositionWhite / 8 + t * i][Main.kingPositionWhite % 8] == 'q') {
					return false;
				}
			} catch (Exception e) {
			}

			t = 1;
		}

		// knight
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					if (Main.chessBoard[Main.kingPositionWhite / 8 + i][Main.kingPositionWhite
							% 8 + 2 * j] == 'n')
						return false;

				} catch (Exception e) {
				}
				try {
					if (Main.chessBoard[Main.kingPositionWhite / 8 + 2 * i][Main.kingPositionWhite
							% 8 + j] == 'n')
						return false;

				} catch (Exception e) {
				}

			}
		}

		// pawn
		if (Main.kingPositionWhite >= 16) {
			try {
				if (Main.chessBoard[Main.kingPositionWhite / 8 - 1][Main.kingPositionWhite % 8 - 1] == 'p')
					return false;

			} catch (Exception e) {
			}

			try {
				if (Main.chessBoard[Main.kingPositionWhite / 8 - 1][Main.kingPositionWhite % 8 + 1] == 'p')
					return false;

			} catch (Exception e) {
			}
		}

		// king
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					try {
						if (Main.chessBoard[Main.kingPositionWhite / 8 + i][Main.kingPositionWhite
								% 8 + j] == 'k')
							return false;

					} catch (Exception e) {
					}
				}
			}
		}

		return true;
	}

	public static void makeMove(String move) {
		if (move.charAt(4) == 'e') {
			if (Character.getNumericValue(move.charAt(2)) == 2) {
				Main.chessBoard[Character.getNumericValue(move.charAt(2))][Character
						.getNumericValue(move.charAt(3))] = 'P';
				Main.chessBoard[Character.getNumericValue(move.charAt(0))][Character
						.getNumericValue(move.charAt(1))] = ' ';
				Main.chessBoard[Character.getNumericValue(move.charAt(0))][Character
						.getNumericValue(move.charAt(3))] = ' ';
				try {
					epStack.pop();
					setEPDepths(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		else {
			setEPDepths(1);

			int i = 0;
			while (i <= 63) {
				if (Main.chessBoard[i / 8][i % 8] == 'e') {
					Main.chessBoard[i / 8][i % 8] = ' ';
					break;
				}
				i++;
			}

			if (move.charAt(4) == 'C') {
				whiteCastled = true;
				int kingDest = Character.getNumericValue(move.charAt(3));

				if (kingDest == 6) { // G kolona - mala rokada
					Main.chessBoard[7][7] = ' ';
					Main.chessBoard[7][5] = 'R';
					Main.kingPositionWhite += 2;
					Main.kingRookPositionWhite = 61;
					whiteKingRookNumberOfMoves++;
				} else if (kingDest == 2) { // C kolona - velika rokada
					Main.chessBoard[7][0] = ' ';
					Main.chessBoard[7][3] = 'R';
					Main.kingPositionWhite -= 2;
					Main.queenRookPositionWhite = 59;
					whiteQueenRookNumberOfMoves++;
				}

				whiteKingNumberOfMoves++;
				Main.chessBoard[7][kingDest] = 'K';
				Main.chessBoard[7][4] = ' ';
			}

			else if (move.charAt(4) == 'P') {

				Main.chessBoard[1][Character.getNumericValue(move.charAt(0))] = ' ';
				Main.chessBoard[0][Character.getNumericValue(move.charAt(1))] = move
						.charAt(3);
				if (move.charAt(2) == 'r') {
					if (Character.getNumericValue(move.charAt(1)) == Main.kingRookPositionBlack) {
						Main.kingRookPositionBlackLast = Main.kingRookPositionBlack;
						Main.kingRookPositionBlack = -1;
					}

					if (Character.getNumericValue(move.charAt(1)) == Main.queenRookPositionBlack) {
						Main.queenRookPositionBlackLast = Main.queenRookPositionBlack;
						Main.queenRookPositionBlack = -1;
					}
				}
			} else {

				// Ako je odigran pesak za 2 polja
				try {
					if (Main.chessBoard[Character.getNumericValue(move
							.charAt(0))][Character.getNumericValue(move
							.charAt(1))] == 'P'
							&& Character.getNumericValue(move.charAt(0))
									- Character.getNumericValue(move.charAt(2)) == 2) {

						Main.chessBoard[Character.getNumericValue(move
								.charAt(2))][Character.getNumericValue(move
								.charAt(3))] = 'P';
						Main.chessBoard[Character.getNumericValue(move
								.charAt(0))][Character.getNumericValue(move
								.charAt(1))] = ' ';

						int enPassantSquare = 8
								* (Character.getNumericValue(move.charAt(0)) - 1)
								+ Character.getNumericValue(move.charAt(1));
						int enPassantDepth = 0;
						epStack.push(new EnPassantStruct(enPassantSquare,
								enPassantDepth));
					}

					else {
						if (move.charAt(4) == 'r') {
							if (Character.getNumericValue(move.charAt(2)) * 8
									+ Character.getNumericValue(move.charAt(3)) == Main.kingRookPositionBlack) {
								Main.kingRookPositionBlackLast = Main.kingRookPositionBlack;
								Main.kingRookPositionBlack = -1;

							}

							if (Character.getNumericValue(move.charAt(2)) * 8
									+ Character.getNumericValue(move.charAt(3)) == Main.queenRookPositionBlack) {
								Main.queenRookPositionBlackLast = Main.queenRookPositionBlack;
								Main.queenRookPositionBlack = -1;
							}
						}

						Main.chessBoard[Character.getNumericValue(move
								.charAt(2))][Character.getNumericValue(move
								.charAt(3))] = Main.chessBoard[Character
								.getNumericValue(move.charAt(0))][Character
								.getNumericValue(move.charAt(1))];

						Main.chessBoard[Character.getNumericValue(move
								.charAt(0))][Character.getNumericValue(move
								.charAt(1))] = ' ';

						switch (Main.chessBoard[Character.getNumericValue(move
								.charAt(2))][Character.getNumericValue(move
								.charAt(3))]) {
						case 'K':
							Main.kingPositionWhite = 8
									* Character.getNumericValue(move.charAt(2))
									+ Character.getNumericValue(move.charAt(3));
							whiteKingNumberOfMoves++;
							break;
						case 'R':
							if (Character.getNumericValue(move.charAt(0)) * 8
									+ Character.getNumericValue(move.charAt(1)) == Main.kingRookPositionWhite) {
								Main.kingRookPositionWhite = Character
										.getNumericValue(move.charAt(2))
										* 8
										+ Character.getNumericValue(move
												.charAt(3));
							}

							if (Character.getNumericValue(move.charAt(0)) * 8
									+ Character.getNumericValue(move.charAt(1)) == Main.queenRookPositionWhite) {
								Main.queenRookPositionWhite = Character
										.getNumericValue(move.charAt(2))
										* 8
										+ Character.getNumericValue(move
												.charAt(3));
							}
							break;
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void undoMove(String move) {
		if (move.charAt(4) == 'e') {
			Main.chessBoard[Character.getNumericValue(move.charAt(2))][Character
					.getNumericValue(move.charAt(3))] = ' ';
			Main.chessBoard[Character.getNumericValue(move.charAt(0))][Character
					.getNumericValue(move.charAt(1))] = 'P';
			Main.chessBoard[Character.getNumericValue(move.charAt(0))][Character
					.getNumericValue(move.charAt(3))] = 'p';
			int enPassantSquare = 8 * Character.getNumericValue(move.charAt(2))
					+ Character.getNumericValue(move.charAt(3));
			int enPassantDepth = 0;
			setEPDepths(-1);
			epStack.push(new EnPassantStruct(enPassantSquare, enPassantDepth));
		} else {
			setEPDepths(-1);

			if (!epStack.empty() && epStack.peek().depth == -1) {
				epStack.pop();
			}

			if (move.charAt(4) == 'C') {
				whiteCastled = false;
				int kingDest = Character.getNumericValue(move.charAt(3));

				if (kingDest == 6) { // G kolona - mala rokada
					Main.chessBoard[7][7] = 'R';
					Main.chessBoard[7][5] = ' ';
					Main.kingPositionWhite -= 2;
					Main.kingRookPositionWhite = 63;
					whiteKingRookNumberOfMoves = 0;
				} else if (kingDest == 2) { // C kolona - velika rokada
					Main.chessBoard[7][0] = 'R';
					Main.chessBoard[7][3] = ' ';
					Main.kingPositionWhite += 2;
					Main.queenRookPositionWhite = 56;
					whiteQueenRookNumberOfMoves = 0;
				}
				whiteKingNumberOfMoves = 0;
				Main.chessBoard[7][kingDest] = ' ';
				Main.chessBoard[7][4] = 'K';
			} else if (move.charAt(4) == 'P') {// if pawn promotion
				// col1, col2, captured-piece, new-piece, P
				if (move.charAt(2) == 'r') {
					if (Character.getNumericValue(move.charAt(1)) == Main.kingRookPositionBlackLast) {
						Main.kingRookPositionBlack = Main.kingRookPositionBlackLast;
					}

					if (Character.getNumericValue(move.charAt(1)) == Main.queenRookPositionBlackLast) {
						Main.queenRookPositionBlack = Main.queenRookPositionBlackLast;
					}
				}
				Main.chessBoard[1][Character.getNumericValue(move.charAt(0))] = 'P';
				Main.chessBoard[0][Character.getNumericValue(move.charAt(1))] = move
						.charAt(2);
			} else {

				if (move.charAt(4) == 'r') {
					if (8 * Character.getNumericValue(move.charAt(2))
							+ Character.getNumericValue(move.charAt(3)) == Main.kingRookPositionBlackLast) {
						Main.kingRookPositionBlack = Main.kingRookPositionBlackLast;
					}

					if (8 * Character.getNumericValue(move.charAt(2))
							+ Character.getNumericValue(move.charAt(3)) == Main.queenRookPositionBlackLast) {
						Main.queenRookPositionBlack = Main.queenRookPositionBlackLast;
					}
				}

				Main.chessBoard[Character.getNumericValue(move.charAt(0))][Character
						.getNumericValue(move.charAt(1))] = Main.chessBoard[Character
						.getNumericValue(move.charAt(2))][Character
						.getNumericValue(move.charAt(3))];

				Main.chessBoard[Character.getNumericValue(move.charAt(2))][Character
						.getNumericValue(move.charAt(3))] = move.charAt(4);

				switch (Main.chessBoard[Character.getNumericValue(move
						.charAt(0))][Character.getNumericValue(move.charAt(1))]) {
				case 'K':
					Main.kingPositionWhite = 8
							* Character.getNumericValue(move.charAt(0))
							+ Character.getNumericValue(move.charAt(1));
					whiteKingNumberOfMoves--;
					break;
				case 'R':
					if (Character.getNumericValue(move.charAt(2)) * 8
							+ Character.getNumericValue(move.charAt(3)) == Main.kingRookPositionWhite) {
						Main.kingRookPositionWhite = Character
								.getNumericValue(move.charAt(0))
								* 8
								+ Character.getNumericValue(move.charAt(1));
					}

					if (Character.getNumericValue(move.charAt(2)) * 8
							+ Character.getNumericValue(move.charAt(3)) == Main.queenRookPositionWhite) {
						Main.queenRookPositionWhite = Character
								.getNumericValue(move.charAt(0))
								* 8
								+ Character.getNumericValue(move.charAt(1));
					}
					break;
				}

			}
		}
	}

	public static ArrayList<String> sortMoves(ArrayList<String> list) {
		int[] score = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			makeMove(list.get(i));
			score[i] = Rating.rating(-1, 0);
			undoMove(list.get(i));
		}
		ArrayList<String> newListA = new ArrayList<String>();
		ArrayList<String> newListB = list;
		for (int i = 0; i < list.size(); i++) {// first few moves only
			int max = -1000000, maxLocation = 0;
			for (int j = 0; j < list.size(); j++) {
				if (score[j] >= max) {
					/*
					 * int tmp = score[j]; score[j] = score[i]; score[i] = tmp;
					 * 
					 * String tmp1 = list.get(j); list.set(j, list.get(i));
					 * list.set(i,tmp1);
					 */
					max = score[j];
					maxLocation = j;
				}
			}
			score[maxLocation] = -1000000;
			newListA.add(list.get(maxLocation));
			newListB.remove(list.get(maxLocation));
		}
		newListA.addAll(newListB);
		return newListA;
	}

	public static void setEPDepths(int k) {
		for (EnPassantStruct struct : epStack) {
			struct.depth += k;
		}
	}

	public static void flipEPSquares() {
		for (EnPassantStruct struct : epStack) {
			struct.square = 8 * (7 - struct.square / 8) + struct.square % 8;
		}
	}

	public static String moveNotate(String move, boolean white) {
		String notate = "";
		if (move.charAt(4) != 'P') {

			int rowStart;
			int rowEnd;
			if (!white) {
				rowStart = 8 - Character.getNumericValue(move.charAt(0));
				rowEnd = 8 - Character.getNumericValue(move.charAt(2));
			} else {
				rowStart = Character.getNumericValue(move.charAt(0)) + 1;
				rowEnd = Character.getNumericValue(move.charAt(2)) + 1;
			}

			char colStart;

			char colEnd;

			String abcdefgh = "abcdefgh";

			colStart = abcdefgh
					.charAt(Character.getNumericValue(move.charAt(1)));
			colEnd = abcdefgh.charAt(Character.getNumericValue(move.charAt(3)));

			notate += colStart + Integer.toString(rowStart) + colEnd
					+ Integer.toString(rowEnd) + move.charAt(4);
		}
		return notate;
	}
}
