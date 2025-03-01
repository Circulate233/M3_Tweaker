package com.circulation.m3t.item;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.Util.Function;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import project.studio.manametalmod.core.ManaItemType;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.*;

public class M3EBaublesSuit extends M3EBaublesBasic {

    protected static Map<String,List<M3EBaublesAttribute>> mapSuit = new HashMap<>();

    public M3EBaublesSuit(String Name) {
        super(Name);
    }

    @Override
    public String getItemStackDisplayName(ItemStack item) {
        if (StatCollector.canTranslate(item.getUnlocalizedName() + ".name")){
            return StatCollector.translateToLocal(item.getUnlocalizedName() + ".name");
        } else {
            String Name = StatCollector.translateToLocal("suit." + this.Names + ".name");
            if (item.getItemDamage() == 0){
                Name += StatCollector.translateToLocal("suit.baubles.0." + (mapSuit.get(this.Names).size() == 15) + ".name");
            } else {
                Name += StatCollector.translateToLocal("suit.baubles." + item.getItemDamage() + ".name");
            }
            return Name;
        }
    }
    @SideOnly(Side.CLIENT)//客户端渲染物品
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);
        list.removeIf(item -> item instanceof String && ((String) item).contains(this.Names + "_introduce" + itemStack.getItemDamage()));
        list.add(Function.getText(this.Names + "_introduce"));
    }

    //套装注册
    public static void registerSuitEffect(String Names, int level, Integer[][] effectNames, Float[][] effectValues) {
        List<M3EBaublesAttribute> list = new ArrayList<>();
        int i = 0;
        for (Suit type:Suit.values()){
            list.add(i,new M3EBaublesAttribute(type.getType(),level,effectNames[i],effectValues[i]));
            i++;
        }
        if (effectNames.length > 14) {
            list.add(i,new M3EBaublesAttribute(Suit.Cloak.getType(),level,effectNames[i],effectValues[i]));
            i++;
        }
        mapSuit.put(Names,list);
    }

    //套装注册
    public static void registerSuitEffect(String Names, int level, List<IMagicEffect>[] effects) {
        List<M3EBaublesAttribute> list = new ArrayList<>();
        int i = 0;
        for (Suit type:Suit.values()){
            list.add(i,new M3EBaublesAttribute(type.getType(),level,effects[i]));
            i++;
        }
        if (effects.length > 14) {
            list.add(i,new M3EBaublesAttribute(Suit.Cloak.getType(),level,effects[i]));
            i++;
        }
        mapSuit.put(Names,list);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        final int lecher = mapSuit.get(this.Names).size();
        for(int i = 0; i < lecher; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.icons = new IIcon[mapSuit.get(this.Names).size()];
        for(int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = register.registerIcon(M3Tweaker.MOD_ID + ":" + Names + i);
        }
    }

    @Override
    public List<IMagicEffect> getItemEffect(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            return mapSuit.get(b.Names).get(itemStack.getItemDamage()).effects;
        }
        return Collections.emptyList();
    }

    @Override
    public int getNeedLV(ItemStack itemStack, EntityPlayer entityPlayer) {
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            return mapSuit.get(b.Names).get(itemStack.getItemDamage()).level;
        }
        return 0;
    }

    @Override
    public ManaItemType getType(ItemStack itemStack) {
        if (itemStack.getItem() instanceof M3EBaublesBasic){
            M3EBaublesBasic b = (M3EBaublesBasic) itemStack.getItem();
            return mapSuit.get(b.Names).get(itemStack.getItemDamage()).type;
        }
        return ManaItemType.Cloak;
    }

    public static void registerAllSuit(){
        mapSuit.keySet().forEach((name) -> {
            M3EBaublesSuit Suit = new M3EBaublesSuit(name);
            Suit.size = mapSuit.get(name).size();
            GameRegistry.registerItem(Suit, name);
        });
    }

    private enum Suit {
        Cloak(ManaItemType.Cloak),
        Earring(ManaItemType.Earring),
        Medal(ManaItemType.Medal),
        Shoulder(ManaItemType.Shoulder),
        Wrist(ManaItemType.Wrist),
        Anklet(ManaItemType.Anklet),
        Bracelet(ManaItemType.Bracelet),
        Gloves(ManaItemType.Gloves),
        Sock(ManaItemType.Sock),
        Scarf(ManaItemType.Scarf),
        Necklace(ManaItemType.Necklace),
        Brooch(ManaItemType.Brooch),
        Belt(ManaItemType.Belt),
        Ring(ManaItemType.Ring);

        private final ManaItemType type;

        Suit(ManaItemType manaItemType) {
            this.type = manaItemType;
        }

        public ManaItemType getType() {
            return this.type;
        }
    }
}
