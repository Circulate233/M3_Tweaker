package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.M3TConfig;
import com.circulation.m3t.Util.Function;
import com.circulation.m3t.Util.SuitTooltips;
import com.circulation.m3t.hander.M3TBaubleScatteredSuitHandler;
import com.circulation.m3t.hander.M3TBaubleTagSuitHandler;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.archeology.ItemAntiquitiesBase;

import java.util.ArrayList;
import java.util.List;

import static com.circulation.m3t.hander.M3TBaubleScatteredSuitHandler.Scattereds;
import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

@Mixin(value = ItemAntiquitiesBase.class)
public class MixinItemAntiquitiesBase {

    @Inject(method = "addInformation",at = @At(value = "TAIL"))
    public void addInformation(ItemStack item, EntityPlayer player, List<String> finallist, boolean booleans, CallbackInfo ci) {
        String registrySuitName = Item.itemRegistry.getNameForObject(this);
        boolean hasSuit = false;
        List<String> list = new ArrayList<>();
        if (M3TBaublesSuitHandler.hasSuit(registrySuitName)) {
            SuitTooltips.handleSuitInfo(item, player, list, registrySuitName);
            hasSuit = true;
        }

        String nbtSuit = null;
        if (item.hasTagCompound() && item.getTagCompound().hasKey(nbtName)) {
            String tagSuitName = item.getTagCompound().getString(nbtName);
            if (M3TBaubleTagSuitHandler.Tags.containsKey(tagSuitName)){
                SuitTooltips.handleSuitInfo(item, player, list, tagSuitName);
                nbtSuit = tagSuitName;
                hasSuit = true;
            }
        }

        if (Scattereds.containsKey(M3TBaubleScatteredSuitHandler.Scattered.getScattered(item))) {
            final String finalNbtSuit = nbtSuit;
            Scattereds.get(M3TBaubleScatteredSuitHandler.Scattered.getScattered(item))
                .stream().filter(suitName -> finalNbtSuit == null || !finalNbtSuit.equals(suitName))
                .forEach(suitName -> SuitTooltips.handleSuitInfo(item, player, list, suitName));
            hasSuit = true;
        }

        if (!M3TConfig.showAllSuitEffects && hasSuit && !SuitTooltips.isShift() && !SuitTooltips.isAlt()) {
            list.add(Function.getText("bauble.key"));
        }

        if (hasSuit){
            finallist.add(Function.getText("bauble.tooltip"));
            finallist.addAll(list);
            finallist.add(Function.getText("bauble.endtip"));
        }
    }

}
