package aoc2023;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Block {
  Block(char c, int x, int y) {
    this._c = c;
    this._touched = false;
    this._blockCoord = new BlockCoord(x,y);
  }

  @Override
  public String toString() {
    return Character.toString(this._c);
  }

  List<Block> targets(List<Block> currentPath, BlockDiagram blockDiagram) {
    int currentPathSize = currentPath.size();
    int excludeDirection = BlockDirectionNone;
    if (currentPathSize >= 3) {
      int prevDirection = Block.direction(currentPath, currentPathSize-1, currentPathSize-2);
      int prevPrevDirection = Block.direction(currentPath, currentPathSize-2, currentPathSize-3);

      if (prevDirection == prevPrevDirection) {
        excludeDirection = prevDirection;
      }
    }
    if (_blockCoord.isMinX(blockDiagram)) {
      excludeDirection |= BlockDirectionUp;
    } else if (_blockCoord.isMaxX(blockDiagram)) {
      excludeDirection |= BlockDirectionDown;
    }
    if (_blockCoord.isMinY(blockDiagram)) {
      excludeDirection |= BlockDirectionLeft;
    } else if (_blockCoord.isMaxY(blockDiagram)) {
      excludeDirection |= BlockDirectionRight;
    }

    List<Block> nextBlock = new ArrayList<Block>();
    if ((excludeDirection & BlockDirectionUp) == 0) {
      nextBlock.add(up(blockDiagram));
    }
    if ((excludeDirection & BlockDirectionDown) == 0) {
      nextBlock.add(down(blockDiagram));
    }
    if ((excludeDirection & BlockDirectionLeft) == 0) {
      nextBlock.add(left(blockDiagram));
    }
    if ((excludeDirection & BlockDirectionRight) == 0) {
      nextBlock.add(right(blockDiagram));
    }
    return nextBlock;
  }

  boolean touched() {
    return _touched;
  }
  void clear() {
    _touched = false;
  }
  void touch() {
    _touched = true;
  }

  private Block up(BlockDiagram blockDiagram) {
    return blockDiagram.blockMap()[_blockCoord.x()][_blockCoord.y()-1];
  }
  private Block down(BlockDiagram blockDiagram) {
    return blockDiagram.blockMap()[_blockCoord.x()][_blockCoord.y()+1];
  }
  private Block left(BlockDiagram blockDiagram) {
    return blockDiagram.blockMap()[_blockCoord.x()-1][_blockCoord.y()];
  }
  private Block right(BlockDiagram blockDiagram) {
    return blockDiagram.blockMap()[_blockCoord.x()+1][_blockCoord.y()];
  }

  private boolean beforeX(Block secondBlock) {
    return _blockCoord.x() < secondBlock._blockCoord.x();
  }
  private boolean afterX(Block secondBlock) {
    return _blockCoord.x() > secondBlock._blockCoord.x();
  }
  private boolean beforeY(Block secondBlock) {
    return _blockCoord.y() < secondBlock._blockCoord.y();
  }
  private boolean afterY(Block secondBlock) {
    return _blockCoord.y() > secondBlock._blockCoord.y();
  }

  private static int direction(List<Block> path, int first, int second) {
    Block firstBlock = path.get(first);
    Block secondBlock = path.get(second);
    if (firstBlock.beforeX(secondBlock)) {
      return BlockDirectionRight;
    } else if (firstBlock.afterX(secondBlock)) {
      return BlockDirectionLeft;
    } else if (firstBlock.beforeY(secondBlock)) {
      return BlockDirectionDown;
    } else if (firstBlock.afterY(secondBlock)) {
      return BlockDirectionUp;
    }
    return BlockDirectionNone;
  }

  private BlockCoord _blockCoord;
  private char _c;
  private boolean _touched;

  static final int BlockDirectionNone = 0x0;
  static final int BlockDirectionUp   = 0x1;
  static final int BlockDirectionDown = 0x2;
  static final int BlockDirectionRight= 0x4;
  static final int BlockDirectionLeft = 0x8;
}
