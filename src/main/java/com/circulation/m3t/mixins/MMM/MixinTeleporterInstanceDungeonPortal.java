package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.DungeonBoxHandler;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.instance_dungeon.IDungeonDifficult;
import project.studio.manametalmod.instance_dungeon.InstanceDungeonType;
import project.studio.manametalmod.instance_dungeon.TeleporterInstanceDungeonPortal;

@Mixin(value = TeleporterInstanceDungeonPortal.class,remap = false)
public class MixinTeleporterInstanceDungeonPortal extends Teleporter{

    public MixinTeleporterInstanceDungeonPortal(WorldServer worldIn) {
        super(worldIn);
    }

    @Inject(method = "spawnSchematicMain",at = @At("HEAD"))
    public void spawnSchematicMain(World world, int x, int y, int z, String name, InstanceDungeonType type, IDungeonDifficult diff, int metadata_doors, long mobuuid, CallbackInfo ci) {
        if (DungeonBoxHandler.BoxYes){
            DungeonBoxHandler.registerDungeonBox();
            DungeonBoxHandler.BoxYes = false;
        }
    }

    @Inject(method = "spawnSchematicRepeat",at = @At("HEAD"))
    private static void spawnSchematicRepeat(World world, int x, int y, int z, String name, InstanceDungeonType type, IDungeonDifficult diff, int metadata_doors, long mobuuid, CallbackInfo ci) {
        if (DungeonBoxHandler.BoxYes){
            DungeonBoxHandler.registerDungeonBox();
            DungeonBoxHandler.BoxYes = false;
        }
    }

}
