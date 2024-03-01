    package com.example.pm_gamecenter.game2048;

    import android.content.Context;
    import android.util.DisplayMetrics;
    import android.util.Log;
    import android.widget.GridLayout;
    import com.example.pm_gamecenter.R;
    import androidx.appcompat.view.ContextThemeWrapper;

    public class Grid extends GridLayout {
        private int gridWidth, gridHeight;
        private final Block[][] gridMatrix;
        private Context gameBlockStyledContext = new ContextThemeWrapper(this.getContext(), R.style.GameBlockStyle); // Context with custom style is created
        private MergeListener mergeListener;

        public Grid(Context context, int gridWidth, int gridHeight) {
            super(context);
            this.gridWidth = gridWidth;
            this.gridHeight = gridHeight;
            this.gridMatrix = new Block[gridHeight][gridWidth];
            this.initGrid();
            this.valueToRandomGameBlock();
        }

        public void valueToRandomGameBlock() {
            int counter = 0;
            int randomWidth = (int) (Math.random() * gridWidth);
            int randomHeight = (int) (Math.random() * gridHeight);
            boolean gameOver = false;

            // If random position value isn't empty (0) regenerate a random position.
            while (gridMatrix[randomWidth][randomHeight].getValue() != 0 && !gameOver) {
                if (counter > gridWidth + gridHeight) {
                    gameOver = true;
                }

                randomWidth = (int) (Math.random() * gridWidth);
                randomHeight = (int) (Math.random() * gridHeight);

                counter++;
            }

            if (gameOver) {
                // Player loses
            } else {
                gridMatrix[randomWidth][randomHeight].setValue(2); // Add new gameBlock
            }
        }


        public void handleSweep(String direction) {
            boolean[][] merged = new boolean[gridHeight][gridWidth]; // Keep track of merges

            if (direction.equals("LEFT") || direction.equals("UP")) {
                // Move and merge from left-to-right or top-to-bottom
                for (int row = 0; row < gridHeight; row++) {
                    for (int col = 0; col < gridWidth; col++) {
                        Log.d("Direction", "Direction: " + direction);
                        moveAndMergeTiles(row, col, direction, merged);
                    }
                }
            } else if (direction.equals("RIGHT") || direction.equals("DOWN")) {

                for (int row = gridHeight - 1; row >= 0; row--) {
                    for (int col = gridWidth - 1; col >= 0; col--) {
                        Log.d("Direction", "Direction: " + direction);
                        moveAndMergeTiles(row, col, direction, merged);
                    }
                }
            }
        }

        private void moveAndMergeTiles(int row, int col, String direction, boolean[][] merged) {
            boolean sweepHandled = false;

            if (gridMatrix[row][col].getValue() == 0 || merged[row][col]) {
                return; // Skip empty cells
            }

            int currentRow = row, currentCol = col;
            int nextRow = row, nextCol = col;

            switch (direction) {
                case "UP":
                    nextRow--;
                    break;
                case "DOWN":
                    nextRow++;
                    break;
                case "LEFT":
                    nextCol--;
                    break;
                case "RIGHT":
                    nextCol++;
                    break;
            }

            // While in bounds
            while (inBounds(nextRow, nextCol, gridHeight, gridWidth) && !sweepHandled) {
                if (this.gridMatrix[nextRow][nextCol].getValue() == 0) {
                    // Move the tile
                    this.gridMatrix[nextRow][nextCol].setValue(gridMatrix[currentRow][currentCol].getValue());
                    this.gridMatrix[currentRow][currentCol].setValue(0);
                    currentRow = nextRow;
                    currentCol = nextCol;
                } else if (gridMatrix[currentRow][currentCol].getValue() == gridMatrix[nextRow][nextCol].getValue() && !merged[nextRow][nextCol]) {
                    // Merge the tiles

                    gridMatrix[nextRow][nextCol].setValue(gridMatrix[nextRow][nextCol].getValue() * 2);
                    if (mergeListener != null) {
                        mergeListener.onMerged(gridMatrix[nextRow][nextCol].getValue());
                    }
                    gridMatrix[currentRow][currentCol].setValue(0);
                    merged[nextRow][nextCol] = true;
                    sweepHandled = true;
                } else {
                    sweepHandled = true; // Stop if we hit a non-matching tile
                }

                if (direction.equals("UP")) nextRow--;
                else if (direction.equals("DOWN")) nextRow++;
                else if (direction.equals("LEFT")) nextCol--;
                else if (direction.equals("RIGHT")) nextCol++;

            }
        }

        public boolean inBounds(int nextRow, int nextCol, int height, int width) {
            if (nextRow >= 0 && nextRow < height && nextCol >= 0 && nextCol < width){
                return true;
            }
            else {
                return false;
            }
        }
        public void addBlockToMatrix(Block b) {
            gridMatrix[b.getPosX()][b.getPosY()] = b;
        }
        public void initGrid() {
            int spacing = (int) (getDisplayWidth()*0.012f);

            for (int col = 0; col < gridWidth; col++) {
                for (int row = 0; row < gridHeight; row++) {
                    Block b = new Block(gameBlockStyledContext, col, row, 0);

                    b.getTextView().setTextSize(getResponsiveTextSize(b.getValue()));
                    this.addBlockToMatrix(b);
                    this.addView(b, createGridParams(col, row, spacing));

                }
            }
        }
        public void resetGrid(){


            for (Block[] row : this.getGameBlockMatrix()) {
                for (Block b : row) {
                    b.setValue(0);
                }
            }

            valueToRandomGameBlock();
        }
        public int getDisplayWidth(){
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            return displayMetrics.widthPixels;
        }
        public int getResponsiveTextSize(int val) {
            int baseSize = (int) (getDisplayWidth()*0.05);
            float scaleFactor = 0.83f;

            int valDigits = String.valueOf(val).length();

            return (int) (baseSize * Math.pow(scaleFactor, valDigits - 1));
        }
        public GridLayout.LayoutParams createGridParams(int x, int y, int spacing) {

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(x);
            params.columnSpec = GridLayout.spec(y);
            params.setMargins(spacing,spacing,spacing,spacing);
            this.setPadding(spacing, spacing, spacing, spacing);

            return params;
        }




        // GETTERS and SETTERS
        public int getGridWidth() {
            return gridWidth;
        }
        public void setGridWidth(int gridWidth) {
            this.gridWidth = gridWidth;
        }
        public int getGridHeight() {
            return gridHeight;
        }
        public void setGridHeight(int gridHeight) {
            this.gridHeight = gridHeight;
        }
        public Block[][] getGameBlockMatrix() {
            return gridMatrix;
        }
        public void setMergeListener(MergeListener mergeListener) {
            this.mergeListener = mergeListener;
        }
    }
