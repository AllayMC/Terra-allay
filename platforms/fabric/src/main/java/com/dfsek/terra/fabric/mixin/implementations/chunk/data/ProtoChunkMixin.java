/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.fabric.mixin.implementations.chunk.data;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.fabric.generation.BiomeProviderHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(net.minecraft.world.chunk.ProtoChunk.class)
@Implements(value = {
        @Interface(iface = ProtoChunk.class, prefix = "terra$"),
        @Interface(iface = BiomeProviderHolder.class, prefix = "provider$")
})
public abstract class ProtoChunkMixin {
    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos pos);
    
    @Shadow
    public abstract HeightLimitView getHeightLimitView();
    
    private BiomeProvider biomeProvider;
    
    public void terra$setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        ((net.minecraft.world.chunk.Chunk) (Object) this).setBlockState(new BlockPos(x, y, z), (net.minecraft.block.BlockState) blockState,
                                                                        false);
    }
    
    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return (BlockState) getBlockState(new BlockPos(x, y, z));
    }
    
    public int terra$getMaxHeight() {
        return getHeightLimitView().getTopY();
    }
    
    public void provider$setBiomeProvider(BiomeProvider provider) {
        if(this.biomeProvider != null) {
            throw new IllegalStateException("Already set biome provider for chunk " + this);
        }
        this.biomeProvider = provider;
    }
    
    public BiomeProvider provider$getBiomeProvider() {
        return biomeProvider;
    }
}
