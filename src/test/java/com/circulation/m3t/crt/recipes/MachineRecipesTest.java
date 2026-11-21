package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.mixins.MMM.AccessorEternalAnvilRecipe;
import com.circulation.m3t.reload.M3TCrtReload;
import com.circulation.m3t.test.ForgeTestRuntime;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.Test;
import project.studio.manametalmod.core.Ores;
import project.studio.manametalmod.dark_magic.DarkFission;
import project.studio.manametalmod.dark_magic.TileEntityDarkFission;
import project.studio.manametalmod.originmagic.EternalAnvilRecipe;
import project.studio.manametalmod.originmagic.OriginCrystalRecipe;
import project.studio.manametalmod.originmagic.TileEntityEternalAnvil;
import project.studio.manametalmod.originmagic.TileEntityMirrorPool;
import project.studio.manametalmod.originmagic.TileEntityMysteriousEnergyGenerator;
import project.studio.manametalmod.originmagic.TileEntityPrimalForge;
import project.studio.manametalmod.world.thuliumempire.ThuliumEmpireCore;
import stanhebben.zenscript.ZenModule;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.impl.GenericCompileEnvironment;
import stanhebben.zenscript.impl.GenericErrorLogger;
import stanhebben.zenscript.impl.GenericRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static project.studio.manametalmod.ManaMetalAPI.DarkFissionList;
import static project.studio.manametalmod.ManaMetalAPI.MirrorPoolCopyableItems;
import static project.studio.manametalmod.ManaMetalAPI.MysteriousEnergyItemList;
import static project.studio.manametalmod.ManaMetalAPI.PrimalForgeItemList;

public class MachineRecipesTest {

    @Test
    public void zenScriptChangesTheRecipesReadByDevicesAndReloadRestoresDefaults() throws Exception {
        ForgeTestRuntime.run(Scenario.class, "verify");
    }

    @Test
    public void eternalAnvilUsesScriptRecipesAndRestoresTheOriginalOnReload() throws Exception {
        ForgeTestRuntime.runWithMixins(Scenario.class, "verifyAnvil");
    }

    public static class Scenario {

        public static void verify() throws Exception {
            Bootstrap.func_151354_b();

            ItemStack crystalResult = new ItemStack(Items.diamond);
            ItemStack crystalMaterial = new ItemStack(Items.emerald, 3);
            ItemStack fissionInput = new ItemStack(Items.coal);
            ItemStack otherMeta = new ItemStack(Items.coal, 1, 1);
            ItemStack firstProduct = new ItemStack(Items.flint, 2);
            ItemStack secondProduct = new ItemStack(Items.redstone, 4);
            ItemStack mirrorTemplate = new ItemStack(Items.gold_ingot, 3);
            mirrorTemplate.setTagCompound(new NBTTagCompound());
            mirrorTemplate.getTagCompound()
                          .setString("test", "mirror sample");
            ItemStack replacementTemplate = mirrorTemplate.copy();
            replacementTemplate.stackSize++;
            ItemStack energyInput = new ItemStack(Items.iron_ingot);
            ItemStack forgeInput = new ItemStack(Items.quartz);
            ItemStack extraFuel = new ItemStack(Items.stick);
            int oldValue = 12;
            int value = 37;
            int level = 17;

            OriginCrystalRecipe defaultCrystal = new OriginCrystalRecipe(crystalResult, level - 1, new ItemStack[0]);
            OriginCrystalRecipe.recipes.add(defaultCrystal);
            DarkFission defaultFission = new DarkFission(fissionInput, new ItemStack[]{crystalResult});
            DarkFission charcoalFission = new DarkFission(otherMeta, new ItemStack[]{crystalResult});
            DarkFissionList.add(defaultFission);
            DarkFissionList.add(charcoalFission);
            MirrorPoolCopyableItems.add(mirrorTemplate.copy());
            MysteriousEnergyItemList.put(energyInput.getItem(), oldValue);
            PrimalForgeItemList.put(forgeInput.getItem(), oldValue);

            RecipeItems.stacks = new ItemStack[]{crystalResult, crystalMaterial, fissionInput, firstProduct,
                secondProduct, mirrorTemplate, energyInput, forgeInput, replacementTemplate, extraFuel};
            M3TCrtReload[] handlers = {new OriginCrystalCoreHandler(), new DarkFissionHandler(),
                new MirrorPoolHandler(), new MysteriousEnergyGeneratorHandler(), new PrimalForgeHandler()};
            runScript(
                handlers,
                "replace_recipes.zs",
                "import mods.m3t.test.RecipeItems;\n"
                    + "mods.m3t.OriginCrystalCore.removeRecipe(RecipeItems.stack(0));\n"
                    + "mods.m3t.OriginCrystalCore.addRecipe(RecipeItems.stack(0), [RecipeItems.stack(1)], "
                    + level
                    + ");\n"
                    + "mods.m3t.DarkFission.removeRecipe(RecipeItems.stack(2));\n"
                    + "mods.m3t.DarkFission.addRecipe(RecipeItems.stack(2), [RecipeItems.stack(3), RecipeItems.stack(4)]);\n"
                    + "mods.m3t.MirrorPool.removeRecipe(RecipeItems.stack(5));\n"
                    + "mods.m3t.MysteriousEnergyGenerator.addRecipe(RecipeItems.stack(6), "
                    + value
                    + ");\n"
                    + "mods.m3t.MysteriousEnergyGenerator.addRecipe(RecipeItems.stack(9), "
                    + value
                    + ");\n"
                    + "mods.m3t.PrimalForge.removeRecipe(RecipeItems.stack(7));\n"
                    + "mods.m3t.PrimalForge.addRecipe(RecipeItems.stack(7), "
                    + value
                    + ");\n");

            OriginCrystalRecipe newCrystal = OriginCrystalRecipe.getRecipe(0);
            assertEquals(level, newCrystal.needExperienceLevel);
            assertTrue(ItemStack.areItemStacksEqual(crystalMaterial, newCrystal.ingredients[0]));
            assertSame(charcoalFission, DarkFissionList.get(0));
            TileEntityDarkFission fission = new TileEntityDarkFission();
            fission.setInventorySlotContents(0, fissionInput.copy());
            assertTrue(fission.canMake());
            fission.make();
            assertNull(fission.getStackInSlot(0));
            assertTrue(ItemStack.areItemStacksEqual(firstProduct, fission.getStackInSlot(1)));
            assertTrue(ItemStack.areItemStacksEqual(secondProduct, fission.getStackInSlot(2)));

            TileEntityMirrorPool mirror = new TileEntityMirrorPool();
            assertNull(mirror.getCopyResult(mirrorTemplate));
            TileEntityMysteriousEnergyGenerator generator = new TileEntityMysteriousEnergyGenerator();
            generator.setInventorySlotContents(0, energyInput);
            assertTrue(generator.getManaItem());
            assertEquals(value, generator.timeMAX);
            generator.setInventorySlotContents(0, extraFuel);
            assertTrue(generator.getManaItem());
            PrimalForgeProbe forge = new PrimalForgeProbe();
            assertEquals(value, forge.points(forgeInput));

            runScript(
                handlers,
                "remove_recipes.zs",
                "import mods.m3t.test.RecipeItems;\n" + "mods.m3t.MirrorPool.removeRecipe(RecipeItems.stack(5));\n"
                    + "mods.m3t.MirrorPool.addRecipe(RecipeItems.stack(8));\n"
                    + "mods.m3t.MysteriousEnergyGenerator.removeRecipe(RecipeItems.stack(6));\n");
            assertSame(defaultCrystal, OriginCrystalRecipe.getRecipe(0));
            assertSame(defaultFission, DarkFissionList.get(0));
            assertFalse(generator.getManaItem());
            generator.setInventorySlotContents(0, energyInput);
            assertFalse(generator.getManaItem());
            assertEquals(oldValue, forge.points(forgeInput));
            ItemStack sample = mirrorTemplate.copy();
            sample.stackSize = 1;
            assertTrue(ItemStack.areItemStacksEqual(replacementTemplate, mirror.getCopyResult(sample)));
            assertNull(mirror.getCopyResult(new ItemStack(Items.gold_ingot)));

            reload(handlers);
            assertTrue(ItemStack.areItemStacksEqual(mirrorTemplate, mirror.getCopyResult(sample)));
            assertTrue(generator.getManaItem());
            assertEquals(oldValue, generator.timeMAX);
        }

        public static void verifyAnvil() throws Exception {
            Bootstrap.func_151354_b();
            Loader loader = Loader.instance();
            LoadController controller = new LoadController(loader);
            ModMetadata metadata = new ModMetadata();
            metadata.modId = "manametalmod";
            ForgeTestRuntime.setField(Loader.class, "modController", loader, controller);
            ForgeTestRuntime
                .setField(LoadController.class, "activeContainer", controller, new DummyModContainer(metadata));
            ThuliumEmpireCore.Badyala = new Ores();
            ThuliumEmpireCore.Badyala.ingot = Items.iron_ingot;

            ItemStack output = new ItemStack(Items.golden_apple);
            ItemStack apple = new ItemStack(Items.apple);
            ItemStack ingot = new ItemStack(Items.gold_ingot);
            ItemStack[] ingredients = {apple, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot};
            EternalAnvilRecipe original = new EternalAnvilRecipe("test.original", ingredients, output);
            AccessorEternalAnvilRecipe.m3t$getRecipes()
                                      .clear();
            EternalAnvilRecipe.registerRecipe(original);

            RecipeItems.stacks = new ItemStack[]{output, apple, ingot, new ItemStack(Items.diamond)};
            M3TCrtReload[] handlers = {new EternalAnvilHandler()};
            String materials = "[RecipeItems.stack(1), RecipeItems.stack(2), RecipeItems.stack(2), RecipeItems.stack(2), "
                + "RecipeItems.stack(2), RecipeItems.stack(2), RecipeItems.stack(2), RecipeItems.stack(2), RecipeItems.stack(2)]";
            runScript(
                handlers,
                "replace_anvil_recipe.zs",
                "import mods.m3t.test.RecipeItems;\n" + "mods.m3t.EternalAnvil.removeRecipe(RecipeItems.stack(0));\n"
                    + "mods.m3t.EternalAnvil.addRecipe(RecipeItems.stack(0), "
                    + materials
                    + ", \"test.custom\");\n"
                    + "mods.m3t.EternalAnvil.addRecipe(RecipeItems.stack(3), "
                    + materials
                    + ");\n");

            TileEntityEternalAnvil anvil = new TileEntityEternalAnvil();
            EternalAnvilRecipe custom = anvil.getSelectedRecipe();
            assertEquals("test.custom", custom.getNameKey());
            assertTrue(ItemStack.areItemStacksEqual(output, custom.getResult()));
            ItemStack[] inventory = {new ItemStack(Items.gold_ingot, 8), apple.copy()};
            assertTrue(custom.consume(inventory, inventory.length));
            assertNull(inventory[0]);
            assertNull(inventory[1]);
            assertTrue(anvil.selectRecipe(1));
            assertTrue(
                ItemStack.areItemStacksEqual(
                    RecipeItems.stacks[3],
                    anvil.getSelectedRecipe()
                         .getResult()));

            reload(handlers);
            assertNull(anvil.getSelectedRecipe());
            assertTrue(anvil.selectRecipe(0));
            assertSame(original, anvil.getSelectedRecipe());
        }

        private static void runScript(M3TCrtReload[] handlers, String fileName, String script) throws Exception {
            for (M3TCrtReload handler : handlers) {
                handler.reload();
            }
            GenericCompileEnvironment environment = new GenericCompileEnvironment();
            GenericRegistry registry = new GenericRegistry(environment, new GenericErrorLogger(System.err));
            environment.setRegistry(registry);
            registry.registerNativeClass(IItemStack.class);
            registry.registerNativeClass(RecipeItems.class);
            for (M3TCrtReload handler : handlers) {
                registry.registerNativeClass(handler.getClass());
            }
            registry.registerGlobal(
                "mods",
                registry.getRoot()
                        .get("mods"));
            ZenModule.compileScriptString(script, fileName, environment, Scenario.class.getClassLoader())
                     .getMain()
                     .run();
            for (M3TCrtReload handler : handlers) {
                handler.postReload();
            }
        }

        private static void reload(M3TCrtReload[] handlers) {
            for (M3TCrtReload handler : handlers) {
                handler.reload();
                handler.postReload();
            }
        }
    }

    @ZenClass("mods.m3t.test.RecipeItems")
    public static class RecipeItems {

        private static ItemStack[] stacks;

        @ZenMethod
        public static IItemStack stack(int index) {
            return MineTweakerMC.getIItemStack(stacks[index]);
        }
    }

    private static class PrimalForgeProbe extends TileEntityPrimalForge {

        int points(ItemStack stack) {
            return getRecyclePoint(stack);
        }
    }
}
