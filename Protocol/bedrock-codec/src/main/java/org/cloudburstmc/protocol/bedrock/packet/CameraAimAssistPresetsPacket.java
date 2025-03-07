package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAimAssistCategories;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAimAssistOperation;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAimAssistPresetDefinition;
import org.cloudburstmc.protocol.common.PacketSignal;

import java.util.List;

@Data
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class CameraAimAssistPresetsPacket implements BedrockPacket {
    private final List<CameraAimAssistCategories> categories = new ObjectArrayList<>();
    private final List<CameraAimAssistPresetDefinition> presets = new ObjectArrayList<>();
    /**
     * @since v776
     */
    private CameraAimAssistOperation operation;

    @Override
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CAMERA_AIM_ASSIST_PRESETS;
    }

    @Override
    public CameraAimAssistPresetsPacket clone() {
        try {
            return (CameraAimAssistPresetsPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}