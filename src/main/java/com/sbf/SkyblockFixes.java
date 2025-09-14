package com.sbf;

import com.sbf.feature.Lasso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

public class SkyblockFixes implements ClientModInitializer {
    public static final String NAMESPACE = "skyblockfixes";
    public static final String MOD_ID = "skyblockfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        Lasso.init();
    }
}
