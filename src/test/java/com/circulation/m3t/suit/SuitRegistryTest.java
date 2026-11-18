package com.circulation.m3t.suit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.After;
import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectLists;

public class SuitRegistryTest {

    @After
    public void clearSuits() {
        M3TBaublesSuitHandler.reload();
        M3TBaubleScatteredSuitHandler.reload();
        M3TBaubleTagSuitHandler.reload();
    }

    @Test
    public void tierSelectionUsesTheHighestUnlockedThresholdRegardlessOfRegistrationOrder() {
        M3TBaublesSuitHandler.SuitHandler.create("example")
            .addSuit(4, "Four", ObjectLists.emptyList())
            .addSuit(1, "One", ObjectLists.emptyList())
            .addSuit(2, "Two", ObjectLists.emptyList())
            .register();

        var tiers = M3TBaublesSuitHandler.getSuit("example");
        assertEquals(3, tiers.size());
        assertNull(M3TBaublesSuitHandler.getActiveTier(tiers, 0));
        assertEquals("One", M3TBaublesSuitHandler.getActiveTier(tiers, 1).tooltip);
        assertEquals("Two", M3TBaublesSuitHandler.getActiveTier(tiers, 3).tooltip);
        assertEquals("Four", M3TBaublesSuitHandler.getActiveTier(tiers, 100).tooltip);
    }

    @Test
    public void scatteredMembershipMatchesItemAndMetaWithoutDuplicateEntries() {
        Item item = new Item();
        ItemStack member = new ItemStack(item, 1, 7);
        ItemStack otherMeta = new ItemStack(item, 1, 8);
        M3TBaubleScatteredSuitHandler.ScatteredSuitHandler.create("example")
            .addItems(member, member.copy())
            .register();

        assertEquals(ObjectLists.singleton("example"), M3TBaubleScatteredSuitHandler.getSuits(member));
        assertTrue(
            M3TBaubleScatteredSuitHandler.getSuits(otherMeta)
                .isEmpty());
        assertTrue(
            M3TBaubleScatteredSuitHandler.getSuits(new ItemStack(new Item(), 1, 7))
                .isEmpty());

        clearSuits();
        assertTrue(
            M3TBaubleScatteredSuitHandler.getSuits(member)
                .isEmpty());
        assertFalse(M3TBaublesSuitHandler.hasSuit("example"));
    }
}
