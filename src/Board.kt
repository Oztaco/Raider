import java.awt.Point

class Board {
    var board = Array<Piece>(64, { Piece.EMPTY });
    var playerTurn = PlayerColor.WHITE;
    var kingMoved = false;
    var moveHistory = listOf<MoveHistoryItem>();


    /**
     * Gets the piece at the specified board location.
     * Uses 1-indexing
     */
    fun getPiece(column: Int, row:Int): Piece {
        var c = column - 1;
        var r = row - 1;

        var index = (r * 8) + c;

        return board[index];
    }


    /**
     * Sets the piece at the specified board location.
     * Uses 1-indexing
     */
    fun setPiece(column: Int, row:Int, value:Piece) {
        var c = column - 1;
        var r = row - 1;

        var index = (r * 8) + c;

        board[index] = value;
    }


    override fun toString(): String {
        var value = "\n";
        for (r in 1..8) {
            value += r.toString() + " | ";
            for (c in 1..8) {
                value += getPiece(c, r).displaySymbol();
            }
            value += "\n";
        }
        value += "\n     A  B  C  D  E  F  G  H"
        return value;
    }


    fun setRow(row: Int, one: Piece, two: Piece,
               three: Piece, four: Piece, five: Piece,
               six: Piece, seven: Piece, eight: Piece) {
        setPiece(1, row, one);
        setPiece(2, row, two);
        setPiece(3, row, three);
        setPiece(4, row, four);
        setPiece(5, row, five);
        setPiece(6, row, six);
        setPiece(7, row, seven);
        setPiece(8, row, eight);
    }


    /**
     * Fills the board with empty spaces
     */
    fun clearBoard() {
        for (i in 0 until board.size) {
            board[i] = Piece.EMPTY;
        }
    }


    /**
     * Fills the board with the standard chess set-up
     */
    fun setBoardUp() {
        clearBoard();

        setRow(1, Piece.ROOK_WHITE, Piece.KNIGHT_WHITE,
            Piece.BISHOP_WHITE, Piece.QUEEN_WHITE, Piece.KING_WHITE,
            Piece.BISHOP_WHITE, Piece.KNIGHT_WHITE, Piece.ROOK_WHITE);
        setRow(2, Piece.PAWN_WHITE, Piece.PAWN_WHITE,
            Piece.PAWN_WHITE, Piece.PAWN_WHITE, Piece.PAWN_WHITE,
            Piece.PAWN_WHITE, Piece.PAWN_WHITE, Piece.PAWN_WHITE);

        setRow(8, Piece.ROOK_BLACK, Piece.KNIGHT_BLACK,
            Piece.BISHOP_BLACK, Piece.QUEEN_BLACK, Piece.KING_BLACK,
            Piece.BISHOP_BLACK, Piece.KNIGHT_BLACK, Piece.ROOK_BLACK);
        setRow(7, Piece.PAWN_BLACK, Piece.PAWN_BLACK,
            Piece.PAWN_BLACK, Piece.PAWN_BLACK, Piece.PAWN_BLACK,
            Piece.PAWN_BLACK, Piece.PAWN_BLACK, Piece.PAWN_BLACK);
    }


    enum class Piece {
        KING_WHITE, QUEEN_WHITE, ROOK_WHITE, BISHOP_WHITE, KNIGHT_WHITE, PAWN_WHITE,
        KING_BLACK, QUEEN_BLACK, ROOK_BLACK, BISHOP_BLACK, KNIGHT_BLACK, PAWN_BLACK,
        EMPTY;

        fun type(): PieceType {
            return when (this) {
                KING_WHITE, KING_BLACK     -> PieceType.KING
                QUEEN_WHITE, QUEEN_BLACK   -> PieceType.QUEEN
                ROOK_WHITE, ROOK_BLACK     -> PieceType.ROOK
                BISHOP_WHITE, BISHOP_BLACK -> PieceType.BISHOP
                KNIGHT_WHITE, KNIGHT_BLACK -> PieceType.KNIGHT
                PAWN_WHITE, PAWN_BLACK     -> PieceType.PAWN
                EMPTY                      -> PieceType.EMPTY
            }
        }

        fun color(): PieceColor {
            return when (this) {
                KING_WHITE, QUEEN_WHITE,
                ROOK_WHITE, BISHOP_WHITE,
                KNIGHT_WHITE, PAWN_WHITE   -> PieceColor.WHITE
                KING_BLACK, QUEEN_BLACK,
                ROOK_BLACK, BISHOP_BLACK,
                KNIGHT_BLACK, PAWN_BLACK   -> PieceColor.BLACK
                EMPTY                      -> PieceColor.EMPTY
            }
        }

        fun displaySymbol(): String {
            return when (this) {
                KING_WHITE   -> " K "
                KING_BLACK   -> "_K_"
                QUEEN_WHITE  -> " Q "
                QUEEN_BLACK  -> "_Q_"
                ROOK_WHITE   -> " R "
                ROOK_BLACK   -> "_R_"
                BISHOP_WHITE -> " B "
                BISHOP_BLACK -> "_B_"
                KNIGHT_WHITE -> " N "
                KNIGHT_BLACK -> "_N_"
                PAWN_WHITE   -> " P "
                PAWN_BLACK   -> "_P_"
                EMPTY        -> " . "
            }
        }
    }

    enum class PieceType {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, EMPTY;

        fun moves(): Array<Array<IntRange>> {
            return when (this) {
                KING -> arrayOf(
                    arrayOf( 1.. 1,  0.. 0), // Straight
                    arrayOf( 0.. 0,  1.. 1),
                    arrayOf(-1..-1,  0.. 0),
                    arrayOf( 0.. 0, -1..-1),
                    arrayOf( 1.. 1,  1.. 1), // Diagonal
                    arrayOf(-1..-1, -1..-1),
                    arrayOf( 1.. 1, -1..-1),
                    arrayOf(-1..-1,  1.. 1)
                )
                QUEEN -> arrayOf(
                    arrayOf( 1.. 7,  0.. 0), // X
                    arrayOf(-1..-7,  0.. 0),
                    arrayOf( 0.. 0,  1.. 7), // Y
                    arrayOf( 0.. 0, -1..-7),
                    arrayOf( 1.. 7,  1.. 7), // Downward right
                    arrayOf(-1..-7, -1..-7), // Upward left
                    arrayOf( 1.. 7, -1..-7), // Upward right
                    arrayOf(-1..-7,  1.. 7)  // Downward left
                )
                ROOK -> arrayOf(
                    arrayOf( 1.. 7,  0.. 0), // X
                    arrayOf(-1..-7,  0.. 0),
                    arrayOf( 0.. 0,  1.. 7), // Y
                    arrayOf( 0.. 0, -1..-7)
                )
                BISHOP -> arrayOf(
                    arrayOf( 1.. 7,  1.. 7), // Downward right
                    arrayOf(-1..-7, -1..-7), // Upward left
                    arrayOf( 1.. 7, -1..-7), // Upward right
                    arrayOf(-1..-7,  1.. 7)  // Downward left
                )
                KNIGHT -> arrayOf(
                    arrayOf( 1.. 1,  -2.. -2), // Top right
                    arrayOf( 2.. 2,  -1.. -1),
                    arrayOf( 1.. 1,   2..  2), // Bottom right
                    arrayOf( 2.. 2,   1..  1),
                    arrayOf(-1..-1,   2..  2), // Bottom left
                    arrayOf(-2..-2,   1..  1),
                    arrayOf(-1..-1,  -2.. -2), // Top left
                    arrayOf(-2..-2,  -1.. -1)
                )
                PAWN -> arrayOf(
                    arrayOf( 0.. 0,   2..  2)
                )
                else -> Array<Array<IntRange>>(0, {
                    Array<IntRange>(0, {0..0})
                })
            }
        }
    }

    enum class PieceColor {
        BLACK, WHITE, EMPTY
    }

    enum class PlayerColor {
        BLACK, WHITE
    }

    enum class MoveType {
        MOVE, TAKE, CASTLE, EN_PASSANT, PROMOTE
    }


    class MoveHistoryItem(
        piece: PieceType, origin: Point,
        moveType: MoveType, destination: Point) {

    }

}