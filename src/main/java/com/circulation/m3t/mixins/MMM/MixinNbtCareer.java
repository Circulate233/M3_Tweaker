package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.CareerModifierHandler;
import com.circulation.m3t.crt.LevelRewardHandler;
import com.circulation.m3t.crt.events.CareerEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.core.EntityNBTBaseM3;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.entity.nbt.NbtCareer;

@Mixin(value = NbtCareer.class, remap = false)
public abstract class MixinNbtCareer extends EntityNBTBaseM3 {

    @Shadow
    public ManaMetalModRoot obj;

    @Shadow
    public int careerLV;

    @Shadow
    public int careerType;

    @Inject(method = "upLevel()V", at = @At("RETURN"))
    private void onLevelUp(CallbackInfo ci) {
        if (this.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.entity;

            // 发布升级事件
            CareerEvent.LevelUpEvent event = new CareerEvent.LevelUpEvent(
                player, this.careerType, this.careerLV
            );
            
            M3TEventAPI.publishCareerLevelUp(event);

            // 应用职业修改器
            CareerModifierHandler.applyCareerModifiers(player, this.obj);

            // 处理等级奖励
            LevelRewardHandler.handleLevelUp(player, this.careerType, this.careerLV);
        }
    }

    // 在加载NBT时应用修改
    @Inject(method = "LoadNBT", at = @At("RETURN"))
    private void applyModifiersOnLoad(NBTTagCompound nbt, CallbackInfo ci) {
        if (this.entity instanceof EntityPlayer && this.obj != null) {
            CareerModifierHandler.applyCareerModifiers((EntityPlayer) this.entity, this.obj);
        }
    }
}
