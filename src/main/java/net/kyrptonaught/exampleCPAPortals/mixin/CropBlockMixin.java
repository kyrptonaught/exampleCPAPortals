package net.kyrptonaught.exampleCPAPortals.mixin;

import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.kyrptonaught.customportalapi.portal.PortalPlacer;
import net.kyrptonaught.exampleCPAPortals.ExampleCPAPortalsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends Block {

    public CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Shadow public abstract boolean isMature(BlockState state);

    /* triggered when a crop gets placed.This also occurs when growing. Make sure this isn't too broad,
    *  a portal will attempt to be lit everytime this occurs. We want to avoid lag.
    */
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if(!isMature(state)) return; // we only want fully grown

        //attempt to light a portal on all sides
        if (PortalPlacer.attemptPortalLight(world, pos.down().north(), ExampleCPAPortalsMod.CROPGROWIGNITE) ||
                PortalPlacer.attemptPortalLight(world, pos.down().south(), ExampleCPAPortalsMod.CROPGROWIGNITE) ||
                PortalPlacer.attemptPortalLight(world, pos.down().east(), ExampleCPAPortalsMod.CROPGROWIGNITE) ||
                PortalPlacer.attemptPortalLight(world, pos.down().west(), ExampleCPAPortalsMod.CROPGROWIGNITE));
    }
}
