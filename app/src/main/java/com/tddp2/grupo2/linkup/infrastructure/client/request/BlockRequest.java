package com.tddp2.grupo2.linkup.infrastructure.client.request;

import com.tddp2.grupo2.linkup.model.Block;

import java.io.Serializable;

public class BlockRequest implements Serializable {

    private Block block;

    public BlockRequest(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
