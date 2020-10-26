package com.github.jummes.supremeitem.placeholder.string.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFormat String Placeholder", description = "gui.placeholder.string.operator.format.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0")
public class FormatStringPlaceholder extends StringOperatorPlaceholder {

    private static final String TO_FORMAT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NGNmN2Q3OWYxYWViMjU1NGMxYmZkNDZlNmI3OGNhNmFlM2FhMmEyMTMyMzQ2YTQxMGYxNWU0MjZmMzEifX19";
    private static final String PLACEHOLDERS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";

    @Serializable(headTexture = TO_FORMAT_HEAD, description = "gui.placeholder.string.operator.format.to-format")
    private String toFormat;
    @Serializable(headTexture = PLACEHOLDERS_HEAD, description = "gui.placeholder.string.operator.format.placeholders")
    private List<StringPlaceholder> placeholders;

    public FormatStringPlaceholder(boolean target, String toFormat, List<StringPlaceholder> placeholders) {
        super(target);
        this.toFormat = toFormat;
        this.placeholders = placeholders;
    }

    public FormatStringPlaceholder() {
        this(TARGET_DEFAULT, "Example", Lists.newArrayList());
    }

    public static FormatStringPlaceholder deserialize(Map<String, Object> map) {
        String toFormat = (String) map.get("toFormat");
        List<StringPlaceholder> placeholders = (List<StringPlaceholder>) map.get("placeholders");
        return new FormatStringPlaceholder(TARGET_DEFAULT, toFormat, placeholders);
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        return String.format(toFormat,
                placeholders.stream().map(placeholder ->
                        placeholder.computePlaceholder(target, source)).toArray(String[]::new));
    }

    @Override
    public String getName() {
        return String.format(toFormat, placeholders.stream().map(Placeholder::getName).toArray(String[]::new));
    }

    @Override
    public StringPlaceholder clone() {
        return new FormatStringPlaceholder(TARGET_DEFAULT, toFormat, placeholders);
    }
}
