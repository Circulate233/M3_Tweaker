package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;

import java.util.*;

@ZenClass(M3TCrtAPI.CrtClass + "LevelRewards")
public class LevelRewardHandler implements M3TCrtReload {

    // 存储等级奖励
    private static final Map<Integer, Map<Integer, List<RewardEntry>>> levelRewards = new HashMap<>();
    private static final Map<Integer, Map<Integer, List<RewardEntry>>> defLevelRewards = new HashMap<>();

    private static final Map<String, Set<String>> claimedRewards = new HashMap<>();

    @Override
    public void reload() {
        levelRewards.clear();
        claimedRewards.clear();
    }

    @Override
    public void postReload() {
        if (defLevelRewards.isEmpty()) {
            defLevelRewards.putAll(levelRewards);
        }
    }

    //添加等级奖励
    @ZenMethod
    public static void addLevelReward(int careerID, int level, IItemStack item) {
        addLevelReward(careerID, level, item, null);
    }

    @ZenMethod
    public static void addLevelReward(int careerID, int level, IItemStack item, String message) {
        Map<Integer, List<RewardEntry>> careerRewards = levelRewards.computeIfAbsent(careerID, k -> new HashMap<>());
        List<RewardEntry> rewards = careerRewards.computeIfAbsent(level, k -> new ArrayList<>());

        ItemStack itemStack = MineTweakerMC.getItemStack(item);
        rewards.add(new RewardEntry(itemStack, message));
    }

    //为所有职业添加奖励
    @ZenMethod
    public static void addUniversalReward(int level, IItemStack item) {
        addUniversalReward(level, item, null);
    }

    @ZenMethod
    public static void addUniversalReward(int level, IItemStack item, String message) {
        addLevelReward(0, level, item, message);
    }

    //处理玩家升级奖励
    public static void handleLevelUp(EntityPlayer player, int careerID, int level) {
        String playerName = player.getCommandSenderName();
        String rewardKey = careerID + "_" + level;

        // 检查是否已领取
        Set<String> claimed = claimedRewards.computeIfAbsent(playerName, k -> new HashSet<>());
        if (claimed.contains(rewardKey)) {
            return;
        }

        List<RewardEntry> rewards = new ArrayList<>();

        // 获取通用奖励
        Map<Integer, List<RewardEntry>> universalRewards = levelRewards.get(0);
        if (universalRewards != null && universalRewards.containsKey(level)) {
            rewards.addAll(universalRewards.get(level));
        }

        // 获取职业特定奖励
        Map<Integer, List<RewardEntry>> careerRewards = levelRewards.get(careerID);
        if (careerRewards != null && careerRewards.containsKey(level)) {
            rewards.addAll(careerRewards.get(level));
        }

        // 发放奖励
        if (!rewards.isEmpty()) {
            claimed.add(rewardKey);

            // 发送标题消息
            player.addChatMessage(new ChatComponentText("§6═══════════════════════"));
            player.addChatMessage(new ChatComponentText("§e§l    等级 " + level + " 奖励！"));
            player.addChatMessage(new ChatComponentText("§6═══════════════════════"));

            for (RewardEntry reward : rewards) {
                // 给予物品
                if (!player.inventory.addItemStackToInventory(reward.item.copy())) {
                    // 如果背包满了，掉落在地上
                    player.dropPlayerItemWithRandomChoice(reward.item.copy(), false);
                }

                // 发送消息
                String msg = reward.message != null ? reward.message :
                    "§f  + " + reward.item.getDisplayName() + " x" + reward.item.stackSize;
                player.addChatMessage(new ChatComponentText(msg));
            }

            player.addChatMessage(new ChatComponentText("§6═══════════════════════"));

            // 播放音效
            player.worldObj.playSoundAtEntity(player, "random.levelup", 1.0F, 1.0F);
        }
    }

    //清除玩家的奖励记录
    @ZenMethod
    public static void resetPlayerRewards(String playerName) {
        claimedRewards.remove(playerName);
    }

    //批量添加奖励
    @ZenMethod
    public static void addLevelRewardBatch(int careerID, int level, IItemStack[] items) {
        for (IItemStack item : items) {
            addLevelReward(careerID, level, item);
        }
    }

    private static class RewardEntry {
        final ItemStack item;
        final String message;

        RewardEntry(ItemStack item, String message) {
            this.item = item;
            this.message = message;
        }
    }
}
