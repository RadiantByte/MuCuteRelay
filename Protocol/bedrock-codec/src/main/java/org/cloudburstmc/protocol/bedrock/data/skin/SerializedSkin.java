package org.cloudburstmc.protocol.bedrock.data.skin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.*;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.JSONValue;

import java.util.Collections;
import java.util.List;

import static org.cloudburstmc.protocol.common.util.Preconditions.checkArgument;

@Getter
@ToString(exclude = {"geometryData"})
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SerializedSkin {
    private static final int PIXEL_SIZE = 4;

    public static final int SINGLE_SKIN_SIZE = 64 * 32 * PIXEL_SIZE;
    public static final int DOUBLE_SKIN_SIZE = 64 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_64_SIZE = 128 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_128_SIZE = 128 * 128 * PIXEL_SIZE;

    private final String skinId;
    /**
     * @since v428
     */
    private final String playFabId;
    private final String geometryName;
    private final String skinResourcePatch;
    private final ImageData skinData;
    private final List<AnimationData> animations;
    private final ImageData capeData;
    private final String geometryData;
    /**
     * @since v465
     */
    private final String geometryDataEngineVersion;
    private final String animationData;
    private final boolean premium;
    private final boolean persona;
    private final boolean capeOnClassic;
    /**
     * @since v465
     */
    private final boolean primaryUser;
    private final String capeId;
    private final String fullSkinId;
    private final String armSize;
    private final String skinColor;
    private final List<PersonaPieceData> personaPieces;
    private final List<PersonaPieceTintData> tintColors;
    private final boolean overridingPlayerAppearance;

    public static SerializedSkin of(String skinId, String playFabId, ImageData skinData, ImageData capeData, String geometryName,
                                    String geometryData, boolean premiumSkin) {
        skinData.checkLegacySkinSize();
        capeData.checkLegacyCapeSize();

        return new SerializedSkin(skinId, playFabId, geometryName, null, skinData, Collections.emptyList(), capeData,
                geometryData, "", "", premiumSkin, false, false, true, "", "",
                "wide", "#0", Collections.emptyList(), Collections.emptyList(), true);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String animationData, boolean premium, boolean persona, boolean capeOnClassic,
                                    String capeId, String fullSkinId) {
        return of(skinId, playFabId, skinResourcePatch, skinData, Collections.unmodifiableList(new ObjectArrayList<>(animations)),
                capeData, geometryData, animationData, premium, persona, capeOnClassic, capeId, fullSkinId,
                "wide", "#0", Collections.emptyList(), Collections.emptyList());
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String animationData, boolean premium, boolean persona, boolean capeOnClassic,
                                    String capeId, String fullSkinId, String armSize, String skinColor,
                                    List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors) {
        return of(skinId, playFabId, skinResourcePatch, skinData, animations, capeData, geometryData, animationData, premium, persona, capeOnClassic, true, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String animationData, boolean premium, boolean persona, boolean capeOnClassic,
                                    boolean primaryUser, String capeId, String fullSkinId, String armSize,
                                    String skinColor, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors) {
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, "", animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, true);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String geometryDataEngineVersion, String animationData, boolean premium,
                                    boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId,
                                    String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors) {

        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, geometryDataEngineVersion, animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, true);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData,
                                    List<AnimationData> animations, ImageData capeData, String geometryData,
                                    String geometryDataEngineVersion, String animationData, boolean premium,
                                    boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId,
                                    String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces,
                                    List<PersonaPieceTintData> tintColors, boolean overridingPlayerAppearance) {

        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData,
                Collections.unmodifiableList(new ObjectArrayList<>(animations)), capeData, geometryData, geometryDataEngineVersion, animationData,
                premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, overridingPlayerAppearance);
    }

    public static Builder builder() {
        return new Builder();
    }


    public boolean isValid() {
        return isValidSkin() && isValidResourcePatch();
    }

    private boolean isValidSkin() {
        return skinId != null && !skinId.trim().isEmpty() &&
                skinData != null && skinData.getWidth() >= 64 && skinData.getHeight() >= 32 &&
                skinData.getImage().length >= SINGLE_SKIN_SIZE;
    }

    public String getSkinResourcePatch() {
        if (skinResourcePatch == null && geometryName != null) {
            return convertLegacyGeometryName(geometryName);
        }
        return skinResourcePatch;
    }

    public String getGeometryName() {
        if (geometryName == null && skinResourcePatch != null) {
            return convertSkinPatchToLegacy(skinResourcePatch);
        }
        return geometryName;
    }

    private static String convertLegacyGeometryName(String geometryName) {
        return "{\"geometry\" : {\"default\" : \"" + JSONValue.escape(geometryName) + "\"}}";
    }

    private static String convertSkinPatchToLegacy(String skinResourcePatch) {
        checkArgument(validateSkinResourcePatch(skinResourcePatch), "Invalid skin resource patch");
        JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
        JSONObject geometry = (JSONObject) object.get("geometry");
        return (String) geometry.get("default");
    }

    private boolean isValidResourcePatch() {
        return skinResourcePatch != null && validateSkinResourcePatch(skinResourcePatch);
    }

    private static boolean validateSkinResourcePatch(String skinResourcePatch) {
        try {
            JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
            JSONObject geometry = (JSONObject) object.get("geometry");
            return geometry.containsKey("default") && geometry.get("default") instanceof String;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    public Builder toBuilder() {
        return new Builder().skinId(this.skinId).geometryData(this.geometryName)
                .skinResourcePatch(this.skinResourcePatch).skinData(this.skinData).animations(this.animations)
                .capeData(this.capeData).geometryData(this.geometryData).animationData(this.animationData)
                .premium(this.premium).persona(this.persona).capeOnClassic(this.capeOnClassic).capeId(this.capeId)
                .fullSkinId(this.fullSkinId).armSize(this.armSize).skinColor(this.skinColor)
                .personaPieces(this.personaPieces).tintColors(this.tintColors);
    }

    public static class Builder {
        private String skinId;
        private String playFabId;
        private String geometryName;
        private String skinResourcePatch;
        private ImageData skinData;
        private List<AnimationData> animations;
        private ImageData capeData;
        private String geometryData;
        private String animationData;
        private boolean premium;
        private boolean persona;
        private boolean capeOnClassic;
        private String capeId;
        private String fullSkinId;
        private String armSize;
        private String skinColor;
        private List<PersonaPieceData> personaPieces;
        private List<PersonaPieceTintData> tintColors;
        private String geometryDataEngineVersion;
        private boolean primaryUser;

        Builder() {
        }

        public Builder skinId(String skinId) {
            this.skinId = skinId;
            return this;
        }

        public Builder playFabId(String playFabId) {
            this.playFabId = playFabId;
            return this;
        }

        public Builder geometryName(String geometryName) {
            this.geometryName = geometryName;
            return this;
        }

        public Builder skinResourcePatch(String skinResourcePatch) {
            this.skinResourcePatch = skinResourcePatch;
            return this;
        }

        public Builder skinData(ImageData skinData) {
            this.skinData = skinData;
            return this;
        }

        public Builder animations(List<AnimationData> animations) {
            this.animations = animations;
            return this;
        }

        public Builder capeData(ImageData capeData) {
            this.capeData = capeData;
            return this;
        }

        public Builder geometryData(String geometryData) {
            this.geometryData = geometryData;
            return this;
        }

        public Builder animationData(String animationData) {
            this.animationData = animationData;
            return this;
        }

        public Builder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public Builder persona(boolean persona) {
            this.persona = persona;
            return this;
        }

        public Builder capeOnClassic(boolean capeOnClassic) {
            this.capeOnClassic = capeOnClassic;
            return this;
        }

        public Builder capeId(String capeId) {
            this.capeId = capeId;
            return this;
        }

        public Builder fullSkinId(String fullSkinId) {
            this.fullSkinId = fullSkinId;
            return this;
        }

        public Builder armSize(String armSize) {
            this.armSize = armSize;
            return this;
        }

        public Builder skinColor(String skinColor) {
            this.skinColor = skinColor;
            return this;
        }

        public Builder personaPieces(List<PersonaPieceData> personaPieces) {
            this.personaPieces = personaPieces;
            return this;
        }

        public Builder tintColors(List<PersonaPieceTintData> tintColors) {
            this.tintColors = tintColors;
            return this;
        }

        public Builder geometryDataEngineVersion(String version) {
            this.geometryDataEngineVersion = version;
            return this;
        }

        public Builder primaryUser(boolean primaryUser) {
            this.primaryUser = primaryUser;
            return this;
        }

        public SerializedSkin build() {
            if (playFabId == null) playFabId = "";
            if (animations == null) animations = Collections.emptyList();
            if (animationData == null) animationData = "";
            if (capeData == null) capeData = ImageData.EMPTY;
            if (capeId == null) capeId = "";
            if (fullSkinId == null) fullSkinId = skinId + capeId;
            if (armSize == null) armSize = "wide";
            if (geometryDataEngineVersion == null) geometryDataEngineVersion = "";
            if (skinColor == null) skinColor = "#0";
            if (personaPieces == null) personaPieces = Collections.emptyList();
            if (tintColors == null) tintColors = Collections.emptyList();

            if (skinResourcePatch == null) {
                return SerializedSkin.of(skinId, playFabId, geometryName, skinData, animations, capeData, geometryData,
                        animationData, premium, persona, capeOnClassic, capeId, fullSkinId);
            } else {
                return SerializedSkin.of(skinId, playFabId, skinResourcePatch, skinData, animations, capeData, geometryData, geometryDataEngineVersion,
                        animationData, premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors);
            }
        }

        public String toString() {
            return "SerializedSkin.Builder(skinId=" + this.skinId +
                    ", playFabId=" + this.playFabId +
                    ", geometryName=" + this.geometryName +
                    ", skinResourcePatch=" + this.skinResourcePatch +
                    ", skinData=" + this.skinData +
                    ", animations=" + this.animations +
                    ", capeData=" + this.capeData +
                    ", geometryData=" + this.geometryData +
                    ", animationData=" + this.animationData +
                    ", premium=" + this.premium +
                    ", persona=" + this.persona +
                    ", capeOnClassic=" + this.capeOnClassic +
                    ", capeId=" + this.capeId +
                    ", fullSkinId=" + this.fullSkinId +
                    ", armSize=" + this.armSize +
                    ", skinColor=" + this.skinColor +
                    ", personaPieces=" + this.personaPieces +
                    ", tintColors=" + this.tintColors +
                    ")";
        }
    }
}
