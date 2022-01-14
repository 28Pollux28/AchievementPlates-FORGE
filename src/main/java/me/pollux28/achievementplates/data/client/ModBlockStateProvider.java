package me.pollux28.achievementplates.data.client;

import me.pollux28.achievementplates.AchievementPlates;
import me.pollux28.achievementplates.setup.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, AchievementPlates.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerPlate();
        registerWallPlate();
    }

    private void registerPlate() {
        BlockModelBuilder builder = models().getBuilder("block/achievement_plate");
        builder.parent(models().getExistingFile(mcLoc("block/block")));
        builder.element().from(3, 0, 7).to(13, 1, 10).allFaces((direction, faceBuilder) -> {
//            faceBuilder.uvs(3,7,13,10).texture("#basalt");
            switch (direction) {
                case DOWN, UP -> faceBuilder.uvs(3, 7, 13, 10).texture("#basalt").end();
                case NORTH, SOUTH -> faceBuilder.uvs(3, 7, 13, 8).texture("#basalt").end();
                case WEST, EAST -> faceBuilder.uvs(6, 7, 9, 8).texture("#basalt").end();
            }
        });
        builder.element().from(3, 1, 8).to(4, 10, 9)
                .face(Direction.UP).uvs(2, 7, 3, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(2, 3, 3, 12).texture("#gold").end()
                .face(Direction.SOUTH).uvs(2, 3, 3, 12).texture("#gold").end()
                .face(Direction.EAST).uvs(2, 3, 3, 12).texture("#gold").end()
                .face(Direction.WEST).uvs(2, 3, 3, 12).texture("#gold").end();
//        .allFaces((direction, faceBuilder) -> {
//            switch (direction){
//                case UP -> faceBuilder.uvs(2,7,3,8).texture("#gold").end();
//                case NORTH,SOUTH,WEST,EAST -> faceBuilder.uvs(2,3,3,12).texture("#gold").end();
//            }
//        });
        builder.element().from(12, 1, 8).to(13, 10, 9)
                .face(Direction.UP).uvs(13, 7, 14, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(13, 3, 14, 12).texture("#gold").end()
                .face(Direction.SOUTH).uvs(13, 3, 14, 12).texture("#gold").end()
                .face(Direction.EAST).uvs(13, 3, 14, 12).texture("#gold").end()
                .face(Direction.WEST).uvs(13, 3, 14, 12).texture("#gold").end();
//                .allFaces((direction, faceBuilder) -> {
//            switch (direction){
//                case UP -> faceBuilder.uvs(13,7,14,8).texture("#gold").end();
//                case NORTH,SOUTH,WEST,EAST -> faceBuilder.uvs(13,3,14,12).texture("#gold").end();
//            }
//        });
        builder.element().from(4, 1, 8).to(12, 2, 9)
                .face(Direction.UP).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.SOUTH).uvs(3, 7, 12, 8).texture("#gold").end();
//                .allFaces((direction, faceBuilder) -> {
//            switch (direction){
//                case UP,NORTH,SOUTH: faceBuilder.uvs(3,7,12,8).texture("#gold").end();
//            }
//        });
        builder.element().from(4, 9, 8).to(12, 10, 9)
                .face(Direction.UP).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.DOWN).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.SOUTH).uvs(3, 7, 12, 8).texture("#gold").end().end();
//                .allFaces((direction, faceBuilder) -> {
//            switch (direction){
//                case UP,DOWN,NORTH,SOUTH: faceBuilder.uvs(3,7,12,8).texture("#gold").end();
//            }
//        }).end();
        builder.texture("gold", mcLoc("block/gold_block")).texture("basalt", mcLoc("block/smooth_basalt")).texture("particle", mcLoc("block/gold_block"));

        VariantBlockStateBuilder vStateBuilder = getVariantBuilder(ModBlocks.PLATE_BLOCK.get());
        vStateBuilder.forAllStates(state -> {
            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
            return ConfiguredModel.builder().modelFile(builder).rotationY(switch (dir) {
                        case NORTH -> 0;
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        default -> 0;
                    })
                    .build();
        });
    }

    private void registerWallPlate() {
        BlockModelBuilder builder = models().getBuilder("block/achievement_wall_plate");
        builder.parent(models().getExistingFile(mcLoc("block/block")));
        builder.element().from(3, 3, 15).to(4, 13, 16)
                .face(Direction.UP).uvs(2, 7, 3, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(2, 3, 3, 12).texture("#gold").end()
                .face(Direction.SOUTH).uvs(2, 3, 3, 12).texture("#gold").end()
                .face(Direction.EAST).uvs(2, 3, 3, 12).texture("#gold").end()
                .face(Direction.WEST).uvs(2, 3, 3, 12).texture("#gold").end();
//        .faces((direction, faceBuilder) -> {
//            switch (direction){
//                case DOWN, UP -> {
//                    faceBuilder.uvs(3,7,13,10).texture("block/smooth_basalt").end();
//                }
//                case NORTH, SOUTH -> faceBuilder.uvs(3,7,13,7).texture("block/smooth_basalt").end();
//                case WEST, EAST -> faceBuilder.uvs(6,7,9,7).texture("block/smooth_basalt").end();
//            }
//        });
        builder.element().from(12, 3, 15).to(13, 13, 16)
                .face(Direction.UP).uvs(13, 7, 14, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(13, 3, 14, 12).texture("#gold").end()
                .face(Direction.SOUTH).uvs(13, 3, 14, 12).texture("#gold").end()
                .face(Direction.EAST).uvs(13, 3, 14, 12).texture("#gold").end()
                .face(Direction.WEST).uvs(13, 3, 14, 12).texture("#gold").end();
//                .faces((direction, faceBuilder) -> {
//            switch (direction){
//                case UP -> faceBuilder.uvs(6,6,7,7).texture("block/gold_block").end();
//                case NORTH,SOUTH,WEST,EAST -> faceBuilder.uvs(7,3,8,12).texture("block/gold_block").end();
//            }
//        });
        builder.element().from(4, 3, 15).to(12, 4, 16)
                .face(Direction.UP).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.DOWN).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.SOUTH).uvs(3, 7, 12, 8).texture("#gold").end();
        builder.element().from(4, 12, 15).to(12, 13, 16)
                .face(Direction.UP).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.DOWN).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.NORTH).uvs(3, 7, 12, 8).texture("#gold").end()
                .face(Direction.SOUTH).uvs(3, 7, 12, 8).texture("#gold").end().end();
        builder.texture("gold", mcLoc("block/gold_block")).texture("particle", mcLoc("block/gold_block"));

        VariantBlockStateBuilder vStateBuilder = getVariantBuilder(ModBlocks.WALL_PLATE_BLOCK.get());
        vStateBuilder.forAllStates(state -> {
            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
            return ConfiguredModel.builder().modelFile(builder).rotationY(switch (dir) {
                        case NORTH -> 0;
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        default -> 0;
                    })
                    .build();
        });
    }


}
