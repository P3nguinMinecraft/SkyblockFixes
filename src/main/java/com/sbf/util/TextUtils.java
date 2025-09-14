package com.sbf.util;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;

public class TextUtils {
    public static String getFormattedText(Text text) {
        StringBuilder sb = new StringBuilder();

        text.visit((style, content) -> {
            sb.append(serializeFormattingToString(style));
            sb.append(content);
            sb.append("§r");
            return Optional.empty();
        }, Style.EMPTY);

        return sb.toString();
    }

    public static String serializeFormattingToString(Style style) {
        StringBuilder sb = new StringBuilder();
        if (style.getColor() != null) {
            Formatting formatting = Formatting.byName(style.getColor().getName());
            if (formatting != null) {
                sb.append(formatting);
            }
        }
        if (style.isBold()) sb.append("§l");
        if (style.isItalic()) sb.append("§o");
        if (style.isUnderlined()) sb.append("§n");
        if (style.isObfuscated()) sb.append("§k");
        if (style.isStrikethrough()) sb.append("§m");

        return sb.toString();
    }
}
