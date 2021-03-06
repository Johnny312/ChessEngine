package mainapp;

public class Rating {
    static int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20, -5, -10,-10,-5, 20, 20},
        { 20, 35, 10, -10,  0, 10, 35, 20}};
    static int kingEndBoard[][]={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30, 0,  10,  0,  0, 10,  0,-30},
        {-30, 10, 25, 30, 30, 25, 10,-30},
        {-30, 10, 35, 40, 40, 35, 10,-30},
        {-30, 10, 35, 40, 40, 35, 10,-30},
        {-30, 10, 25, 30, 30, 25, 10,-30},
        {-30,- 0,  10,  0,  0, 10, 0,-30},
        {-50,-35,-30,-25,-25,-30,-35,-50}};
    
    
    public static int aggressivines = 1;
    public static int moveablility = 1;
    public static int positionaliness = 1;
    public static int materialist = 1;
    public static boolean mode3 = false;
    
    
	public static int rating(int list, int depth) {
		int counter = 0, material = rateMaterial();
		counter += rateAttack();
		counter += material;
		counter += rateMoveability(list, depth, material);
		counter += ratePositional(material);
		counter += !Moves.whiteCastled && Moves.whiteKingNumberOfMoves != 0 && material >= 2000 ? -40 : 40;
		counter += Moves.whiteCastled ? 20 : -20;
		Main.flipBoard();
		material = rateMaterial();
		counter -= rateAttack();
		counter -= material;
		counter -= rateMoveability(list, depth, material);
		counter -= ratePositional(material);
		counter -= !Moves.whiteCastled && Moves.whiteKingNumberOfMoves != 0 && material >= 2000 ? -40 : 40;
		counter -= Moves.whiteCastled ? 20 : -20;
		Main.flipBoard();
		return counter + depth * 50;
	}

	public static int rateAttack() {
		int counter = 0;
		int tempPositionC = Main.kingPositionWhite;
		for (int i = 0; i < 64; i++) {
			switch (Main.chessBoard[i / 8][i % 8]) {
			case 'P': {
				Main.kingPositionWhite = i;
				if (!Moves.kingSafe()) {
					counter -= 20;
				}
			}
				break;
			case 'R': {
				Main.kingPositionWhite = i;
				if (!Moves.kingSafe()) {
					counter -= 100;
				}
			}
				break;
			case 'N': {
				Main.kingPositionWhite = i;
				if (!Moves.kingSafe()) {
					counter -= 58;
				}
			}
				break;
			case 'B': {
				Main.kingPositionWhite = i;
				if (!Moves.kingSafe()) {
					counter -= 62;
				}
			}
				break;
			case 'Q': {
				Main.kingPositionWhite = i;
				if (!Moves.kingSafe()) {
					counter -= 200;
				}
			}
				break;
			}
		}
		Main.kingPositionWhite = tempPositionC;
		
		if (!Moves.kingSafe()) {
			counter -= 101;
		}
		return counter * (3/2) * aggressivines;
	}

	public static int rateMaterial() {
		int counter = 0, bishopCounter = 0;
		for (int i = 0; i < 64; i++) {
			switch (Main.chessBoard[i / 8][i % 8]) {
			case 'P':
				counter += 100;
				break;
			case 'R':
				counter += 500;
				break;
			case 'N':
				counter += 300;
				break;
			case 'B':
				bishopCounter += 1;
				break;
			case 'Q':
				counter += 900;
				break;
			}
		}
		if (bishopCounter >= 2) {
			counter += 350 * bishopCounter;
		} else {
			if (bishopCounter == 1) {
				counter += 300;
			}
		}
		return counter * materialist;
	}

	public static int rateMoveability(int listLength, int depth, int material) {
		int counter = 0;

		counter += listLength*5;// 5 pointer per valid move
		if (listLength == 0) {// current side is in checkmate or stalemate
			if (!Moves.kingSafe()) {// if checkmate
				counter += -200000 * depth;
			} else {// if stalemate
				counter += -150000*depth;
			}
		}
		return counter * (5/4) * moveablility;
	}

	public static int ratePositional(int material) {
		int counter = 0;
		for (int i = 0; i < 64; i++) {
			switch (Main.chessBoard[i / 8][i % 8]) {
			case 'P':
				counter += pawnBoard[i / 8][i % 8];
				break;
			case 'R':
				counter += rookBoard[i / 8][i % 8];
				break;
			case 'N':
				counter += knightBoard[i / 8][i % 8];
				break;
			case 'B':
				counter += bishopBoard[i / 8][i % 8];
				break;
			case 'Q':
				counter += queenBoard[i / 8][i % 8];
				break;
			case 'K':
				if (material >= 1750) {
					counter += kingMidBoard[i / 8][i % 8] * 5;
					//counter += Moves.possibleK(Main.kingPositionWhite).size() * 50;
				} else {
					counter += kingEndBoard[i / 8][i % 8];
					counter += Moves.possibleK(Main.kingPositionWhite).size() * 150;
				}
				break;
			}
		}
		return counter * (4/5) * positionaliness;
	}
}
