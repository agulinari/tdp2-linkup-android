package com.tddp2.grupo2.linkup.infrastructure.client.response;

import com.tddp2.grupo2.linkup.model.Block;

public class BlockResponse {

    private Block block;

    public BlockResponse(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
