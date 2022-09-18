package dev.elrol.serverutilities.econ.chestshop;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.IPlayerDatabase;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.api.econ.AbstractShop;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.init.ShopRegistry;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChestShop extends AbstractShop {

    final ChestShopType type;
    protected List<ItemStack> items = new ArrayList<>();

    public ChestShop(ChestShopType type) {
        super(type.name());
        this.type = type;
    }

    @Override
    public boolean link(ServerPlayer player, Location signLoc, Location linkLoc) {
        if(!canCreate(player)) return false;

        Level w1 = signLoc.getLevelObj();
        BlockPos pos1 = signLoc.getBlockPos();
        BlockEntity te1 = w1.getBlockEntity(pos1);

        Level w2 = linkLoc.getLevelObj();
        BlockPos pos2 = linkLoc.getBlockPos();
        BlockEntity te2 = w2.getBlockEntity(pos2);

        if(te1 == null || te2 == null) return false;
        if(!(te1 instanceof SignBlockEntity)) return false;

        LazyOptional<IItemHandler> opt = te2.getCapability(ForgeCapabilities.ITEM_HANDLER);

        if(canEdit(player)) {
            IPlayerData data = Main.database.get(player.getUUID());
            opt.ifPresent(itemHandler -> {
                items.clear();
                int size = itemHandler.getSlots();
                for(int s = 0; s < size; s++) {
                    ItemStack stack = itemHandler.extractItem(s, 64, true);
                    if(!stack.isEmpty()) {
                        items.add(stack);
                    }
                }
            });
            if(items.isEmpty()) {
                Main.textUtils.err(player, Errs.missing_starting_stock());
                return false;
            }
            setLinkLoc(linkLoc);
            data.addShop(signLoc);
            updateSign((SignBlockEntity) te1);
            return true;
        }
        return false;
    }

    public boolean useShop(ServerPlayer player, Location signLoc) {
        if(!isLinked()) {
            Main.textUtils.err(player, Errs.not_linked(tag()));
            return false;
        }
        BlockEntity te = linkLoc.getBlockEntity();

        if(te == null) return false;

        LazyOptional<IItemHandler> opt = te.getCapability(ForgeCapabilities.ITEM_HANDLER);

        AtomicBoolean flag = new AtomicBoolean(false);
        opt.ifPresent(itemHandler -> {
            Location loc = ShopRegistry.confirmMap.getOrDefault(player.getUUID(), null);
            if(loc == null || !loc.equals(signLoc)) {
                Main.textUtils.sendConfirmation(player, confirm());
                ShopRegistry.confirmMap.put(player.getUUID(), signLoc);
                return;
            }
            if(isBuy()) {
                flag.set(buy(itemHandler, player));
            } else {
                flag.set(sell(itemHandler, player));
            }
        });
        return flag.get();
    }

    @Override
    public Component[] confirm() {
        Component[] msgs = new Component[items.size()+3];
        String action = isBuy() ? "buy from " : "sell to ";
        String owner = isAdmin() ? "the Server" : Methods.getDisplayName(getOwner());
        msgs[0] = Component.literal(ChatFormatting.GREEN + "Are you sure you wish to " + action + owner + ":\n");
        for(int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            String name = item.getDisplayName().getString();
            msgs[i+1] = Component.literal("  " + name.substring(1, (name.length()-1)) +
                    ChatFormatting.DARK_AQUA + " [" +
                    ChatFormatting.GRAY + item.getItem().getName(item).getString() + " " +
                    ChatFormatting.DARK_GRAY + "(x" + item.getCount() + ")" +
                    ChatFormatting.DARK_AQUA + "]" + ChatFormatting.RESET);
        }
        msgs[msgs.length-2] = Component.literal(ChatFormatting.GREEN + "\nFor " + Main.textUtils.parseCurrency(cost, false) + "?");
        msgs[msgs.length-1] = Component.literal(ChatFormatting.RED + "Right click again to confirm.");
        return msgs;
    }

    @Override
    public boolean canCreate(ServerPlayer player) {
        if(canEdit(player)) {
            IPlayerData data = Main.database.get(player.getUUID());
            int shops = data.getShops().size();
            int max = maxShops(CommandConfig.chestshop_max_perm.get(), player);
            if(shops >= max) {
                Main.textUtils.err(player, Errs.max_shops());
                return false;
            }
            return IElrolAPI.getInstance().getPermissionHandler().hasPermission(player, type.perm);
        }
        return false;
    }

    @Override
    public boolean hitShop(ServerPlayer player) {
        return false;
    }

    @Override
    public boolean isAdmin() {
        return type == ChestShopType.AdminBuy ||
            type == ChestShopType.AdminSell;
    }

    private boolean isBuy() {
        return type == ChestShopType.Buy || type == ChestShopType.AdminBuy;
    }

    private boolean buy(IItemHandler itemHandler, ServerPlayer player) {
        if(!isAdmin()) {
            for(ItemStack item : items) {
                int qty = 0;
                for(int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack slotItem = itemHandler.extractItem(i, item.getCount(), true);
                    if(slotItem.sameItem(item)) {
                        qty += slotItem.getCount();
                    }
                }
                if(qty >= item.getCount()) {
                    continue;
                }
                Main.textUtils.msg(player, Errs.shop_missing_stock(tag()));
                return false;
            }
        }
        IPlayerDatabase db = IElrolAPI.getInstance().getPlayerDatabase();

        IPlayerData shopOwner = db.get(getOwner());
        IPlayerData customer = db.get(player.getUUID());

        if(customer.getBal() < cost) return false;

        String seller = "The Server";
        if(!isAdmin()) {
            Methods.takeFromChest(itemHandler, items);
            seller = shopOwner.getDisplayName();
            shopOwner.pay(cost);
            Main.textUtils.msg(Methods.getPlayerFromUUID(getOwner()), Msgs.paid_by.get(customer.getDisplayName(), Main.textUtils.parseCurrency(cost, false)));
        }
        for(ItemStack si : items) {
            ItemStack shopItem = si.copy();
            if(!player.getInventory().add(shopItem)) {
                ItemEntity itemEntity = new ItemEntity(
                        player.level,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        shopItem
                );
                player.level.addFreshEntity(itemEntity);
            }
        }
        customer.charge(cost);
        Main.textUtils.msg(player, Msgs.paid_player.get(seller, Main.textUtils.parseCurrency(cost, false)));
        return true;
    }

    private boolean sell(IItemHandler itemHandler, ServerPlayer player) {
        for(ItemStack item : items) {
            int size = player.getInventory().getContainerSize();
            int qty = 0;
            for(int i = 0; i < size; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if(item.sameItem(stack)) {
                    qty += stack.getCount();
                    if(item.getCount() <= qty) break;
                }
            }
            if(qty < item.getCount()) {
                Main.textUtils.err(player, Errs.missing_items(tag()));
                return false;
            }
        }
        IPlayerDatabase db = IElrolAPI.getInstance().getPlayerDatabase();
        IPlayerData ownerData = db.get(getOwner());
        IPlayerData sellerData = db.get(player.getUUID());

        String seller = "The Server";
        if(!isAdmin()) {
            if(ownerData.getBal() < cost) {
                Main.textUtils.err(player, Errs.shop_missing_funds(Methods.getDisplayName(getOwner())));
                return false;
            }
            if(Methods.canAllItemsFit(itemHandler, items)) {
                Methods.addItemsToChest(itemHandler, items, false);
                seller = ownerData.getDisplayName();
                ownerData.charge(cost);
                Main.textUtils.msg(Methods.getPlayerFromUUID(getOwner()), Msgs.paid_player.get(sellerData.getDisplayName(), Main.textUtils.parseCurrency(cost, false)));
            } else {
                Main.textUtils.err(player, Errs.sign_full());
                return false;
            }
        }
        items.forEach(item -> removeFromPlayer(item.copy(), player.getInventory()));

        sellerData.pay(cost);
        Main.textUtils.msg(player, Msgs.paid_by.get(seller, Main.textUtils.parseCurrency(cost, false)));
        return true;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();

        JsonArray array = new JsonArray();
        Codec<ItemStack> codec = ItemStack.CODEC;
        JsonOps ops = JsonOps.INSTANCE;

        items.forEach(item -> codec.encodeStart(ops, item).result().ifPresent(array::add));

        json.add("items", array);

        return json;
    }

    public static ChestShop fromJson(ChestShopType type, JsonObject jsonObj) {
        ChestShop shop = new ChestShop(type);
        if(jsonObj.has("owner"))
            shop.setOwner(UUID.fromString(jsonObj.get("owner").getAsString()));
        if(jsonObj.has("cost"))
            shop.setCost(jsonObj.get("cost").getAsFloat());
        if(jsonObj.has("linkLoc"))
            shop.setLinkLoc(Location.fromString(jsonObj.get("linkLoc").getAsString()));
        if(jsonObj.has("items")) {
            JsonArray array = jsonObj.get("items").getAsJsonArray();
            array.forEach(je -> {
                Codec<ItemStack> codec = ItemStack.CODEC;
                JsonOps ops = JsonOps.INSTANCE;

                codec.parse(ops, je).result().ifPresent(item -> shop.items.add(item));
            });
        }

        return shop;
    }

    private void removeFromPlayer(ItemStack item, Inventory inv) {
        int limit = inv.getContainerSize();
        int qty = item.getCount();

        for(int i = 0; i < limit; i++) {
            ItemStack slot = inv.getItem(i);
            if(slot.sameItem(item)) {
                if(slot.getCount() > qty) {
                    slot.setCount(slot.getCount()-qty);
                    qty = 0;
                } else {
                    qty -= slot.getCount();
                    slot.setCount(0);
                }
            }
            if(qty <= 0) {
                break;
            }
        }

        if(qty > 0) Logger.err("Item could not fully be added to the players inventory");
        if(qty < 0) Logger.err("The quantity is less than zero, this should never happen");
    }
}
