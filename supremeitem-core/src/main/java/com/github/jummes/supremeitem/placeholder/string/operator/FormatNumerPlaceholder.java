package com.github.jummes.supremeitem.placeholder.string.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.entity.HealthPlaceholder;
import com.github.jummes.supremeitem.placeholder.string.StringPlaceholder;
import lombok.Getter;
import lombok.Setter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFormat Number Placeholder", description = "gui.placeholder.string.operator.number-format.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==")
public class FormatNumerPlaceholder extends StringOperatorPlaceholder {

    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";
    private static final String FORMAT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY4ODZkOWM0MGVmN2Y1MGMyMzg4MjQ3OTJjNDFmYmZiNTRmNjY1ZjE1OWJmMWJjYjBiMjdiM2VhZDM3M2IifX19";

    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.placeholder.string.operator.number-format.placeholder")
    private NumericPlaceholder placeholder;
    @Serializable(headTexture = FORMAT_HEAD, description = "gui.placeholder.string.operator.number-format.format")
    private String numberFormat;

    public FormatNumerPlaceholder() {
        this(TARGET_DEFAULT, new HealthPlaceholder(), "0");
    }

    public FormatNumerPlaceholder(boolean target, NumericPlaceholder placeholder, String numberFormat) {
        super(target);
        this.placeholder = placeholder;
        this.numberFormat = numberFormat;
    }

    public static FormatNumerPlaceholder deserialize(Map<String, Object> map) {
        NumericPlaceholder placeholder = (NumericPlaceholder) map.get("placeholder");
        String numberFormat = (String) map.get("numberFormat");
        return new FormatNumerPlaceholder(TARGET_DEFAULT, placeholder, numberFormat);
    }

    @Override
    public String computePlaceholder(Target target, Source source) {
        DecimalFormat df = new DecimalFormat(numberFormat);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(placeholder.computePlaceholder(target, source));
    }

    @Override
    public String getName() {
        return String.format("&6&l(&c%s&6&l).format(%s)&c", placeholder.getName(), numberFormat);
    }

    @Override
    public StringPlaceholder clone() {
        return new FormatNumerPlaceholder(TARGET_DEFAULT, placeholder.clone(), numberFormat);
    }
}
