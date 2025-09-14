package com.sbf.feature;

import com.sbf.object.LeashConnection;
import com.sbf.util.TextUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.decoration.ArmorStandEntity;

import java.util.ArrayList;

public class Lasso {
    public static volatile boolean blockClick = false;
    private static long lastClick = -1;

    public static void init(){
        ClientTickEvents.END_CLIENT_TICK.register(Lasso::tick);
    }
    private static void tick(MinecraftClient client){
        blockClick = false;
        if (client.world == null || client.player == null) return;
        if (!client.player.getMainHandStack().isOf(net.minecraft.item.Items.LEAD)) return;
        boolean isDeployed = false;
        boolean isDouble = false;
        Entity leashedEntity = null;

        ArrayList<LeashConnection> connections = getConnections(client);
        ArrayList<LeashConnection> additive = new ArrayList<>();
        for (LeashConnection connection : connections) {
            if (connection.getHolder() == client.player){
                isDeployed = true;
                leashedEntity = connection.getLeashedEntity();
            }
            for (LeashConnection past : additive){
                if (past.getLeashedEntity().getPos().distanceTo(connection.getLeashedEntity().getPos()) > 0.5) continue;
                if (past.getHolder() == client.player || connection.getHolder() == client.player){
                    isDouble = true;
                    break;
                }
            }
            additive.add(connection);
        }

        if (isDeployed && leashedEntity != null) {
            if (System.currentTimeMillis() - lastClick < 300) {
                blockClick = true;
            }
            else {
                for (Entity entity : client.world.getEntities()) {
                    if (!(entity instanceof ArmorStandEntity)) continue;
                    if (entity.getPos().distanceTo(leashedEntity.getPos().add(0, 2, 0)) > 3) continue;
                    if (TextUtils.getFormattedText(entity.getDisplayName()).contains("§e§lREEL")) {
                        blockClick = false;
                        break;
                    }
                    if (TextUtils.getFormattedText(entity.getDisplayName()).contains("§l§m")) {
                        blockClick = true;
                    }
                }
                if (isDouble) {
                    blockClick = false;
                }
            }
        }
    }

    private static ArrayList<LeashConnection> getConnections(MinecraftClient client){
        ArrayList<LeashConnection> connections = new ArrayList<>();
        for (Entity entity : client.world.getEntities()){
            if (!(entity instanceof Leashable leashedEntity)) continue;
            Entity holder = leashedEntity.getLeashHolder();
            if (holder == null) continue;
            connections.add(new LeashConnection(holder, entity));
        }
        return connections;
    }

    public static void clicked(){
        lastClick = System.currentTimeMillis();
    }
}
