package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.tiles.NormalTile;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;
import cz.cvut.fel.pjv.arimaa.model.tiles.Trap;

public class Board {
    private final Tile[][] tiles;

    public Board() {
        tiles = new Tile[8][8];

        // Create NormalTile objects for all tiles
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new NormalTile(i, j);
            }
        }

        // Create Trap objects for the specified locations
        tiles[3][3] = new Trap(3, 3);
        tiles[3][6] = new Trap(3, 6);
        tiles[6][3] = new Trap(6, 3);
        tiles[6][6] = new Trap(6, 6);
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
