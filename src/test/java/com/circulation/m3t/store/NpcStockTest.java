package com.circulation.m3t.store;

import com.circulation.m3t.test.ForgeTestRuntime;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.Test;
import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.npc.NpcStoreType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NpcStockTest {

    @Test
    public void preservesStoreRulesWhenApplyingAScriptBatch() throws Exception {
        ForgeTestRuntime.run(Scenario.class, "verify");
    }

    public static class Scenario {

        public static void verify() {
            Bootstrap.func_151354_b();
            verifyAdditions();
            NpcStock.clearChanges();
            verifyRemovals();
        }

        private static void verifyAdditions() {
            NpcStoreType type = NpcStoreType.fruit;
            int limitedPrice = 12;
            int unlimitedPrice = 15;
            int maxCount = 6;
            NpcStock.addItem(
                0,
                "minecraft:apple:0:2;" + limitedPrice + ";0;" + maxCount + ";extra",
                "minecraft:missing_item;12",
                "minecraft:apple:1;" + unlimitedPrice + ";1;" + maxCount + ";extra");
            NpcStock.sortAdditions();

            List<Icommodity> result = NpcStock.apply(type, new ObjectArrayList<>());
            assertEquals(2, result.size());
            assertTrue(
                result.get(0)
                    .isLimit());
            assertEquals(
                limitedPrice,
                result.get(0)
                    .getPrice());
            assertEquals(
                maxCount,
                result.get(0)
                    .getMaxCount());
            assertFalse(
                result.get(1)
                    .isLimit());
            assertEquals(
                unlimitedPrice,
                result.get(1)
                    .getPrice());
        }

        private static void verifyRemovals() {
            NpcStoreType type = NpcStoreType.fruit;
            Icommodity apple = new Icommodity(new ItemStack(Items.apple), 10);
            Icommodity variant = new Icommodity(new ItemStack(Items.apple, 2, 1), 20);
            Icommodity carrot = new Icommodity(new ItemStack(Items.carrot), 5);
            Icommodity replacement = new Icommodity(new ItemStack(Items.apple), 12);
            List<Icommodity> original = ObjectArrayList.of(apple, variant, carrot);

            NpcStock.removeItem(type, "minecraft:apple", "minecraft:missing_item", "minecraft:carrot");
            NpcStock.addItem(type, replacement);
            List<Icommodity> result = NpcStock.apply(type, original);
            assertEquals(2, result.size());
            assertSame(variant, result.get(0));
            assertSame(replacement, result.get(1));
            assertSame(apple, original.get(0));
            assertSame(carrot, original.get(2));
        }
    }
}
