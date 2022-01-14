package net.kyrptonaught.exampleCPAPortals;

import net.kyrptonaught.customportalapi.portal.frame.FlatPortalAreaHelper;
import net.kyrptonaught.customportalapi.portal.frame.PortalFrameTester;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Optional;
import java.util.function.Predicate;

public class GrownCropFrameTester extends FlatPortalAreaHelper {


    public Optional<PortalFrameTester> getOrEmpty(WorldAccess worldAccess, BlockPos blockPos, Predicate<PortalFrameTester> predicate, Direction.Axis axis, Block... foundations) {
        return Optional.of((PortalFrameTester) new GrownCropFrameTester().init(worldAccess, blockPos, axis, foundations)).filter(predicate);
    }


    public boolean isValidFrame() {
        return super.isValidFrame() && frameHasGrownCrops();
    }

    private boolean frameHasGrownCrops() {
        for (int i = 0; i < this.xSize; i++) {
            if (!isValidCrop(this.lowerCorner.offset(Direction.Axis.Z, -1).offset(Direction.Axis.X, i).up()) ||
                    !isValidCrop(this.lowerCorner.offset(Direction.Axis.Z, this.zSize).offset(Direction.Axis.X, i).up()))
                return false;
        }

        for (int i = 0; i < this.zSize; i++) {
            if (!isValidCrop(this.lowerCorner.offset(Direction.Axis.X, -1).offset(Direction.Axis.Z, i).up()) ||
                    !isValidCrop(this.lowerCorner.offset(Direction.Axis.X, this.xSize).offset(Direction.Axis.Z, i).up()))
                return false;
        }
        return true;
    }

    private boolean isValidCrop(BlockPos pos) {
        BlockState cropState = world.getBlockState(pos);
        return cropState.getBlock() instanceof CropBlock && ((CropBlock)cropState.getBlock()).isMature(cropState); // can check here if it's mature
    }

    @Override
    public void createPortal(World world, BlockPos pos, BlockState frameBlock, Direction.Axis axis) {
        for (int i = -1; i < 4; i++) {
            world.setBlockState(pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, -1), frameBlock);
            world.setBlockState(pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 3), frameBlock);

            world.setBlockState(pos.offset(Direction.Axis.Z, i).offset(Direction.Axis.X, -1), frameBlock);
            world.setBlockState(pos.offset(Direction.Axis.Z, i).offset(Direction.Axis.X, 3), frameBlock);
        }

        for (int i = 0; i < 3; i++) {
            placeLandingPad(world, pos.offset(Direction.Axis.X, i).down(), frameBlock); // blocks underneath the portal so the player does not fall
            placeLandingPad(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 1).down(), frameBlock);
            placeLandingPad(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 2).down(), frameBlock);

            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i));//clearing out the inside of the portal
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 1));
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 2));

            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).up()); //clearing area above the portal
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 1).up());
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 2).up());
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).up(2));
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 1).up(2));
            fillAirAroundPortal(world, pos.offset(Direction.Axis.X, i).offset(Direction.Axis.Z, 2).up(2));
        }
        //inits this instance based off of the newly created portal;
        this.lowerCorner = pos;
        this.xSize = zSize = 3;// x and z size of portal blocks, not frames
        this.world = world;
        //this.foundPortalBlocks = 4; // we aren't lighting the destination portal
        //lightPortal(frameBlock.getBlock());
    }
    @Override
    public BlockPos doesPortalFitAt(World world, BlockPos attemptPos, Direction.Axis axis) {
        BlockLocating.Rectangle rect = BlockLocating.getLargestRectangle(attemptPos.up(), Direction.Axis.X, 5, Direction.Axis.Z, 5, blockPos -> {
            return world.getBlockState(blockPos).getMaterial().isSolid() &&
                    !world.getBlockState(blockPos.up()).getMaterial().isSolid() && !world.getBlockState(blockPos.up()).getMaterial().isLiquid() &&
                    !world.getBlockState(blockPos.up(2)).getMaterial().isSolid() && !world.getBlockState(blockPos.up(2)).getMaterial().isLiquid();
        });
        return rect.width >= 5 && rect.height >= 5 ? rect.lowerLeft : null; // must change size requirements to 5 for destination portal placement
    }
}
