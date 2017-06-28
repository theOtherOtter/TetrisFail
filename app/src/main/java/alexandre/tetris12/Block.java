package alexandre.tetris12;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Block implements Serializable {

	public enum BlockColor {
		RED(0xff990000, (byte) 2), GREEN(0xff009900, (byte) 3), BLUE(
				0xff000099, (byte) 4), YELLOW(0xffffcc33, (byte) 5), CYAN(
				0xff3399aa, (byte) 6);
		private final int color;
		private final byte value;

		private BlockColor(int color, byte value) {
			this.color = color;
			this.value = value;
		}
	}
	//Une case est-elle vide ou active ?
	public static final byte CELL_EMPTY = 0;
	public static final byte CELL_DYNAMIC = 1;

	private static Random random = new Random();

	//Le bloc à son initiation
	private int shape = 0;
	private int frame = 0;
	private Point topLeft = new Point( Model.NUM_COLS / 2, 0);
	private BlockColor color;

	public int getFrame() {
		return frame;
	}

	public int getColor() {
		return color.color;
	}

	public byte getStaticValue() {
		return color.value;
	}

	public static int getColorForStaticValue(byte b) {
		for (BlockColor item : BlockColor.values()) {
			if (b == item.value) {
				return item.color;
			}
		}
		return -1; //pas de couleur trouvée
	}

	public final void setState(int frame, Point topLeft) {
		this.frame = frame;
		this.topLeft = topLeft;
	}

	public final int getFramesCount() {
		return Shape.values()[shape].frameCount;
	}

	public final byte[][] getShape(int nFrame) {
		return Shape.values()[shape].getFrame(nFrame).get();
	}

	//On génère un tetrimino sans qu'il ne puisse être altéré (synchronised)
	public final static synchronized Block createBlock() {
		//Forme aléatoire du tetrimino
		int indexShape = random.nextInt(Shape.values().length);
		//Couleur aléatoire du tetrimino
		BlockColor blockColor = BlockColor.values()[random.nextInt(BlockColor
				.values().length)];
		//On crée le tetrimino
		Block result = new Block(indexShape, blockColor);
		//On le place au milieu grâce à topleft (donc placé sur la 4ème case de notre tableau de 10 colonnes)
		result.topLeft.setX( result.topLeft.getX() - Shape.values()[indexShape].getStartMiddleX());
		
		return result;
		
	}

	private Block(int nShape, BlockColor blockColor) {
		shape = nShape;
		this.color = blockColor;
	}

	public Point getTopLeft() {
		return this.topLeft;
	}

	//Liste des tetriminos ainsi que leurs différentes positions, pour pouvoir les tourner
	private enum Shape {
		//Le tetrimino O
		S1(1, 1) {
			@Override
			public Frame getFrame(int n) {
				return new Frame(2).add("11").add("11");
			}

		},
		//Le tetrimino I
		S2(2, 2) {

			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:

					return new Frame(4).add("1111");
				case 1:
					return new Frame(1).add("1").add("1").add("1").add("1");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		//Le tetrimino T
		S3(4, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("010").add("111");
				case 1:
					return new Frame(2).add("10").add("11").add("10");
				case 2:
					return new Frame(3).add("111").add("010");
				case 3:
					return new Frame(2).add("01").add("11").add("01");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		//Le tetrimino J
		S4(4, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("100").add("111");
				case 1:
					return new Frame(2).add("11").add("10").add("10");
				case 2:
					return new Frame(3).add("111").add("001");
				case 3:
					return new Frame(2).add("01").add("01").add("11");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		//Le tetrimino L
		S5(4, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("001").add("111");
				case 1:
					return new Frame(2).add("10").add("10").add("11");
				case 2:
					return new Frame(3).add("111").add("100");
				case 3:
					return new Frame(2).add("11").add("01").add("01");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		//Le tetrimino Z
		S6(2, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("110").add("011");
				case 1:
					return new Frame(2).add("01").add("11").add("10");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		//Le tetrimino S
		S7(2, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("011").add("110");
				case 1:
					return new Frame(2).add("10").add("11").add("01");

				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		};
		private final int frameCount;
		private final int startMiddleX;

		private Shape(int frameCount, int startMiddleX) {
			this.frameCount = frameCount;
			this.startMiddleX = startMiddleX;
		}
		
		private int getStartMiddleX() {
			return startMiddleX;
		}

		public abstract Frame getFrame(int n);
	}

	//Gestion des pièces
	private static class Frame {
		private final int width;

		private Frame(int width) {
			this.width = width;
		}

		private final List<byte[]> data = new ArrayList<byte[]>(4);

		//On ajoute les lignes d'une pièce pour créer la pièce complète
		private Frame add(String rowStr) {
			byte[] row = new byte[rowStr.length()];
			for (int i = 0; i < rowStr.length(); i++) {
				row[i] = Byte.valueOf("" + rowStr.charAt(i));
			}
			data.add(row);
			return this;
		}

		//On récupère la pièce finie
		private byte[][] get() {
			byte[][] result = new byte[data.size()][];
			return data.toArray(result);
		}
	}
}
