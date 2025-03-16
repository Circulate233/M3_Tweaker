package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.Function;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.ManaMetalMod;
import project.studio.manametalmod.instance_dungeon.InstanceDungeonCore;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft10;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft2;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft3;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft8;
import project.studio.manametalmod.produce.ProduceCore;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass(M3TCrtAPI.CrtClass + "TrophyItem")
public class TrophyItemsHandler implements M3TCrtReload {

    public static int totalWeight;
    public static int totalWeightLv2;
    public static final Map<TrophyItem,Integer> defTrophyItems;
    public static final Map<TrophyItem,Integer> defTrophyItemsLv2;
    private static final Map<TrophyItem,Integer> addTrophyItems = new HashMap<>();
    private static final Map<TrophyItem,Integer> addTrophyItemsLv2 = new HashMap<>();
    private static final List<ItemStack> removeTrophyItems = new ArrayList<>();
    private static final List<ItemStack> removeTrophyItemsLv2 = new ArrayList<>();
    public static Map<TrophyItem,Integer> TrophyItems = new HashMap<>();
    public static Map<TrophyItem,Integer> TrophyItemsLv2 = new HashMap<>();

    static {
        Map<TrophyItem,Integer> map = new HashMap<>();

        map.put(new TrophyItem(new ItemStack(ItemCraft10.nuggetRandomReel)){
            @Override
            public ItemStack getOutput(){
                return new ItemStack(this.output.getItem(),MMM.rand.nextInt(12) + 5);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpCrystal1, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.brokenLifeCrystal)){
            @Override
            public ItemStack getOutput(){
                return new ItemStack(this.output.getItem(),MMM.rand.nextInt(6) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.expMedal, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.expReel, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.mysteriousCoinBag, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemRandomReel, 3)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.DeadReel, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpBook1, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.LifeStone, 1)), 4);
        map.put(new TrophyItem(new ItemStack(Items.diamond, 1)), 1);
        map.put(new TrophyItem(new ItemStack(Items.nether_star, 1)), 1);
        map.put(new TrophyItem(new ItemStack(Items.gold_ingot, 4)), 1);
        map.put(new TrophyItem(new ItemStack(Items.golden_apple, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.DoubleEXPReel, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft3.ItemNewHPwaterE, 10, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft3.ItemNewHPPillE, 15, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ancientExpBook, 1, 0)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft3.ItemNewManaWaterE, 10, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Cheese, 10)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Coin2, 10)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Coin1, 64)), 1);
        map.put(new TrophyItem(new ItemStack(Items.ender_pearl, 5)), 1);
        map.put(new TrophyItem(new ItemStack(Items.emerald, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.PalladiumApple, 10)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.SilverApples, 10)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.gemMagical, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.SageofTheStone, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpCrystal2, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpCrystal3, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 1, 0)), 2);
        map.put(new TrophyItem(MMM.findItemStackM3("MagicItemSpecial", 1, 0)){
            @Override
            public ItemStack getOutput() {
                if (output != null){
                    return new ItemStack(output.getItem(), 1, MMM.rand.nextInt(13));
                } else {
                    return new ItemStack(Items.apple);
                }
            }
        }, 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemMobStatueMakes, 1)), 1);
        map.put(new TrophyItem(null) {
            @Override
            public ItemStack getOutput() {
                return MMM.rand.nextInt(50) == 0
                    ? new ItemStack(ItemCraft10.ItemBagPets, 1)
                    : new ItemStack(ItemCraft8.ExpCrystal3, 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemPowerBookRemoves, 1, 0)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), 1, MMM.rand.nextInt(4));
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StrengthenStone, 1)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(6) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemBagSkyAdventureCards, 1)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(3) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemBagLegendaryWeapon1s, 1)), 2);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.PalladiumApple, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.chocolate, 10)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ArtifactDast, 1)), 1);
        map.put(new TrophyItem(new ItemStack(Items.golden_apple, 1)), 4);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemTrophyExpansion, 1)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemWarehouseExpansion, 1)), 1);
        map.put(new TrophyItem(new ItemStack(Items.book, 1, 0)) {
            @Override
            public ItemStack getOutput() {
                ItemStack book = super.getOutput();
                EnchantmentHelper.addRandomEnchantment(MMM.rand, book, 30);
                return book;
            }
        }, 4);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpBook1, 1)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.AdvancedArrow, 8)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), 8 + MMM.rand.nextInt(8));
            }
        }, 3);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemSpell, 1, 0)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemSpell, 1, 1)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemSpell, 1, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ProduceCore.ItemMedical, 1, 0)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), 1, MMM.rand.nextInt(14));
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemSPCs, 1, 0)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemMagicCompasss, 1, 0)), 1);
        map.put(new TrophyItem(new ItemStack(InstanceDungeonCore.ItemKeyDungeon, 1, 0)), 2);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ItemCraeerFeature, 1, 0)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft3.candy1, 1)), 1);

        totalWeight = map.values().stream().mapToInt(Integer::intValue).sum();
        defTrophyItems = Collections.unmodifiableMap(map);
    }

    static {
        Map<TrophyItem,Integer> map = new HashMap<>();
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Coin5, 4)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Coin5, 6)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Coin5, 8)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.SageofTheStone, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.TrueTimeHourglass, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Antimatter, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.Neutron, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.UnlimitedRing, 3)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.courageGem, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 1, 4)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 1, 5)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 1, 6)), 1);

        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpCrystal7)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(5) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpCrystal8)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(5) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft8.ExpCrystal9)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(5) + 1);
            }
        }, 1);

        map.put(new TrophyItem(new ItemStack(ItemCraft10.ArtifactDast)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(5) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.ArtifactNugget, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.SwordRoll, 1, 19)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.SwordRoll, 1, 20)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.IntermediateExpBook, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.SilversmithHammer, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.GoldenCraftsmanHammer, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.GoldenPearl, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.PlatinumCube, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.DiamondCube, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ResonanceStone, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.MiracleResonanceStone, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.GoldenPrism, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ColorPrism, 1)), 1);

        map.put(new TrophyItem(new ItemStack(ItemCraft2.Itemblack_ball)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(5) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.DoubleEXPReel)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(3) + 1);
            }
        }, 1);

        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 3, 0)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 3, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft10.StarReels, 3, 2)), 1);

        map.put(new TrophyItem(new ItemStack(ItemCraft10.StrengthenStone)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(8) + 1);
            }
        }, 3);

        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemSPCs)) {
            @Override
            public ItemStack getOutput() {
                return new ItemStack(output.getItem(), MMM.rand.nextInt(3) + 1);
            }
        }, 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemSPCs, 1, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.GoldenDagger)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.StarStone, 1, 0)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.StarStone, 1, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.StarStone, 1, 2)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemGoldenBeetle, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ItemCraft2.ItemClover, 1)), 1);
        map.put(new TrophyItem(new ItemStack(ManaMetalMod.courageGem, 1)), 2);

        map.put(new TrophyItem(null) {
            @Override
            public ItemStack getOutput() {
                ItemStack book = new ItemStack(Items.book, 1, 0);
                EnchantmentHelper.addRandomEnchantment(MMM.rand, book, 50);
                return book;
            }
        }, 1);

        totalWeightLv2 = map.values().stream().mapToInt(Integer::intValue).sum();
        defTrophyItemsLv2 = Collections.unmodifiableMap(map);
    }

    @Override
    public void postReload() {
        Map TrophyItems = new HashMap<>();
        defTrophyItems.forEach((item,weight) -> {
            if (Function.noHasItem(removeTrophyItems,item.getOutput())){
                TrophyItems.put(item,weight);
            }
        });
        TrophyItems.putAll(addTrophyItems);
        TrophyItemsHandler.TrophyItems = Collections.unmodifiableMap(TrophyItems);

        Map TrophyItemsLv2 = new HashMap<>();
        defTrophyItemsLv2.forEach((item,weight) -> {
            if (Function.noHasItem(removeTrophyItemsLv2,item.getOutput())){
                TrophyItemsLv2.put(item,weight);
            }
        });
        TrophyItems.putAll(addTrophyItemsLv2);
        TrophyItemsHandler.TrophyItemsLv2 = Collections.unmodifiableMap(TrophyItemsLv2);
    }

    @Override
    public void reload() {
        addTrophyItems.clear();
        addTrophyItemsLv2.clear();
        removeTrophyItems.clear();
        removeTrophyItemsLv2.clear();
    }

    @ZenMethod
    public static void addItem(IItemStack item, int weight,boolean isLv2) {
        if (isLv2){
            addTrophyItemsLv2.put(new TrophyItem(MineTweakerMC.getItemStack(item)),weight);
        } else {
            addTrophyItems.put(new TrophyItem(MineTweakerMC.getItemStack(item)),weight);
        }
    }

    @ZenMethod
    public static void addItem(IItemStack item, int weight,int min,int max,boolean isLv2) {
        TrophyItem output = new TrophyItem(new ItemStack(Items.apple)){
            @Override
            public ItemStack getOutput() {
                final ItemStack itemStack = MineTweakerMC.getItemStack(item);
                itemStack.stackSize = Math.max(MMM.rand.nextInt(max),min);
                return itemStack;
            }
        };
        if (isLv2){
            addTrophyItemsLv2.put(output,weight);
        } else {
            addTrophyItems.put(output,weight);
        }
    }

    @ZenMethod
    public static void removeItem(IItemStack item,boolean isLv2){
        if (isLv2){
            removeTrophyItemsLv2.add(MineTweakerMC.getItemStack(item));
        } else {
            removeTrophyItems.add(MineTweakerMC.getItemStack(item));
        }
    }

    public static class TrophyItem {

        public ItemStack output;

        public TrophyItem(ItemStack output) {
            this.output = output;
        }

        public ItemStack getOutput() {
            return output.copy();
        }
    }

}
